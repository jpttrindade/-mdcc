package com.arctouch.codechallenge.ui.details;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.arctouch.codechallenge.data.RepositoryMovies;
import com.arctouch.codechallenge.model.Movie;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class DetailsViewModel extends ViewModel {

    private final RepositoryMovies repoMovies;
    private MutableLiveData<Movie> detail;
    private Movie mMovie;

    public DetailsViewModel() {
        this.repoMovies = RepositoryMovies.getInstance();
        detail = new MutableLiveData<>();
    }

    public LiveData<Movie> getMovieDetail() {
        return detail;
    }

    public void loadMovieDetail(int movieId) {
        if (mMovie == null || movieId != mMovie.id) {
            repoMovies.getMovieDetails(movieId, new Observer<Movie>() {

                @Override
                public void onSubscribe(Disposable d) {

                }

                @Override
                public void onNext(Movie movie) {
                    mMovie = movie;
                    detail.setValue(mMovie);
                }

                @Override
                public void onError(Throwable e) {

                }

                @Override
                public void onComplete() {

                }
            });
        } else {
            detail.setValue(mMovie);
        }

    }
}
