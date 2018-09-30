package com.arctouch.codechallenge.data;

import android.arch.lifecycle.MutableLiveData;
import android.arch.paging.DataSource;

public class MovieDataSourceFactory extends DataSource.Factory {
    private MutableLiveData<MovieDataSource> movieDataSourceLiveData;

    public MovieDataSourceFactory() {
        movieDataSourceLiveData = new MutableLiveData<>();
    }

    @Override
    public DataSource create() {
        MovieDataSource movieDataSource = new MovieDataSource();
        movieDataSourceLiveData.postValue(movieDataSource);
        return movieDataSource;
    }


    public MutableLiveData<MovieDataSource> getMovieDataSourceLiveData() {
        return movieDataSourceLiveData;
    }
}
