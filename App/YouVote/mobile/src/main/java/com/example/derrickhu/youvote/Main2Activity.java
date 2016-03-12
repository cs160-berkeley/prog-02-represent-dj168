package com.example.derrickhu.youvote;

import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;


public class Main2Activity extends MainActivity {

    private TextView topDisplay;
    private TextView instructions;
    private ListView senList;
    private ListView repList;

    private String repsUrl;
    private String sunlightKey = "&apikey=2f7797179af1460c8b86c8b2cbd66615";
    final String googleKey = "&key=AIzaSyBR4N75a508ZhD6D6GoBmE_K-TPog_rclc" ;
    public String zipCode;
    public String latLon;
    public Double[] lat_long = new Double[2];


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        topDisplay = (TextView) findViewById(R.id.topDisplay);
        instructions = (TextView) findViewById(R.id.instructions);
        Bundle extras = getIntent().getExtras();
        zipCode = extras.getString("ZIP_CODE");
        latLon = extras.getString("LAT_LON");

        String text = "Your Representatives for".toString();
        topDisplay.setText(text + " " + zipCode);
        instructions.setText("Tap a portrait for more information.");
        if (latLon != null) {
            String[] latLonArray = latLon.split("[!]+");
            String lat = latLonArray[0];
            String lon = latLonArray[1];
            repsUrl = "http://congress.api.sunlightfoundation.com/legislators/locate?latitude=" + lat + "&" + "longitude=" + lon + sunlightKey;
        }

