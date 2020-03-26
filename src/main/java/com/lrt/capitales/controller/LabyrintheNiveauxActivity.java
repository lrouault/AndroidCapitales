package com.lrt.capitales.controller;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Toast;

import com.lrt.capitales.R;
import com.lrt.capitales.model.labyrinthe.LabyrintheBank;

public class LabyrintheNiveauxActivity extends AppCompatActivity {

    private LinearLayout m_layout;

    private CheckedTextView m_btnDiff1, m_btnDiff2, m_btnDiff3;
    private SeekBar m_seekBar_1, m_seekBar_2, m_seekBar_3, m_seekBar_4;
    private Button m_btnGO_1, m_btnGO_2, m_btnGO_3, m_btnGO_4;

    private LabyrintheBank m_bank = null;
    private int m_difficulte = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_labyrinthe_niveaux);

        m_layout = findViewById(R.id.activity_labyrintheLayout);
        m_btnDiff1 = findViewById(R.id.activity_labyrintheDiff_1);
        m_btnDiff2 = findViewById(R.id.activity_labyrintheDiff_2);
        m_btnDiff3 = findViewById(R.id.activity_labyrintheDiff_3);
        m_btnDiff1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!m_btnDiff1.isChecked()) {
                    m_difficulte = 1;
                    m_layout.setBackgroundColor(getResources().getColor(R.color.btn2048_3));
                    m_btnDiff1.setChecked(true);
                    m_btnDiff2.setChecked(false);
                    m_btnDiff3.setChecked(false);
                }
            }
        });
        m_btnDiff2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!m_btnDiff2.isChecked()) {
                    m_difficulte = 2;
                    m_layout.setBackgroundColor(getResources().getColor(R.color.btn2048_4));
                    m_btnDiff2.setChecked(true);
                    m_btnDiff1.setChecked(false);
                    m_btnDiff3.setChecked(false);
                }
            }
        });
        m_btnDiff3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!m_btnDiff3.isChecked()) {
                    m_difficulte = 3;
                    m_layout.setBackgroundColor(getResources().getColor(R.color.btn2048_5));
                    m_btnDiff3.setChecked(true);
                    m_btnDiff2.setChecked(false);
                    m_btnDiff1.setChecked(false);
                    Toast.makeText(getApplicationContext(),"ATTENTION : Algo de collision a ameliorer",Toast.LENGTH_SHORT).show();
                }
            }
        });

        m_bank = new LabyrintheBank();
        m_bank.lectureFichier(getBaseContext().getAssets());
        m_seekBar_1 = findViewById(R.id.activity_labyrinthe_1_SeekBar);
        m_seekBar_1.setMax(m_bank.getM_listeDeCategorieDeLabyrinthe().get(0).size()-1);
        m_seekBar_2 = findViewById(R.id.activity_labyrinthe_2_SeekBar);
        m_seekBar_2.setMax(m_bank.getM_listeDeCategorieDeLabyrinthe().get(1).size()-1);
        m_seekBar_3 = findViewById(R.id.activity_labyrinthe_3_SeekBar);
        m_seekBar_3.setMax(m_bank.getM_listeDeCategorieDeLabyrinthe().get(2).size()-1);
        m_seekBar_4 = findViewById(R.id.activity_labyrinthe_4_SeekBar);
        m_seekBar_4.setMax(m_bank.getM_listeDeCategorieDeLabyrinthe().get(3).size()-1);

        m_btnGO_1 = findViewById(R.id.activity_labyrinthe_1_GO);
        m_btnGO_1.setEnabled(true);
        m_btnGO_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _startActivityLabyrinthe(1, m_seekBar_1.getProgress());
            }
        });

        m_btnGO_2 = findViewById(R.id.activity_labyrinthe_2_GO);
        m_btnGO_2.setEnabled(true);
        m_btnGO_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _startActivityLabyrinthe(2, m_seekBar_2.getProgress());
            }
        });

        m_btnGO_3 = findViewById(R.id.activity_labyrinthe_3_GO);
        m_btnGO_3.setEnabled(true);
        m_btnGO_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _startActivityLabyrinthe(3, m_seekBar_3.getProgress());
            }
        });

        m_btnGO_4 = findViewById(R.id.activity_labyrinthe_4_GO);
        m_btnGO_4.setEnabled(true);
        m_btnGO_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _startActivityLabyrinthe(4, m_seekBar_4.getProgress());
            }
        });
    }

    private void _startActivityLabyrinthe(int ai_categorie, int ai_niveau) {
        Intent w_labyrintheActivity = new Intent(LabyrintheNiveauxActivity.this, LabyrintheActivity.class);
        w_labyrintheActivity.putExtra("Difficulte", m_difficulte);
        w_labyrintheActivity.putExtra("Categorie", ai_categorie);
        w_labyrintheActivity.putExtra("Niveau", ai_niveau);
        startActivity(w_labyrintheActivity);
    }
}
