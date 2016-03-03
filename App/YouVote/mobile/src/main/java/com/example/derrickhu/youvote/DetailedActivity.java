package com.example.derrickhu.youvote;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

public class DetailedActivity extends AppCompatActivity {




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed);


        Bundle extras = getIntent().getExtras();
        String name = extras.getString("NAME");

        ImageView pictureView = (ImageView) findViewById(R.id.portrait);
        TextView nameView = (TextView) findViewById(R.id.name1);
        TextView partyView = (TextView) findViewById(R.id.party);
        TextView senatorView = (TextView) findViewById(R.id.senator);
        TextView termView = (TextView) findViewById(R.id.term);
        TextView billView = (TextView) findViewById(R.id.bills);
        TextView committeeView = (TextView) findViewById(R.id.committees);

        billView.setText("Bill 1, Bill 2");
        committeeView.setText("Armed Services");


        if (name.equals("Dianne Feinstein")) {

            pictureView.setImageResource(R.drawable.dianne);
            nameView.setText("Dianne Feinstein");
            partyView.setText("Democrat");
            senatorView.setText("Senator");
            termView.setText("5th term 2012-2016");

        }

        if (name.equals("Barbara Boxer")) {
            pictureView.setImageResource(R.drawable.dianne);
            nameView.setText("Barbara Boxer");
            partyView.setText("Democrat");
            senatorView.setText("Senator");
            termView.setText("5th term 2012-2016");

        }

        if (name.equals("Darrell Issa")) {
            pictureView.setImageResource(R.drawable.dianne);
            nameView.setText("Darrell Issa");
            partyView.setText("Republican");
            senatorView.setText("Congressman");
            termView.setText("5th term 2012-2014");

        }

    }

}
