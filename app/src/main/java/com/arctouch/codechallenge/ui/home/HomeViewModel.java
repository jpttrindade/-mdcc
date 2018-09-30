package com.arctouch.codechallenge.ui.home;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.arch.lifecycle.ViewModel;
import android.arch.paging.LivePagedListBuilder;
import android.arch.paging.PagedList;

import com.arctouch.codechallenge.data.MovieDataSourceFactory;
import com.arctouch.codechallenge.data.RepositoryMovies;
import com.arctouch.codechallenge.model.Movie;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class HomeViewModel extends ViewModel {
    private RepositoryMovies repositoryMovies;

    private LiveData<PagedList<Movie>> movies;


    private long page;
    private Executor executor;
    private LiveData networkState;
    private LiveData movieLiveData;


    public HomeViewModel() {
        page = 1;
        repositoryMovies = RepositoryMovies.getInstance();
        movies = new MutableLiveData<>();
    }

    public void init() {
        executor = Executors.newFixedThreadPool(5);
        MovieDataSourceFactory movieDataSourceFactory = new MovieDataSourceFactory();
        networkState = Transformations.switchMap(movieDataSourceFactory.getMutableLiveData(), datasource -> datasource.getNetworkState());

        PagedList.Config pagedListConfig = new PagedList.Config.Builder()
                .setEnablePlaceholders(false)
                .setInitialLoadSizeHint(10)
                .setPageSize(20).build();

        movieLiveData = new LivePagedListBuilder(movieDataSourceFactory, pagedListConfig)
                .setFetchExecutor(executor)
                .build();


    }

    public LiveData<PagedList<Movie>> getMovieLiveData() {
        return movieLiveData;
    }

}
