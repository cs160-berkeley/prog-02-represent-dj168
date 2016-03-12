package com.example.derrickhu.youvote;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.WearableListenerService;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Random;
import java.util.concurrent.ExecutionException;

/**
 * Created by derrickhu on 2/26/16.
 */

public class PhoneListenerService extends WearableListenerService {

    //   WearableListenerServices don't need an iBinder or an onStartCommand: they just need an onMessageReceieved.
    private static final String bioguideFromWatch = "/BIOGUIDEID";
    private static final String zipFromWatch = "/ZIP_CODE";

    @Override
    public void onMessageReceived(MessageEvent messageEvent) {
        Log.d("T", "in PhoneListenerService, got: " + messageEvent.getPath());
        if( messageEvent.getPath().equalsIgnoreCase(bioguideFromWatch) ) {

            // Value contains the String we sent over in WatchToPhoneService, "good job"
            String value = new String(messageEvent.getData(), StandardCharsets.UTF_8);
            //else {
            Intent detailedIntent = new Intent(this, DetailedActivity.class);
            detailedIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            detailedIntent.putExtra("BIOGUIDEID", value);
            startActivity(detailedIntent);
          //  }
        }

        if( messageEvent.getPath().equalsIgnoreCase(zipFromWatch) ) {
            // Value contains the String we sent over in WatchToPhoneService, "good job"
            String value = new String(messageEvent.getData(), StandardCharsets.UTF_8);

            final String sunlightUrl = "http://congress.api.sunlightfoundation.com/legislators/locate?zip=";
            final String sunlightApiKey = "&apikey=2f7797179af1460c8b86c8b2cbd66615";
            String validUrl = sunlightUrl + value + sunlightApiKey;
            try {
                String obj = new DownloadWebpageTask(getApplicationContext()).execute(validUrl).get();
                while (obj.charAt(12) == ']') {
                    Random rand = new Random();
                    int randZip = rand.nextInt(99999 - 10000 + 1) + 10000;
                    String randZipString = Integer.toString(randZip);
                    value = randZipString;
                    validUrl = sunlightUrl + value + sunlightApiKey;
                    obj = new DownloadWebpageTask(getApplicationContext()).execute(validUrl).get();
                }
                    Intent zipIntent = new Intent(this, Main2Activity.class);
                    zipIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    zipIntent.putExtra("ZIP_CODE", value);
                    startActivity(zipIntent);

            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }

        }

        else {
            super.onMessageReceived( messageEvent);
        }

    }

    private class DownloadWebpageTask extends AsyncTask<String, Void, String> {
        private Context context;
        private DownloadWebpageTask(Context con) {
            context = con;
        }
        @Override
        protected String doInBackground(String ... url) {
            // params comes from the execute() call: params[0] is the url.
            try {
                URL url2 = new URL(url[0]);
                HttpURLConnection urlConnection = (HttpURLConnection) url2.openConnection();
                try {
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                    StringBuilder stringBuilder = new StringBuilder();
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        stringBuilder.append(line).append("\n");
                    }
                    bufferedReader.close();
                    return stringBuilder.toString();
                }
                finally{
                    urlConnection.disconnect();
                }
            }
            catch(Exception e) {
                Log.e("ERROR", e.getMessage(), e);
                return null;
            }

        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            //textView.setText(result);

        }
    }
}
