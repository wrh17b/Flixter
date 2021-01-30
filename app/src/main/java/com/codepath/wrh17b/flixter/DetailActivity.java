package com.codepath.wrh17b.flixter;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.RatingBar;
import android.widget.TextView;

import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.codepath.wrh17b.flixter.models.Movie;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import okhttp3.Headers;

public class DetailActivity extends YouTubeBaseActivity {
    public static final String YOUTUBE_API_KEY ="AIzaSyApFFtwMRpHPJteaEPyvR2Mh_9ZxWiw104";
    public static final String VIDEOS_URL ="https://api.themoviedb.org/3/movie/%d/videos?api_key=a07e22bc18f5cb106bfe4cc1f83ad8ed";
    public static final String TAG="DetailActivity";

    String youtubeKey;

    TextView tvTitle;
    TextView tvRelease;
    TextView tvOverview;
    RatingBar rbRatingBar;
    YouTubePlayerView youTubePlayerView;
    Movie movie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Intent intent = getIntent();
        movie = Parcels.unwrap(intent.getParcelableExtra("movie"));

        tvTitle=findViewById(R.id.tvTitle);
        tvRelease=findViewById(R.id.tvReleaseDate);
        tvOverview=findViewById(R.id.tvOverview);
        rbRatingBar=findViewById(R.id.ratingBar);
        youTubePlayerView = findViewById(R.id.player);

        tvTitle.setText(movie.getTitle());
        tvOverview.setText(movie.getOverview());
        tvRelease.setText(String.format("Release Date:%s",movie.getRelease_date()));
        rbRatingBar.setRating((float)movie.getVote_average());

        AsyncHttpClient client = new AsyncHttpClient();
        client.get(String.format(VIDEOS_URL, movie.getId()), new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Headers headers, JSON json) {
                Log.d(TAG,"client onSuccess");
                try {
                    JSONArray results = json.jsonObject.getJSONArray("results");
                    if(results.length()==0){
                        return;
                    }
                    youtubeKey=null;
                    for(int j=0;j<results.length();j++){
                        JSONObject details = results.getJSONObject(j);
                        String site = details.getString("site");
                        String type=details.getString("type");

                        if(site.equals("YouTube")&&type.equals("Trailer")){
                            youtubeKey=details.getString("key");
                            break;
                        }
                    }
                    if(youtubeKey==null){
                        return;
                    }

                    initializeYoutube(youtubeKey);


                } catch (JSONException e) {
                    Log.e(TAG,"Error loading videos",e);
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(int i, Headers headers, String s, Throwable throwable) {
                Log.d(TAG,"client onFailure");

            }
        });
/*
        youTubePlayerView.initialize(YOUTUBE_API_KEY, new YouTubePlayer.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
                Log.d(TAG,"onInitializationSuccess");

                AsyncHttpClient client = new AsyncHttpClient();
                client.get(String.format(VIDEOS_URL, movie.getId()), new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int i, Headers headers, JSON json) {
                        Log.d(TAG,"client onSuccess");
                        try {
                            JSONArray results = json.jsonObject.getJSONArray("results");
                            if(results.length()==0){
                                return;
                            }
                            String youtubeKey=null;
                            for(int j=0;j<results.length();j++){
                                JSONObject details = results.getJSONObject(j);
                                String site = details.getString("site");
                                String type=details.getString("type");

                                if(site.equals("YouTube")&&type.equals("Trailer")){
                                    youtubeKey=details.getString("key");
                                    break;
                                }
                            }
                            if(youtubeKey==null){
                                return;
                            }
                            if(movie.getVote_average()>2.5){
                                youTubePlayer.loadVideo(youtubeKey);
                            }else{
                                youTubePlayer.cueVideo(youtubeKey);
                            }
                            //String youtubeKey = results.getJSONObject(0).getString("key");



                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }

                    @Override
                    public void onFailure(int i, Headers headers, String s, Throwable throwable) {
                        Log.d(TAG,"client onFailure");


                    }
                });


            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
                Log.d(TAG,String.format("onInitializationFailure: %s",youTubeInitializationResult.toString()));
            }
        });
*/

    }

    private void initializeYoutube(String youtubeKey) {
        youTubePlayerView.initialize(YOUTUBE_API_KEY, new YouTubePlayer.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
                Log.d(TAG,"onInitializationSuccess");
                if(movie.getVote_average()>2.5){
                    //If popular movie, autoplay
                    youTubePlayer.loadVideo(youtubeKey);
                }else{
                    youTubePlayer.cueVideo(youtubeKey);
                }
            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
                Log.d(TAG,"onInitializationFailure");
            }
        });
    }
}