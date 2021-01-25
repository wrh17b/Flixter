package com.codepath.wrh17b.flixter.adapters;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.codepath.wrh17b.flixter.R;
import com.codepath.wrh17b.flixter.models.Movie;
import com.codepath.wrh17b.flixter.viewholders.ViewHolderLame;
import com.codepath.wrh17b.flixter.viewholders.ViewHolderPopular;

import java.util.List;

public class ComplexMovieAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final int POPULAR =0, LAME=1;
    private final String TAG ="ComplexMovieAdapter";
    Context context;
    List<Movie> movies;

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
        if(holder.getItemViewType()==POPULAR){
            ViewHolderPopular pop = (ViewHolderPopular) holder;
            bindpop(context, pop, position);
            /*Movie movie = movies.get(position);
            pop.getPbLoadingImage().setVisibility(View.VISIBLE);
            pop.getPbLoadingImage().bringToFront();
            pop.getPbLoadingImage().animate();
            int width=450, height=250;


            pop.getRbPopular().setRating((float)movie.getVote_average()/(float)2.0);
            if(movie.getBackdropPath()!=null) {
                Glide.with(context).load(movie.getBackdropPath())
                        .placeholder(R.drawable.placeholder).override(width,height)
                        .into(pop.getIvBackdrop());
                pop.getPbLoadingImage().setVisibility(View.INVISIBLE);
            }*/
        }else{
            ViewHolderLame lame = (ViewHolderLame) holder;
            bindlame(context, lame,position);
            /*Movie movie = movies.get(position);
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
                Glide.with(context).load(imgUrl).placeholder(R.drawable.placeholder)
                        .override(width,height).into(lame.getIvPoster());
                lame.getPbLoadingImage().setVisibility(View.INVISIBLE);
            }*/


        }

    }

    private void bindlame(Context context, ViewHolderLame lame, int position) {

        Movie movie = movies.get(position);
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
            Glide.with(context).load(imgUrl).listener(new RequestListener<Drawable>() {
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
                    .override(width,height).into(lame.getIvPoster());
            //lame.getPbLoadingImage().setVisibility(View.INVISIBLE);
        }
        /*
        Glide.with(context).load(imgUrl).listener(new RequestListener<Drawable>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                lame.getPbLoadingImage().setVisibility(View.GONE);
                lame.getIvPoster().bringToFront();
                Log.e(TAG, String.format("Glide failed load: ",e));

                return false;
            }

            @Override
            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                lame.getPbLoadingImage().setVisibility(View.GONE);
                lame.getIvPoster().setVisibility(View.VISIBLE);
                lame.getIvPoster().bringToFront();


                return false;
            }
        }).into(lame.getIvPoster());*/
        //lame.getPbLoadingImage().setVisibility(View.INVISIBLE);


    }

    private void bindpop(Context context, ViewHolderPopular pop, int position) {
        Movie movie = movies.get(position);
        pop.getPbLoadingImage().setVisibility(View.VISIBLE);
        pop.getPbLoadingImage().bringToFront();
        pop.getPbLoadingImage().animate();
        int width=450, height=250;


        pop.getRbPopular().setRating((float)movie.getVote_average()/(float)2.0);
        if(movie.getBackdropPath()!=null) {
            Glide.with(context).load(movie.getBackdropPath()).listener(new RequestListener<Drawable>() {
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
                    .placeholder(R.drawable.placeholder).override(width,height)
                    .into(pop.getIvBackdrop());
            //pop.getPbLoadingImage().setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    @Override
    public int getItemViewType(int position) {
        //Popular movies only get special display in portrait mode
        if(movies.get(position).getVote_average()>5
                &&context.getResources().getConfiguration().orientation==Configuration.ORIENTATION_PORTRAIT){
            return POPULAR;
        }else{
            return LAME;
        }
    }
}
