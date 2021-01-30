package com.codepath.wrh17b.flixter.viewholders;

import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.codepath.wrh17b.flixter.R;

public class ViewHolderPopular extends RecyclerView.ViewHolder {

    private ImageView ivBackdrop;
    private RatingBar rbPopular;
    private ProgressBar pbLoadingImage;
    private RelativeLayout container;


    public ViewHolderPopular(@NonNull View itemView) {
        super(itemView);
        ivBackdrop = itemView.findViewById(R.id.ivPoster);
        rbPopular = itemView.findViewById(R.id.rbPopular);
        pbLoadingImage = itemView.findViewById(R.id.pbLoadingImage);
        container = itemView.findViewById(R.id.container);


    }

    public RelativeLayout getContainer() {
        return container;
    }

    public ImageView getIvBackdrop() {
        return ivBackdrop;
    }

    public RatingBar getRbPopular() {
        return rbPopular;
    }

    public ProgressBar getPbLoadingImage() {
        return pbLoadingImage;
    }

    public void setPbLoadingImage(ProgressBar pbLoadingImage) {
        this.pbLoadingImage = pbLoadingImage;
    }

    public void setIvBackdrop(ImageView ivBackdrop) {
        this.ivBackdrop = ivBackdrop;
    }

    public void setRbPopular(RatingBar rbPopular) {
        this.rbPopular = rbPopular;
    }

}
