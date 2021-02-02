package com.codepath.wrh17b.flixter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ActionBar;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.widget.Toast;

import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.codepath.wrh17b.flixter.adapters.ComplexMovieAdapter;
import com.codepath.wrh17b.flixter.adapters.MovieAdapter;
import com.codepath.wrh17b.flixter.interfaces.OnBottomReachedListener;
import com.codepath.wrh17b.flixter.interfaces.OnTopReachedListener;
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
    public static final String TAG = "MainActivity: ",PAGETAG="Page: ";
    public static final int NEXT_PAGE=1, PREVIOUS_PAGE=-1, MAX_SIZE=40;
    List<Movie> movies;
    RecyclerView rvMovies;
    ComplexMovieAdapter complexMovieAdapter;
    Context context;
    AsyncHttpClient client;
    int page;
    int total_pages;
    int page_in_loading;
    int[] pages_currently_shown;


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
                    page_in_loading=page;
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

    private void getMoreMovies(AsyncHttpClient client,int direction){
        int page_to_load;
        if(direction==NEXT_PAGE){
            page_to_load=pages_currently_shown[1]+1;
        }else{
            page_to_load=pages_currently_shown[0]-1;
        }
        if((page_to_load>total_pages||page_to_load<1)&&page_to_load!=page_in_loading){
            Log.i(PAGETAG,"invalid getMoreMovies call");
        }else {
            page_in_loading =page+direction;
            String request = String.format("%s%s%d",NOW_PLAYING_URL, "&page=", page_to_load);
            client.get(request, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int i, Headers headers, JSON json) {
                    JSONObject jsonObject = json.jsonObject;
                    try {
                        JSONArray jsonArray = jsonObject.getJSONArray("results");
                        List<Movie> newMovies =Movie.fromJsonArray(jsonArray);
                        if(direction==NEXT_PAGE){
                            pages_currently_shown[0]++;
                            pages_currently_shown[1]++;
                            for(int j=0;j<newMovies.size();j++){
                                movies.add(newMovies.get(j));
                                complexMovieAdapter.notifyItemInserted(movies.size()-1);
                            }
                            while(movies.size()>MAX_SIZE){
                                movies.remove(0);
                                complexMovieAdapter.notifyItemRemoved(0);
                            }
                        }else{
                            pages_currently_shown[0]--;
                            pages_currently_shown[1]--;
                            for(int j=newMovies.size()-1;j>=0;j--){
                                movies.add(0,newMovies.get(j));
                                complexMovieAdapter.notifyItemInserted(0);
                            }
                            while(movies.size()>MAX_SIZE){
                                movies.remove(movies.size()-1);
                                complexMovieAdapter.notifyItemRemoved(movies.size());
                            }
                        }
                        page = jsonObject.getInt("page");
                        page_in_loading=-1;

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(int i, Headers headers, String s, Throwable throwable) {
                    page_in_loading=-1;

                }
            });
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context =this;

        page=1;
        total_pages=1;
        pages_currently_shown=new int[]{0,1};

        movies = new ArrayList<>();
        complexMovieAdapter = new ComplexMovieAdapter(this, movies);
        complexMovieAdapter.setOnBottomReachedListener(new OnBottomReachedListener() {
            @Override
            public void OnBottomReached(int position) {
                if(total_pages>1) {
                    Log.i(TAG, "OnBottomReachCalled");
                    getMoreMovies(client, NEXT_PAGE);
                }
            }
        });
        complexMovieAdapter.setOnTopReachedListener(new OnTopReachedListener() {
            @Override
            public void OnTopReached(int position) {
                    if(pages_currently_shown[1]>2){
                    Log.i(TAG,"OnTopReachedCalled");
                    getMoreMovies(client,PREVIOUS_PAGE);
                }
            }
        });
        client = new AsyncHttpClient();
        rvMovies = findViewById(R.id.rvMovies);
        rvMovies.setAdapter(complexMovieAdapter);
        rvMovies.setLayoutManager(new LinearLayoutManager(this));
        movies.addAll(Movie.EmptyMovieList());
        complexMovieAdapter.notifyDataSetChanged();
        getMovies(client);
    }
}

