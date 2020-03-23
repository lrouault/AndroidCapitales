package com.lrt.capitales.controller;

import android.content.SharedPreferences;
import android.graphics.PointF;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.davemorrissey.labs.subscaleview.ImageSource;
import com.lrt.capitales.model.capitales.Capitales;
import com.lrt.capitales.model.capitales.CapitalesBank;
import com.lrt.capitales.model.capitales.GamePreference;
import com.lrt.capitales.R;
import com.lrt.capitales.view.PinView;

public class MapActivity extends AppCompatActivity {

    // TODO rotation de l'ecran
    // TODO Trait entre le point touche et la capitale
    // TODO Commentaire

    private TextView mScoreText;
    private Button mVille;
    private PointF mCoordClick;

    private GamePreference mGamePreference;
    private CapitalesBank mCapitalesBank;
    private Capitales mCurrentCapitale;

    private double mmapWidth = 4000;
    private double mmapLonDelta = 350;
    private double mmapLatBottom = -68;
    private double mmapLatBottomRadian = mmapLatBottom*Math.PI/180;
    private double mmapHeight = 3000;
    private double mmapLonLeft = -171.1;

    private Integer mCompteur,mScore,mMeilleurScore;
    private SharedPreferences mMeilleurScoreSVG;


    private static final String BUNDLE_STATE_SCORE="MAPcurrentScore";
    private static final String BUNDLE_STATE_CPT="MAPcurrentCpt";




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        if(savedInstanceState!=null){
            mScore=savedInstanceState.getInt(BUNDLE_STATE_SCORE);
            mCompteur=savedInstanceState.getInt(BUNDLE_STATE_CPT);
        }else {
            mCompteur = 1;
            mScore = 0;
        }

        mScoreText = findViewById(R.id.activity_map_score);
        mVille = findViewById(R.id.activity_map_ville);

        mGamePreference = (GamePreference) getIntent().getSerializableExtra("GamePreference");

        mMeilleurScoreSVG = getPreferences(MODE_PRIVATE);
        mMeilleurScore = mMeilleurScoreSVG.getInt("MAP"+mGamePreference.getStringPreference(),0);

        mCapitalesBank  = new CapitalesBank(mGamePreference,this);
        mCurrentCapitale = mCapitalesBank.getCapitales();
        displayCapitale();

        final PinView mImageView = (PinView) findViewById(R.id.activity_map_map);
        mImageView.setMaxScale(10.f);

        final GestureDetector gestureDetector = new GestureDetector(this,new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onSingleTapConfirmed(MotionEvent e) {
                double[] latlong;
                Double dist;

                if (mImageView.isReady()) {
                    mCoordClick = mImageView.viewToSourceCoord(e.getX(), e.getY());
                    if (mCoordClick.x<0) mCoordClick.x=0;
                    if (mCoordClick.y<0) mCoordClick.y=0;
                    if (mCoordClick.x>mmapWidth) mCoordClick.x=(float)mmapWidth;
                    if (mCoordClick.y>mmapHeight) mCoordClick.y=(float)mmapHeight;


                    latlong = xyTOlatlong(mCoordClick.x,mCoordClick.y);

                    dist = DistanceOiseau(latlong[0],latlong[1],
                            mCurrentCapitale.getCapitalLatitude(),
                            mCurrentCapitale.getCapitalLongitude());

                    Toast.makeText(getApplicationContext(), "Distance: "+dist.intValue()+" km" , Toast.LENGTH_SHORT).show();

                    if (dist.intValue()<100){
                        mScore += 100;
                    }else if (dist.intValue()<2000) {
                        mScore += (2000-dist.intValue())/20;
                    }
                    mCompteur+=1;

                    mImageView.setPin(latlongTOxy(mCurrentCapitale.getCapitalLatitude(),
                            mCurrentCapitale.getCapitalLongitude()));

                    if(mCompteur>10) {
                        if (mScore>mMeilleurScore) {
                            int bestScore=mScore/10;
                            mMeilleurScoreSVG.edit().putInt("MAP" + mGamePreference.getStringPreference(), bestScore).apply();
                        }
                        int score=mScore/10;
                        Toast.makeText(getApplicationContext(),"Score: "+score+" Best: "+mMeilleurScore,Toast.LENGTH_LONG).show();
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            public void run() {
                                finish();
                            }
                        }, 1000);
                    }else {
                        mCurrentCapitale = mCapitalesBank.getCapitales();
                        displayCapitale();
                    }
                }
                return true;
            }
        });

        mImageView.setImage(ImageSource.resource(R.drawable.map2));

        mImageView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return gestureDetector.onTouchEvent(motionEvent);
            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt(BUNDLE_STATE_SCORE,mScore);
        outState.putInt(BUNDLE_STATE_CPT,mCompteur);
        super.onSaveInstanceState(outState);
    }

    public void displayCapitale() {
        int scorePourcentage = mScore;
        int cpt=mCompteur;
        if (mCompteur!=1) scorePourcentage = mScore/(mCompteur-1);
        if (mCompteur==11) cpt=10;
        mVille.setText(mCurrentCapitale.getCapitalName());
        mScoreText.setText("Ville: "+cpt+"/10   Score: "+scorePourcentage+" Best:"+mMeilleurScore);
    }

    private double DistanceOiseau(double LAT1, double LON1, double LAT2, double LON2){
        return Math.acos(Math.sin(LAT1*Math.PI/180)*Math.sin(LAT2*Math.PI/180)+
                Math.cos(LAT1*Math.PI/180)*Math.cos(LAT2*Math.PI/180)*Math.cos((LON1-LON2)*Math.PI/180))*6371;
    }


    private double[] xyTOlatlong(double x, double y) {
        double[] loclatlong = {0.,0.};


        double worldMapRadius = mmapWidth / mmapLonDelta * 360/(2 * Math.PI);
        double mapOffsetY = ( worldMapRadius / 2 * Math.log( (1 + Math.sin(mmapLatBottomRadian) ) / (1 - Math.sin(mmapLatBottomRadian))  ));
        double equatorY = mmapHeight + mapOffsetY;
        double a = (equatorY-y)/worldMapRadius;

        loclatlong[0]= 180/Math.PI * (2 * Math.atan(Math.exp(a)) - Math.PI/2);
        loclatlong[1] = mmapLonLeft+x/mmapWidth*mmapLonDelta;

        return loclatlong;
    }

    private PointF latlongTOxy(double lati, double longi) {
        double xx,yy;
        double worldMapRadius = mmapWidth / mmapLonDelta * 360/(2 * Math.PI);
        double mapOffsetY = ( worldMapRadius / 2 * Math.log( (1 + Math.sin(mmapLatBottomRadian) ) / (1 - Math.sin(mmapLatBottomRadian))  ));
        double equatorY = mmapHeight + mapOffsetY;

        xx = (longi-mmapLonLeft)/mmapLonDelta*mmapWidth;
        yy = (float) (equatorY -worldMapRadius*Math.log(Math.tan(Math.PI*lati/360 + Math.PI/4)));
        return new PointF((float)xx,(float)yy);
    }
}
