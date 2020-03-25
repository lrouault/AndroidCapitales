package com.lrt.capitales.controller;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.SeekBar;

import com.lrt.capitales.R;
import com.lrt.capitales.model.labyrinthe.LabyrintheBank;

public class LabyrintheNiveauxActivity extends AppCompatActivity {

    private CheckedTextView m_btnDiff1;
    private CheckedTextView m_btnDiff2;
    private CheckedTextView m_btnDiff3;

    private SeekBar m_seekBar;

    private LabyrintheBank m_bank = null;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_labyrinthe_niveaux);

        m_btnDiff1 = findViewById(R.id.activity_labyrintheDiff_1);
        m_btnDiff2 = findViewById(R.id.activity_labyrintheDiff_2);
        m_btnDiff3 = findViewById(R.id.activity_labyrintheDiff_3);
        m_btnDiff1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!m_btnDiff1.isChecked()) {
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
                    m_btnDiff3.setChecked(true);
                    m_btnDiff2.setChecked(false);
                    m_btnDiff1.setChecked(false);
                }
            }
        });

        m_bank = new LabyrintheBank();
        m_bank.lectureFichier(getBaseContext().getAssets());
        m_seekBar = findViewById(R.id.activity_labyrintheSeekBar);
        m_seekBar.setMax(m_bank.getM_listeDeLabyrinthe().size()-1);

        Button w_btnLabyrintheGO = findViewById(R.id.activity_labyrintheGO);
        w_btnLabyrintheGO.setEnabled(true);
        w_btnLabyrintheGO.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int w_difficulte;
                if (m_btnDiff1.isChecked()) w_difficulte = 1;
                else if (m_btnDiff1.isChecked()) w_difficulte = 2;
                else w_difficulte = 3;
                Intent w_labyrintheActivity = new Intent(LabyrintheNiveauxActivity.this, LabyrintheActivity.class);
                w_labyrintheActivity.putExtra("Difficulte", w_difficulte);
                w_labyrintheActivity.putExtra("Niveau", m_seekBar.getProgress());
                startActivity(w_labyrintheActivity);
            }
        });
    }
}
