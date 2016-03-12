package com.example.derrickhu.youvote;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.widget.ImageView;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.MessageApi;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.NodeApi;
import com.google.android.gms.wearable.Wearable;

import java.io.InputStream;

/**
 * Created by derrickhu on 2/26/16.
 */
public class WatchToPhoneService extends Service {

    private GoogleApiClient mApiClient;

    @Override
    public void onCreate() {
        super.onCreate();
        //initialize the googleAPIClient for message passing
        mApiClient = new GoogleApiClient.Builder( this )
                .addApi( Wearable.API )
                .addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
                    @Override
                    public void onConnected(Bundle connectionHint) {
                    }
                    @Override
                    public void onConnectionSuspended(int cause) {
                    }
                })
                .build();
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        mApiClient.disconnect();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // Which cat do we want to feed? Grab this info from INTENT
        // which was passed over when we called startService
        if (intent.getExtras() != null) {
            Bundle extras = intent.getExtras();
            final String name = extras.getString("NAME");
            final String zip = extras.getString("ZIP_CODE");
            final String bioGuideID = extras.getString("BIOGUIDEID");

            new Thread(new Runnable() {
                @Override
                public void run() {
                    //first, connect to the apiclient
                    mApiClient.connect();
                    //now that you're connected, send a massage with the cat name
                    if (bioGuideID != null) {
                        sendMessage("/BIOGUIDEID", bioGuideID);
                    }
                    if (name != null) {
                        sendMessage("/NAME", name);
                    }
                    if (zip != null) {
                        sendMessage("/ZIP_CODE", zip);
                    }
                    //change new mssage
                }
            }).start();

            return START_STICKY;
        }
        return -1;
        }






    @Override //remember, all services need to implement an IBiner
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void sendMessage( final String path, final String text ) {
        //one way to send message: start a new thread and call .await()
        //see watchtophoneservice for another way to send a message
        new Thread( new Runnable() {
            @Override
            public void run() {
                NodeApi.GetConnectedNodesResult nodes = Wearable.NodeApi.getConnectedNodes( mApiClient ).await();
                for(Node node : nodes.getNodes()) {
                    //we find 'nodes', which are nearby bluetooth devices (aka emulators)
                    //send a message for each of these nodes (just one, for an emulator)
                    MessageApi.SendMessageResult result = Wearable.MessageApi.sendMessage(
                            mApiClient, node.getId(), path, text.getBytes() ).await();
                    //4 arguments: api client, the node ID, the path (for the listener to parse),
                    //and the message itself (you need to convert it to bytes.)
                }
            }
        }).start();
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