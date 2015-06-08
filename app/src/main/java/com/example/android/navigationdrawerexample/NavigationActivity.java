package com.example.android.navigationdrawerexample;

import android.app.Activity;
import android.hardware.SensorEvent;
import android.os.Bundle;
import android.widget.TextView;

public class NavigationActivity extends Activity {

    private NavigationView naviPfeil;
    private TextView entfernung;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);

//		setContentView(R.layout.navigation);
//
//		naviPfeil = (NavigationView) findViewById(R.id.naviPfeil);
//		entfernung = (TextView) findViewById(R.id.entfernung);
//
//		naviPfeil.setWinkel(5f);
//		entfernung.setText("Test");

        naviPfeil = new NavigationView(this);

        double winkelRad = Math.atan(-1.0/1.0);
        naviPfeil.setWinkel((float) Math.toDegrees(winkelRad));

        setContentView(naviPfeil);
    }

//	public void onSensorChanged(SensorEvent event) {
//		if (naviPfeil != null) {
//			naviPfeil.setWinkel(-event.values[0]);
//		}
//	}

    // Handy-eigener Zurück-Button
    @Override
    public void onBackPressed() {
        this.finish();
    }
}