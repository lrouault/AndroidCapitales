package com.lrt.capitales.model.labyrinthe;

import android.graphics.RectF;

import com.lrt.capitales.common.CommonEnum.Type;

public class Bloc {
    private float SIZE = Boule.RAYON * 2;

    private Type mType = null;
    private RectF mRectangle = null;

    public Type getType() {
        return mType;
    }

    public RectF getRectangle() {
        return mRectangle;
    }

    public Bloc(Type pType, int pX, int pY) {
        this.mType = pType;
        this.mRectangle = new RectF(pX * SIZE, pY * SIZE, (pX + 1) * SIZE, (pY + 1) * SIZE);
    }
}
