package com.arctouch.codechallenge.ui.home;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.arctouch.codechallenge.data.RepositoryMovies;
import com.arctouch.codechallenge.model.UpcomingMoviesResponse;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class HomeViewModel extends ViewModel {
    RepositoryMovies repositoryMovies;

    MutableLiveData<UpcomingMoviesResponse> movies;

    public  HomeViewModel() {
        repositoryMovies = RepositoryMovies.getInstance();
    }


    public LiveData<UpcomingMoviesResponse> getUpcomingMovies() {

        if (movies == null) {
            movies = new MutableLiveData<>();
        }
        repositoryMovies.upcomingMovies(new Observer<UpcomingMoviesResponse>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(UpcomingMoviesResponse response) {
                movies.setValue(response);
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });


        return movies;
    }
}
