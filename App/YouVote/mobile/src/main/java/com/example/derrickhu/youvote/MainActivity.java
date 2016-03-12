package com.example.derrickhu.youvote;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.concurrent.ExecutionException;

import io.fabric.sdk.android.Fabric;

public class MainActivity extends Activity implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener{

    // Note: Your consumer key and secret should be obfuscated in your source code before shipping.
    private static final String TWITTER_KEY = "0UkFP52r2lXxsYsyiYEhYz1iY";
    private static final String TWITTER_SECRET = "e5qAEEeSOydCpzvX9budacvlHMvkhHj8aNxy0qGJ11KgSszv5V";

    //there's not much interesting happening. when the buttons are pressed, they start
    //the PhoneToWatchService with the cat name passed in.

    private Button mZipButton;
    private Button mCurrLocButton;
    private EditText zipInput;
    private Intent sendIntent;
    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;
    private String zipCode;
    private double lat;
    private double lon;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        Fabric.with(this, new Twitter(authConfig));
        setContentView(R.layout.activity_main);

        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }



        mZipButton = (Button) findViewById(R.id.zip_button);
        zipInput = (EditText) findViewById(R.id.zipInput);
        mCurrLocButton = (Button) findViewById(R.id.currLoc_button);
        final Context context = this;
        final String zipToCountyUrl = "https://maps.googleapis.com/maps/api/geocode/json?latlng=";
        final String apiKey = "AIzaSyBR4N75a508ZhD6D6GoBmE_K-TPog_rclc" ;
        final String zipToLatUrl = "https://maps.googleapis.com/maps/api/geocode/json?address=";

        final String sunlightUrl = "http://congress.api.sunlightfoundation.com/legislators/locate?zip=";
        final String sunlightApiKey = "&apikey=2f7797179af1460c8b86c8b2cbd66615";



        mZipButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (zipInput.getText().toString().length() == 5 && TextUtils.isDigitsOnly(zipInput.getText())) {
                    String zipCode = zipInput.getText().toString();
                    String validUrl = sunlightUrl + zipCode + sunlightApiKey;
                    try {
                        String obj = new DownloadWebpageTask(context).execute(validUrl).get();
                        if (obj.charAt(12) == (']')) {
                            TextView output = (TextView) findViewById(R.id.Output);
                            output.setText("Please type in a valid Zip Code.");
                        }
                        else {

                            sendIntent = new Intent(getBaseContext(), Main2Activity.class);
                            sendIntent.putExtra("ZIP_CODE", zipCode);
                            sendIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(sendIntent);
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }
                    //CONVERT ZIP TO LAT/LONG
                    //String url = zipToLatUrl + zipCode + apiKey;
                    //new DownloadWebpageTask(context).execute(url);


                    //Intent sendIntent2 = new Intent(getBaseContext(), PhoneToWatchService.class);
                    //sendIntent2.putExtra("ZIP_CODE", zipCode);
                    //startService(sendIntent2);
                } else {
                    TextView output = (TextView) findViewById(R.id.Output);
                    output.setText("Please type in a valid Zip Code.");
                }
            }
        });

        mCurrLocButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendIntent = new Intent(getBaseContext(), Main2Activity.class);
                mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                        mGoogleApiClient);
                if (mLastLocation != null) {
                    lat = mLastLocation.getLatitude();
                    lon = mLastLocation.getLongitude();
                    String zipCode = getZipCode(lat, lon);

                    //get postal code
                    sendIntent.putExtra("LAT_LON", lat + "!" + lon);
                    sendIntent.putExtra("ZIP_CODE", zipCode);
                    sendIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(sendIntent);

                    //GET COUNTY
                    //String reverseGeoUrl = zipToCountyUrl + lat + "," + lon + apiKey;
                    //new DownloadWebpageTask(context).execute(reverseGeoUrl);


                    //Intent sendIntent2 = new Intent(getBaseContext(), PhoneToWatchService.class);
                    //sendIntent2.putExtra("ZIP_CODE", zipCode);
                    //startService(sendIntent2);
                }

            }
        });
    }

    protected void onStart() {
        mGoogleApiClient.connect();
        super.onStart();
    }

    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    @Override
    public void onConnected(Bundle connectionHint) {

    }
    public String getZipCode(double lat, double lon) {
        final Geocoder gcd = new Geocoder(getApplicationContext());

        List<Address> addresses = null;
        try {
            addresses = gcd.getFromLocation(lat, lon, 5);
        } catch (IOException e) {
            e.printStackTrace();
        }
        for (Address address : addresses) {
            if(address.getLocality()!=null && address.getPostalCode()!=null){
                return address.getPostalCode();
            }
        }
        return "No Zip Code";
    }





    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onConnectionFailed(ConnectionResult connResult) {}

    @Override
    public void onConnectionSuspended(int i) {}

    private EditText urlText;
    private TextView textView;
    // When user clicks button, calls AsyncTask.
    // Before attempting to fetch the URL, makes sure that there is a network connection.
    public void myClickHandler(Context context) {
        // Gets the URL from the UI's text field.
        Activity context1 = (Activity) context;
        String stringUrl = urlText.getText().toString();
        ConnectivityManager connMgr = (ConnectivityManager)
                context1.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            new DownloadWebpageTask(context1).execute(stringUrl);
        } else {
            textView.setText("No network connection available.");
        }
    }

    // Uses AsyncTask to create a task away from the main UI thread. This task takes a
    // URL string and uses it to create an HttpUrlConnection. Once the connection
    // has been established, the AsyncTask downloads the contents of the webpage as
    // an InputStream. Finally, the InputStream is converted into a string, which is
    // displayed in the UI by the AsyncTask's onPostExecute method.
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