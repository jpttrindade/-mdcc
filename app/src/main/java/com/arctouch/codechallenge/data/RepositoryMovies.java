package com.arctouch.codechallenge.data;


import android.util.Log;

import com.arctouch.codechallenge.api.TmdbApi;
import com.arctouch.codechallenge.model.Movie;
import com.arctouch.codechallenge.model.UpcomingMoviesResponse;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.moshi.MoshiConverterFactory;

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
        this.api = new Retrofit.Builder()
                .baseUrl(TmdbApi.URL)
                .client(new OkHttpClient.Builder().build())
                .addConverterFactory(MoshiConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
                .create(TmdbApi.class);
    }

    public void upcomingMovies(Observer<UpcomingMoviesResponse> observer) {
        Log.d("DEBUG", "UpcomingMovies()");
        api.upcomingMovies(TmdbApi.API_KEY, 1L)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

    public void getMovieDetails(long id, Observer<Movie> observer) {
        api.movie(id, TmdbApi.API_KEY)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }
}
