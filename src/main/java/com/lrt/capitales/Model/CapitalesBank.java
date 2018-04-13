package com.lrt.capitales.Model;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


/**
 * Created by lrouault on 26/02/2018.
 */

public class CapitalesBank {
    private List<Capitales> mCapitalesList;
    private int mNextCapitaleIndex;

    public CapitalesBank(GamePreference GamePref, Context context) {
        ArrayList<Capitales> locList = new ArrayList<>();
        String json = null;
        try {
            /*AssetManager assetManager = getBaseContext().getAssets();
            InputStream is =  assetManager.open("people.xml");*/
            InputStream is = context.getAssets().open("country-capitals.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return;
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
                    ContinentOK = GamePref.getAfrique();
                }else if(location.getContinentName().equals("Amerique")){
                    ContinentOK = GamePref.getAmerique();
                }else if(location.getContinentName().equals("Asie")){
                    ContinentOK = GamePref.getAsie();
                }else{
                    ContinentOK = GamePref.getEurope();
                }

                //Add your values in your `ArrayList` as below:
                if(location.getDifficulty() <= GamePref.getDifficulty() && ContinentOK) {
                    locList.add(location);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        mCapitalesList = locList;
        Collections.shuffle(mCapitalesList);
        mNextCapitaleIndex =0;
    }

    public CapitalesBank(List<Capitales> capitalesList) {
        mCapitalesList = capitalesList;
        Collections.shuffle(mCapitalesList);
        mNextCapitaleIndex =0;
    }

    public Capitales getCapitales() {
        if(mNextCapitaleIndex == mCapitalesList.size()) {
            mNextCapitaleIndex = 0;
        }
        return mCapitalesList.get(mNextCapitaleIndex++);
    }

    public List<String> getAutoComplCapitales(){
        List<String> liste = new ArrayList<String>();
        for (int i=0; i<mCapitalesList.size(); i++){
            liste.add(mCapitalesList.get(i).getCapitalName());
        }
        return liste;
    }

}
