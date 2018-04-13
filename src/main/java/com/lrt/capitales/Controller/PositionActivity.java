package com.lrt.capitales.Controller;

import android.annotation.SuppressLint;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PointF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.lrt.capitales.Model.Capitales;
import com.lrt.capitales.Model.CapitalesBank;
import com.lrt.capitales.Model.GamePreference;
import com.lrt.capitales.R;
import com.lrt.capitales.View.TouchImageView;

import java.io.File;

public class PositionActivity extends AppCompatActivity {

    private Button mVille;
    private TouchImageView mMap;
    private Bitmap b;
    private Drawable d;

    private GamePreference mGamePreference;
    private CapitalesBank mCapitalesBank;
    private Capitales mCurrentCapitale;

    private double mmapWidth = 4000;
    private double mmapLonDelta = 350;
    private double mmapLatBottom = -68;
    private double mmapLatBottomRadian = mmapLatBottom*Math.PI/180;
    private double mmapHeight = 3000;
    private double mmapLonLeft = -171.1;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_position);

        mVille = (Button) findViewById(R.id.activity_position_ville);
        mMap = (TouchImageView) findViewById(R.id.activity_position_map);
       // mImage = (ZoomView) findViewById(R.id.activity_position_image);

        //b = decodeSampledBitmapFromResource(getResources(),R.drawable.map_color,100,100);
        //d = getResources().getDrawable(R.drawable.linuxlogo);


        mGamePreference = (GamePreference) getIntent().getSerializableExtra("GamePreference");
        mCapitalesBank  = new CapitalesBank(mGamePreference,this);
        mCurrentCapitale = mCapitalesBank.getCapitales();
        displayCapitale(mCurrentCapitale);

        //mMap.setImageBitmap(decodeSampledBitmapFromResource(getResources(),R.drawable.map_color,400,400));
        mMap.setImageBitmap(decodeSampledBitmapFromResource(getResources(),R.drawable.map2,400,400));


        mMap.setMyOnClickMapListener(new TouchImageView.MyOnClickMapListener() {
            @Override
            public void onCliqueMap(float ratioX, float ratioY) {
                double[] latlong,xy;
                Double Dist,t1,t2;

                latlong = xyTOlatlong(ratioX,ratioY);
                xy = latlongTOxy(latlong[0],latlong[1]);

                Dist = DistanceOiseau(latlong[0],latlong[1],
                        mCurrentCapitale.getCapitalLatitude(),
                        mCurrentCapitale.getCapitalLongitude());

                Toast.makeText(getApplicationContext(), "Distance: "+Dist.intValue()+" km" , Toast.LENGTH_SHORT).show();
                mCurrentCapitale = mCapitalesBank.getCapitales();
                displayCapitale(mCurrentCapitale);
            }
        });
    }


    public void displayCapitale(final Capitales capitales) {
        mVille.setText(capitales.getCapitalName());
    }




    public static Bitmap decodeSampledBitmapFromResource(Resources res, int resId,
                                                         int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res, resId, options);
    }

    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) >= reqHeight
                    && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    private double DistanceOiseau(double LAT1, double LON1, double LAT2, double LON2){
        return Math.acos(Math.sin(LAT1*Math.PI/180)*Math.sin(LAT2*Math.PI/180)+
                Math.cos(LAT1*Math.PI/180)*Math.cos(LAT2*Math.PI/180)*Math.cos((LON1-LON2)*Math.PI/180))*6371;
    }


    private double[] xyTOlatlong(double x, double y) {
        double[] loclatlong = {0.,0.};

        double ty = y*mmapHeight;
        double tx = x*mmapWidth;

        double worldMapRadius = mmapWidth / mmapLonDelta * 360/(2 * Math.PI);
        double mapOffsetY = ( worldMapRadius / 2 * Math.log( (1 + Math.sin(mmapLatBottomRadian) ) / (1 - Math.sin(mmapLatBottomRadian))  ));
        double equatorY = mmapHeight + mapOffsetY;
        double a = (equatorY-ty)/worldMapRadius;

        loclatlong[0]= 180/Math.PI * (2 * Math.atan(Math.exp(a)) - Math.PI/2);
        loclatlong[1] = mmapLonLeft+tx/mmapWidth*mmapLonDelta;

        return loclatlong;
    }

    private double[] latlongTOxy(double lati, double longi) {
        double[] xy = {0.,0.};

        double worldMapRadius = mmapWidth / mmapLonDelta * 360/(2 * Math.PI);
        double mapOffsetY = ( worldMapRadius / 2 * Math.log( (1 + Math.sin(mmapLatBottomRadian) ) / (1 - Math.sin(mmapLatBottomRadian))  ));
        double equatorY = mmapHeight + mapOffsetY;

        xy[0] = (longi-mmapLonLeft)/mmapLonDelta;
        xy[1] = equatorY -worldMapRadius*Math.log(Math.tan(Math.PI*lati/360 + Math.PI/4));
        xy[1] = xy[1]/mmapHeight;
        return xy;
    }

}
