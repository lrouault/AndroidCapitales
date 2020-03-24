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

    private TextView m_scoreText;
    private Button m_ville;
    private PointF m_coordClick;

    private GamePreference m_gamePreference;
    private CapitalesBank m_capitalesBank;
    private Capitales m_currentCapitale;

    private double m_mapWidth = 4000;
    private double m_mapLonDelta = 350;
    private double m_mapLatBottom = -68;
    private double m_mapLatBottomRadian = m_mapLatBottom *Math.PI/180;
    private double m_mapHeight = 3000;
    private double m_mapLonLeft = -171.1;

    private Integer m_compteur, m_score, m_meilleurScore;
    private SharedPreferences m_meilleurScoreSVG;


    private static final String BUNDLE_STATE_SCORE="MAPcurrentScore";
    private static final String BUNDLE_STATE_CPT="MAPcurrentCpt";




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        if(savedInstanceState!=null){
            m_score =savedInstanceState.getInt(BUNDLE_STATE_SCORE);
            m_compteur =savedInstanceState.getInt(BUNDLE_STATE_CPT);
        }else {
            m_compteur = 1;
            m_score = 0;
        }

        m_scoreText = findViewById(R.id.activity_map_score);
        m_ville = findViewById(R.id.activity_map_ville);

        m_gamePreference = (GamePreference) getIntent().getSerializableExtra("GamePreference");

        m_meilleurScoreSVG = getPreferences(MODE_PRIVATE);
        m_meilleurScore = m_meilleurScoreSVG.getInt("MAP"+ m_gamePreference.getStringPreference(),0);

        m_capitalesBank = new CapitalesBank(m_gamePreference,this);
        m_currentCapitale = m_capitalesBank.getCapitales();
        displayCapitale();

        final PinView mImageView = (PinView) findViewById(R.id.activity_map_map);
        mImageView.setMaxScale(10.f);

        final GestureDetector gestureDetector = new GestureDetector(this,new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onSingleTapConfirmed(MotionEvent e) {
                double[] latlong;
                Double dist;

                if (mImageView.isReady()) {
                    m_coordClick = mImageView.viewToSourceCoord(e.getX(), e.getY());
                    if (m_coordClick.x<0) m_coordClick.x=0;
                    if (m_coordClick.y<0) m_coordClick.y=0;
                    if (m_coordClick.x> m_mapWidth) m_coordClick.x=(float) m_mapWidth;
                    if (m_coordClick.y> m_mapHeight) m_coordClick.y=(float) m_mapHeight;


                    latlong = xyTOlatlong(m_coordClick.x, m_coordClick.y);

                    dist = DistanceOiseau(latlong[0],latlong[1],
                            m_currentCapitale.getCapitalLatitude(),
                            m_currentCapitale.getCapitalLongitude());

                    Toast.makeText(getApplicationContext(), "Distance: "+dist.intValue()+" km" , Toast.LENGTH_SHORT).show();

                    if (dist.intValue()<100){
                        m_score += 100;
                    }else if (dist.intValue()<2000) {
                        m_score += (2000-dist.intValue())/20;
                    }
                    m_compteur +=1;

                    mImageView.setPin(latlongTOxy(m_currentCapitale.getCapitalLatitude(),
                            m_currentCapitale.getCapitalLongitude()));

                    if(m_compteur >10) {
                        if (m_score > m_meilleurScore) {
                            int bestScore= m_score /10;
                            m_meilleurScoreSVG.edit().putInt("MAP" + m_gamePreference.getStringPreference(), bestScore).apply();
                        }
                        int score= m_score /10;
                        Toast.makeText(getApplicationContext(),"Score: "+score+" Best: "+ m_meilleurScore,Toast.LENGTH_LONG).show();
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            public void run() {
                                finish();
                            }
                        }, 1000);
                    }else {
                        m_currentCapitale = m_capitalesBank.getCapitales();
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
        outState.putInt(BUNDLE_STATE_SCORE, m_score);
        outState.putInt(BUNDLE_STATE_CPT, m_compteur);
        super.onSaveInstanceState(outState);
    }

    public void displayCapitale() {
        int scorePourcentage = m_score;
        int cpt= m_compteur;
        if (m_compteur !=1) scorePourcentage = m_score /(m_compteur -1);
        if (m_compteur ==11) cpt=10;
        m_ville.setText(m_currentCapitale.getCapitalName());
        m_scoreText.setText("Ville: "+cpt+"/10   Score: "+scorePourcentage+" Best:"+ m_meilleurScore);
    }

    private double DistanceOiseau(double LAT1, double LON1, double LAT2, double LON2){
        return Math.acos(Math.sin(LAT1*Math.PI/180)*Math.sin(LAT2*Math.PI/180)+
                Math.cos(LAT1*Math.PI/180)*Math.cos(LAT2*Math.PI/180)*Math.cos((LON1-LON2)*Math.PI/180))*6371;
    }


    private double[] xyTOlatlong(double x, double y) {
        double[] loclatlong = {0.,0.};


        double worldMapRadius = m_mapWidth / m_mapLonDelta * 360/(2 * Math.PI);
        double mapOffsetY = ( worldMapRadius / 2 * Math.log( (1 + Math.sin(m_mapLatBottomRadian) ) / (1 - Math.sin(m_mapLatBottomRadian))  ));
        double equatorY = m_mapHeight + mapOffsetY;
        double a = (equatorY-y)/worldMapRadius;

        loclatlong[0]= 180/Math.PI * (2 * Math.atan(Math.exp(a)) - Math.PI/2);
        loclatlong[1] = m_mapLonLeft +x/ m_mapWidth * m_mapLonDelta;

        return loclatlong;
    }

    private PointF latlongTOxy(double lati, double longi) {
        double xx,yy;
        double worldMapRadius = m_mapWidth / m_mapLonDelta * 360/(2 * Math.PI);
        double mapOffsetY = ( worldMapRadius / 2 * Math.log( (1 + Math.sin(m_mapLatBottomRadian) ) / (1 - Math.sin(m_mapLatBottomRadian))  ));
        double equatorY = m_mapHeight + mapOffsetY;

        xx = (longi- m_mapLonLeft)/ m_mapLonDelta * m_mapWidth;
        yy = (float) (equatorY -worldMapRadius*Math.log(Math.tan(Math.PI*lati/360 + Math.PI/4)));
        return new PointF((float)xx,(float)yy);
    }
}
