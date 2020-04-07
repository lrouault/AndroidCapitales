package com.lrt.capitales.controller;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.WindowManager;

import com.lrt.capitales.model.labyrinthe.Bloc;
import com.lrt.capitales.model.labyrinthe.Boule;
import com.lrt.capitales.model.labyrinthe.Lab2048Bank;
import com.lrt.capitales.model.labyrinthe.LabyrintheBank;
import com.lrt.capitales.view.Lab2048View;
import com.lrt.capitales.view.LabyrintheView;

import java.util.List;

// TODO niveaux de difficulté en changeant les vitesses de detection / rebond

public class Lab2048Activity extends Activity {
    private static final String TAG = "Lab2048Activity"; // pour les logs

    // Identifiant de la boîte de dialogue de victoire
    public static final int VICTORY_DIALOG = 0;
    // Identifiant de la boîte de dialogue de défaite
    public static final int DEFEAT_DIALOG = 1;

    // Le moteur graphique du jeu
    private Lab2048View mView = null;
    // Le moteur physique du jeu
    private Lab2048Engine mEngine = null;
    // La base de donnees de niveaux
    private Lab2048Bank m_bank = null;
    private int m_indexLabyrinthe ;
    private int m_difficulte;
    private int m_categorie;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "appel de onCreate");
        super.onCreate(savedInstanceState);

        // Récupération des infos de niveau
        m_difficulte = 1;
        m_categorie  = 0;
        m_indexLabyrinthe  = 0;

        // L'ecran ne passe pas en veille
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        mView = new Lab2048View(this);
        setContentView(mView);

        mEngine = new Lab2048Engine(this);
        m_bank = new Lab2048Bank();

        Boule b = new Boule();
        mView.setBoule(b);
        mEngine.setBoule(b);
        mEngine.setDifficulty(m_difficulte);

        // Attente de la creation de la vue pour obtenir ses dimensions
        // -> onDimensionSet
    }

    public void onDimensionSet() {
        Log.d(TAG, "appel de onDimensionSet");
        //List<Bloc> mList = mEngine.buildLabyrinthe(mView.getWidth(), mView.getHeight());
        m_bank.setScreenWidthHeight(mView.getWidth(), mView.getHeight());
        //List<Bloc> mList = mEngine.lectureFichier();
        m_bank.lectureFichier(getBaseContext().getAssets());
        _setNextLabyrinthe();
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
        super.onPause();
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
                        .setNegativeButton("Continuer", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                _setNextLabyrinthe();
                                mEngine.reset();
                                mEngine.resume();
                            }
                        })
                        .setNeutralButton("Rejouer", new DialogInterface.OnClickListener() {
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

    private void _setNextLabyrinthe() {
        if (m_indexLabyrinthe+1 < m_bank.getM_listeDeCategorieDeLabyrinthe().get(m_categorie).size()) {
            m_indexLabyrinthe++;
        } else {
            m_indexLabyrinthe =0;
        }
        Log.d(TAG, "_setNextLabyrinthe: categorie "+m_categorie+" lecture "+m_indexLabyrinthe+" / "+m_bank.getM_listeDeCategorieDeLabyrinthe().size());
        List<Bloc> mList = m_bank.getM_listeDeCategorieDeLabyrinthe().get(m_categorie).get(m_indexLabyrinthe);
        mEngine.setBlocks(mList);
        mView.setBlocks(mList);
    }
}