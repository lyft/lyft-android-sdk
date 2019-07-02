package com.lyft.networking;

import com.lyft.networking.apis.LyftPublicApi;
import com.lyft.networking.apis.LyftPublicApiRx;

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
     * To avoid an exception and manually handle null checking, use #getUnchekedLyftPublicApi().
     */
    public LyftPublicApi getLyftPublicApi() {
        Retrofit retrofitPublicApi = getRetrofitBuilder(true).build();
        return retrofitPublicApi.create(LyftPublicApi.class);
    }

    /**
     * @return An implementation of Lyft's Public API endpoints that do not require a user.
     * The return type of API calls will be {@link retrofit2.Call}. Used by the LyftButton.
     *
     * The Retrofit client will not massage the response passed by the server. It is the responsibility of the caller
     * to verify that the required contents of each payload is non-null before access.
     */
    public LyftPublicApi getUnchekedLyftPublicApi() {
        Retrofit retrofitPublicApi = getRetrofitBuilder(false).build();
        return retrofitPublicApi.create(LyftPublicApi.class);
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
     * To avoid an exception and manually handle null checking, use #getUnchekedLyftPublicApiRx().
     */
    public LyftPublicApiRx getLyftPublicApiRx() {
        Retrofit retrofitPublicApi = getRetrofitBuilder(true)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
        return retrofitPublicApi.create(LyftPublicApiRx.class);
    }

    /**
     * @return An implementation of Lyft's Public API endpoints that do not require a user.
     * The return type of API calls will be {@link io.reactivex.Observable}.
     *
     * The Retrofit client will not massage the response passed by the server. It is the responsibility of the caller
     * to verify that the required contents of each payload is non-null before access.
     */
    public LyftPublicApiRx getUncheckedLyftPublicApiRx() {
        Retrofit retrofitPublicApi = getRetrofitBuilder(false)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
        return retrofitPublicApi.create(LyftPublicApiRx.class);
    }

    private Retrofit.Builder getRetrofitBuilder(boolean requiresCompleteResponse) {
        OkHttpClient publicClient = getPublicOkHttpClient();
        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(LyftPublicApi.API_ROOT)
                .client(publicClient);

        if (requiresCompleteResponse) {
            builder.addConverterFactory(new NullCheckErrorConverter());
        }

        return builder.addConverterFactory(GsonConverterFactory.create());
    }

    private OkHttpClient getPublicOkHttpClient()
    {
        return new OkHttpClient.Builder()
                .addInterceptor(new RequestInterceptor(apiConfig.getClientToken()))
                .build();
    }
}
