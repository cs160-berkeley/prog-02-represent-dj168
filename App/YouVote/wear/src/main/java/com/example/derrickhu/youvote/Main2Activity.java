package com.example.derrickhu.youvote;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.wearable.view.GridViewPager;

import java.util.Random;

public class Main2Activity extends Activity {

    private SensorManager mSensorManager;
    private ShakeEventListener mSensorListener;
    private Intent selfIntent;
    private Intent sendIntent2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        Bundle extras = getIntent().getExtras();

        String repData = extras.getString("REP_DATA");
        final GridViewPager pager = (GridViewPager) findViewById(R.id.pager);
        pager.setAdapter(new GridPagerAdapter(this, getFragmentManager(), repData));

        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mSensorListener = new ShakeEventListener();
        mSensorListener.setOnShakeListener(new ShakeEventListener.OnShakeListener() {
            public void onShake() {
                //Create random zip Code
                Random rand = new Random();
                int randZip = rand.nextInt(99999 - 10000 + 1) + 10000;
                String randZipString = Integer.toString(randZip);

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
