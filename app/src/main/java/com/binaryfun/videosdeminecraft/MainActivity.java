package com.binaryfun.videosdeminecraft;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ListView videosFound;
    private Handler handler;
    public List<VideoItem> searchResults;
    InterstitialAd mInterstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.mipmap.ic_launcher);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        getSupportActionBar().setTitle("   Vídeos de Minecraft");
        setContentView(R.layout.activity_main);

        int k = getResources().getConfiguration().orientation;

        if (k == 1){
            Toast.makeText(this, "LANDSCAPE MODE", Toast.LENGTH_LONG).show();
        }

        // BANNER
        AdView mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-6871836896677551/5764536624");

        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                requestNewInterstitial();
            }
        });
        requestNewInterstitial();

        videosFound = (ListView) findViewById(R.id.videos_found);
        handler = new Handler();

        searchOnYoutube();
        addClickListener();

     // cria o alarme a cada dia pra exibir o alerta
        Intent notificationIntent = new Intent(this, CheckUpdates.class);
        PendingIntent contentIntent = PendingIntent.getService(this, 0, notificationIntent,
                PendingIntent.FLAG_CANCEL_CURRENT);

        AlarmManager am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        am.cancel(contentIntent);
        am.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis()
                + AlarmManager.INTERVAL_DAY, AlarmManager.INTERVAL_DAY, contentIntent);
    }

    private void searchOnYoutube() {

        new Thread() {
            @Override
            public void run() {
                YoutubeConnector yc = new YoutubeConnector(MainActivity.this);

                searchResults = yc.search();

                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        updateVideosFound();
                    }
                });
            }
        }.start();
    }

    public void updateVideosFound() {
        ArrayAdapter<VideoItem> adapter = new ArrayAdapter<VideoItem>(getApplicationContext(), R.layout.video_item, searchResults) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                if (convertView == null) {
                    convertView = getLayoutInflater().inflate(R.layout.video_item, parent, false);
                }

                ImageView thumbnail = (ImageView) convertView.findViewById(R.id.video_thumbnail);
                TextView title = (TextView) convertView.findViewById(R.id.video_title);
                TextView channeltitle = (TextView) convertView.findViewById(R.id.video_channeltitle);
                TextView views = (TextView) convertView.findViewById(R.id.video_views);

                VideoItem searchResult = searchResults.get(position);

                Picasso.with(getApplicationContext()).load(searchResult.getThumbnailURL()).into(thumbnail);
                title.setText(searchResult.getTitle());
                channeltitle.setText(searchResult.getChanneltitle());
                views.setText(searchResult.getViews());
                return convertView;
            }

        };
        videosFound.setAdapter(adapter);
    }

    private void addClickListener() {
        videosFound.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent intent = new Intent(getApplication(), PlayerActivity.class);
                        intent.putExtra("VIDEO_ID", searchResults.get(position).getId());
                        intent.putExtra("VIDEO_TITLE", searchResults.get(position).getTitle());
                        intent.putExtra("VIDEO_DESCRIPTION", searchResults.get(position).getDescription());

                        startActivity(intent);
                    }
                }
        );
    }

    // cria o menu
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }
    // pega o click do menu
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.action_settings:
                openSettings();
                return true;
            case R.id.aboutus:
                openAboutUs();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    // abre a view de configuracoes
    private void openSettings(){
        Intent set = new Intent(getApplication(), SettingsActivity.class);
        startActivity(set);
    }
    private void openAboutUs(){
        Intent set = new Intent(getApplication(), AboutUs.class);
        startActivity(set);
    }

    // fechar a notificacao quando abre a tela
    public static void cancelNotification(Context ctx, int notifyId) {
        String ns = Context.NOTIFICATION_SERVICE;
        NotificationManager nMgr = (NotificationManager) ctx.getSystemService(ns);
        nMgr.cancel(notifyId);
    }


    @Override
    protected void onResume() {
        super.onResume();
        displayInterstitial();
    }

    private void requestNewInterstitial() {
        AdRequest adRequest1 = new AdRequest.Builder().build();

        mInterstitialAd.loadAd(adRequest1);
    }

    public void displayInterstitial() {
        if (mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        }
    }
}

