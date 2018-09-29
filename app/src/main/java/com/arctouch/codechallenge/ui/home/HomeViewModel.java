package com.arctouch.codechallenge.ui.home;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.arctouch.codechallenge.data.RepositoryMovies;
import com.arctouch.codechallenge.model.Movie;
import com.arctouch.codechallenge.model.UpcomingMoviesResponse;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class HomeViewModel extends ViewModel {
    private RepositoryMovies repositoryMovies;

    private final List<Movie> upcomingMovies;
    private MutableLiveData<List<Movie>> movies;

    public HomeViewModel() {
        repositoryMovies = RepositoryMovies.getInstance();
        movies = new MutableLiveData<>();
        upcomingMovies = new ArrayList<>();
    }


    public LiveData<List<Movie>> getUpcomingMovies() {
        return movies;
    }

    public void loadData(boolean forceUpdate) {
        if (forceUpdate || upcomingMovies.size() == 0) {
            repositoryMovies.upcomingMovies(new Observer<UpcomingMoviesResponse>() {
                @Override
                public void onSubscribe(Disposable d) {

                }

                @Override
                public void onNext(UpcomingMoviesResponse response) {
                    upcomingMovies.addAll(response.results);
                    movies.setValue(upcomingMovies);
                }

                @Override
                public void onError(Throwable e) {

                }

                @Override
                public void onComplete() {

                }
            });
        } else {
            movies.setValue(upcomingMovies);
        }

    }
}
