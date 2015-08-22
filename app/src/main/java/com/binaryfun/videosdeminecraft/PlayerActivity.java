package com.binaryfun.videosdeminecraft;

import android.os.Bundle;
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

    InterstitialAd mInterstitialAd;
    private boolean descriptionOn = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-6871836896677551/5764536624");

        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                requestNewInterstitial();
            }
        });
        requestNewInterstitial();

        YouTubePlayerView playerView;
        playerView = (YouTubePlayerView) findViewById(R.id.player_view);
        playerView.initialize(YoutubeConnector.KEY, this);

        TextView title = (TextView) findViewById(R.id.videoBig_title);
        title.setText(getIntent().getStringExtra("VIDEO_TITLE"));

        showDescription();

    }

    @Override
    protected void onStop() {
        super.onStop();
        displayInterstitial();
    }

    private void requestNewInterstitial() {
        AdRequest adRequest1 = new AdRequest.Builder().build();

        mInterstitialAd.loadAd(adRequest1);
    }

    public void displayInterstitial() {
        if (mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        }else {
            Toast.makeText(this, "Falha ao carregar Ad", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
        if(!b) {
            youTubePlayer.cueVideo(getIntent().getStringExtra("VIDEO_ID"));
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

