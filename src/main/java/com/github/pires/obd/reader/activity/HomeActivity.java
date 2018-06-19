package com.github.pires.obd.reader.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.Button;

import com.github.pires.obd.reader.R;

public class HomeActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        //Event listener for new drive card
        final CardView newdrive = (CardView) findViewById(R.id.newDrive);
        newdrive.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent newdriveintent = new Intent(HomeActivity.this, NewDrive.class);
                startActivity(newdriveintent);
            }
        });
        //Event listener for settings
        final CardView settings = (CardView) findViewById(R.id.settings);
        settings.setOnClickListener(new View.OnClickListener() {
            public void onClick(View x) {
                Intent settingsintent = new Intent(HomeActivity.this, ConfigActivity.class);
                startActivity(settingsintent);
            }
        });
    }
}
