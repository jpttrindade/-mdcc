package com.arctouch.codechallenge.ui.home;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.arch.paging.PagedList;

import com.arctouch.codechallenge.data.RepositoryMovies;
import com.arctouch.codechallenge.model.Movie;
import com.arctouch.codechallenge.util.NetworkState;

public class HomeViewModel extends ViewModel {
    private RepositoryMovies repositoryMovies;
    private LiveData movieListLiveData;

    public HomeViewModel() {
        repositoryMovies = RepositoryMovies.getInstance();
    }

    public LiveData<PagedList<Movie>> getMovieListLiveData() {
        if (movieListLiveData == null ) {
            movieListLiveData = repositoryMovies.getMovieListLiveData();
        }
        return movieListLiveData;
    }

    public LiveData<NetworkState> getNetworkState() {
        return repositoryMovies.getNetworkState();
    }

    public LiveData<Boolean> init() {
        return repositoryMovies.loadGenres();
    }
}
