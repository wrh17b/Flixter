package com.codepath.wrh17b.flixter.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.List;
@Parcel
public class Movie {

    String backdropPath;
    String posterPath;
    String title;
    String overview;
    String release_date;
    double vote_average;
    int id;

    //Empty constructor for parcel
    public Movie(){

    }

    public Movie(JSONObject jsonObject) throws JSONException {
        backdropPath = jsonObject.getString("backdrop_path");
        posterPath = jsonObject.getString("poster_path");
        title = jsonObject.getString("title");
        overview = jsonObject.getString("overview");
        release_date =jsonObject.getString("release_date");
        vote_average = jsonObject.getDouble("vote_average");
        id =jsonObject.getInt("id");

    }

    private Movie(String loading){
        backdropPath=null;
        posterPath=null;
        title="Loading Title";
        overview="Loading Overview";
        vote_average=0;
    }

    public static List<Movie> EmptyMovieList(){
        List<Movie> movies= new ArrayList<>();
        for(int i=0;i<3;i++){
            movies.add(new Movie("loading"));
        }
        return movies;

    }

    public static List<Movie> fromJsonArray(JSONArray movieJsonArray) throws JSONException {
        List<Movie> movies = new ArrayList<>();
        for(int i=0;i<movieJsonArray.length();i++){
            movies.add(new Movie(movieJsonArray.getJSONObject(i)));
        }
        return movies;


    }

    public String getBackdropPath() {
        if(backdropPath!=null) {
            return String.format("https://image.tmdb.org/t/p/w342%s", backdropPath);
        }else{
            return null;
        }
    }

    public String getPosterPath() {
        if(posterPath!=null) {
            return String.format("https://image.tmdb.org/t/p/w342%s", posterPath);
        }else{
            return null;
        }
    }

    public String getTitle() {
        return title;
    }

    public String getOverview() {
        return overview;
    }

    public double getVote_average() {
        return (vote_average/2.0);
    }

    public String getRelease_date() {
        return release_date;
    }

    public int getId() {
        return id;
    }
}
