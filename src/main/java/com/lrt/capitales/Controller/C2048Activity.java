package com.lrt.capitales.Controller;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lrt.capitales.Model.Game2048;
import com.lrt.capitales.R;
import com.lrt.capitales.View.OnSwipeListener;
import com.lrt.capitales.Common.commonEnum;

import java.util.StringTokenizer;

import static java.lang.Math.pow;

public class C2048Activity extends AppCompatActivity implements View.OnTouchListener {
    private static final String TAG = "C2048Activity";
    private static final int C_CASES_X = 4;
    private static final int C_CASES_Y = 4;

    // Sauvegarde du plateau a la fermeture de l'app
    private SharedPreferences m_sharedPreferences;

    // Model
    private Game2048 m_Game2048;

    // Gestion des deplacements des tuiles
    private GestureDetector m_gestureDetector;

    // Plateau de jeu (0123 / 4567 / 891011 / 12131415 )
    private TextView m_2048btn[] = new TextView[C_CASES_X * C_CASES_Y];

    // Layout et boutons actionnables
    private TextView m_txtScore, m_txtBestScore;
    private Button m_btnUndo;

    // Couleurs des tuiles
    private int m_colorArray[] = new int[]{R.color.btn2048_1,R.color.btn2048_2,R.color.btn2048_3,
            R.color.btn2048_4,R.color.btn2048_5,R.color.btn2048_6,
            R.color.btn2048_7,R.color.btn2048_8,R.color.btn2048_9,
            R.color.btn2048_10,R.color.btn2048_11};

    // Instanciation du jeu
    // Listener sur le layout
    // Button listener
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_2048);

        // Instantiation du plateau avec celle sauvegardee si existante
        m_sharedPreferences = getPreferences(MODE_PRIVATE);
        int w_savedBestScore = m_sharedPreferences.getInt("C2048_bestScore",0);
        String w_strSavedPlateau = m_sharedPreferences.getString("C2048_plateau","");
        Log.d(TAG,"Plateau sauvegarde :"+w_strSavedPlateau);
        Log.d(TAG,"Score   sauvegarde :"+w_savedBestScore);
        if (w_strSavedPlateau!="") {
            StringTokenizer st = new StringTokenizer(w_strSavedPlateau, ",");
            int[] w_savedPlateau = new int[C_CASES_X * C_CASES_Y];
            for (int i = 0; i < C_CASES_X * C_CASES_Y; i++) {
                w_savedPlateau[i] = Integer.parseInt(st.nextToken());
            }
            m_Game2048 = new Game2048(w_savedPlateau,w_savedBestScore);
        } else {
            m_Game2048 = new Game2048(w_savedBestScore);
        }

        // Cablage de la vue
        m_txtScore = findViewById(R.id.activity_2048_txtScore);
        m_txtBestScore = findViewById(R.id.activity_2048_txtBestScore);
        m_btnUndo = findViewById(R.id.activity_2048_btnUndo);

        int i = 0;
        m_2048btn[i++] = findViewById(R.id.activity_2048_btn00);
        m_2048btn[i++] = findViewById(R.id.activity_2048_btn10);
        m_2048btn[i++] = findViewById(R.id.activity_2048_btn20);
        m_2048btn[i++] = findViewById(R.id.activity_2048_btn30);
        m_2048btn[i++] = findViewById(R.id.activity_2048_btn01);
        m_2048btn[i++] = findViewById(R.id.activity_2048_btn11);
        m_2048btn[i++] = findViewById(R.id.activity_2048_btn21);
        m_2048btn[i++] = findViewById(R.id.activity_2048_btn31);
        m_2048btn[i++] = findViewById(R.id.activity_2048_btn02);
        m_2048btn[i++] = findViewById(R.id.activity_2048_btn12);
        m_2048btn[i++] = findViewById(R.id.activity_2048_btn22);
        m_2048btn[i++] = findViewById(R.id.activity_2048_btn32);
        m_2048btn[i++] = findViewById(R.id.activity_2048_btn03);
        m_2048btn[i++] = findViewById(R.id.activity_2048_btn13);
        m_2048btn[i++] = findViewById(R.id.activity_2048_btn23);
        m_2048btn[i]   = findViewById(R.id.activity_2048_btn33);

        // Creation et affichage du premier bloc
        _majAffichage();

        // Gestion des mouvements
        LinearLayout w_2048blocsLayout = findViewById(R.id.activity_2048_btnLayout);
        w_2048blocsLayout.setOnTouchListener(this);
        m_gestureDetector = new GestureDetector(this, new OnSwipeListener() {
            @Override
            public boolean onSwipe(commonEnum.Direction direction) {
                Log.d(TAG, "appel de Overriden on swipe");
                m_Game2048.onMouvement(direction);
                _majAffichage();
                _majSavedPlateau();
                return true;
            }
        }); /// END gestureDetector

        // Restart
        Button m_btnRestart = findViewById(R.id.activity_2048_btnRestart);
        m_btnRestart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                m_Game2048.restart();
                m_Game2048.creationBloc();
                _majAffichage();
                _majSavedPlateau();
            }
        });

        // Undo
        m_btnUndo.setEnabled(false);
        m_btnUndo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                m_Game2048.undo();
                _majAffichage();
                _majSavedPlateau();
            }
        });
    }

    // Gestion des mouvements tactiles
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        m_gestureDetector.onTouchEvent(event);
        return true;
    }

    // Recuperation des infos du model pour l'affichage
    // Score / Meilleur score / Grille
    private void _majAffichage() {
        Log.d(TAG,"appel de _majAffichage");
        // Plateau
        int w_plateau[] = m_Game2048.getM_plateau();
        for(int i = 0; i< C_CASES_X * C_CASES_Y; i++) {
            if(w_plateau[i]==0) {
                m_2048btn[i].setText("");
                m_2048btn[i].setBackgroundResource(R.color.btn2048_0);
            } else {
                String w_str = ((Integer) (int) pow(2, w_plateau[i])).toString();
                m_2048btn[i].setText(w_str);
                m_2048btn[i].setBackgroundResource(m_colorArray[w_plateau[i]%m_colorArray.length]);
            }
        }
        // Undo
        if (m_Game2048.isM_enableUndo()){
            m_btnUndo.setTextColor(getResources().getColor(R.color.txt2048_OK));
            m_btnUndo.setEnabled(true);
        } else {
            m_btnUndo.setTextColor(getResources().getColor(R.color.txt2048_KO));
            m_btnUndo.setEnabled(false);
        }

        // Score
        String w_strScore = "Score : \n\n"+m_Game2048.getM_score();
        String w_strBestScore = "Best score : \n\n"+m_Game2048.getM_bestScore();
        m_txtScore.setText(w_strScore);
        m_txtBestScore.setText(w_strBestScore);
    } // END _majAffichage

    private void _majSavedPlateau() {
        Log.d(TAG,"appel de _majSavedPlateau");
        int[] w_plateau = m_Game2048.getM_plateau();
        StringBuilder w_strSavedPlateau = new StringBuilder();
        for (int i = 0; i < w_plateau.length; i++) {
            w_strSavedPlateau.append(w_plateau[i]).append(",");
        }
        m_sharedPreferences.edit().putString("C2048_plateau", w_strSavedPlateau.toString()).apply();
        m_sharedPreferences.edit().putInt("C2048_bestScore", m_Game2048.getM_bestScore()).apply();
    }
}
