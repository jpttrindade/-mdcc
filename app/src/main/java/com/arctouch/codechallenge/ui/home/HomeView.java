package com.arctouch.codechallenge.ui.home;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModelProviders;
import android.arch.paging.PagedList;
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
import com.arctouch.codechallenge.model.Movie;
import com.arctouch.codechallenge.ui.details.DetailsActivity;
import com.arctouch.codechallenge.util.NetworkState;

public class HomeView extends Fragment implements MovieClickListener {

    private HomeViewModel mViewModel;
    private RecyclerView recyclerView;
    private View progressBar;
    private MovieAdapter adapter;
    private LiveData<NetworkState> networkState;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_view_fragment, container, false);

        this.recyclerView = view.findViewById(R.id.recyclerView);
        this.progressBar = view.findViewById(R.id.progressBar);
        adapter = new MovieAdapter(this);
        recyclerView.setAdapter(adapter);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(HomeViewModel.class);
        mViewModel.getMovieListLiveData().observe(this, response -> updateAdapterList(response));
        networkState = mViewModel.getNetworkState();
        networkState.observe(this, networkState -> onNetworkStateChange(networkState));
    }

    private void onNetworkStateChange(NetworkState networkState) {
        adapter.setNetworkState(networkState);
    }

    private void updateAdapterList(PagedList<Movie> response) {
        adapter.submitList(response);
        progressBar.setVisibility(View.GONE);
    }


    @Override
    public void onClick(Movie movie) {
        Intent it = new Intent(getContext(), DetailsActivity.class);
        it.putExtra("mid", movie.id);
        startActivity(it);
    }
}
