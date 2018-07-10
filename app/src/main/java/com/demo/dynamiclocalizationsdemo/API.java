package com.demo.dynamiclocalizationsdemo;

import android.util.Log;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;

/**
 * Created by rahulhariharan on 10/07/18.
 */

public class API {


    public interface LanguagePackAPI {
        @GET("/api/breeds/image/random")
        Call<LanguageSuite> getLanguageSuite();
    }

    public void getLanguagePack() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.github.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        LanguagePackAPI service = retrofit.create(LanguagePackAPI.class);
        Call<LanguageSuite> call = service.getLanguageSuite();
        call.enqueue(new Callback<LanguageSuite>() {
            @Override
            public void onResponse(Call<LanguageSuite> call, Response<LanguageSuite> response) {
                Log.v("event_", response.body().toString());
            }

            @Override
            public void onFailure(Call<LanguageSuite> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }
}
