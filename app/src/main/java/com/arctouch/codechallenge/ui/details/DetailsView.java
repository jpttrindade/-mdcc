package com.arctouch.codechallenge.ui.details;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.arctouch.codechallenge.R;
import com.arctouch.codechallenge.model.Movie;
import com.arctouch.codechallenge.util.MovieImageUrlBuilder;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

public class DetailsView extends Fragment {

    private DetailsViewModel mViewModel;
    private int mMovieID;
    private TextView tvTitle;
    private View progressbar;
    private TextView tvOverview;
    private ImageView ivPoster;
    private ImageView ivBackdrop;
    private RecyclerView rvGenres;
    private GenreAdapter genreAdapter;
    private TextView tvReleaseDate;

    public static DetailsView newInstance() {
        return new DetailsView();
    }

    private final MovieImageUrlBuilder movieImageUrlBuilder = new MovieImageUrlBuilder();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mMovieID = getArguments().getInt("mid");

        View view = inflater.inflate(R.layout.details_view_fragment, container, false);

        tvTitle = view.findViewById(R.id.tvTilte);
        tvOverview = view.findViewById(R.id.tvOverview);
        tvReleaseDate = view.findViewById(R.id.tvRelaseDate);
        ivPoster = view.findViewById(R.id.ivPoster);
        ivBackdrop = view.findViewById(R.id.ivBackdrop);
        progressbar = view.findViewById(R.id.progressbar);
        rvGenres = view.findViewById(R.id.rvGenres);
        genreAdapter = new GenreAdapter();
        rvGenres.setAdapter(genreAdapter);
        rvGenres.setHasFixedSize(true);
        rvGenres.setNestedScrollingEnabled(false);


        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(DetailsViewModel.class);
        mViewModel.getMovieDetailLiveData().observe(this, movie -> loadData(movie));
    }


    @Override
    public void onResume() {
        super.onResume();
        mViewModel.loadMovieDetail(mMovieID);
    }

    public void loadData(Movie movie) {
        tvTitle.setText(movie.title);
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

        genreAdapter.setGenres(movie.genres);

        progressbar.setVisibility(View.GONE);


    }

}
