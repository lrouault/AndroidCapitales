package com.lrt.capitales.controller;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.lrt.capitales.model.Game2048;
import com.lrt.capitales.R;
import com.lrt.capitales.view.OnSwipeListener;
import com.lrt.capitales.common.CommonEnum;

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
            public boolean onSwipe(CommonEnum.Direction direction) {
                Log.d(TAG, "appel de Overriden on swipe");
                m_Game2048.onMouvement(direction);
                _majAffichage();
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
            }
        });

        // Undo
        m_btnUndo.setEnabled(false);
        m_btnUndo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                m_Game2048.undo();
                _majAffichage();
            }
        });
    } // END onCreate()

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu_2048option, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){

            case R.id.menu_2048optionUndo:
                _showDialogUndo();
                break;

            case R.id.menu_2048optionFreq4:
                _showDialogFreq4();
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onStop() {
        super.onStop();
        _majSavedPlateau();
    }

    // Gestion des mouvements tactiles
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        m_gestureDetector.onTouchEvent(event);
        return true;
    }

    private void _showDialogUndo() {
        final TextView w_text=new TextView(this);
        w_text.setPadding(10, 10, 10, 10);

        final SeekBar w_seek=new SeekBar(this);
        w_seek.setMax(m_Game2048.C_UNDO_MAX);
        w_seek.setProgress(m_Game2048.getM_nbUndoMax());
        w_text.setText("Apres validation : " + m_Game2048.getM_nbUndoMax()+" (defaut : 3)");
        w_seek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                w_text.setText("Apres validation : " + progress+" (defaut : 3)");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        final AlertDialog.Builder w_alert = new AlertDialog.Builder(this);
        LinearLayout w_linear=new LinearLayout(this);
        w_linear.setOrientation(LinearLayout.VERTICAL);
        w_linear.addView(w_seek);
        w_linear.addView(w_text);

        w_alert.setTitle("Nombre maximum de undo");
        w_alert.setView(w_linear);

        w_alert.setPositiveButton("Ok",new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog,int id)
            {
                m_Game2048.setM_nbUndoMax(w_seek.getProgress());
                _majAffichage(); // maj du bouton undo
            }
        });
        w_alert.show();
    }

    private void _showDialogFreq4() {
        final TextView w_text=new TextView(this);
        w_text.setPadding(10, 10, 10, 10);

        final SeekBar w_seek=new SeekBar(this);
        w_seek.setMax(9);
        w_seek.setProgress(m_Game2048.getM_freq4());
        w_text.setText("Apres validation  1 sur "+ m_Game2048.getM_freq4()+" (defaut : 1/4)");
        w_seek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                w_text.setText("Apres validation 1 sur "+ (progress+1)+" (defaut : 1/4)");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        final AlertDialog.Builder w_alert = new AlertDialog.Builder(this);
        LinearLayout w_linear=new LinearLayout(this);
        w_linear.setOrientation(LinearLayout.VERTICAL);
        w_linear.addView(w_seek);
        w_linear.addView(w_text);

        w_alert.setTitle("Frequence d'appartition du 4");
        w_alert.setView(w_linear);

        w_alert.setPositiveButton("Ok",new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog,int id)
            {
                m_Game2048.setM_freq4(w_seek.getProgress()+1);
            }
        });
        w_alert.show();
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
        if (m_Game2048.getM_nbEnableUndo() > 0) {
            m_btnUndo.setText("Undo ("+m_Game2048.getM_nbEnableUndo()+")");
            m_btnUndo.setTextColor(getResources().getColor(R.color.txt2048_OK));
            m_btnUndo.setEnabled(true);
        } else {
            m_btnUndo.setText("Undo");
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
