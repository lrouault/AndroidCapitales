package com.lrt.capitales.Model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by lrouault on 26/02/2018.
 */

public class CapitalesBank {
    private List<Capitales> mCapitalesList;
    private int mNextCapitaleIndex;

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
