package com.arctouch.codechallenge.data;


import com.arctouch.codechallenge.api.TmdbApi;
import com.arctouch.codechallenge.api.TmdbApiFactory;
import com.arctouch.codechallenge.model.Movie;
import com.arctouch.codechallenge.model.UpcomingMoviesResponse;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class RepositoryMovies {
    private TmdbApi api;
    private static RepositoryMovies instance;

    public static RepositoryMovies getInstance() {
        if (instance == null) {
            instance = new RepositoryMovies();
        }

        return instance;
    }

    private RepositoryMovies() {
        this.api = TmdbApiFactory.create();
    }

    public void getMovieDetails(long id, Observer<Movie> observer) {
        api.movie(id, TmdbApi.API_KEY)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

    public void nextUpcomingMovies(long page, Observer<UpcomingMoviesResponse> observer) {
        api.upcomingMovies(TmdbApi.API_KEY, page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }
}
