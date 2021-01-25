package com.codepath.wrh17b.flixter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.codepath.wrh17b.flixter.adapters.MovieAdapter;
import com.codepath.wrh17b.flixter.models.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Headers;

public class MainActivity extends AppCompatActivity {


    public static final String NOW_PLAYING_URL = "https://api.themoviedb.org/3/movie/now_playing?api_key=a07e22bc18f5cb106bfe4cc1f83ad8ed";
    public static final String TAG = "MainActivity: ";
    List<Movie> movies;
    RecyclerView rvMovies;
    MovieAdapter movieAdapter;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context =this;

        movies = new ArrayList<>();
        movieAdapter = new MovieAdapter(this, movies);

        rvMovies = findViewById(R.id.rvMovies);
        rvMovies.setAdapter(movieAdapter);
        rvMovies.setLayoutManager(new LinearLayoutManager(this));

        AsyncHttpClient client = new AsyncHttpClient();


        client.get(NOW_PLAYING_URL, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Headers headers, JSON json) {
                Log.d(TAG,"Success");
                JSONObject jsonObject = json.jsonObject;
                try {
                    JSONArray results = jsonObject.getJSONArray("results");
                    Log.i(TAG,"results: "+ results.toString());

                    /*
                    for(int j=0;j<results.length();j++){

                        movies.add(new Movie(results.getJSONObject(j)));
                        movieAdapter.notifyDataSetChanged();

                    }
                     */

                    movies.addAll(Movie.fromJsonArray(results));
                    movieAdapter.notifyDataSetChanged();

                    Log.i(TAG,"Movies: "+movies.size());

                } catch (JSONException e) {
                    Log.e(TAG,"Hit json exception",e);
                    Toast.makeText(context,(CharSequence) "Could not load movies.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(int i, Headers headers, String s, Throwable throwable) {
                Log.d(TAG,"Failure");
            }
        });










    }
}