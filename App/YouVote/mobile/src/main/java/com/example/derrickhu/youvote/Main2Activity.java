package com.example.derrickhu.youvote;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;


public class Main2Activity extends MainActivity {

    private TextView topDisplay;
    private TextView instructions;
    private ListView senList;
    private ListView repList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        topDisplay = (TextView) findViewById(R.id.topDisplay);
        instructions = (TextView) findViewById(R.id.instructions);
        Bundle extras = getIntent().getExtras();
        String zipCode = extras.getString("ZIP_CODE");
        String text = "Your Representatives for".toString();
        topDisplay.setText(text + " " + zipCode);
        instructions.setText("Tap a portrait for more information.");



        senList = (ListView) findViewById(R.id.listView1);
        int[] senPhotos = {R.drawable.dianne, R.drawable.dianne, R.drawable.dianne};
        String[] senNames = {"Dianne Feinstein", "Barbara Boxer"};
        String[] senParties = {"Democrat", "Democrat"};
        String[] senWebsites = {"feinstein.senate.gov", "boxer.senate.gov"};
        String[] senEmails = {"feinstein@senate.gov", "boxer@senate.gov"};
        String[] senTweets = {"Hi I am Feinstein" , "Hi I am Boxer"};

        final RepandSenAdapter senAdapter = new RepandSenAdapter(this, senPhotos, senNames, senParties, senWebsites, senEmails, senTweets);
        senList.setAdapter(senAdapter);


        repList = (ListView) findViewById(R.id.listView2);
        int[] repPhotos = {R.drawable.dianne};
        String[] repNames = {"Darrell Issa"};
        String[] repParties = {"Republican"};
        String[] repWebsites = {"issa.house.gov"};
        String[] repEmails = {"issa@rep.gov"};
        String[] repTweets = {"Hi I am Issa"};

        //Create the Adapter
        final RepandSenAdapter repAdapter = new RepandSenAdapter(this, repPhotos, repNames, repParties, repWebsites, repEmails, repTweets);

        //Set the Adapter
        repList.setAdapter(repAdapter);

    }

}
