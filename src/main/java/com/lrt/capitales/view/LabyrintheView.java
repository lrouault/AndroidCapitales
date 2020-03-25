package com.lrt.capitales.view;


import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
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

    public void setBoule(Boule pBoule) {
        this.mBoule = pBoule;
    }

    final SurfaceHolder mSurfaceHolder;
    DrawingThread mThread;
    private LabyrintheActivity mActivity; // Initialise dans le constructeur

    private List<Bloc> mBlocks = null;

    private Bitmap m_bitmapUp = null;
    private Bitmap m_bitmapDown = null;
    private Bitmap m_bitmapLeft = null;
    private Bitmap m_bitmapRight = null;

    public void setBlocks(List<Bloc> pBlocks) {
        this.mBlocks = pBlocks;
        _setBitmap();
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
                        pCanvas.drawRect(b.getRectangle(), mPaint);
                        break;
                    case ARRIVEE:
                        mPaint.setColor(getResources().getColor(R.color.btn2048_5));
                        pCanvas.drawRect(b.getRectangle(), mPaint);
                        break;
                    case TROU:
                        mPaint.setColor(getResources().getColor(R.color.btn2048_0));
                        pCanvas.drawRect(b.getRectangle(), mPaint);
                        break;
                    case MUR:
                        mPaint.setColor(Color.GREEN);
                        pCanvas.drawRect(b.getRectangle(), mPaint);
                        break;
                    case TRAMPO:
                        mPaint.setColor(Color.GRAY);
                        pCanvas.drawRect(b.getRectangle(), mPaint);
                        break;
                    case SPEED_H:
                        if(m_bitmapUp!=null) pCanvas.drawBitmap(m_bitmapUp, b.getRectangle().left, b.getRectangle().top, null);
                        break;
                    case SPEED_B:
                        if(m_bitmapDown!=null) pCanvas.drawBitmap(m_bitmapDown, b.getRectangle().left, b.getRectangle().top, null);
                        break;
                    case SPEED_G:
                        if(m_bitmapLeft!=null) pCanvas.drawBitmap(m_bitmapLeft, b.getRectangle().left, b.getRectangle().top, null);
                        break;
                    case SPEED_D:
                        // TODO changer le fond (fleche)
                        if(m_bitmapRight!=null) pCanvas.drawBitmap(m_bitmapRight, b.getRectangle().left, b.getRectangle().top, null);
                        break;
                }
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
            } catch (InterruptedException e) {
                Log.e(TAG, "surfaceDestroyed: Join thread KO");
            }
        }

    }

    private void _setBitmap() {

        Matrix matrix = new Matrix();

        Bitmap w_bitmap = BitmapFactory.decodeResource(this.getResources(), R.drawable.pushpin_blue);
        float w_blocX = mBlocks.get(0).getRectangle().width();
        float w_blocY = mBlocks.get(0).getRectangle().height();

        m_bitmapDown = Bitmap.createScaledBitmap(w_bitmap, (int) w_blocX, (int)w_blocY, true);

        matrix.setRotate(90);
        m_bitmapLeft = Bitmap.createBitmap(w_bitmap, 0, 0, w_bitmap.getWidth(), w_bitmap.getHeight(), matrix, true);
        m_bitmapLeft = Bitmap.createScaledBitmap(m_bitmapLeft, (int) w_blocX, (int)w_blocY, true);

        matrix.setRotate(180);
        m_bitmapUp = Bitmap.createBitmap(w_bitmap, 0, 0, w_bitmap.getWidth(), w_bitmap.getHeight(), matrix, true);
        m_bitmapUp = Bitmap.createScaledBitmap(m_bitmapUp, (int) w_blocX, (int)w_blocY, true);

        matrix.setRotate(-90);
        m_bitmapRight = Bitmap.createBitmap(w_bitmap, 0, 0, w_bitmap.getWidth(), w_bitmap.getHeight(), matrix, true);
        m_bitmapRight = Bitmap.createScaledBitmap(m_bitmapRight, (int) w_blocX, (int)w_blocY, true);


    }

    private class DrawingThread extends Thread {
        boolean keepDrawing = true;

        @SuppressLint("WrongCall")
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
                } catch (InterruptedException e) {
                    Log.e(TAG, "run: thread sleep KO");
                }
            }
        }
    }
}
