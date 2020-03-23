package com.lrt.capitales.view;

import android.view.GestureDetector;
import android.view.MotionEvent;

import com.lrt.capitales.common.CommonEnum.Direction;

/**
 * Created by lrouault on 20/03/2020.
 */

public class OnSwipeListener extends GestureDetector.SimpleOnGestureListener {
    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        float x1 = e1.getX();
        float y1 = e1.getY();

        float x2 = e2.getX();
        float y2 = e2.getY();

        Direction direction = Direction.fromPoints(x1,y1,x2,y2);
        return onSwipe(direction);
    }

    /** Override this method. The Direction enum will tell you how the user swiped. */
    public boolean onSwipe(Direction direction){
        return false;
    }
}
