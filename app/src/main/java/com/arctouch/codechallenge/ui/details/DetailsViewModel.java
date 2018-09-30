package com.arctouch.codechallenge.ui.details;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.arctouch.codechallenge.data.RepositoryMovies;
import com.arctouch.codechallenge.model.Movie;

public class DetailsViewModel extends ViewModel {

    private final RepositoryMovies repoMovies;
    private MutableLiveData<Movie> detailLiveData;

    public DetailsViewModel() {
        this.repoMovies = RepositoryMovies.getInstance();
    }

    public LiveData<Movie> getMovieDetailLiveData() {
        if (detailLiveData == null) {
            detailLiveData = repoMovies.getMovieDetailLiveData();
        }
        return detailLiveData;
    }

    public void loadMovieDetail(int movieId) {
        if (detailLiveData.getValue() == null || movieId != detailLiveData.getValue().id) {
            repoMovies.loadMovieDetail(movieId);
        }
    }
}
