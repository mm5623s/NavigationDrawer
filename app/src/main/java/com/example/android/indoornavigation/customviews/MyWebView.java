package com.example.android.indoornavigation.customviews;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.webkit.WebView;

/**
 * Created by mm5623s on 21.05.15.
 */
public class MyWebView extends WebView {
    Paint paint = new Paint();
    private int drawPath = 0;

    public MyWebView(Context context) {
        super(context);
        init();
    }

    public MyWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MyWebView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        paint.setStrokeWidth(10.0f);
        paint.setColor(Color.BLUE);
    }

    public int getDraPath() {
        return drawPath;
    }

    public void setDrawPath(int drawPath) {
        this.drawPath = drawPath;
    }

    @Override
    protected void onDraw(Canvas canvas) {

        super.onDraw(canvas);

        switch (Integer.valueOf(drawPath)) {
            case 1:
                canvas.drawLine(0, 0, 50, 50, paint);
                canvas.drawLine(50, 50, 100, 50, paint);
                canvas.drawLine(100, 50, 150, 150, paint);
                canvas.drawLine(150, 150, 200, 100, paint);
                canvas.drawLine(200, 100, 250, 300, paint);
                break;
            case 2:
                canvas.drawLine(200, 200, 250, 250, paint);
                canvas.drawLine(250, 250, 300, 250, paint);
                canvas.drawLine(300, 250, 350, 350, paint);
                canvas.drawLine(350, 350, 400, 300, paint);
                canvas.drawLine(400, 300, 450, 500, paint);
                break;
            default:
                break;
        }

        invalidate();
    }
}
