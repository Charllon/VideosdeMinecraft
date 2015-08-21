package com.binaryfun.videosdeminecraft;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class AboutUs  extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aboutus);

        TextView email = (TextView) findViewById(R.id.Emails);

        email.setText("charllondeveloper@gmail.com");

    }
}