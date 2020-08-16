package com.lrt.capitales.controller;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.lrt.capitales.R;
import com.lrt.capitales.common.CommonEnum;
import com.lrt.capitales.model.Game2048;
import com.lrt.capitales.model.molky.MolkyJoueurBank;
import com.lrt.capitales.view.OnSwipeListener;

import java.util.StringTokenizer;

import static java.lang.Math.pow;

public class MolkyActivity extends AppCompatActivity  {
    private static final String TAG = "MolkyActivity";

    // Sauvegarde du plateau a la fermeture de l'app
    private SharedPreferences m_sharedPreferences;

    // Model
    private MolkyJoueurBank m_joueurBank, m_previousJoueurBank, m_previousJoueurBank2;

    // Gestion des deplacements des tuiles
    private GestureDetector m_gestureDetector;

    // Plateau de jeu (0123 / 4567 / 891011 / 12131415 )
    private TextView m_molkyBtn[] = new TextView[13];

    // Layout et boutons actionnables
    private TextView m_txtScore;
    private Button m_btnClear;
    private Button m_btnAddPlayer;
    private Button m_btnRestart;
    private Button m_btnUndo;

    private int m_nbUndo = 0;
    private int m_nbUndoMax = 2;


    /** onCreate()
     * Instanciation du jeu
     * Listener sur le layout
     * Button listener
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        m_joueurBank = new MolkyJoueurBank();
        m_previousJoueurBank = new MolkyJoueurBank(m_joueurBank);


        setContentView(R.layout.activity_molky);

        // Instantiation du plateau avec celle sauvegardee si existante
        m_sharedPreferences = getPreferences(MODE_PRIVATE);

        // Cablage de la vue
        m_txtScore = findViewById(R.id.activity_molky_txtScore);
        m_btnClear = findViewById(R.id.activity_molky_btnClear);
        m_btnAddPlayer = findViewById(R.id.activity_molky_btnAddPlayer);
        m_btnRestart = findViewById(R.id.activity_molky_btnRestart);
        m_btnUndo = findViewById(R.id.activity_molky_btnUndo);
        m_btnUndo.setText("UNDO ("+m_nbUndo+")");

        int i = 0;
        m_molkyBtn[i++] = findViewById(R.id.activity_molky_btn00);
        m_molkyBtn[i++] = findViewById(R.id.activity_molky_btn01);
        m_molkyBtn[i++] = findViewById(R.id.activity_molky_btn02);
        m_molkyBtn[i++] = findViewById(R.id.activity_molky_btn03);
        m_molkyBtn[i++] = findViewById(R.id.activity_molky_btn04);
        m_molkyBtn[i++] = findViewById(R.id.activity_molky_btn05);
        m_molkyBtn[i++] = findViewById(R.id.activity_molky_btn06);
        m_molkyBtn[i++] = findViewById(R.id.activity_molky_btn07);
        m_molkyBtn[i++] = findViewById(R.id.activity_molky_btn08);
        m_molkyBtn[i++] = findViewById(R.id.activity_molky_btn09);
        m_molkyBtn[i++] = findViewById(R.id.activity_molky_btn10);
        m_molkyBtn[i++] = findViewById(R.id.activity_molky_btn11);
        m_molkyBtn[i]   = findViewById(R.id.activity_molky_btn12);

        for (int w_i = 0; w_i < 13; w_i++) {
            final int finalW_i = w_i;
            Log.e(TAG, "onCreate: valeur de ai_lancer : "+finalW_i );
            m_molkyBtn[w_i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (m_joueurBank.getSomeoneHasWon()) {
                        Toast.makeText(getApplicationContext(), "Someone has won ! Restart ?", Toast.LENGTH_LONG).show();
                    } else {
                        _svgPreviousState();
                        m_joueurBank.addLancer(finalW_i);
                        _majAffichage();
                    }
                }
            });

        }

        // AddPlayer
        m_btnAddPlayer.setEnabled(true);
        m_btnAddPlayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _showDialogAddPlayer();
            }
        });

        // Clear
        m_btnClear.setEnabled(true);
        m_btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _showDialogClear();
            }
        });

        // Restart
        m_btnRestart = findViewById(R.id.activity_molky_btnRestart);
        m_btnRestart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _showDialogRestart();
            }
        });

        // Undo
        m_btnUndo.setEnabled(true);
        m_btnUndo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e(TAG, "onClick: UNDO" );
                m_joueurBank = m_previousJoueurBank;
                m_previousJoueurBank = m_previousJoueurBank2;
                if (m_nbUndo > 0) {
                    m_nbUndo--;
                }
                m_btnUndo.setText("UNDO ("+m_nbUndo+")");
                _majAffichage();
            }
        });

        _majAffichage();
    } // END onCreate()

    private void _showDialogAddPlayer() {
        final TextView w_text=new TextView(this);
        w_text.setPadding(10, 10, 10, 10);
        w_text.setText("Nom du joueur ?");

        final EditText w_edit =new EditText(this);
        w_edit.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
        w_edit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                w_edit.post(new Runnable() {
                    @Override
                    public void run() {
                        InputMethodManager inputMethodManager= (InputMethodManager) getApplication().getSystemService(Context.INPUT_METHOD_SERVICE);
                        inputMethodManager.showSoftInput(w_edit, InputMethodManager.SHOW_IMPLICIT);
                    }
                });
            }
        });
        w_edit.requestFocus();

        final AlertDialog.Builder w_alert = new AlertDialog.Builder(this);
        LinearLayout w_linear=new LinearLayout(this);
        w_linear.setOrientation(LinearLayout.VERTICAL);
        w_linear.addView(w_text);
        w_linear.addView(w_edit);

        w_alert.setTitle("Frequence d'appartition du 4");
        w_alert.setView(w_linear);

        w_alert.setPositiveButton("Ok",new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog,int id)
            {
                _svgPreviousState();
                m_joueurBank.addJoueur(w_edit.getText().toString());
                _majAffichage();
            }
        });
        w_alert.show();
    }

    private void _showDialogRestart() {
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:
                        _svgPreviousState();
                        m_joueurBank.restart();
                        _majAffichage();
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        //No button clicked
                        break;
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure?").setPositiveButton("Yes", dialogClickListener)
                .setNegativeButton("No", dialogClickListener).show();
    }

    private void _showDialogClear() {
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:
                        _svgPreviousState();
                        m_joueurBank.clearJoueurs();
                        _majAffichage();
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        //No button clicked
                        break;
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure?").setPositiveButton("Yes", dialogClickListener)
                .setNegativeButton("No", dialogClickListener).show();
    }

    private void _svgPreviousState() {
        m_previousJoueurBank2 = new MolkyJoueurBank(m_previousJoueurBank);
        m_previousJoueurBank = new MolkyJoueurBank(m_joueurBank);
        if (m_nbUndo < m_nbUndoMax) {
            m_nbUndo++;
        }
        m_btnUndo.setText("UNDO ("+m_nbUndo+")");
    }

    private void _majAffichage() {
        Log.e(TAG, "_majAffichage: " );
        m_txtScore.setText(m_joueurBank.getText());
    }
}
