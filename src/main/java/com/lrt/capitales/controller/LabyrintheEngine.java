package com.lrt.capitales.controller;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.charset.IllegalCharsetNameException;
import java.nio.charset.UnsupportedCharsetException;
import java.util.ArrayList;
import java.util.List;

import android.app.Service;
import android.graphics.RectF;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

import com.lrt.capitales.common.CommonEnum.Direction;
import com.lrt.capitales.common.CommonEnum.Type;
import com.lrt.capitales.model.labyrinthe.Bloc;
import com.lrt.capitales.model.labyrinthe.Boule;


public class LabyrintheEngine {
    private static final String TAG = "LabyrintheEngine"; // pour les logs
    private Boule mBoule = null;
    public Boule getBoule() {
        return mBoule;
    }

    public void setBoule(Boule pBoule) {
        this.mBoule = pBoule;
    }

    // Le labyrinthe
    private List<Bloc> mBlocks = null;

    private LabyrintheActivity mActivity = null;

    private SensorManager mManager = null;
    private Sensor mAccelerometre = null;

    private float m_screenWidth = -1;
    private float m_screenHeight = -1;

    SensorEventListener mSensorEventListener = new SensorEventListener() {

        @Override
        public void onSensorChanged(SensorEvent pEvent) {
            float x = pEvent.values[1];
            float y = pEvent.values[0];

            if(mBoule != null) {
                // On met à jour les coordonnées de la boule
                RectF hitBox = mBoule.putXAndY(x, y);

                // Pour tous les blocs du labyrinthe
                Log.d(TAG, "-- calcul intersections");
                // Pour le moment, tous les blocs font la meme taille
                if(mBlocks != null) {
                    float w_blocWidth = mBlocks.get(0).getRectangle().width();
                    float w_blocHeight = mBlocks.get(0).getRectangle().height();
                    for (Bloc block : mBlocks) {
                        // On crée un nouveau rectangle pour ne pas modifier celui du bloc
                        RectF inter = new RectF(block.getRectangle());
                        if (inter.intersect(hitBox)) {
                            // On recherche d'ou vient la collision
                            // pour trouver la position du "mur" le plus proche
                            float w_positionMur = 0.f;
                            // Permet de s'assurer que l'on rencontre bien le bon bloc
                            // /!\ Rouler contre un mur horizontal entrainait des collisions gauche droite
                            boolean isTrueCollision = false;
                            Direction w_directionContact = Direction.fromPoints(hitBox.centerX(), hitBox.centerY(), block.getRectangle().centerX(), block.getRectangle().centerY());
                            switch (w_directionContact) {
                                case UP:
                                    w_positionMur = block.getRectangle().bottom;
                                    if(inter.width() > Math.min(mBoule.RAYON/2, w_blocWidth/4)) {isTrueCollision = true;}
                                    break;
                                case DOWN:
                                    w_positionMur = block.getRectangle().top;
                                    if(inter.width() > Math.min(mBoule.RAYON/2, w_blocWidth/4)) {isTrueCollision = true;}
                                    break;
                                case LEFT:
                                    Log.d(TAG, "contact gauche aire inter : " + inter.width() +","+ inter.height());
                                    w_positionMur = block.getRectangle().right;
                                    if(inter.height() > Math.min(mBoule.RAYON/2, w_blocHeight/4)) {isTrueCollision = true;}
                                    break;
                                case RIGHT:
                                    Log.d(TAG, "contact droit aire inter : " + inter.width() +","+ inter.height());
                                    w_positionMur = block.getRectangle().left;
                                    if(inter.height() > Math.min(mBoule.RAYON/2, w_blocHeight/4)) {isTrueCollision = true;}
                                    break;
                            }
                            if(isTrueCollision) {
                                // On agit différement en fonction du type de bloc
                                switch (block.getType()) {
                                    case TROU:
                                        mActivity.showDialog(LabyrintheActivity.DEFEAT_DIALOG);
                                        break;

                                    case DEPART:
                                        break;

                                    case ARRIVEE:
                                        mActivity.showDialog(LabyrintheActivity.VICTORY_DIALOG);
                                        break;

                                    case MUR:
                                        mBoule.mur(w_directionContact, w_positionMur);
                                        break;

                                    case TRAMPO:
                                        mBoule.rebond(w_directionContact, w_positionMur);
                                        break;

                                    case SPEED_H:
                                        mBoule.accelerateur(w_directionContact);
                                        break;
                                }
                                break;
                            }
                        }
                    }
                }
            }
        }

        @Override
        public void onAccuracyChanged(Sensor pSensor, int pAccuracy) {

        }
    };

