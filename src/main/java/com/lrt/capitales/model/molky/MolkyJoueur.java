package com.lrt.capitales.model.molky;

import com.lrt.capitales.model.labyrinthe.Bloc;

import java.util.ArrayList;

public class MolkyJoueur {
    private static final String TAG = "MolkyJoueur";

    private String m_name;
    ArrayList<Integer> m_score = new ArrayList<>();
    private Integer m_nbZero = 0;
    private Integer m_maxNbZero = 3;


    private  Boolean m_isDownForOther = false;
    private boolean m_hasWon = false;

    public MolkyJoueur(String ai_name) {
        m_name = ai_name;
        clearScore();
    }

    public MolkyJoueur(MolkyJoueur ai_copy) {
        m_name = ai_copy.m_name;
        m_nbZero = ai_copy.m_nbZero;
        m_maxNbZero = ai_copy.m_maxNbZero;
        m_isDownForOther = ai_copy.m_isDownForOther;
        m_hasWon = ai_copy.m_hasWon;
        m_score = new ArrayList<>(ai_copy.m_score);
    }

    public void newLancer(Integer ai_lancer) {
        m_isDownForOther = false;
        if (ai_lancer == 0) {
            m_nbZero += 1;
            if (m_nbZero == m_maxNbZero) {
                clearScore();
            }
        } else {
            m_score.add(m_score.get(m_score.size()-1) + ai_lancer);
            if (getLastScore() > 40) {
                m_score.clear();
                m_score.add(20);
            } else if (getLastScore() == 40)  {
                m_hasWon = true;
            } else {
                m_isDownForOther = true;
            }
        }
    }

    public void otherScore(Integer ai_otherScore) {
        if (m_score.size() > 1) {
            int w_size = m_score.size()-1;
            if (m_score.get(w_size) == ai_otherScore) {
                m_score.remove(w_size);
            }
        }
    }

    public int getLastScore() {
        if (!m_score.isEmpty()) {
            return m_score.get(m_score.size() -1);
        }
        return 0;
    }

    public int getPreviousScore() {
        if (m_score.size() > 1) {
            return m_score.get(m_score.size() -2);
        }
        return m_score.get(0);
    }

    public String getText() {
        String w_text = m_name + " -- "+ getLastScore()  +"\n";
        w_text += "--> Previous : "+ getPreviousScore()  +" NbZero : "+  m_nbZero +"\n";

        return w_text;
    }

    public void clearScore() {
        m_score.clear();
        m_score.add(0);
        m_nbZero = 0;
        m_hasWon = false;
    }


    public Boolean getIsDownForOther() {
        return m_isDownForOther;
    }
    public Boolean getHasWon() {
        return m_hasWon;
    }
}