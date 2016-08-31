package com.lyft.networking;

import com.lyft.networking.apis.internal.GoogleApi;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class GoogleApiBuilder {

    public GoogleApi build() {
        Retrofit retrofitGoogleApi = new Retrofit.Builder()
                .baseUrl(GoogleApi.API_ROOT)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        return retrofitGoogleApi.create(GoogleApi.class);
    }
}
