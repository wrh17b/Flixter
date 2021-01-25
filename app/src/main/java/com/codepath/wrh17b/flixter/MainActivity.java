package com.codepath.wrh17b.flixter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ActionBar;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.codepath.wrh17b.flixter.adapters.ComplexMovieAdapter;
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
    ComplexMovieAdapter complexMovieAdapter;
    Context context;
    int page;
    int total_pages;

    private void getMovies(AsyncHttpClient client){
        client.get(NOW_PLAYING_URL, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Headers headers, JSON json) {
                Log.d(TAG,"Success");
                JSONObject jsonObject = json.jsonObject;
                try {
                    JSONArray results = jsonObject.getJSONArray("results");
                    page = jsonObject.getInt("page");
                    total_pages=jsonObject.getInt("total_pages");
                    Log.i(TAG,"results: "+ results.toString());
                    movies.clear();
                    movies.addAll(Movie.fromJsonArray(results));

                    for(int j=0;j<movies.size();j++){
                        complexMovieAdapter.notifyItemChanged(j);
                    }

                    //movieAdapter.notifyDataSetChanged();

                    Log.i(TAG,"Movies: "+movies.size());

                } catch (JSONException e) {
                    Log.e(TAG,"Hit json exception",e);
                    Toast.makeText(context,(CharSequence) "Could not load movies.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(int i, Headers headers, String s, Throwable throwable) {
                Log.d(TAG,"Failure");
                getMovies(client);

            }
        });

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context =this;

        page=1;
        total_pages=1;

        movies = new ArrayList<>();
        complexMovieAdapter = new ComplexMovieAdapter(this, movies);

        rvMovies = findViewById(R.id.rvMovies);
        rvMovies.setAdapter(complexMovieAdapter);
        //rvMovies.setAdapter(new ComplexMovieAdapter(this,movies));
        rvMovies.setLayoutManager(new LinearLayoutManager(this));


        movies.addAll(Movie.EmptyMovieList());
        complexMovieAdapter.notifyDataSetChanged();

        AsyncHttpClient client = new AsyncHttpClient();
        getMovies(client);

/*
        client.get(NOW_PLAYING_URL, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Headers headers, JSON json) {
                Log.d(TAG,"Success");
                JSONObject jsonObject = json.jsonObject;
                try {
                    JSONArray results = jsonObject.getJSONArray("results");
                    Log.i(TAG,"results: "+ results.toString());

                    movies.addAll(Movie.fromJsonArray(results));

                    for(int j=0;j<movies.size();j++){
                        movieAdapter.notifyItemInserted(j);
                    }

                    //movieAdapter.notifyDataSetChanged();

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
*/









    }
}