package com.arctouch.codechallenge.ui.home;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.arctouch.codechallenge.model.Movie;

public interface MovieClickListener {
    void onClick(Movie movie, ImageView posterImageView, TextView tvTitle);
}
