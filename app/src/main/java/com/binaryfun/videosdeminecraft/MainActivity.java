package com.binaryfun.videosdeminecraft;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MainActivity extends ActionBarActivity {

    private ListView videosFound;
    private Handler handler;
    public List<VideoItem> searchResults;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // BANNER
        AdView mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)//apagar
                .build();
        mAdView.loadAd(adRequest);

        videosFound = (ListView) findViewById(R.id.videos_found);
        handler = new Handler();

        searchOnYoutube();
        addClickListener();
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
}

