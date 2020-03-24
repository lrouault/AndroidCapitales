package com.lrt.capitales.model;

import android.util.Log;

import com.lrt.capitales.common.CommonEnum;

import java.util.Arrays;
import java.util.Random;
import static java.lang.Math.pow;

/**
 * Created by lrouault on 26/02/2018.
 * Partie model du jeu 2048
 */

public class Game2048 {
    private static final String TAG = "Game2048"; // pour les logs
    public static final int C_CASES_X = 4;
    public static final int C_CASES_Y = 4;
    public static final int C_DEFAULT_FREQ4 = 4;
    public static final int C_UNDO_MAX = 3;

    // Options du jeu
    private int m_nbUndoMax = C_UNDO_MAX;
    private int m_nbEnableUndo = 0;
    private int m_freq4 = C_DEFAULT_FREQ4;

    private int m_score = 0;
    private int m_bestScore;
    private int[] m_plateau = new int[]{0,0,0,0, 0,0,0,0, 0,0,0,0, 0,0,0,0};
    private int[] m_plateauPrevious = Arrays.copyOf(m_plateau, m_plateau.length);
    private int[] m_plateauPrevious2 = Arrays.copyOf(m_plateau, m_plateau.length);
    private int[] m_plateauPrevious3 = Arrays.copyOf(m_plateau, m_plateau.length);


    public Game2048(int ai_bestScore) {
        Log.d(TAG, "appel du constructeur par defaut");
        m_bestScore = ai_bestScore;
        creationBloc();
    }

    public Game2048(int[] ai_plateau, int ai_bestScore) {
        Log.d(TAG, "appel du constructeur avec tableau");
        m_plateau = ai_plateau;
        m_bestScore = ai_bestScore;
        _majScore();
    }

    // METHODES PUBLIQUES
    public void onMouvement(CommonEnum.Direction ai_direction) {
        // Algo de reference ecrit pour un decalage vers le haut
        // la fonction _translateMvtIJ permet de changer d'indice pour les 3 autres directions
        Log.d(TAG,"appel de onMouvement");
        boolean w_hadModif = false;
        int[] w_plateauCopy = Arrays.copyOf(m_plateau, m_plateau.length);

        // Boucle sur les colonnes
        for(int i = 0; i< C_CASES_X; i++) {
            // Tous les elements sont colles en haut
            boolean w_modif = true;
            while (w_modif) {
                w_modif = false;
                for (int j = 0; j < C_CASES_Y -1; j++) {
                    if (m_plateau[_translateMvtIJ(ai_direction, i, j)] == 0 && m_plateau[_translateMvtIJ(ai_direction, i, j+1)] != 0) {
                        m_plateau[_translateMvtIJ(ai_direction, i, j)] = m_plateau[_translateMvtIJ(ai_direction, i, j+1)];
                        m_plateau[_translateMvtIJ(ai_direction, i, j+1)] = 0;
                        w_modif = true;
                        w_hadModif = true;
                    }
                }
            }
            // Concatenation des elements identiques
            // Pas de double concatenation grace a l'indice k
            for (int j = 0; j < C_CASES_Y -1; j++) {
                if (m_plateau[_translateMvtIJ(ai_direction, i, j)] == m_plateau[_translateMvtIJ(ai_direction, i, j+1)]
                        && m_plateau[_translateMvtIJ(ai_direction, i, j)] != 0) {
                    m_plateau[_translateMvtIJ(ai_direction, i, j)] += 1;
                    for (int k = j+1; k < C_CASES_Y -1; k++) {
                        m_plateau[_translateMvtIJ(ai_direction, i, k)] = m_plateau[_translateMvtIJ(ai_direction, i, k+1)];
                    }
                    m_plateau[_translateMvtIJ(ai_direction, i, C_CASES_Y -1)] = 0;
                    w_hadModif = true;
                }
            }
        }
        if (w_hadModif) {
            m_plateauPrevious3 =m_plateauPrevious2;
            m_plateauPrevious2 = m_plateauPrevious;
            m_plateauPrevious = w_plateauCopy;
            if (m_nbEnableUndo < m_nbUndoMax) { m_nbEnableUndo +=1;}
            creationBloc();
            _majScore();
        }
    }

    public void creationBloc() {
        Log.d(TAG,"appel de creationBloc");
        Random w_r = new Random();
        int w_countVide = 0;

        for (int i = 0; i< C_CASES_X; i++)
            for (int j = 0; j< C_CASES_Y; j++)
            {
                if (m_plateau[i+ C_CASES_X *j]==0){
                    w_countVide++;
                }
            }

        if(w_countVide != 0){
            int newPosition = 1 + w_r.nextInt(w_countVide);
            w_countVide = 0;
            for (int i = 0; i< C_CASES_X; i++) {
                for (int j = 0; j < C_CASES_Y; j++) {
                    if (m_plateau[i + C_CASES_X *j] == 0) {
                        w_countVide++;
                    }
                    if (w_countVide == newPosition) {
                        int w_random5 = w_r.nextInt(m_freq4);
                        if (w_random5==0) {m_plateau[i + C_CASES_X *j] = 2;}
                        else {m_plateau[i + C_CASES_X *j] = 1;}
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

    public void undo() {
        Log.d(TAG,"appel de undo");
        if (m_nbEnableUndo >0) {
            m_nbEnableUndo -= 1;
            m_plateau = m_plateauPrevious;
            m_plateauPrevious = m_plateauPrevious2;
            m_plateauPrevious2 = m_plateauPrevious3;
        }
        _majScore();
    }


    // METHODES PRIVEES
    private int _translateMvtIJ(CommonEnum.Direction direction, int i, int j) {
        switch (direction)  {
            case UP: // Cas de reference
                return i+ C_CASES_X *j;
            case DOWN: // Inversion verticale
                return i+ C_CASES_X *(C_CASES_Y-1-j);
            case LEFT: // Inversion (i,j) par rapport au cas de reference
                return j+ C_CASES_Y *i;
            case RIGHT: // Inversion horizontale
                return (C_CASES_Y -1-j)+ C_CASES_X *i;
        }
        Log.e(TAG, "_translateMvtIJ Direction inconnue");
        return -1;
    }

    private void _majScore() {
        Log.d(TAG,"appel de _majScore");
        m_score = 0;
        // Calcul du score
        for(int i = 0; i< C_CASES_X * C_CASES_Y; i++)
            if (m_plateau[i]!=0) {m_score += pow(2,m_plateau[i]);}
        // Maj du meilleur scrore
        if (m_score > m_bestScore) {m_bestScore=m_score;}
    }


    // GETTER AND SETTER


    public int getM_freq4() {
        return m_freq4;
    }

    public void setM_freq4(int m_freq4) {
        this.m_freq4 = m_freq4;
    }

    public int getM_score() {
        return m_score;
    }

    public int getM_bestScore() {
        return m_bestScore;
    }

    public int[] getM_plateau() {
        return m_plateau;
    }

    public void setM_plateau(int[] m_plateau) {
        this.m_plateau = m_plateau;
    }

    public int[] getM_plateauPrevious() {
        return m_plateauPrevious;
    }

    public int getM_nbEnableUndo() {
        return m_nbEnableUndo;
    }

    public int getM_nbUndoMax() {
        return m_nbUndoMax;
    }

    public void setM_nbUndoMax(int m_nbUndoMax) {
        this.m_nbUndoMax = m_nbUndoMax;
        if (m_nbEnableUndo > m_nbUndoMax) {m_nbEnableUndo = m_nbUndoMax;}
    }
}
