package com.lrt.capitales.Controller;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;
import com.lrt.capitales.R;

public class MapActivity extends AppCompatActivity {

    SubsamplingScaleImageView mImageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        mImageView = (SubsamplingScaleImageView) findViewById(R.id.activity_map_map);
        mImageView.setMaxScale(10.f);
        mImageView.setImage(ImageSource.resource(R.drawable.map2));
    }
}
