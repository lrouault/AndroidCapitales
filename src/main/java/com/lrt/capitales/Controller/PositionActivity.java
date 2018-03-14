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

import com.lrt.capitales.R;
import com.lrt.capitales.View.TouchImageView;

import java.io.File;

public class PositionActivity extends AppCompatActivity {

    private Button mVille;
    private TouchImageView mMap;
    private Bitmap b;
    private Drawable d;

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

        //mMap.setImageBitmap(decodeSampledBitmapFromResource(getResources(),R.drawable.map_color,400,400));
        mMap.setImageBitmap(decodeSampledBitmapFromResource(getResources(),R.drawable.map2,400,400));


        mMap.setMyOnClickMapListener(new TouchImageView.MyOnClickMapListener() {
            @Override
            public void onCliqueMap(float ratioX, float ratioY) {
                double Latitude;
                double Longitude;

                double mapWidth = 4000;
                double mapLonDelta = 350;
                double mapLatBottomRadian = -68*Math.PI/180;
                double mapHeight = 3000;
                double ty = ratioY*mapHeight;
                double tx = ratioX*mapWidth;
                double mapLonLeft = -171.1;

                double worldMapRadius = mapWidth / mapLonDelta * 360/(2 * Math.PI);
                double mapOffsetY = ( worldMapRadius / 2 * Math.log( (1 + Math.sin(mapLatBottomRadian) ) / (1 - Math.sin(mapLatBottomRadian))  ));
                double equatorY = mapHeight + mapOffsetY;
                double a = (equatorY-ty)/worldMapRadius;

                Latitude  = 180/Math.PI * (2 * Math.atan(Math.exp(a)) - Math.PI/2);
                Longitude = mapLonLeft+tx/mapWidth*mapLonDelta;

                mVille.setText(Latitude+", "+Longitude);




            }
        });
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


}
