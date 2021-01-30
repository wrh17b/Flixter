package com.codepath.wrh17b.flixter.viewholders;

import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.codepath.wrh17b.flixter.R;

public class ViewHolderLame extends RecyclerView.ViewHolder {

    private TextView tvTitle;
    private TextView tvOverview;
    private ImageView ivPoster;
    private ProgressBar pbLoadingImage;
    private RelativeLayout container;
    private int width,height;

    public ViewHolderLame(@NonNull View itemView) {
        super(itemView);

        tvTitle = itemView.findViewById(R.id.tvTitle);
        tvOverview = itemView.findViewById(R.id.tvOverview);
        ivPoster = itemView.findViewById(R.id.ivPoster);
        pbLoadingImage = itemView.findViewById(R.id.pbLoadingImage);
        container = itemView.findViewById(R.id.container);


    }

    public RelativeLayout getContainer() {
        return container;
    }

    public TextView getTvTitle() {
        return tvTitle;
    }

    public TextView getTvOverview() {
        return tvOverview;
    }

    public ImageView getIvPoster() {
        return ivPoster;
    }

    public ProgressBar getPbLoadingImage() {
        return pbLoadingImage;
    }

    public void setTvTitle(TextView tvTitle) {
        this.tvTitle = tvTitle;
    }

    public void setTvOverview(TextView tvOverview) {
        this.tvOverview = tvOverview;
    }

    public void setIvPoster(ImageView ivPoster) {
        this.ivPoster = ivPoster;
    }

    public void setPbLoadingImage(ProgressBar pbLoadingImage) {
        this.pbLoadingImage = pbLoadingImage;
    }


}
