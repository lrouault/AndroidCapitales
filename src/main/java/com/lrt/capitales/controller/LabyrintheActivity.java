package com.lrt.capitales.controller;

import java.util.List;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.lrt.capitales.model.labyrinthe.Bloc;
import com.lrt.capitales.model.labyrinthe.Boule;
import com.lrt.capitales.view.LabyrintheView;

public class LabyrintheActivity extends Activity {
    private static final String TAG = "LabyrintheActivity"; // pour les logs

    // Identifiant de la boîte de dialogue de victoire
    public static final int VICTORY_DIALOG = 0;
    // Identifiant de la boîte de dialogue de défaite
    public static final int DEFEAT_DIALOG = 1;

    // Le moteur graphique du jeu
    private LabyrintheView mView = null;
    // Le moteur physique du jeu
    private LabyrintheEngine mEngine = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "appel de onCreate");
        super.onCreate(savedInstanceState);

        mView = new LabyrintheView(this);
        setContentView(mView);

        mEngine = new LabyrintheEngine(this);

        Boule b = new Boule();
        mView.setBoule(b);
        mEngine.setBoule(b);

        // Attente de la creation de la vue pour obtenir ses dimensions
        // -> onDimensionSet
    }

    public void onDimensionSet() {
        Log.d(TAG, "appel de onDimensionSet");
        //List<Bloc> mList = mEngine.buildLabyrinthe(mView.getWidth(), mView.getHeight());
        List<Bloc> mList = mEngine.lectureFichier(mView.getWidth(), mView.getHeight());
        mView.setBlocks(mList);
    }

    @Override
    protected void onResume() {
        Log.d(TAG, "appel de onResume");
        super.onResume();
        mEngine.resume();
    }

    @Override
    protected void onPause() {
        Log.d(TAG, "appel de onPause");
        super.onStop();
        mEngine.stop();
    }

    @Override
    public Dialog onCreateDialog (int id) {
        Log.d(TAG, "appel de onCreateDialog");
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        switch(id) {
            case VICTORY_DIALOG:
                builder.setCancelable(false)
                        .setMessage("Bravo, vous avez gagné !")
                        .setTitle("Champion ! ")
                        .setNegativeButton("Recommencer", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mEngine.reset();
                                mEngine.resume();
                            }
                        })
                        .setPositiveButton("Quitter", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                Handler handler = new Handler();
                                handler.postDelayed(new Runnable() {
                                    public void run() {
                                        finish();
                                    }
                                }, 500);
                            }
                        });
                break;

            case DEFEAT_DIALOG:
                builder.setCancelable(false)
                        .setMessage("Il faut tenir le téléphone avec douceur !")
                        .setTitle("Bah bravo !")
                        .setNegativeButton("Recommencer", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mEngine.reset();
                                mEngine.resume();
                            }
                        })
                        .setPositiveButton("Quitter", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                Handler handler = new Handler();
                                handler.postDelayed(new Runnable() {
                                    public void run() {
                                        finish();
                                    }
                                }, 500);
                            }
                        });
        }
        return builder.create();
    }

    @Override
    public void onPrepareDialog (int id, Dialog box) {
        Log.d(TAG, "appel de onPrepareDialog");
        // A chaque fois qu'une boîte de dialogue est lancée, on arrête le moteur physique
        mEngine.stop();
    }
}
