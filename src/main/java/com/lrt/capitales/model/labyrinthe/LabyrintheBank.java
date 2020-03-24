package com.lrt.capitales.model.labyrinthe;

import android.content.res.AssetManager;
import android.graphics.RectF;
import android.util.Log;

import com.lrt.capitales.common.CommonEnum;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.charset.IllegalCharsetNameException;
import java.nio.charset.UnsupportedCharsetException;
import java.util.ArrayList;
import java.util.List;

public class LabyrintheBank {
    private static final String TAG = "LabyrintheBank"; // pour les logs

    private float m_screenWidth = -1;
    private float m_screenHeight = -1;

    ArrayList<ArrayList<Bloc>> m_listeDeLabyrinthe = new ArrayList<ArrayList<Bloc>>();


    // Construit le labyrinthe
    public void lectureFichier(AssetManager ai_asset) {
        Log.d(TAG, "appel de lectureFichier");
        InputStreamReader w_input = null;
        BufferedReader w_reader = null;
        Bloc w_bloc = null;
        ArrayList<Bloc> w_labyrinthe = null;
        boolean w_isLabyrintheEncours = false;

        int w_maxColonne = 0;
        int w_maxLigne = 0;


        float w_rectWidth = m_screenWidth/(19+1);
        float w_rectHeight = m_screenHeight/(13+1);

        try {
            w_input = new InputStreamReader(ai_asset.open("listeLabyrinthes.dat"), Charset.forName("UTF-8"));
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
                    w_bloc = new Bloc(CommonEnum.Type.TROU, i, j, w_rectWidth, w_rectHeight);
                else if(character == 'd') {
                    w_bloc = new Bloc(CommonEnum.Type.DEPART, i, j, w_rectWidth, w_rectHeight);
                } else if(character == 'a')
                    w_bloc = new Bloc(CommonEnum.Type.ARRIVEE, i, j, w_rectWidth, w_rectHeight);
                else if(character == 'm')
                    w_bloc = new Bloc(CommonEnum.Type.MUR, i, j, w_rectWidth, w_rectHeight);
                else if(character == 't')
                    w_bloc = new Bloc(CommonEnum.Type.TRAMPO, i, j, w_rectWidth, w_rectHeight);
                else if(character == '/') {
                    // Ligne de commentaire
                    w_reader.readLine();
                    i = -1;
                }
                else if(character == '#') {
                    if(w_isLabyrintheEncours) {
                        Log.d(TAG, "-- fin de lecture du labyrinte nbwidth, height "+w_maxColonne+" , "+w_maxLigne);
                        // Fin de labyrinthe
                        for (Bloc block : w_labyrinthe) {
                            block.setSize(m_screenWidth/(w_maxColonne+1), m_screenHeight/(w_maxLigne+1));
                        }
                        m_listeDeLabyrinthe.add(w_labyrinthe);
                    } else {
                        Log.d(TAG, "-- debut de lecture du labyrinte");
                        w_labyrinthe = new ArrayList<Bloc>();
                        i=-1; j=0;
                        w_maxColonne = 0; w_maxLigne = 0;
                    }
                    // Caractere de debut et de fin d'un labyinthe
                    w_reader.readLine();
                    w_isLabyrintheEncours = !w_isLabyrintheEncours;
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
                    w_labyrinthe.add(w_bloc);
                    if(i>w_maxColonne) {w_maxColonne=i;}
                    if(j>w_maxLigne) {w_maxLigne=j;}
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

    }


    // GETTER AND SETTER
    public void setScreenWidthHeight(float m_screenWidth, float m_screenHeight) {
        this.m_screenWidth = m_screenWidth;
        this.m_screenHeight = m_screenHeight;
    }

    public ArrayList<ArrayList<Bloc>> getM_listeDeLabyrinthe() {
        return m_listeDeLabyrinthe;
    }
}
