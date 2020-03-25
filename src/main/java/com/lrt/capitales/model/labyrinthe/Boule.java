package com.lrt.capitales.model.labyrinthe;

import android.graphics.Color;
import android.graphics.RectF;
import android.util.Log;

import com.lrt.capitales.common.CommonEnum.Direction;

public class Boule {
    private static final String TAG = "Boule"; // pour les logs
    // Rayon de la boule
    public static final int RAYON = 30;

    // Couleur de la boule
    private int mCouleur = Color.GREEN;
    public int getCouleur() {
        return mCouleur;
    }

    // Vitesse maximale autorisée pour la boule
    private static final float MAX_SPEED = 20.0f;

    // Permet à la boule d'accélérer moins vite
    private static final float COMPENSATEUR = 8.0f;

    // Utilisé pour compenser les rebonds
    private static final float REBOND = 1.25f;
    private static final float REBOND_MUR = 10f;

    // Utilisé pour compenser les rebonds
    private static final float ACCELERATION = 0.1f;

    // Le rectangle qui correspond à la position de départ de la boule
    private RectF mInitialRectangle = null;

    // A partir du rectangle initial on détermine la position de la boule
    public void setInitialRectangle(RectF pInitialRectangle) {
        Log.d(TAG, "appel de setInitialRectangle");
        this.mInitialRectangle = pInitialRectangle;
        this.mX = pInitialRectangle.centerX();
        this.mY = pInitialRectangle.centerY();
        Log.d(TAG, "-- (mx,my) -> "+(int) mY+" , "+(int) mX);
    }

    // Le rectangle de collision
    private RectF mRectangle;

    // Coordonnées en x
    private float mX;
    public float getX() {
        return mX;
    }
    public void setPosX(float pPosX) {
        mX = pPosX;

        // Si la boule sort du cadre, on rebondit
        if(mX < RAYON) {
            mX = RAYON;
            // Rebondir, c'est changer la direction de la balle
            mSpeedY = -mSpeedY / REBOND;
        } else if(mX > mWidth - RAYON) {
            mX = mWidth - RAYON;
            mSpeedY = -mSpeedY / REBOND;
        }
    }

    // Coordonnées en y
    private float mY;
    public float getY() {
        return mY;
    }

    public void setPosY(float pPosY) {
        mY = pPosY;
        if(mY < RAYON) {
            mY = RAYON;
            mSpeedX = -mSpeedX / REBOND;
        } else if(mY > mHeight - RAYON) {
            mY = mHeight - RAYON;
            mSpeedX = -mSpeedX / REBOND;
        }
    }

    // Vitesse sur l'axe x, y
    private float mSpeedX = 0;
    private float mSpeedY = 0;

    // Taille de l'écran en hauteur
    private int mHeight = -1;
    public void setHeight(int pHeight) {
        this.mHeight = pHeight;
    }

    // Taille de l'écran en largeur
    private int mWidth = -1;
    public void setWidth(int pWidth) {
        this.mWidth = pWidth;
    }

    public Boule() {
        mRectangle = new RectF();
    }

    // Mettre à jour les coordonnées de la boule
    public RectF putXAndY(float pX, float pY) {
        mSpeedX += pX / COMPENSATEUR;
        if(mSpeedX > MAX_SPEED)
            mSpeedX = MAX_SPEED;
        if(mSpeedX < -MAX_SPEED)
            mSpeedX = -MAX_SPEED;

        mSpeedY += pY / COMPENSATEUR;
        if(mSpeedY > MAX_SPEED)
            mSpeedY = MAX_SPEED;
        if(mSpeedY < -MAX_SPEED)
            mSpeedY = -MAX_SPEED;

        setPosX(mX + mSpeedX);
        setPosY(mY + mSpeedY);

        // Met à jour les coordonnées du rectangle de collision
        mRectangle.set(mX - RAYON, mY - RAYON, mX + RAYON, mY + RAYON);

        return mRectangle;
    }

    // Remet la boule à sa position de départ
    public void reset() {
        Log.d(TAG, "appel de reset");
        mSpeedX = 0;
        mSpeedY = 0;
        this.mX = mInitialRectangle.left + RAYON;
        this.mY = mInitialRectangle.top + RAYON;
        Log.d(TAG, "-- (mx,my) -> "+(int) mY+" , "+(int) mX);
    }

    public void rebond(Direction ai_dir, float ai_pos) {
        switch (ai_dir) {
            case UP :
                mY = ai_pos + RAYON;
                mSpeedY = -mSpeedY / REBOND;
                break;
            case DOWN:
                mY = ai_pos - RAYON;
                mSpeedY = -mSpeedY / REBOND;
                break;
            case LEFT :
                mX = ai_pos + RAYON;
                mSpeedX = -mSpeedX / REBOND;
                break;
            case RIGHT:
                mX = ai_pos - RAYON;
                mSpeedX = -mSpeedX / REBOND;
                break;
        }
        // Mise a jour les coordonnées du rectangle de collision
        mRectangle.set(mX - RAYON, mY - RAYON, mX + RAYON, mY + RAYON);
    }

    public void mur(Direction ai_dir, float ai_pos) {
        switch (ai_dir) {
            case UP :
                mY = ai_pos + RAYON + 1;
                mSpeedY = -mSpeedY /REBOND_MUR;
                break;
            case DOWN:
                mY = ai_pos - RAYON - 1;
                mSpeedY = -mSpeedY /REBOND_MUR;
                break;
            case LEFT :
                mX = ai_pos + RAYON + 1;
                mSpeedX = -mSpeedX /REBOND_MUR;
                break;
            case RIGHT:
                mX = ai_pos - RAYON - 1;
                mSpeedX = -mSpeedX /REBOND_MUR;
                break;
        }
        // Mise a jour les coordonnées du rectangle de collision
        mRectangle.set(mX - RAYON, mY - RAYON, mX + RAYON, mY + RAYON);
    }

    public void accelerateur(Direction ai_dir) {
        switch (ai_dir) {
            case UP :
                mSpeedY -= ACCELERATION;
                break;
            case DOWN:
                mSpeedY += ACCELERATION;
                break;
            case LEFT :
                mSpeedX -= ACCELERATION;
                break;
            case RIGHT:
                mSpeedX += ACCELERATION;
                break;
        }
        if (mSpeedX < -MAX_SPEED) mSpeedX = -MAX_SPEED;
        if (mSpeedX >  MAX_SPEED) mSpeedX =  MAX_SPEED;
        if (mSpeedY < -MAX_SPEED) mSpeedY = -MAX_SPEED;
        if (mSpeedY >  MAX_SPEED) mSpeedY =  MAX_SPEED;
    }
}