    public LabyrintheEngine(LabyrintheActivity pView) {
        Log.d(TAG, "appel du constructeur");
        mActivity = pView;
        mManager = (SensorManager) mActivity.getBaseContext().getSystemService(Service.SENSOR_SERVICE);
        mAccelerometre = mManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
    }

    // Remet à zéro l'emplacement de la boule
    public void reset() {
        Log.d(TAG, "appel de reset");
        mBoule.reset();
    }

    // Arrête le capteur
    public void stop() {
        Log.d(TAG, "appel de stop");
        mManager.unregisterListener(mSensorEventListener, mAccelerometre);
    }

    // Redémarre le capteur
    public void resume() {
        Log.d(TAG, "appel de resume");
        mManager.registerListener(mSensorEventListener, mAccelerometre, SensorManager.SENSOR_DELAY_GAME);
    }

    // Construit le labyrinthe
    public List<Bloc> lectureFichier() {
        // TODO lecture du nombre de colonnes lignes
        // TODO Stockage de plusieurs niveaux
        Log.d(TAG, "appel de lectureFichier");
        InputStreamReader w_input = null;
        BufferedReader w_reader = null;
        Bloc w_bloc = null;
        mBlocks = new ArrayList<Bloc>();


        float w_rectWidth = m_screenWidth/(19+1);
        float w_rectHeight = m_screenHeight/(13+1);

        try {
            w_input = new InputStreamReader(mActivity.getBaseContext().getAssets().open("listeLabyrinthes.dat"), Charset.forName("UTF-8"));
            w_reader = new BufferedReader(w_input);

            // i -> colonnes (axeX)
            // j -> lignes (axeY)
            int i = 0; int j = 0;

            // La valeur récupérée par le flux
            int c;
            // Tant que la valeur n'est pas de -1, c'est qu'on lit un caractère du fichier
            while((c = w_reader.read()) != -1) {
                char character = (char) c;
                if(character == 'o')
                    w_bloc = new Bloc(Type.TROU, i, j, w_rectWidth, w_rectHeight);
                else if(character == 'd') {
                    w_bloc = new Bloc(Type.DEPART, i, j, w_rectWidth, w_rectHeight);
                    mBoule.setInitialRectangle(new RectF(w_bloc.getRectangle()));
                } else if(character == 'a')
                    w_bloc = new Bloc(Type.ARRIVEE, i, j, w_rectWidth, w_rectHeight);
                else if(character == 'm')
                    w_bloc = new Bloc(Type.MUR, i, j, w_rectWidth, w_rectHeight);
                else if(character == 't')
                    w_bloc = new Bloc(Type.TRAMPO, i, j, w_rectWidth, w_rectHeight);
                else if(character == '/') {
                    w_reader.readLine();
                    i = -1;
                }
                else if (character == '\n') {
                    // Si le caractère est un retour à la ligne, on retourne avant la première colonne
                    // Car on aura i++ juste après, ainsi i vaudra 0, la première colonne !
                    i = -1;
                    // Et on passe à la ligne suivante
                    j++;
                }
                // Si le bloc n'est pas nul, alors le caractère n'était pas un retour à la ligne
                if(w_bloc != null) {
                    // On l'ajoute alors au labyrinthe
                    mBlocks.add(w_bloc);
                }
                // On passe à la colonne suivante
                i++;
                // On remet bloc à null, utile quand on a un retour à la ligne pour ne pas ajouter de bloc qui n'existe pas
                w_bloc = null;
            }
        } catch (IllegalCharsetNameException e) {
            e.printStackTrace();
        } catch (UnsupportedCharsetException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(w_input != null) {
                try {
                    w_input.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
            if(w_reader != null) {
                try {
                    w_reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return mBlocks;
    }

    // GETTER AND SETTER

    public void setScreenWidthHeight(float m_screenWidth, float m_screenHeight) {
        this.m_screenWidth = m_screenWidth;
        this.m_screenHeight = m_screenHeight;
    }

    public void setBlocks(List<Bloc> pBlocks) {
        this.mBlocks = pBlocks;
        for (Bloc block : pBlocks) {
            if(block.getType() == Type.DEPART) {
                mBoule.setInitialRectangle(new RectF(block.getRectangle()));
                break;
            }
        }
    }
}

