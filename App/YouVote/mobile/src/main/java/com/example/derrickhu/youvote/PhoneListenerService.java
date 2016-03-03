package com.example.derrickhu.youvote;

import android.content.Intent;
import android.util.Log;

import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.WearableListenerService;

import java.nio.charset.StandardCharsets;

/**
 * Created by derrickhu on 2/26/16.
 */

public class PhoneListenerService extends WearableListenerService {

    //   WearableListenerServices don't need an iBinder or an onStartCommand: they just need an onMessageReceieved.
    private static final String nameFromWatch = "/NAME";
    private static final String zipFromWatch = "/ZIP_CODE";

    @Override
    public void onMessageReceived(MessageEvent messageEvent) {
        Log.d("T", "in PhoneListenerService, got: " + messageEvent.getPath());
        if( messageEvent.getPath().equalsIgnoreCase(nameFromWatch) ) {

            // Value contains the String we sent over in WatchToPhoneService, "good job"

            String value = new String(messageEvent.getData(), StandardCharsets.UTF_8);

//            if (value.matches("\\d+")) {
//                Intent zipIntent = new Intent(this, Main2Activity.class);
//                zipIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                zipIntent.putExtra("ZIP_CODE", value);
//                startActivity(zipIntent);
//            }

            //else {
            Intent detailedIntent = new Intent(this, DetailedActivity.class);
            detailedIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            detailedIntent.putExtra("NAME", value);
            startActivity(detailedIntent);
          //  }
        }

        if( messageEvent.getPath().equalsIgnoreCase(zipFromWatch) ) {

            // Value contains the String we sent over in WatchToPhoneService, "good job"

            String value = new String(messageEvent.getData(), StandardCharsets.UTF_8);
            //if (value.matches("\\d+")) {
            Intent zipIntent = new Intent(this, Main2Activity.class);
            zipIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            zipIntent.putExtra("ZIP_CODE", value);
            startActivity(zipIntent);
           // }

//            else {
//                Intent detailedIntent = new Intent(this, DetailedActivity.class);
//                detailedIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                detailedIntent.putExtra("NAME", value);
//                startActivity(detailedIntent);
//            }
        }

        else {
            super.onMessageReceived( messageEvent);
        }

    }
}
