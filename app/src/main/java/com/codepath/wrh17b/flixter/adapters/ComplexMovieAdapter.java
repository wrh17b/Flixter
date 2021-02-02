package com.codepath.wrh17b.flixter.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityOptionsCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.codepath.wrh17b.flixter.DetailActivity;
import com.codepath.wrh17b.flixter.MainActivity;
import com.codepath.wrh17b.flixter.R;
import com.codepath.wrh17b.flixter.interfaces.OnBottomReachedListener;
import com.codepath.wrh17b.flixter.interfaces.OnTopReachedListener;
import com.codepath.wrh17b.flixter.models.Movie;
import com.codepath.wrh17b.flixter.viewholders.ViewHolderLame;
import com.codepath.wrh17b.flixter.viewholders.ViewHolderPopular;

import org.parceler.Parcels;

import java.util.List;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

public class ComplexMovieAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final int POPULAR =0, LAME=1;
    private final String TAG ="ComplexMovieAdapter";
    Context context;
    List<Movie> movies;

    OnBottomReachedListener onBottomReachedListener;
    OnTopReachedListener onTopReachedListener;

    public void setOnBottomReachedListener(OnBottomReachedListener onBottomReachedListener) {
        this.onBottomReachedListener = onBottomReachedListener;
    }

    public void setOnTopReachedListener(OnTopReachedListener onTopReachedListener) {
        this.onTopReachedListener = onTopReachedListener;
    }

    public ComplexMovieAdapter(Context context, List<Movie> movies) {
        this.context=context;
        this.movies=movies;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        if(viewType ==POPULAR){
            View v =inflater.inflate(R.layout.popular_movie,parent,false);
            viewHolder = new ViewHolderPopular(v);
        }else{
            View v =inflater.inflate(R.layout.item_movie,parent,false);
            viewHolder = new ViewHolderLame(v);
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Movie movie=movies.get(position);

        if(position==movies.size()-1){
            onBottomReachedListener.OnBottomReached(position);
        }else if(position==0){
            onTopReachedListener.OnTopReached(position);
        }
        if(holder.getItemViewType()==POPULAR){
            ViewHolderPopular pop = (ViewHolderPopular) holder;
            bindpop(context, pop, movie);
        }else{
            ViewHolderLame lame = (ViewHolderLame) holder;
            bindlame(context, lame,movie);
        }
        holder.itemView.findViewById(R.id.container).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DetailActivity.class);
                intent.putExtra("movie", Parcels.wrap(movie));
                context.startActivity(intent);
            }
        });

    }

    private void bindlame(Context context, ViewHolderLame lame, Movie movie) {
        lame.getTvTitle().setText(movie.getTitle());
        lame.getTvOverview().setText(movie.getOverview());
        String imgUrl=null;
        int width, height;

        if(context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
            imgUrl=movie.getBackdropPath();
            width=450;
            height=250;
        }else{
            imgUrl = movie.getPosterPath();
            width=180;
            height=270;
        }

        lame.getPbLoadingImage().setVisibility(View.VISIBLE);
        lame.getPbLoadingImage().bringToFront();
        lame.getPbLoadingImage().animate();



        if(imgUrl!=null) {
            Glide.with(context).load(imgUrl).transform(new RoundedCornersTransformation(30,10)).listener(new RequestListener<Drawable>() {
                @Override
                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                    lame.getPbLoadingImage().setVisibility(View.INVISIBLE);

                    return false;
                }

                @Override
                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {

                    lame.getPbLoadingImage().setVisibility(View.INVISIBLE);
                    return false;
                }
            }).placeholder(R.drawable.placeholder)
                    .error(R.drawable.no_image_available)
                    .override(width,height).into(lame.getIvPoster());

        }



    }

    private void bindpop(Context context, ViewHolderPopular pop, Movie movie) {
        pop.getPbLoadingImage().setVisibility(View.VISIBLE);
        pop.getPbLoadingImage().bringToFront();
        pop.getPbLoadingImage().animate();
        int width=450, height=250;


        pop.getRbPopular().setRating((float)movie.getVote_average());
        if(movie.getBackdropPath()!=null) {
            Glide.with(context).load(movie.getBackdropPath()).transform(new RoundedCornersTransformation(30,10)).listener(new RequestListener<Drawable>() {
                @Override
                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                    pop.getPbLoadingImage().setVisibility(View.INVISIBLE);
                    return false;
                }

                @Override
                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                    pop.getPbLoadingImage().setVisibility(View.INVISIBLE);
                    return false;
                }
            })
                    .placeholder(R.drawable.placeholder)
                    .error(R.drawable.no_image_available)
                    .override(width,height)
                    .into(pop.getIvBackdrop());
        }
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    @Override
    public int getItemViewType(int position) {
        //Popular movies only get special display in portrait mode
        if(movies.get(position).getVote_average()>2.5
                &&context.getResources().getConfiguration().orientation==Configuration.ORIENTATION_PORTRAIT){
            return POPULAR;
        }else{
            return LAME;
        }
    }
}
