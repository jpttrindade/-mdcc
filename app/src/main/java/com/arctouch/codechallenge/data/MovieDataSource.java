package com.arctouch.codechallenge.data;

import android.arch.lifecycle.MutableLiveData;
import android.arch.paging.PageKeyedDataSource;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.arctouch.codechallenge.api.TmdbApi;
import com.arctouch.codechallenge.api.TmdbApiFactory;
import com.arctouch.codechallenge.model.Movie;
import com.arctouch.codechallenge.model.UpcomingMoviesResponse;
import com.arctouch.codechallenge.util.NetworkState;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

public class MovieDataSource extends PageKeyedDataSource<Long, Movie> {
    private final MutableLiveData networkState;
    private final MutableLiveData initialLoading;
    private TmdbApi api;

    public MovieDataSource() {
        this.api = TmdbApiFactory.create();
        networkState = new MutableLiveData();
        initialLoading = new MutableLiveData();
    }

    @Override
    public void loadInitial(@NonNull LoadInitialParams<Long> params, @NonNull LoadInitialCallback<Long, Movie> callback) {
        initialLoading.postValue(NetworkState.LOADING);
        networkState.postValue(NetworkState.LOADING);

        this.api.upcomingMovies(TmdbApi.API_KEY, 1L)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> onSuccess(response, params, callback),  failure -> onFail(failure));

    }

    @Override
    public void loadBefore(@NonNull LoadParams<Long> params, @NonNull LoadCallback<Long, Movie> callback) {

    }

    @Override
    public void loadAfter(@NonNull LoadParams<Long> params, @NonNull LoadCallback<Long, Movie> callback) {

        networkState.postValue(NetworkState.LOADING);
        this.api.upcomingMovies(TmdbApi.API_KEY, params.key)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> onSuccess(response, params, callback),  failure -> onFail(failure));

    }

    private void onFail(Throwable failure) {
        networkState.postValue(NetworkState.FAILED);
    }

    private void onSuccess(Response<UpcomingMoviesResponse> response, LoadInitialParams<Long> params, LoadInitialCallback<Long, Movie> callback) {
        if (response.isSuccessful()) {
            callback.onResult(response.body().results, null, 2L);
            initialLoading.postValue(NetworkState.LOADED);
            networkState.postValue(NetworkState.LOADED);
        } else {
            networkState.postValue(NetworkState.FAILED);
        }
    }

    private void onSuccess(Response<UpcomingMoviesResponse> response, LoadParams<Long> params, LoadCallback<Long, Movie> callback) {
        try {
            if (response.isSuccessful()) {
                long nextKey = (params.key == response.body().totalPages) ? null : params.key + 1;
                callback.onResult(response.body().results, nextKey);
                networkState.postValue(NetworkState.LOADED);
            } else {
                networkState.postValue(NetworkState.FAILED);
            }

        } catch (Exception e) {
            String message = e.getMessage();
            e.printStackTrace();
        }
    }

    public MutableLiveData getNetworkState() {
        return networkState;
    }

    public MutableLiveData getInitialLoading() {
        return initialLoading;
    }


}
