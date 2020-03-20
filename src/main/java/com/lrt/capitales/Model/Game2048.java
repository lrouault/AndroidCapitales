package com.lrt.capitales.Model;

import android.util.Log;

import java.util.Random;
import static java.lang.Math.pow;

/**
 * Created by lrouault on 26/02/2018.
 */

public class Game2048 {
    private static final String TAG = "Game2048";

    private int m_score = 0;
    private int m_bestScore = 0;
    private int[] m_plateau = new int[]{0,0,0,0, 0,0,0,0, 0,0,0,0, 0,0,0,0};

    public Game2048() {
        Log.d(TAG, "appel du constructeur");
    }

    public void mvtHaut() {
        Log.d(TAG,"appel de mvtHaut");
        boolean w_modif,w_hadModif;
        w_hadModif=false;
        for(int i=0; i<4; i++){
            w_modif = true;
            while (w_modif){
                w_modif=false;
                for(int j=0; j<3; j++){
                    if (m_plateau[i+4*j]==0 && m_plateau[i+4*(j+1)]!=0){
                        m_plateau[i+4*j] = m_plateau[i+4*(j+1)];
                        m_plateau[i+4*(j+1)] = 0;
                        w_modif=true;
                        w_hadModif=true;
                    }
                    if (m_plateau[i+4*j]==m_plateau[i+4*(j+1)] && m_plateau[i+4*j]!=0){
                        m_plateau[i+4*j] += 1;
                        m_plateau[i+4*(j+1)] = 0;
                        w_modif=true;
                        w_hadModif=true;
                    }
                }
            }
        }
        if (w_hadModif) {
            creationBloc();
            _majScore();
        }
    }

    public void mvtBas() {
        Log.d(TAG,"appel de mvtBas");
        boolean w_modif,w_hadModif;
        w_hadModif=false;
        for(int i=0; i<4; i++){
            w_modif = true;
            while (w_modif) {
                w_modif = false;
                for (int j = 3; j > 0; j--) {
                    if (m_plateau[i+4*j] == 0 && m_plateau[i+4*(j-1)] != 0) {
                        m_plateau[i+4*j] = m_plateau[i+4*(j-1)];
                        m_plateau[i+4*(j-1)] = 0;
                        w_modif = true;
                        w_hadModif=true;
                    }
                    if (m_plateau[i+4*j] == m_plateau[i+4*(j-1)] && m_plateau[i+4*j] != 0) {
                        m_plateau[i+4*j] += 1;
                        m_plateau[i+4*(j-1)] = 0;
                        w_modif = true;
                        w_hadModif=true;
                    }
                }
            }
        }
        if (w_hadModif) {
            creationBloc();
            _majScore();
        }
    }

    public void mvtGauche() {
        Log.d(TAG,"appel de mvtGauche");
        boolean w_modif,w_hadModif;
        w_hadModif=false;
        for(int j=0; j<4; j++) {
            w_modif = true;
            while (w_modif) {
                w_modif = false;
                for (int i = 0; i < 3; i++) {
                    if (m_plateau[i+4*j] == 0 && m_plateau[i+1+4*j] != 0) {
                        m_plateau[i+4*j] = m_plateau[i+1+4*j];
                        m_plateau[i+1+4*j] = 0;
                        w_modif = true;
                        w_hadModif=true;
                    }
                    if (m_plateau[i+4*j] == m_plateau[i + 1+4*j] && m_plateau[i+4*j] != 0) {
                        m_plateau[i+4*j] += 1;
                        m_plateau[i + 1+4*j] = 0;
                        w_modif = true;
                        w_hadModif=true;
                    }
                }
            }
        }
        if (w_hadModif) {
            creationBloc();
            _majScore();
        }
    }

    public void mvtDroite() {
        Log.d(TAG,"appel de mvtDroite");
        boolean w_modif,w_hadModif;
        w_hadModif=false;
        for(int j=0; j<4; j++){
            w_modif = true;
            while (w_modif) {
                w_modif = false;
                for (int i = 3; i > 0; i--) {
                    if (m_plateau[i+4*j] == 0 && m_plateau[i-1+4*j] != 0) {
                        m_plateau[i+4*j] = m_plateau[i-1+4*j];
                        m_plateau[i-1+4*j] = 0;
                        w_modif = true;
                        w_hadModif=true;
                    }
                    if (m_plateau[i+4*j] == m_plateau[i-1+4*j] && m_plateau[i+4*j] != 0) {
                        m_plateau[i+4*j] += 1;
                        m_plateau[i-1+4*j] = 0;
                        w_modif = true;
                        w_hadModif=true;
                    }
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

        Log.d(TAG,"-- nbCasesVides "+w_countVide);
        if(w_countVide != 0){
            int newPosition = 1 + w_r.nextInt(w_countVide);
            Log.d(TAG,"-- posNouveauBloc "+newPosition);
            w_countVide = 0;
            for (int i=0;i<4;i++) {
                for (int j = 0; j < 4; j++) {
                    if (m_plateau[i + 4 * j] == 0) {
                        w_countVide++;
                    }
                    if (w_countVide == newPosition) {
                        int random5 = w_r.nextInt(4);
                        if (random5==3) {
                            m_plateau[i + 4 * j] = 2;
                        } else {
                            m_plateau[i + 4 * j] = 1;
                        }

                        w_countVide++;
                    }
                }
            }
        }
        Log.d(TAG,"-- fin appel creationBloc");
    }


    public void restart() {
        Log.d(TAG,"appel de restart");
        m_score = 0;
        m_plateau = new int[]{0,0,0,0, 0,0,0,0, 0,0,0,0, 0,0,0,0};
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
        int j=0;
        return m_plateau;
    }
}
