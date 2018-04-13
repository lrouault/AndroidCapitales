package com.lrt.capitales.Controller;

import android.content.SharedPreferences;
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

import com.lrt.capitales.Model.Capitales;
import com.lrt.capitales.Model.CapitalesBank;
import com.lrt.capitales.Model.GamePreference;
import com.lrt.capitales.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CapitalesActivity extends AppCompatActivity {

    private TextView mPaysTextView;
    private AutoCompleteTextView mReponseEditText;
    private Button mValiderButton;
    private TextView mTVScore;
    private TextView mTVVie;

    private CapitalesBank mCapitalesBank;
    private Capitales mCurrentCapitales;
    private List<String> mAutoComplete;

    private GamePreference mGamePreference;

    private Integer mScore;
    private Integer mVie;

    private static final String BUNDLE_STATE_SCORE="currentScore";
    private static final String BUNDLE_STATE_VIE="currentVie";

    private Integer mMeilleurScore;
    private SharedPreferences mMeilleurScoreSVG;

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

        mMeilleurScoreSVG = getPreferences(MODE_PRIVATE);
        mMeilleurScore = mMeilleurScoreSVG.getInt(mGamePreference.getStringPreference(),0);


        mTVScore = (TextView) findViewById(R.id.activity_capitales_score);
        mTVVie = (TextView) findViewById(R.id.activity_capitales_vie);
        mTVScore.setText("Score: "+mScore+"/"+mMeilleurScore);
        mTVVie.setText("Vie: "+mVie);


        /*mCapitalesBank = this.generateCapitales();*/
        mCapitalesBank = new CapitalesBank(mGamePreference,this);

        mPaysTextView = (TextView) findViewById(R.id.activity_capitales_pays);
        mReponseEditText = (AutoCompleteTextView) findViewById(R.id.activity_capitales_reponse);
        mValiderButton = (Button) findViewById(R.id.activity_capitales_valider);

        mCurrentCapitales = mCapitalesBank.getCapitales();
        this.displayPays(mCurrentCapitales);

        mAutoComplete = mCapitalesBank.getAutoComplCapitales();
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
                        mMeilleurScoreSVG.edit().putInt(mGamePreference.getStringPreference(),mScore).apply();
                    }
                    mTVScore.setText("Score: "+mScore+"/"+mMeilleurScore);
                }else {
                    Toast.makeText(getApplicationContext(), "False: "+mCurrentCapitales.getCapitalName(), Toast.LENGTH_SHORT).show();
                    if(mVie>0){
                        mVie--;
                        mTVVie.setText("Vie: "+mVie);
                    }else{
                        Toast.makeText(getApplicationContext(),"Score: "+mScore+"/"+mMeilleurScore,Toast.LENGTH_LONG).show();
                        finish();
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

    private CapitalesBank generateCapitales() {
        ArrayList<Capitales> locList = new ArrayList<>();
        String json = null;
        try {
            InputStream is = getAssets().open("country-capitals.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        try {
            JSONObject obj = new JSONObject(json);
            JSONArray m_jArry = obj.getJSONArray("pays");

            for (int i = 0; i < m_jArry.length(); i++) {
                JSONObject jo_inside = m_jArry.getJSONObject(i);
                Capitales location = new Capitales();
                Boolean ContinentOK = false;
                location.setCountryName(jo_inside.getString("CountryName"));
                location.setCapitalName(jo_inside.getString("CapitalName"));
                location.setCapitalLatitude(jo_inside.getDouble("CapitalLatitude"));
                location.setCapitalLongitude(jo_inside.getDouble("CapitalLongitude"));
                location.setCountryCode(jo_inside.getString("CountryCode"));
                location.setContinentName(jo_inside.getString("ContinentName"));
                location.setDifficulty(jo_inside.getInt("Difficulty"));

                if(location.getContinentName().equals("Afrique")){
                    ContinentOK = mGamePreference.getAfrique();
                }else if(location.getContinentName().equals("Amerique")){
                    ContinentOK = mGamePreference.getAmerique();
                }else if(location.getContinentName().equals("Asie")){
                    ContinentOK = mGamePreference.getAsie();
                }else{
                    ContinentOK = mGamePreference.getEurope();
                }

                //Add your values in your `ArrayList` as below:
                if(location.getDifficulty() <= mGamePreference.getDifficulty() && ContinentOK) {
                    locList.add(location);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return new CapitalesBank(locList);
    }

    //   Capitales cap1 = new Capitales("France", "Paris",-1.,1.,"fr","Europe",1 );
    //    Capitales cap2 = new Capitales("Espagne", "Madrid",-1.,1.,"fr","Europe",1 );
    //    Capitales cap3 = new Capitales("Italie", "Rome",-1.,1.,"fr","Europe",1 );
    //    Capitales cap4 = new Capitales("Allemagne", "Berlin",-1.,1.,"fr","Europe",1 );
    //
    //    return new CapitalesBank(Arrays.asList(cap1,
    //            cap2, cap3, cap4));
    //}
}
