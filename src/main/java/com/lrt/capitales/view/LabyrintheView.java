package com.lrt.capitales.view;


import java.util.List;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.lrt.capitales.R;
import com.lrt.capitales.controller.LabyrintheActivity;
import com.lrt.capitales.model.labyrinthe.Bloc;
import com.lrt.capitales.model.labyrinthe.Boule;

public class LabyrintheView extends SurfaceView implements SurfaceHolder.Callback {
    private static final String TAG = "LabyrintheView"; // pour les logs

    Boule mBoule;
    public Boule getBoule() {
        return mBoule;
    }

    public void setBoule(Boule pBoule) {
        this.mBoule = pBoule;
    }

    SurfaceHolder mSurfaceHolder;
    DrawingThread mThread;
    private LabyrintheActivity mActivity = null;

    private List<Bloc> mBlocks = null;
    public List<Bloc> getBlocks() {
        return mBlocks;
    }

    public void setBlocks(List<Bloc> pBlocks) {
        this.mBlocks = pBlocks;
    }

    Paint mPaint;

    //public LabyrintheView(LabyrintheActivity pView, Context pContext) {
    public LabyrintheView(Context pContext) {
        super(pContext);
        Log.d(TAG, "appel du constructeur");
        mActivity = (LabyrintheActivity) pContext;
        mSurfaceHolder = getHolder();
        mSurfaceHolder.addCallback(this);
        mThread = new DrawingThread();

        mPaint = new Paint();
        mPaint.setStyle(Paint.Style.FILL);

        mBoule = new Boule();
    }

    @Override
    protected void onDraw(Canvas pCanvas) {
        // Dessiner le fond de l'écran en premier
        pCanvas.drawColor(getResources().getColor(R.color.btn2048_3));
        if(mBlocks != null) {
            // Dessiner tous les blocs du labyrinthe
            for(Bloc b : mBlocks) {
                switch(b.getType()) {
                    case DEPART:
                        mPaint.setColor(getResources().getColor(R.color.txt2048_OK));
                        break;
                    case ARRIVEE:
                        mPaint.setColor(getResources().getColor(R.color.btn2048_5));
                        break;
                    case TROU:
                        mPaint.setColor(getResources().getColor(R.color.btn2048_0));
                        break;
                    case MUR:
                        mPaint.setColor(Color.GREEN);
                        break;
                    case TRAMPO:
                        mPaint.setColor(Color.GRAY);
                        break;
                }
                pCanvas.drawRect(b.getRectangle(), mPaint);
            }
        }

        // Dessiner la boule
        if(mBoule != null) {
            mPaint.setColor(mBoule.getCouleur());
            pCanvas.drawCircle(mBoule.getX(), mBoule.getY(), Boule.RAYON, mPaint);
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder pHolder, int pFormat, int pWidth, int pHeight) {
        Log.d(TAG, "appel de surfaceChanged");
        //
    }

    @Override
    public void surfaceCreated(SurfaceHolder pHolder) {
        Log.d(TAG, "appel de surfaceCreated");
        mThread.keepDrawing = true;
        mThread.start();
        mActivity.onDimensionSet();
        // Quand on crée la boule, on lui indique les coordonnées de l'écran
        if(mBoule != null ) {
            this.mBoule.setHeight(getHeight());
            this.mBoule.setWidth(getWidth());
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder pHolder) {
        Log.d(TAG, "appel de surfaceDestroyed");
        mThread.keepDrawing = false;
        boolean retry = true;
        while (retry) {
            try {
                mThread.join();
                retry = false;
            } catch (InterruptedException e) {}
        }

    }

    private class DrawingThread extends Thread {
        boolean keepDrawing = true;

        @Override
        public void run() {
            Canvas canvas;
            while (keepDrawing) {
                canvas = null;

                try {
                    canvas = mSurfaceHolder.lockCanvas();
                    synchronized (mSurfaceHolder) {
                        onDraw(canvas);
                    }
                } finally {
                    if (canvas != null)
                        mSurfaceHolder.unlockCanvasAndPost(canvas);
                }

                // Pour dessiner à 50 fps
                try {
                    Thread.sleep(20);
                } catch (InterruptedException e) {}
            }
        }
    }
}
