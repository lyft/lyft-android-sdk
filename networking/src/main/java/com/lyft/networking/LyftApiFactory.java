package com.lyft.networking;

import com.lyft.networking.apis.LyftApi;
import com.lyft.networking.apis.LyftApiRx;

import com.lyft.networking.internal.NullCheckErrorConverter;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class LyftApiFactory {

    private final ApiConfig apiConfig;

    public LyftApiFactory(ApiConfig apiConfig) {
        this.apiConfig = apiConfig;
    }

    /**
     * @return An implementation of Lyft's Public API endpoints that do not require a user.
     * The return type of API calls will be {@link retrofit2.Call}. Used by the LyftButton.
     *
     * The Retrofit client will throw a {@link com.lyft.networking.exceptions.PartialResponseException}
     * if the response returned by the server has missing information (i.e. non-null values returned as null).
     *
     * THE CALLER MUST be able to handle {@link com.lyft.networking.exceptions.PartialResponseException}.
     * Unhandled exceptions will cause a runtime crash.
     *
     * To avoid an exception and manually handle null checking, use #getUnchekedLyftApi().
     */
    public LyftApi getLyftApi() {
        Retrofit retrofitPublicApi = getRetrofitBuilder(true).build();
        return retrofitPublicApi.create(LyftApi.class);
    }

    /**
     * @return An implementation of Lyft's Public API endpoints that do not require a user.
     * The return type of API calls will be {@link retrofit2.Call}. Used by the LyftButton.
     *
     * The Retrofit client will not massage the response passed by the server. It is the responsibility of the caller
     * to verify that the required contents of each payload is non-null before access.
     */
    public LyftApi getUnchekedLyftApi() {
        Retrofit retrofitPublicApi = getRetrofitBuilder(false).build();
        return retrofitPublicApi.create(LyftApi.class);
    }

    /**
     * @return An implementation of Lyft's Public API endpoints that do not require a user.
     * The return type of API calls will be {@link io.reactivex.Observable}.
     *
     * The Retrofit client will throw a {@link com.lyft.networking.exceptions.PartialResponseException}
     * if the response returned by the server has missing information (i.e. non-null values returned as null).
     *
     * THE CALLER MUST be able to handle {@link com.lyft.networking.exceptions.PartialResponseException}.
     * Unhandled exceptions will cause a runtime crash.
     *
     * To avoid an exception and manually handle null checking, use #getUncheckedLyftApiRx().
     */
    public LyftApiRx getLyftApiRx() {
        Retrofit retrofitPublicApi = getRetrofitBuilder(true)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
        return retrofitPublicApi.create(LyftApiRx.class);
    }

    /**
     * @return An implementation of Lyft's Public API endpoints that do not require a user.
     * The return type of API calls will be {@link io.reactivex.Observable}.
     *
     * The Retrofit client will not massage the response passed by the server. It is the responsibility of the caller
     * to verify that the required contents of each payload is non-null before access.
     */
    public LyftApiRx getUncheckedLyftApiRx() {
        Retrofit retrofitPublicApi = getRetrofitBuilder(false)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
        return retrofitPublicApi.create(LyftApiRx.class);
    }

    private Retrofit.Builder getRetrofitBuilder(boolean requiresCompleteResponse) {
        OkHttpClient publicClient = getPublicOkHttpClient();
        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(LyftApi.API_ROOT)
                .client(publicClient);

        if (requiresCompleteResponse) {
            builder.addConverterFactory(new NullCheckErrorConverter());
        }

        return builder.addConverterFactory(GsonConverterFactory.create());
    }

    private OkHttpClient getPublicOkHttpClient() {
        return new OkHttpClient.Builder()
                .addInterceptor(new RequestInterceptor(apiConfig.getClientToken()))
                .build();
    }
}
