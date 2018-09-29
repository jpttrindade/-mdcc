package com.arctouch.codechallenge.ui.home;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.arctouch.codechallenge.R;
import com.arctouch.codechallenge.api.TmdbApi;
import com.arctouch.codechallenge.data.Cache;
import com.arctouch.codechallenge.model.Genre;
import com.arctouch.codechallenge.model.Movie;
import com.arctouch.codechallenge.model.UpcomingMoviesResponse;
import com.arctouch.codechallenge.ui.details.DetailsActivity;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class HomeView extends Fragment implements MovieClickListener {

    private HomeViewModel mViewModel;
    private RecyclerView recyclerView;
    private View progressBar;
    private HomeAdapter adapter;

    public static HomeView newInstance() {
        return new HomeView();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_view_fragment, container, false);

        this.recyclerView = view.findViewById(R.id.recyclerView);
        this.progressBar = view.findViewById(R.id.progressBar);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(HomeViewModel.class);
        if (adapter == null) {
            adapter = new HomeAdapter(this);
            recyclerView.setAdapter(adapter);
        }

        mViewModel.getUpcomingMovies().observe(this, response -> {
            adapter.setList(response.results);
            progressBar.setVisibility(View.GONE);
        });
    }

    @Override
    public void onClick(Movie movie) {
        Intent it = new Intent(getContext(), DetailsActivity.class);
        it.putExtra("mid", movie.id);
        startActivity(it);
    }
}
