package com.binaryfun.videosdeminecraft;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;


public class PlayerActivity extends YouTubeBaseActivity implements YouTubePlayer.OnInitializedListener {

    private boolean descriptionOn = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);


        YouTubePlayerView playerView;
        playerView = (YouTubePlayerView) findViewById(R.id.player_view);
        playerView.initialize(YoutubeConnector.KEY, this);



        TextView title = (TextView) findViewById(R.id.videoBig_title);
        title.setText(getIntent().getStringExtra("VIDEO_TITLE"));

        showDescription();
    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
        if (!b) {
            youTubePlayer.loadVideo(getIntent().getStringExtra("VIDEO_ID"));
            //youTubePlayer.setShowFullscreenButton(false);
        }
    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
        Toast.makeText(this, "Initialization Failed", Toast.LENGTH_LONG).show();
    }


    private void showDescription() {
        Button descriptionB = (Button) findViewById(R.id.button);
        final View.OnClickListener myListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView titlebutton = (TextView) findViewById(R.id.button);
                TextView descriptionbutton = (TextView) findViewById(R.id.videoBig_description);

                if (!descriptionOn){
                    descriptionbutton.setText(getIntent().getStringExtra("VIDEO_DESCRIPTION"));
                    descriptionOn=true;
                    titlebutton.setText("Mostrar menos");
                }
                else{
                    descriptionbutton.setText(null);
                    descriptionOn=false;
                    titlebutton.setText("Mostrar mais");
                }
            }
        };
        descriptionB.setOnClickListener(myListener);
    }
}

