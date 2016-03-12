package com.example.derrickhu.youvote;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class DetailedActivity extends AppCompatActivity {
    private ListView billsList;
    private ListView commsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed);
        Bundle extras = getIntent().getExtras();
        String bioGuideId = extras.getString("BIOGUIDEID");


        String apiKey = "&apikey=2f7797179af1460c8b86c8b2cbd66615";
        String repUrl = "http://congress.api.sunlightfoundation.com/legislators?bioguide_id=" + bioGuideId + apiKey;
        String billsUrl = "http://congress.api.sunlightfoundation.com/bills?sponsor_id=" + bioGuideId + apiKey;
        String committeesUrl = "http://congress.api.sunlightfoundation.com/committees?member_ids=" + bioGuideId + apiKey;
        new DownloadWebpageTask(getApplicationContext()).execute(repUrl, billsUrl, committeesUrl);
    }

    private class DownloadWebpageTask extends AsyncTask<String, Void, String[]> {
        private Context context;
        private DownloadWebpageTask(Context con) {
            context = con;
        }

        @Override
        protected String[] doInBackground(String ... urls) {
            String[] toRtn = new String[urls.length];
            // params comes from the execute() call: params[0] is the url.
            try {
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
            JSONArray jsonArrayRep = null;
            JSONArray jsonArrayBill = null;
            JSONArray jsonArrayComm = null;
            try {
                JSONObject repObj = new JSONObject(result[0]);
                jsonArrayRep = repObj.getJSONArray("results");
                JSONObject billObj = new JSONObject(result[1]);
                jsonArrayBill = billObj.getJSONArray("results");
                JSONObject commObj = new JSONObject(result[2]);
                jsonArrayComm = commObj.getJSONArray("results");
            }
            catch (JSONException e){

            }
            billsList = (ListView) findViewById(R.id.bills1);
            commsList = (ListView) findViewById(R.id.comms1);
            String bioGuideId = "";
            String titleAndName = "";
            String endTerm = "";
            String party="";
            String[] committees = new String[jsonArrayComm.length()];
            String[] bills = new String[5];

            for (int i = 0; i < jsonArrayRep.length(); i++) {
                try {
                    JSONObject repData = jsonArrayRep.getJSONObject(i);
                    titleAndName = repData.getString("title") + ". " + repData.getString("first_name") + " " + repData.getString("last_name") + " (" + repData.getString("party") + "- " + repData.getString("state") + ")";
                    endTerm = repData.getString("term_end");
                    party = repData.getString("party");
                    TextView titleAndNameView = (TextView) findViewById(R.id.titleAndName);
                    titleAndNameView.setText(titleAndName);
                    TextView endTermView = (TextView) findViewById(R.id.endTerm);
                    endTermView.setText("End Term: " + endTerm);
                    bioGuideId = repData.getString("bioguide_id");
                }
                catch (JSONException e){
                }
            }
            for (int i = 0; i < jsonArrayComm.length(); i++) {
                try {
                    JSONObject repData = jsonArrayComm.getJSONObject(i);
                    committees[i] = repData.getString("name");
                }
                catch (JSONException e){
                }
            }
            int billIndex = 0;
            int j = 0;
            while (billIndex < 5 && j < jsonArrayBill.length()) {
                try {
                    JSONObject repData = jsonArrayBill.getJSONObject(j);
                    if (!repData.getString("short_title").equals("null")) {
                        bills[billIndex] = repData.getString("introduced_on") + " " + repData.getString("short_title");
                        billIndex++;
                    }
                    j++;
                }
                catch (JSONException e){
                }
            }

            //Create the Adapter
            final DetailedAdapter billsAdapter = new DetailedAdapter(getApplicationContext(), bills, party);

            //Set the Adapter
            billsList.setAdapter(billsAdapter);

            //Create the Adapter
            final DetailedAdapter commsAdapter = new DetailedAdapter(getApplicationContext(),committees, party);

            //Set the Adapter
            commsList.setAdapter(commsAdapter);
            ImageView pictureView = (ImageView) findViewById(R.id.portrait);

            String picUrl = "https://theunitedstates.io/images/congress/225x275/";
            new DownloadImageTask(pictureView)
                    .execute(picUrl + bioGuideId + ".jpg");
        }
    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        private Context context;
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }


        @Override
        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }

}
