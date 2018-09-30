package com.arctouch.codechallenge.ui.details;

import android.arch.lifecycle.ViewModelProviders;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.arctouch.codechallenge.R;
import com.arctouch.codechallenge.model.Genre;
import com.arctouch.codechallenge.model.Movie;
import com.arctouch.codechallenge.ui.home.HomeView;
import com.arctouch.codechallenge.util.MovieImageUrlBuilder;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import br.com.jpttrindade.tagview.DefaultTag;
import br.com.jpttrindade.tagview.SimpleTag;
import br.com.jpttrindade.tagview.TagView;

public class DetailsView extends Fragment {

    private DetailsViewModel mViewModel;
    private int mMovieID;
    private TextView tvTitle;
    private View progressbar;
    private TextView tvOverview;
    private ImageView ivPoster;
    private ImageView ivBackdrop;
    private TextView tvReleaseDate;
    private TagView tagview;
    private Toolbar toolbar;
    private CollapsingToolbarLayout collapsingToolbarLayout;

    public static DetailsView newInstance() {
        return new DetailsView();
    }

    private final MovieImageUrlBuilder movieImageUrlBuilder = new MovieImageUrlBuilder();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mMovieID = getArguments().getInt(HomeView.MID);

        View view = inflater.inflate(R.layout.details_view_fragment, container, false);

        tvTitle = view.findViewById(R.id.tvTilte);
        tvOverview = view.findViewById(R.id.tvOverview);
        tvReleaseDate = view.findViewById(R.id.tvRelaseDate);
        ivPoster = view.findViewById(R.id.ivPoster);
        ivBackdrop = view.findViewById(R.id.ivBackdrop);

        progressbar = view.findViewById(R.id.progressbar);

        tagview = view.findViewById(R.id.tagview);

        toolbar = view.findViewById(R.id.toolbar);
        collapsingToolbarLayout = view.findViewById(R.id.collapsing_toolbar_layout);


        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);

        toolbar.setNavigationOnClickListener(view -> getActivity().onBackPressed());

        ViewCompat.setTransitionName(ivPoster, HomeView.SHARE_POSTER_IMAGE);
       // ViewCompat.setTransitionName(tvTitle, HomeView.SHARE_TITLE);


        mViewModel = ViewModelProviders.of(this).get(DetailsViewModel.class);
        mViewModel.getMovieDetailLiveData().observe(this, movie -> setData(movie));

    }


    @Override
    public void onResume() {
        super.onResume();
        mViewModel.loadMovieDetail(mMovieID);
    }

    public void setData(Movie movie) {
        collapsingToolbarLayout.setTitle(movie.title);
        tvOverview.setText(movie.overview);
        tvReleaseDate.setText(movie.releaseDate);
        Glide.with(this)
                .load(movieImageUrlBuilder.buildPosterUrl(movie.posterPath))
                .apply(new RequestOptions().placeholder(R.drawable.ic_image_placeholder))
                .into(ivPoster);
        Glide.with(this)
                .load(movieImageUrlBuilder.buildBackdropUrl(movie.backdropPath))
                .apply(new RequestOptions().placeholder(R.drawable.ic_image_placeholder))
                .into(ivBackdrop);

        tagview.removeAll();
        DefaultTag defaultTag;
        for (Genre genre : movie.genres) {
            defaultTag = new SimpleTag(genre.name, Color.parseColor("#9E9E9E"));
            tagview.addTag(defaultTag);
        }

        progressbar.setVisibility(View.GONE);


    }
}
