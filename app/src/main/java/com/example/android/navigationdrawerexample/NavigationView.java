package com.example.android.navigationdrawerexample;

        import android.content.Context;
        import android.graphics.Canvas;
        import android.graphics.Color;
        import android.graphics.Paint;
        import android.graphics.Path;
        import android.view.View;

public class NavigationView extends View {

    private float winkel=0;
    private Paint zeichenfarbe = new Paint();

    public void setWinkel(float winkel) {
        this.winkel = winkel;
        invalidate();
    }

    public NavigationView(Context context) {
        super(context);

        zeichenfarbe.setAntiAlias(true);
        zeichenfarbe.setColor(Color.BLUE);
        zeichenfarbe.setStyle(Paint.Style.FILL);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawColor(Color.WHITE);

        int breite = canvas.getWidth();
        int hoehe = canvas.getHeight();
        int laenge = Math.min(breite, hoehe);

        Path pfad = new Path();
        pfad.moveTo(0, -laenge/2);
        pfad.lineTo(laenge/20, laenge/2);
        pfad.lineTo(-laenge/20, laenge/2);
        pfad.close();

        canvas.translate(breite/2, hoehe/2);
        canvas.rotate(winkel);
        canvas.drawPath(pfad, zeichenfarbe);
    }

}
