package com.arctouch.codechallenge.ui.details;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.util.Log;

import com.arctouch.codechallenge.data.RepositoryMovies;
import com.arctouch.codechallenge.model.Movie;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class DetailsViewModel extends ViewModel {

    private final RepositoryMovies repoMovies;
    private MutableLiveData<Movie> detail;

    public DetailsViewModel() {
        this.repoMovies = RepositoryMovies.getInstance();
    }

    public LiveData<Movie> getMovieDetail(int movieId) {
        if (detail == null) {
            detail = new MutableLiveData<>();
        }

        repoMovies.getMovieDetails(movieId, new Observer<Movie>(){

            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(Movie movie) {
                detail.setValue(movie);
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });

        return detail;
    }
}
