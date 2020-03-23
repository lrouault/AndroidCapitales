package com.lrt.capitales.model.capitales;

/**
 * Created by lrouault on 26/02/2018.
 */

public class Capitales {
    private String mCountryName;
    private String mCapitalName;
    private Double mCapitalLatitude;
    private Double mCapitalLongitude;
    private String mCountryCode;
    private String mContinentName;
    private Integer mDifficulty;


    public Capitales() {
    }


    public String getCountryName() {
        return mCountryName;
    }

    public void setCountryName(String countryName) {
        mCountryName = countryName;
    }

    public String getCapitalName() {
        return mCapitalName;
    }

    public void setCapitalName(String capitalName) {
        mCapitalName = capitalName;
    }

    public Double getCapitalLatitude() {
        return mCapitalLatitude;
    }

    public void setCapitalLatitude(Double capitalLatitude) {
        mCapitalLatitude = capitalLatitude;
    }

    public Double getCapitalLongitude() {
        return mCapitalLongitude;
    }

    public void setCapitalLongitude(Double capitalLongitude) {
        mCapitalLongitude = capitalLongitude;
    }

    public String getCountryCode() {
        return mCountryCode;
    }

    public void setCountryCode(String countryCode) {
        mCountryCode = countryCode;
    }

    public String getContinentName() {
        return mContinentName;
    }

    public void setContinentName(String continentName) {
        mContinentName = continentName;
    }

    public Integer getDifficulty() {
        return mDifficulty;
    }

    public void setDifficulty(Integer difficulty) {
        mDifficulty = difficulty;
    }
}
