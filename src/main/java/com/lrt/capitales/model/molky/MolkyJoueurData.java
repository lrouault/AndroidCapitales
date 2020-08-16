package com.lrt.capitales.model.molky;

public class MolkyJoueurData {
    public String m_name;
    public int m_position, m_actualScore, m_previousScore,m_nbZero;
    public Boolean m_myTurn;


    public MolkyJoueurData(String m_name, int m_position, int m_actualScore, int m_previousScore, int m_nbZero, Boolean m_myTurn) {
        this.m_name = m_name;
        this.m_position = m_position;
        this.m_actualScore = m_actualScore;
        this.m_previousScore = m_previousScore;
        this.m_nbZero = m_nbZero;
        this.m_myTurn = m_myTurn;
    }
}
