package com.lrt.capitales.Model;

import android.util.Log;

import com.lrt.capitales.common.commonEnum;

import java.util.Random;
import static java.lang.Math.pow;

/**
 * Created by lrouault on 26/02/2018.
 * Partie model du jeu 2048
 */

public class Game2048 {
    private static final String TAG = "Game2048"; // pour les logs

    private int m_score = 0;
    private int m_bestScore = 0;
    private int[] m_plateau = new int[]{0,0,0,0, 0,0,0,0, 0,0,0,0, 0,0,0,0};

    public Game2048() {
        Log.d(TAG, "appel du constructeur");
    }

    // METHODES PUBLIQUES
    public void onMouvement(commonEnum.Direction direction) {
        // Algo de reference ecrit pour un decalage vers le haut
        // la fonction _translateMvtIJ permet de changer d'indice pour les 3 autres directions
        Log.d(TAG,"appel de onMouvement");
        boolean w_hadModif;
        w_hadModif=false;
        // Boucle sur les colonnes
        for(int i=0; i<4; i++) {
            // Tous les elements sont colles en haut
            boolean w_modif = true;
            while (w_modif) {
                w_modif = false;
                for (int j = 0; j < 3; j++) {
                    if (m_plateau[_translateMvtIJ(direction, i, j)] == 0 && m_plateau[_translateMvtIJ(direction, i, j+1)] != 0) {
                        m_plateau[_translateMvtIJ(direction, i, j)] = m_plateau[_translateMvtIJ(direction, i, j+1)];
                        m_plateau[_translateMvtIJ(direction, i, j+1)] = 0;
                        w_modif = true;
                        w_hadModif = true;
                    }
                }
            }
            // Concatenation des elements identiques
            // Pas de double concatenation grace a l'indice k
            for (int j = 0; j < 3; j++) {
                if (m_plateau[_translateMvtIJ(direction, i, j)] == m_plateau[_translateMvtIJ(direction, i, j+1)]
                        && m_plateau[_translateMvtIJ(direction, i, j)] != 0) {
                    m_plateau[_translateMvtIJ(direction, i, j)] += 1;
                    for (int k = j+1; k < 3; k++) {
                        m_plateau[_translateMvtIJ(direction, i, k)] = m_plateau[_translateMvtIJ(direction, i, k+1)];
                    }
                    m_plateau[_translateMvtIJ(direction, i, 3)] = 0;
                    w_hadModif = true;
                }
            }
        }
        if (w_hadModif) {
            creationBloc();
            _majScore();
        }
    }

    public void creationBloc() {
        Log.d(TAG,"appel de creationBloc");
        Random w_r = new Random();
        int w_countVide = 0;

        for (int i=0;i<4;i++)
            for (int j=0;j<4;j++)
            {
                if (m_plateau[i+4*j]==0){
                    w_countVide++;
                }
            }

        if(w_countVide != 0){
            int newPosition = 1 + w_r.nextInt(w_countVide);
            w_countVide = 0;
            for (int i=0;i<4;i++) {
                for (int j = 0; j < 4; j++) {
                    if (m_plateau[i + 4 * j] == 0) {
                        w_countVide++;
                    }
                    if (w_countVide == newPosition) {
                        int w_random5 = w_r.nextInt(4);
                        if (w_random5==3) {m_plateau[i + 4 * j] = 2;}
                        else {m_plateau[i + 4 * j] = 1;}
                        w_countVide++;
                    }
                }
            }
        }
    }

    public void restart() {
        Log.d(TAG,"appel de restart");
        m_score = 0;
        m_plateau = new int[]{0,0,0,0, 0,0,0,0, 0,0,0,0, 0,0,0,0};
    }


    // METHODES PRIVEES
    private int _translateMvtIJ(commonEnum.Direction direction, int i, int j) {
        switch (direction)  {
            case UP: // Cas de reference
                return i+4*j;
            case DOWN: // Inversion verticale
                return i+4*(3-j);
            case LEFT: // Inversion (i,j) par rapport au cas de reference
                return j+4*i;
            case RIGHT: // Inversion horizontale
                return (3-j)+4*i;
        }
        Log.e(TAG, "_translateMvtIJ Direction inconnue");
        return -1;
    }

    private void _majScore() {
        Log.d(TAG,"appel de _majScore");
        m_score = 0;
        // Calcul du score
        for(int i=0; i<16; i++)
            if (m_plateau[i]!=0) {m_score += pow(2,m_plateau[i]);}
        // Maj du meilleur scrore
        if (m_score > m_bestScore) {m_bestScore=m_score;}
    }


    // GETTER AND SETTER
    public int getM_score() {
        return m_score;
    }

    public int getM_bestScore() {
        return m_bestScore;
    }

    public int[] getM_plateau() {
        return m_plateau;
    }
}
