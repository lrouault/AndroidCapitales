package com.lrt.capitales.controller;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.EditText;

import com.lrt.capitales.model.capitales.GamePreference;
import com.lrt.capitales.R;

// TODO Changer le nom a arcadeGame
// TODO Changer le logo de l'appli

public class MainActivity extends AppCompatActivity {

    private EditText mMainName;
    private Button mMainCapitales;
    private Button mMainPositions;

    private CheckedTextView mMainAfrique;
    private CheckedTextView mMainAmerique;
    private CheckedTextView mMainAsie;
    private CheckedTextView mMainEurope;

    private CheckedTextView mMainDiff1;
    private CheckedTextView mMainDiff2;
    private CheckedTextView mMainDiff3;

    private GamePreference mGamePreference;

    private SharedPreferences mPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mPreferences = getPreferences(MODE_PRIVATE);


        mMainName = findViewById(R.id.activity_main_name);
        mMainCapitales = findViewById(R.id.activity_main_capitales);
        mMainPositions = findViewById(R.id.activity_main_positions);
        mMainAfrique = findViewById(R.id.activity_main_afrique);
        mMainAmerique =  findViewById(R.id.activity_main_amerique);
        mMainAsie = findViewById(R.id.activity_main_asie);
        mMainEurope = findViewById(R.id.activity_main_europe);
        mMainDiff1 = findViewById(R.id.activity_main_diff_1);
        mMainDiff2 = findViewById(R.id.activity_main_diff_2);
        mMainDiff3 = findViewById(R.id.activity_main_diff_3);

        if(mPreferences.contains("firstname"))
            mMainName.setText(mPreferences.getString("firstname",null));

        mMainCapitales.setEnabled(mMainName.getText().toString().length() !=0);
        mMainPositions.setEnabled(mMainName.getText().toString().length() !=0);

        mMainName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mMainCapitales.setEnabled(s.toString().length() !=0);
                mMainPositions.setEnabled(s.toString().length() !=0);
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        mMainAfrique.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mMainAfrique.isChecked()){
                    mMainAfrique.setChecked(false);
                }else{
                    mMainAfrique.setChecked(true);
                }
            }
        });
        mMainAmerique.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mMainAmerique.isChecked()){
                    mMainAmerique.setChecked(false);
                }else{
                    mMainAmerique.setChecked(true);
                }
            }
        });
        mMainAsie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mMainAsie.isChecked()){
                    mMainAsie.setChecked(false);
                }else{
                    mMainAsie.setChecked(true);
                }
            }
        });
        mMainEurope.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mMainEurope.isChecked()){
                    mMainEurope.setChecked(false);
                }else{
                    mMainEurope.setChecked(true);
                }
            }
        });

        mMainDiff1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!mMainDiff1.isChecked()) {
                    mMainDiff1.setChecked(true);
                    mMainDiff2.setChecked(false);
                    mMainDiff3.setChecked(false);
                }
            }
        });
        mMainDiff2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!mMainDiff2.isChecked()) {
                    mMainDiff2.setChecked(true);
                    mMainDiff1.setChecked(false);
                    mMainDiff3.setChecked(false);
                }
            }
        });
        mMainDiff3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!mMainDiff3.isChecked()) {
                    mMainDiff3.setChecked(true);
                    mMainDiff2.setChecked(false);
                    mMainDiff1.setChecked(false);
                }
            }
        });


        mMainCapitales.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mMainAfrique.isChecked()||mMainAmerique.isChecked()||
                        mMainAsie.isChecked()||mMainEurope.isChecked()) {

                    mPreferences.edit().putString("firstname",mMainName.getText().toString()).apply();

                    mGamePreference = new GamePreference();
                    mGamePreference.setAfrique(mMainAfrique.isChecked());
                    mGamePreference.setAmerique(mMainAmerique.isChecked());
                    mGamePreference.setAsie(mMainAsie.isChecked());
                    mGamePreference.setEurope(mMainEurope.isChecked());
                    if (mMainDiff1.isChecked()) {
                        mGamePreference.setDifficulty(1);
                    } else if (mMainDiff2.isChecked()) {
                        mGamePreference.setDifficulty(2);
                    } else {
                        mGamePreference.setDifficulty(3);
                    }

                    Intent capitalesActivity = new Intent(MainActivity.this, CapitalesActivity.class);
                    capitalesActivity.putExtra("GamePreference", mGamePreference);
                    startActivity(capitalesActivity);
                }
            }
        });


        mMainPositions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mMainAfrique.isChecked()||mMainAmerique.isChecked()||
                        mMainAsie.isChecked()||mMainEurope.isChecked()) {

                    mPreferences.edit().putString("firstname", mMainName.getText().toString()).apply();

                    mGamePreference = new GamePreference();
                    mGamePreference.setAfrique(mMainAfrique.isChecked());
                    mGamePreference.setAmerique(mMainAmerique.isChecked());
                    mGamePreference.setAsie(mMainAsie.isChecked());
                    mGamePreference.setEurope(mMainEurope.isChecked());
                    if (mMainDiff1.isChecked()) {
                        mGamePreference.setDifficulty(1);
                    } else if (mMainDiff2.isChecked()) {
                        mGamePreference.setDifficulty(2);
                    } else {
                        mGamePreference.setDifficulty(3);
                    }

                    Intent mapActivity = new Intent(MainActivity.this, MapActivity.class);
                    mapActivity.putExtra("GamePreference", mGamePreference);
                    startActivity(mapActivity);
                }
            }
        });

        Button w_main2048 = findViewById(R.id.activity_main_2048);
        w_main2048.setEnabled(true);
        w_main2048.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent w_2048Activity = new Intent(MainActivity.this, C2048Activity.class);
                startActivity(w_2048Activity);
            }
        });


        Button w_mainLabyrinthe = findViewById(R.id.activity_main_labyrinthe);
        w_mainLabyrinthe.setEnabled(true);
        w_mainLabyrinthe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent w_labyrintheNiveaux = new Intent(MainActivity.this, LabyrintheNiveauxActivity.class);
                startActivity(w_labyrintheNiveaux);
            }
        });

    }
}
