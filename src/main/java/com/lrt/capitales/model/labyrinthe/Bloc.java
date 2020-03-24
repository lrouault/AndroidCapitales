package com.lrt.capitales.model.labyrinthe;

import android.graphics.RectF;

import com.lrt.capitales.common.CommonEnum.Type;

public class Bloc {
    // ATTRIBUTS
    private float _SIZE = Boule.RAYON * 2;

    private Type m_type = null;
    // Position dans la grille de bloc
    private int m_pX, m_pY;
    private RectF m_rectangle = null;

    // CONSTRUCTEUR
    public Bloc(Type ai_type, int ai_pX, int ai_pY) {
        m_type = ai_type;
        m_pX = ai_pX;
        m_pY = ai_pY;
    }

    public Bloc(Type ai_type, int ai_pX, int ai_pY, float ai_sizeX, float ai_sizeY) {
        m_type = ai_type;
        m_pX = ai_pX;
        m_pY = ai_pY;
        m_rectangle = new RectF(ai_pX * ai_sizeX, ai_pY * ai_sizeY, (ai_pX + 1) * ai_sizeX, (ai_pY + 1) * ai_sizeY);
    }

    // GETTER SETTER
    public Type getType() {
        return m_type;
    }
    public RectF getRectangle() {return m_rectangle;}

    public void setSize(float ai_sizeX, float ai_sizeY) {
        m_rectangle = new RectF(m_pX * ai_sizeX, m_pY * ai_sizeY, (m_pX + 1) * ai_sizeX, (m_pY + 1) * ai_sizeY);
    }
}
