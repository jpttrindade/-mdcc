package com.arctouch.codechallenge.data;


import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.arch.paging.LivePagedListBuilder;
import android.arch.paging.PagedList;
import android.util.Log;

import com.arctouch.codechallenge.api.TmdbApi;
import com.arctouch.codechallenge.api.TmdbApiFactory;
import com.arctouch.codechallenge.model.Movie;
import com.arctouch.codechallenge.util.NetworkState;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class RepositoryMovies {

    private final LiveData<NetworkState> networkSate;
    private TmdbApi mApi;
    private static RepositoryMovies instance;
    private MovieDataSourceFactory movieDataSourceFactory;

    private MutableLiveData<Movie> movieDetailLiveData;

    public static RepositoryMovies getInstance() {
        if (instance == null) {
            instance = new RepositoryMovies();
        }

        return instance;
    }

    private RepositoryMovies() {
        mApi = TmdbApiFactory.create();
        movieDataSourceFactory = new MovieDataSourceFactory();
        networkSate = Transformations.switchMap(movieDataSourceFactory.getMovieDataSourceLiveData(), dataSource -> dataSource.getNetworkState());
    }

    public void loadMovieDetail(long id) {
        Log.d("DEBUG", "loadMovieDetail");
        mApi.movie(id, TmdbApi.API_KEY)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(movie -> movieDetailLiveData.postValue(movie));
    }


    public LiveData<PagedList<Movie>> getMovieListLiveData() {
        ExecutorService executor = Executors.newFixedThreadPool(5);
        PagedList.Config pagedListConfig = new PagedList.Config.Builder()
                .setEnablePlaceholders(false)
                .setInitialLoadSizeHint(10)
                .setPageSize(20)
                .build();

        return new LivePagedListBuilder<Long, Movie>(movieDataSourceFactory, pagedListConfig)
                .setFetchExecutor(executor)
                .build();
    }

    public LiveData<NetworkState> getNetworkState() {
        return networkSate;
    }


    public MutableLiveData<Movie> getMovieDetailLiveData() {
        if (movieDetailLiveData == null) {
            movieDetailLiveData = new MutableLiveData<>();
        }
        return movieDetailLiveData;
    }
}
