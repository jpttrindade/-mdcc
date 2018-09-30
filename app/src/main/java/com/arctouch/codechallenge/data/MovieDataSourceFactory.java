package com.arctouch.codechallenge.data;

import android.arch.lifecycle.MutableLiveData;
import android.arch.paging.DataSource;

public class MovieDataSourceFactory extends DataSource.Factory {

    private final MutableLiveData<MovieDataSource> mutableLiveData;
    private MovieDataSource movieDataSource;

    public MovieDataSourceFactory () {
        this.mutableLiveData = new MutableLiveData<>();
    }

    @Override
    public DataSource create() {
        movieDataSource = new MovieDataSource();
        mutableLiveData.postValue(movieDataSource);
        return movieDataSource;
    }

    public MutableLiveData<MovieDataSource> getMutableLiveData() {
        return  mutableLiveData;
    }
}
