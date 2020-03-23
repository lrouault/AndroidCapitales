package com.lrt.capitales.model.capitales;

import java.io.Serializable;

/**
 * Created by lrouault on 28/02/2018.
 *
 */

public class GamePreference implements Serializable {
    private Boolean mAfrique;
    private Boolean mAmerique;
    private Boolean mAsie;
    private Boolean mEurope;
    private Integer mDifficulty;

    public GamePreference() {
    }

    public Boolean getAfrique() {
        return mAfrique;
    }

    public void setAfrique(Boolean afrique) {
        mAfrique = afrique;
    }

    public Boolean getAmerique() {
        return mAmerique;
    }

    public void setAmerique(Boolean amerique) {
        mAmerique = amerique;
    }

    public Boolean getAsie() {
        return mAsie;
    }

    public void setAsie(Boolean asie) {
        mAsie = asie;
    }

    public Boolean getEurope() {
        return mEurope;
    }

    public void setEurope(Boolean europe) {
        mEurope = europe;
    }

    public Integer getDifficulty() {
        return mDifficulty;
    }

    public void setDifficulty(Integer difficulty) {
        mDifficulty = difficulty;
    }

    public String getStringPreference() {
        String res = mDifficulty.toString();
        if (mAfrique)  res = res + "Af";
        if (mAmerique) res = res + "Am";
        if (mAsie)     res = res + "As";
        if (mEurope)   res = res + "Eu";
        return res;
    }
}