        else {
            repsUrl = "http://congress.api.sunlightfoundation.com/legislators/locate?zip=" + zipCode + sunlightKey;

        }
        String reverseGeoUrl = "https://maps.googleapis.com/maps/api/geocode/json?latlng=";
        getLatLong();
        String countyUrl = reverseGeoUrl + String.valueOf(lat_long[0]) + ",%20" + String.valueOf(lat_long[1]) + googleKey;
        new DownloadWebpageTask(getApplicationContext()).execute(repsUrl, countyUrl);
    }

    private class DownloadWebpageTask extends AsyncTask<String, Void, String[]> {
        private Context context;
        private DownloadWebpageTask(Context con) {
            context = con;
        }

        @Override
        protected String[] doInBackground(String ... urls) {
            // params comes from the execute() call: params[0] is the url.

            try {
                String[] toRtn = new String[urls.length];
                for (int i=0; i < urls.length; i++ ) {
                    URL url  = new URL(urls[i]);
                    HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

                    try {
                        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream(), "UTF-8"), 8);
                        StringBuilder stringBuilder = new StringBuilder();
                        String line;
                        while ((line = bufferedReader.readLine()) != null) {
                            stringBuilder.append(line).append("\n");
                        }
                        bufferedReader.close();
                        toRtn[i] = stringBuilder.toString();
                    }
                    finally{
                        urlConnection.disconnect();
                    }
                }
                return toRtn;
            }
            catch(Exception e) {
                Log.e("ERROR", e.getMessage(), e);
                return null;
            }

        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String[] result) {
            if (result == null) {
                System.out.println("Retrieving Json failed");
            }
            JSONArray jsonArray = null;
            try {
                JSONObject jsonObject = new JSONObject(result[0]);
                jsonArray = jsonObject.getJSONArray("results");

                JSONObject jsonCounty = new JSONObject(result[1]);
                String countyAndState = grabCounty(jsonCounty.toString());
                String obamaVote = getJsonResults(countyAndState);


                //Send info to watch
                Intent sendIntent2 = new Intent(getBaseContext(), PhoneToWatchService.class);
                sendIntent2.putExtra("COUNTY_STATE", countyAndState);
                sendIntent2.putExtra("OBAMA_VOTE", obamaVote);
                sendIntent2.putExtra("ZIP_CODE", zipCode);
                sendIntent2.putExtra("REP_DATA", jsonObject.toString());

                startService(sendIntent2);
            }
            catch (JSONException e){

            }
            senList = (ListView) findViewById(R.id.listView1);
            String[] senPhotos = new String[2];
            String[] senNames = new String[2];
            String[] senWebsites = new String[2];
            String[] senEmails = new String[2];
            String[] senTweets = new String[2];
            String[] senParties = new String[2];


            repList = (ListView) findViewById(R.id.listView2);

            String[] repPhotos = new String[jsonArray.length() - 2];
            String[] repNames = new String[jsonArray.length() - 2];
            String[] repWebsites = new String[jsonArray.length() - 2];
            String[] repEmails = new String[jsonArray.length() - 2];
            String[] repTweets = new String[jsonArray.length() - 2];
            String[] repParties = new String[jsonArray.length() - 2];

            int houseIndex = 0;
            int senIndex = 0;

            for (int i = 0; i < jsonArray.length(); i++) {
                try {
                    JSONObject repData = jsonArray.getJSONObject(i);
                    if (repData.getString("chamber").equals(("senate"))) {
                        senNames[senIndex] = "Sen. " + repData.getString("first_name") + " " + repData.getString("last_name") + " (" + repData.getString("party") + "- " + repData.getString("state") + ")";
                        senWebsites[senIndex] = repData.getString("website");
                        senEmails[senIndex] = repData.getString("oc_email");
                        senTweets[senIndex] = repData.getString("twitter_id");
                        senPhotos[senIndex] = repData.getString("bioguide_id");
                        senParties[senIndex] = repData.getString("party");
                        senIndex +=1;
                    }

                    if (repData.getString("chamber").equals(("house"))) {
                        repNames[houseIndex] = "Rep. " + repData.getString("first_name") + " " + repData.getString("last_name") + " (" + repData.getString("party") + "- " + repData.getString("state") + ")";
                        repWebsites[houseIndex] = repData.getString("website");
                        repEmails[houseIndex] = repData.getString("oc_email");
                        repTweets[houseIndex] = repData.getString("twitter_id");
                        repParties[houseIndex] = repData.getString("party");
                        repPhotos[houseIndex] = repData.getString("bioguide_id");
                        houseIndex +=1;
                    }
                }

                catch (JSONException e){

                }

            }

            final RepandSenAdapter senAdapter = new RepandSenAdapter(Main2Activity.this, senPhotos, senParties, senNames,senWebsites, senEmails, senTweets);
            senList.setAdapter(senAdapter);
            //Create the Adapter
            final RepandSenAdapter repAdapter = new RepandSenAdapter(Main2Activity.this, repPhotos, repParties, repNames,repWebsites, repEmails, repTweets);

            //Set the Adapter
            repList.setAdapter(repAdapter);

        }

        private String grabCounty(String jsonData) {
            String county = "administrative_area_level_2";
            String state = "administrative_area_level_1";
            String toRtn = "";
            try {
                JSONObject obj = new JSONObject(jsonData);
                JSONArray data = obj.getJSONArray("results");
                JSONArray address = data.getJSONObject(0).getJSONArray("address_components");
                for (int i = 0; i < address.length(); i++) {
                    JSONObject datum = address.getJSONObject(i);
                    String type = datum.getJSONArray("types").get(0).toString();
                    if (type.equals(county)) {
                        toRtn += datum.getString("short_name");
                    }
                    if (type.equals(state)) {
                        toRtn = toRtn + ", " + datum.getString("short_name");
                        return toRtn;
                    }
                }
            } catch (JSONException e) {
            }
        return "";
    }

        private String getVoteData() {
            String jsonString = "";
            try {
                InputStream stream = context.getAssets().open("newelectioncounty2012.json");
                int size = stream.available();
                byte[] buffer = new byte[size];
                stream.read(buffer);
                stream.close();
                jsonString = new String(buffer, "UTF-8");

            }
            catch (Exception e) {

            }
            return jsonString;
        }

        private String getJsonResults(String countyState) {
            String toRtn = "";
            try {
                JSONObject obj = new JSONObject(getVoteData()).getJSONObject(countyState);
                toRtn = obj.getString("obama") + ";" + obj.getString("romney");
            }
            catch (JSONException e) {

            }
            return toRtn;
        }


    }

    private void getLatLong() {
        Geocoder geocoder = new Geocoder(getApplicationContext());

        try {
            List<Address> addresses = geocoder.getFromLocationName(zipCode, 1);
            if (addresses != null && !addresses.isEmpty()) {
                Address address = addresses.get(0);
                lat_long[0] = address.getLatitude();
                lat_long[1] = address.getLongitude();
            }

        }
        catch (IOException e) {

        }
    }



}
