package com.lrt.capitales.view;

        import android.content.Context;
        import android.graphics.*;
        import android.util.AttributeSet;
        import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;
        import com.lrt.capitales.R;


public class PinView extends SubsamplingScaleImageView {

    private final Paint paint = new Paint();
    private final Paint paintLine = new Paint();
    private final PointF vPin = new PointF();
    private final PointF vPin2 = new PointF();
    private PointF sPin;
    private PointF sPin2;
    private Bitmap pin;
    private Bitmap pin2;

    public PinView(Context context) {
        this(context, null);
    }

    public PinView(Context context, AttributeSet attr) {
        super(context, attr);
        initialise();
    }

    public void setPin(PointF sPin) {
        this.sPin = sPin;
        initialise();
        //invalidate();
    }

    private void initialise() {
        float density = getResources().getDisplayMetrics().densityDpi;
        pin = BitmapFactory.decodeResource(this.getResources(), R.drawable.pushpin_blue);
        float w = (density/420f) * pin.getWidth();
        float h = (density/420f) * pin.getHeight();
        pin = Bitmap.createScaledBitmap(pin, (int)w/3, (int)h/3, true);
    }

    public void setPin2(PointF sPin2) {
        this.sPin2 = sPin2;
        initialise2();
        invalidate();
    }

    private void initialise2() {
        float density = getResources().getDisplayMetrics().densityDpi;
        pin2 = BitmapFactory.decodeResource(this.getResources(), R.drawable.pushpin_red);
        float w = (density/420f) * pin2.getWidth();
        float h = (density/420f) * pin2.getHeight();
        pin2 = Bitmap.createScaledBitmap(pin2, (int)w/12, (int)h/12, true);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // Don't draw pin before image is ready so it doesn't move around during setup.
        if (!isReady()) {
            return;
        }

        paint.setAntiAlias(true);
        paintLine.setColor(Color.BLACK);
        paintLine.setStrokeWidth(8f);

        if (sPin != null && pin != null && sPin2 != null && pin2 != null) {
            sourceToViewCoord(sPin, vPin);
            sourceToViewCoord(sPin2, vPin2);

            float vX = vPin.x - (pin.getWidth()/2);
            float vY = vPin.y - pin.getHeight();
            float vX2 = vPin2.x - (pin2.getWidth()/2);
            float vY2 = vPin2.y - pin2.getHeight();

            canvas.drawBitmap(pin, vX, vY, paint);
            canvas.drawLine(vPin.x, vPin.y, vPin2.x, vPin2.y, paintLine);
            canvas.drawBitmap(pin2, vX2, vY2, paint);
        }

    }

}