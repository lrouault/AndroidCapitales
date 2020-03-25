package com.lrt.capitales.controller;

import java.util.List;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.LongDef;
import android.util.Log;
import android.view.WindowManager;

import com.lrt.capitales.model.labyrinthe.Bloc;
import com.lrt.capitales.model.labyrinthe.Boule;
import com.lrt.capitales.model.labyrinthe.LabyrintheBank;
import com.lrt.capitales.view.LabyrintheView;

// TODO niveaux de difficulté en changeant les vitesses de detection / rebond

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
    // La base de donnees de niveaux
    private LabyrintheBank m_bank = null;
    private int m_indexLabyrinthe = -1;
    private int m_difficulte = 1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "appel de onCreate");
        super.onCreate(savedInstanceState);

        // Récupération des infos de niveau
        m_difficulte = (int)(getIntent().getSerializableExtra("Difficulte"));
        m_indexLabyrinthe  = (int) getIntent().getSerializableExtra("Niveau")-1;

        // L'ecran ne passe pas en veille
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        mView = new LabyrintheView(this);
        setContentView(mView);

        mEngine = new LabyrintheEngine(this);
        m_bank = new LabyrintheBank();

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
        if (m_indexLabyrinthe+1 < m_bank.getM_listeDeLabyrinthe().size()) {
            m_indexLabyrinthe++;
        } else {
            m_indexLabyrinthe =0;
        }
        Log.d(TAG, "_setNextLabyrinthe: lecture "+m_indexLabyrinthe+" / "+m_bank.getM_listeDeLabyrinthe().size());
        List<Bloc> mList = m_bank.getM_listeDeLabyrinthe().get(m_indexLabyrinthe);
        mEngine.setBlocks(mList);
        mView.setBlocks(mList);
    }
}
