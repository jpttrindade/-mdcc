package com.arctouch.codechallenge.ui.home;

import android.arch.lifecycle.ViewModelProviders;
import android.arch.paging.PagedList;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.util.Pair;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.arctouch.codechallenge.R;
import com.arctouch.codechallenge.model.Movie;
import com.arctouch.codechallenge.ui.details.DetailsActivity;
import com.arctouch.codechallenge.util.NetworkState;
import com.facebook.shimmer.Shimmer;
import com.facebook.shimmer.ShimmerFrameLayout;

public class HomeView extends Fragment implements MovieClickListener {
    public static final String MID = "MID";
    public static final String SHARE_POSTER_IMAGE = "POSTER_IMAGE";
    public static final String SHARE_TITLE = "POSTER_TITLE";

    private HomeViewModel mViewModel;
    private RecyclerView recyclerView;
    private ShimmerFrameLayout shimmer_view_container;
    private MovieAdapter adapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_view_fragment, container, false);

        this.recyclerView = view.findViewById(R.id.recyclerView);
        this.shimmer_view_container = view.findViewById(R.id.shimmer_view_container);
        this.shimmer_view_container.startShimmer();
        adapter = new MovieAdapter(this);
        recyclerView.setAdapter(adapter);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mViewModel = ViewModelProviders.of(this).get(HomeViewModel.class);
        mViewModel.init().observe(this, (loadGenres) -> {
            mViewModel.getMovieListLiveData().observe(this, response -> updateAdapterList(response));
            mViewModel.getNetworkState().observe(this, networkState -> onNetworkStateChange(networkState));
        });


    }

    private void onNetworkStateChange(NetworkState networkState) {
        adapter.setNetworkState(networkState);
    }

    private void updateAdapterList(PagedList<Movie> response) {
        adapter.submitList(response);
        //shimmer_view_container.setVisibility(View.GONE);
        shimmer_view_container.stopShimmer();
        shimmer_view_container.setVisibility(View.GONE);
    }


    @Override
    public void onClick(Movie movie, ImageView poster, TextView title) {

        Pair<View, String> pair = new Pair<>(poster, SHARE_POSTER_IMAGE);
        Pair<View, String> pair2= new Pair<>(title, SHARE_TITLE);


        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity(), pair, pair2);
        Intent it = new Intent(getContext(), DetailsActivity.class);
        it.putExtra(MID, movie.id);
        ActivityCompat.startActivity(getContext(), it, options.toBundle());


    }
}
