package com.example.derrickhu.youvote;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.AppSession;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterApiClient;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.core.services.StatusesService;

import java.io.InputStream;
import java.util.List;

import io.fabric.sdk.android.Fabric;

/**
 * Created by derrickhu on 2/27/16.
 */

public class RepandSenAdapter extends BaseAdapter {

    private final Context context;
    private final String[] photos;
    private final String[] parties;
    private final String[] names;
    private final String[] websites;
    private final String[] emails;
    private final String[] tweets;
    private static final String TWITTER_KEY = "0UkFP52r2lXxsYsyiYEhYz1iY";
    private static final String TWITTER_SECRET = "e5qAEEeSOydCpzvX9budacvlHMvkhHj8aNxy0qGJ11KgSszv5V";


    public RepandSenAdapter(Main2Activity context, String[] photos, String[] parties,  String[] names, String[] websites, String[] emails, String[] tweets) {

        this.context = context;
        this.photos = photos;
        this.names = names;
        this.websites = websites;
        this.emails = emails;
        this.tweets = tweets;
        this.parties = parties;
    }

    @Override

    public View getView(final int position, View convertView, ViewGroup parent) {


        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View repRow = inflater.inflate(R.layout.repandsen, parent, false);

        ImageView pictureView = (ImageView) repRow.findViewById(R.id.dianne);
        TextView nameView = (TextView) repRow.findViewById(R.id.senator);
        TextView websiteView = (TextView) repRow.findViewById(R.id.website);
        TextView emailView = (TextView) repRow.findViewById(R.id.email);
        final TextView tweetView = (TextView) repRow.findViewById(R.id.twitter);




        if (parties[position] != null) {
            if (parties[position].equals("R")) {
                repRow.setBackgroundColor(Color.parseColor("#A72020"));
            }
            if (parties[position].equals("D")) {
                repRow.setBackgroundColor(Color.parseColor("#133269"));
            }
            if (parties[position].equals("I")) {
                repRow.setBackgroundColor(Color.parseColor("#C5CAD4"));
            }
        }

        String picUrl = "https://theunitedstates.io/images/congress/225x275/";

        new DownloadImageTask(pictureView, context)
                .execute(picUrl + photos[position] + ".jpg");


        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        Fabric.with(context, new Twitter(authConfig));
        TwitterCore.getInstance().logInGuest(new com.twitter.sdk.android.core.Callback<AppSession>() {

            @Override
            public void success (Result<AppSession> result) {
                AppSession session = result.data;
                TwitterApiClient twitterApiClient = TwitterCore.getInstance().getApiClient();
                StatusesService statusesService = twitterApiClient.getStatusesService();
                statusesService.userTimeline(null, tweets[position], 1, null, null, false, true, false, false, new com.twitter.sdk.android.core.Callback<List<Tweet>>() {
                    @Override
                    public void success(Result<List<Tweet>> result) {
                        for (Tweet Tweet : result.data) {
                            tweetView.setText(Tweet.text);
                        }
                    }

                    @Override
                    public void failure(TwitterException e) {
                        e.printStackTrace();
                    };
        });

        }
        @Override
        public void failure(TwitterException e) {
            e.printStackTrace();

        }
    });


        nameView.setText(names[position]);
        websiteView.setText(websites[position]);
        emailView.setText(emails[position]);
        tweetView.setText(tweets[position]);


        //Attaching onClickListener

        repRow.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                Intent intent = new Intent(context, DetailedActivity.class);
                intent.putExtra("BIOGUIDEID", photos[position]);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });

        return repRow;

    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getCount() {
        return names.length;
    }


    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        private Context context;
        ImageView bmImage;
        public Bitmap imageBitmap;

        public DownloadImageTask(ImageView bmImage, Context context) {
            this.bmImage = bmImage;
            this.context = context;
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
            imageBitmap = result;
            bmImage.setImageBitmap(result);

//            ByteArrayOutputStream stream = new ByteArrayOutputStream();
//            imageBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
//            byte[] byteArray = stream.toByteArray();
//
//            Intent in1 = new Intent(this.context, PhoneToWatchService.class);
//            in1.putExtra("IMAGE", byteArray);
//            this.context.startService(in1);
        }
    }



}
