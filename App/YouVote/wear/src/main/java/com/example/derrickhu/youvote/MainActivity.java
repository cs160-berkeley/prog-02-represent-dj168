package com.example.derrickhu.youvote;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;

import java.util.Random;

public class MainActivity extends Activity {


    private SensorManager mSensorManager;
    private ShakeEventListener mSensorListener;
    private Intent selfIntent;
    private Intent sendIntent2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mSensorListener = new ShakeEventListener();

        mSensorListener.setOnShakeListener(new ShakeEventListener.OnShakeListener() {

            public void onShake() {

                //Create random zip Code
                Random rand = new Random();
                int randZip = rand.nextInt(99999 - 10000 + 1) + 10000;
                String randZipString = Integer.toString(randZip);

                //Send intent to 3 reps on watch with new zipcode
                selfIntent = new Intent(getBaseContext(), Main2Activity.class);
                selfIntent.putExtra("ZIP_CODE", randZipString);
                startActivity(selfIntent);

                //Send intent to Phone to reload everything
                sendIntent2 = new Intent(getBaseContext(), WatchToPhoneService.class);
                sendIntent2.putExtra("ZIP_CODE", randZipString);
                startService(sendIntent2);
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(mSensorListener,
                mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_UI);
    }

    @Override
    protected void onPause() {
        mSensorManager.unregisterListener(mSensorListener);
        super.onPause();
    }
}