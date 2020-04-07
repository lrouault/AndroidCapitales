package com.lrt.capitales.controller;

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

import java.util.List;


class Lab2048Engine {
    private static final String TAG = "Lab2048Engine"; // pour les logs
    private Boule mBoule = null;

    void setBoule(Boule pBoule) {
        this.mBoule = pBoule;
    }

    // Le labyrinthe fourni par LabyrintheBank
    private List<Bloc> mBlocks = null;

    // Callback sur l'activite disponible
    private Lab2048Activity mActivity;

    // Recuperation des donnees de l'accelerometre
    private SensorManager mManager;
    private Sensor mAccelerometre;

    // CONSTRUCTEUR
    Lab2048Engine(Lab2048Activity pView) {
        Log.d(TAG, "appel du constructeur");
        mActivity = pView;
        mManager = (SensorManager) mActivity.getBaseContext().getSystemService(Service.SENSOR_SERVICE);
        assert mManager != null;
        mAccelerometre = mManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
    }

    // Supervision du deplacement de la Boule via l'accelerometre
    private SensorEventListener mSensorEventListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent pEvent) {
            // Ecran en poistion paysage => x et y inverses
            float x = pEvent.values[1];
            float y = pEvent.values[0];

            if(mBoule != null) {
                // On met à jour les coordonnées de la boule
                RectF hitBox = mBoule.putXAndY(x, y);

                // Recherche de contact
                // Pour tous les blocs du labyrinthe
                if(mBlocks != null) {
                    // Recuperation des hauteurs et largeurs des blocs
                    // /!\ pour le moment, tous les blocs ont la meme dimension
                    float w_blocWidth = mBlocks.get(0).getRectangle().width();
                    float w_blocHeight = mBlocks.get(0).getRectangle().height();
                    for (Bloc block : mBlocks) {
                        // On crée un nouveau rectangle pour ne pas modifier celui du bloc
                        // la methode intersection remplace inter par le rectangle d'intersection
                        RectF inter = new RectF(block.getRectangle());
                        if (inter.intersect(hitBox)) {
                            // On recherche d'ou vient la collision
                            // pour trouver la position du "mur" le plus proche
                            float w_positionMur = 0.f;
                            // Permet de s'assurer que l'on rencontre bien le bon bloc
                            // /!\ Rouler contre un mur horizontal entrainait des collisions gauche droite
                            // TODO Robustesse a l'intersection entre deux blocs, la boule peut passer
                            boolean isTrueCollision = false;
                            Direction w_directionContact = Direction.fromPoints(hitBox.centerX(), hitBox.centerY(), block.getRectangle().centerX(), block.getRectangle().centerY());
                            switch (w_directionContact) {
                                case UP:
                                    w_positionMur = block.getRectangle().bottom;
                                    if(inter.width() > Math.min(Boule.RAYON /2.f, w_blocWidth/4)) {isTrueCollision = true;}
                                    break;
                                case DOWN:
                                    w_positionMur = block.getRectangle().top;
                                    if(inter.width() > Math.min(Boule.RAYON/2.f, w_blocWidth/4)) {isTrueCollision = true;}
                                    break;
                                case LEFT:
                                    Log.d(TAG, "contact gauche aire inter : " + inter.width() +","+ inter.height());
                                    w_positionMur = block.getRectangle().right;
                                    if(inter.height() > Math.min(Boule.RAYON/2.f, w_blocHeight/4)) {isTrueCollision = true;}
                                    break;
                                case RIGHT:
                                    Log.d(TAG, "contact droit aire inter : " + inter.width() +","+ inter.height());
                                    w_positionMur = block.getRectangle().left;
                                    if(inter.height() > Math.min(Boule.RAYON/2.f, w_blocHeight/4)) {isTrueCollision = true;}
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
                                        mBoule.accelerateur(Direction.UP);
                                        break;
                                    case SPEED_B:
                                        mBoule.accelerateur(Direction.DOWN);
                                        break;
                                    case SPEED_G:
                                        mBoule.accelerateur(Direction.LEFT);
                                        break;
                                    case SPEED_D:
                                        mBoule.accelerateur(Direction.RIGHT);
                                        break;
                                }
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

    // Remet à zéro l'emplacement de la boule
    // Package private
    void reset() {
        Log.d(TAG, "appel de reset");
        mBoule.reset();
    }

    // Arrête le capteur
    // Package private
    void stop() {
        Log.d(TAG, "appel de stop");
        mManager.unregisterListener(mSensorEventListener, mAccelerometre);
    }

    // Redémarre le capteur
    // Package private
    void resume() {
        Log.d(TAG, "appel de resume");
        mManager.registerListener(mSensorEventListener, mAccelerometre, SensorManager.SENSOR_DELAY_GAME);
    }


    // GETTER AND SETTER
    void setBlocks(List<Bloc> pBlocks) {
        this.mBlocks = pBlocks;
        mBoule.setInitialRectangle(new RectF(0,0,Boule.RAYON,Boule.RAYON));
    }

    void setDifficulty(int ai_diff) {
        mBoule.setDifficulty(ai_diff);
    }
}

