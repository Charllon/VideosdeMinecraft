package com.binaryfun.videosdeminecraft;

import android.content.Context;
import android.util.Log;

import com.fasterxml.jackson.core.io.UTF8Writer;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.PlaylistItem;
import com.google.api.services.youtube.model.PlaylistItemListResponse;
import com.google.api.services.youtube.model.Video;
import com.google.api.services.youtube.model.VideoListResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class YoutubeConnector {

    private static final long MAX_RESULT = 50;

    private YouTube.Videos.List infos;
    private YouTube.PlaylistItems.List play;
    public String IDs;


    public static final String KEY = "AIzaSyAIrawmcZEeoee95Pnga7BwiFneGJD9Oqs";

    public YoutubeConnector(Context context) {
        YouTube youtube;

        youtube = new YouTube.Builder(new NetHttpTransport(), new JacksonFactory(), new HttpRequestInitializer() {
            @Override
            public void initialize(HttpRequest httpRequest) throws IOException {
            }
        }).setApplicationName(context.getString(R.string.app_name)).build();

        try {

            play = youtube.playlistItems().list("snippet");
            play.setKey(KEY);
            play.setPlaylistId("PLZKuf2oBY67iEm6wKSyY-W4X0Pe8sO9SS");
            play.setMaxResults(MAX_RESULT);
            play.setFields("items(snippet/title,snippet/channelId,snippet/description,snippet/thumbnails/medium/url,snippet/resourceId/videoId)");

            infos = youtube.videos().list("snippet,statistics");
            infos.setKey(KEY);
            infos.setFields("items(snippet/channelTitle,statistics/viewCount)");

        } catch (IOException e) {
            Log.d("YC", "Could not initialize: " + e.getMessage());
        }
    }

    public List<VideoItem> search() {

        try {
            PlaylistItemListResponse response = play.execute();
            List<PlaylistItem> result = response.getItems();

            List<Video> temp2;
            List<VideoItem> items = new ArrayList<VideoItem>();

            IDs = result.get(0).getSnippet().getResourceId().getVideoId();

            for(int k = 1; k < MAX_RESULT; k++){

                IDs = IDs + "," + result.get(k).getSnippet().getResourceId().getVideoId();
            }

            infos.setId(IDs);
            VideoListResponse resp = this.infos.execute();
            temp2 = resp.getItems();

            for (int i=0; i < MAX_RESULT; i++) {
                VideoItem item = new VideoItem();

                item.setTitle(result.get(i).getSnippet().getTitle());
                item.setChanneltitle("por " + temp2.get(i).getSnippet().getChannelTitle());
                item.setDescription(result.get(i).getSnippet().getDescription());
                item.setViews(temp2.get(i).getStatistics().getViewCount().toString() + " visualizações");
                item.setThumbnailURL(result.get(i).getSnippet().getThumbnails().getMedium().getUrl());
                item.setId(result.get(i).getSnippet().getResourceId().getVideoId());
                items.add(item);
            }
            return items;
        } catch (IOException e) {
            Log.d("YC", "Could not search: " + e);
            return null;
        }
    }
}
