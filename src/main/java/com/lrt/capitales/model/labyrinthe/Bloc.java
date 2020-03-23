package com.lrt.capitales.model.labyrinthe;

import android.graphics.RectF;

import com.lrt.capitales.common.CommonEnum.Type;

public class Bloc {
    // ATTRIBUTS
    private float _SIZE = Boule.RAYON * 2;

    private Type m_type = null;
    private RectF m_rectangle = null;

    // CONSTRUCTEUR
    public Bloc(Type ai_type, int ai_pX, int ai_pY) {

        this.m_type = ai_type;
        this.m_rectangle = new RectF(ai_pX * _SIZE, ai_pY * _SIZE, (ai_pX + 1) * _SIZE, (ai_pY + 1) * _SIZE);
    }

    public Bloc(Type ai_type, int ai_pX, int ai_pY, float ai_sizeX, float ai_sizeY) {
        this.m_type = ai_type;
        this.m_rectangle = new RectF(ai_pX * ai_sizeX, ai_pY * ai_sizeY, (ai_pX + 1) * ai_sizeX, (ai_pY + 1) * ai_sizeY);
    }

    // GETTER SETTER
    public Type getType() {
        return m_type;
    }
    public RectF getRectangle() {
        return m_rectangle;
    }
}
