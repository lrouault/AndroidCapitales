package com.lrt.capitales.controller;

import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.lrt.capitales.model.capitales.Capitales;
import com.lrt.capitales.model.capitales.CapitalesBank;
import com.lrt.capitales.model.capitales.GamePreference;
import com.lrt.capitales.R;

import java.util.List;

public class CapitalesActivity extends AppCompatActivity {

    private TextView mPaysTextView;
    private AutoCompleteTextView mReponseEditText;
    private Button mValiderButton;
    private TextView mTVScore;
    private TextView mTVVie;

    private CapitalesBank mCapitalesBank;
    private Capitales mCurrentCapitales;

    private GamePreference mGamePreference;

    private Integer mScore;
    private Integer mVie;

    private static final String BUNDLE_STATE_SCORE="currentScore";
    private static final String BUNDLE_STATE_VIE="currentVie";

    private Integer mMeilleurScore;
    private SharedPreferences m_sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_capitales);

        if(savedInstanceState!=null){
            mScore=savedInstanceState.getInt(BUNDLE_STATE_SCORE);
            mVie=savedInstanceState.getInt(BUNDLE_STATE_VIE);
        }else {
            mVie = 3;
            mScore = 0;
        }


        mGamePreference  = (GamePreference) getIntent().getSerializableExtra("GamePreference");

        m_sharedPreferences = getPreferences(MODE_PRIVATE);
        mMeilleurScore = m_sharedPreferences.getInt(mGamePreference.getStringPreference(),0);


        mTVScore = findViewById(R.id.activity_capitales_score);
        mTVVie = findViewById(R.id.activity_capitales_vie);
        mTVScore.setText("Score: "+mScore+" Best: "+mMeilleurScore);
        mTVVie.setText("Vie: "+mVie);


        /*mCapitalesBank = this.generateCapitales();*/
        mCapitalesBank = new CapitalesBank(mGamePreference,this);

        mPaysTextView = (TextView) findViewById(R.id.activity_capitales_pays);
        mReponseEditText = (AutoCompleteTextView) findViewById(R.id.activity_capitales_reponse);
        mValiderButton = (Button) findViewById(R.id.activity_capitales_valider);

        mCurrentCapitales = mCapitalesBank.getCapitales();
        this.displayPays(mCurrentCapitales);

        List<String> mAutoComplete = mCapitalesBank.getAutoComplCapitales();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line,mAutoComplete);
        mReponseEditText.setAdapter(adapter);

        mValiderButton.setEnabled(false);




        mReponseEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mValiderButton.setEnabled(s.toString().length() != 0);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        mValiderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mReponseEditText.getText().toString().toUpperCase().equals(mCurrentCapitales.getCapitalName().toUpperCase())) {
                    Toast.makeText(getApplicationContext(), "Correct!", Toast.LENGTH_SHORT).show();
                    mScore++;
                    if (mScore>mMeilleurScore) {
                        mMeilleurScore = mScore;
                        m_sharedPreferences.edit().putInt(mGamePreference.getStringPreference(),mScore).apply();
                    }
                    mTVScore.setText("Score: "+mScore+" Best: "+mMeilleurScore);
                }else {
                    Toast.makeText(getApplicationContext(), "False: "+mCurrentCapitales.getCapitalName(), Toast.LENGTH_SHORT).show();
                    if(mVie>0){
                        mVie--;
                        mTVVie.setText("Vie: "+mVie);
                    }else{
                        Toast.makeText(getApplicationContext(),"Score: "+mScore+" Best: "+mMeilleurScore,Toast.LENGTH_LONG).show();

                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            public void run() {
                                finish();
                            }
                        }, 1000);
                    }
                }
                mCurrentCapitales = mCapitalesBank.getCapitales();
                displayPays(mCurrentCapitales);
                mReponseEditText.setText("");

            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt(BUNDLE_STATE_SCORE,mScore);
        outState.putInt(BUNDLE_STATE_VIE,mVie);
        super.onSaveInstanceState(outState);
    }

    public void displayPays(final Capitales capitales) {
        mPaysTextView.setText(capitales.getCountryName());
    }
}
