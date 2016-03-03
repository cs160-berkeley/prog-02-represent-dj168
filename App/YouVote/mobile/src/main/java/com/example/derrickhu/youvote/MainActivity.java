package com.example.derrickhu.youvote;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends Activity {
    //there's not much interesting happening. when the buttons are pressed, they start
    //the PhoneToWatchService with the cat name passed in.

    private Button mZipButton;
    private Button mCurrLocButton;
    private EditText zipInput;
    private Intent sendIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mZipButton = (Button) findViewById(R.id.zip_button);
        zipInput = (EditText) findViewById(R.id.zipInput);
        mCurrLocButton = (Button) findViewById(R.id.currLoc_button);

        final Context context = this;

        mZipButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (zipInput.getText().toString().length() == 5 && TextUtils.isDigitsOnly(zipInput.getText())) {
                    String zipCode = zipInput.getText().toString();
                    sendIntent = new Intent(getBaseContext(), Main2Activity.class);
                    sendIntent.putExtra("ZIP_CODE", zipCode);
                    startActivity(sendIntent);

                    Intent sendIntent2 = new Intent(getBaseContext(), PhoneToWatchService.class);
                    sendIntent2.putExtra("ZIP_CODE", zipCode);
                    startService(sendIntent2);
                }
                else {
                    TextView output = (TextView) findViewById(R.id.Output);
                    output.setText("Please type in a valid Zip Code.");
                }
            }
        });

        mCurrLocButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    sendIntent = new Intent(getBaseContext(), Main2Activity.class);
                    sendIntent.putExtra("ZIP_CODE", "94704");
                    startActivity(sendIntent);
                    Intent sendIntent2 = new Intent(getBaseContext(), PhoneToWatchService.class);
                    sendIntent2.putExtra("ZIP_CODE", "94704");
                    startService(sendIntent2);
            }
        });
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
}