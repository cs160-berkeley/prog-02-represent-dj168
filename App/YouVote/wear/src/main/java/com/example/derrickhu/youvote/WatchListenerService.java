package com.example.derrickhu.youvote;

import android.content.Intent;
import android.util.Log;

import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.WearableListenerService;

import java.nio.charset.StandardCharsets;

/**
 * Created by derrickhu on 2/26/16.
 */
public class WatchListenerService extends WearableListenerService {
    // In PhoneToWatchService, we passed in a path, either "/FRED" or "/LEXY"
    // These paths serve to differentiate different phone-to-watch messages

    //create string zipcode
    private static final String zipCode = "/ZIP_CODE";

    @Override
    public void onMessageReceived(MessageEvent messageEvent) {
        Log.d("T", "in WatchListenerService, got: " + messageEvent.getPath());
        //use the 'path' field in sendmessage to differentiate use cases
        //(here, fred vs lexy)

        //check if its zipcode dj
        System.out.println(messageEvent.getPath().equalsIgnoreCase( zipCode ));
        if( messageEvent.getPath().equalsIgnoreCase( zipCode ) ) {
            String value = new String(messageEvent.getData(), StandardCharsets.UTF_8);
            Intent intent = new Intent(this, Main2Activity.class );
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            //you need to add this flag since you're starting a new activity from a service
            System.out.println("poo");
            intent.putExtra("ZIP_CODE", value);
            startActivity(intent);
        }


        else {
            super.onMessageReceived( messageEvent );
        }

    }
}
