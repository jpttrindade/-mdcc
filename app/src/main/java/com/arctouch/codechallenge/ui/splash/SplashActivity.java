package com.arctouch.codechallenge.ui.splash;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.arctouch.codechallenge.R;
import com.arctouch.codechallenge.ui.home.HomeActivity;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_activity);
//
//        api.genres(TmdbApi.API_KEY, TmdbApi.DEFAULT_LANGUAGE)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(response -> {
//                    Cache.setGenres(response.genres);¬
//                    startActivity(new Intent(this, HomeActivity.class));
//                    finish();
//                });
        startActivity(new Intent(this, HomeActivity.class));
        finish();
    }
}
