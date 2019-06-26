package com.lyft.networking;

import com.lyft.networking.apis.LyftPublicApi;
import com.lyft.networking.apis.LyftPublicApiRx;
import com.lyft.networking.apis.LyftUserApi;
import com.lyft.networking.apis.LyftUserApiRx;

import com.lyft.networking.internal.NullCheckErrorConverter;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
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
        Retrofit retrofitPublicApi = getRetrofitBuilder(getPublicOkHttpClient(), true).build();
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
        Retrofit retrofitPublicApi = getRetrofitBuilder(getPublicOkHttpClient(), false).build();
        return retrofitPublicApi.create(LyftPublicApi.class);
    }

    /**
     * @return An implementation of Lyft's Public API endpoints that do not require a user.
     * The return type of API calls will be {@link rx.Observable}.
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
        Retrofit retrofitPublicApi = getRetrofitBuilder(getPublicOkHttpClient(), true)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
        return retrofitPublicApi.create(LyftPublicApiRx.class);
    }

    /**
     * @return An implementation of Lyft's Public API endpoints that do not require a user.
     * The return type of API calls will be {@link rx.Observable}.
     *
     * The Retrofit client will not massage the response passed by the server. It is the responsibility of the caller
     * to verify that the required contents of each payload is non-null before access.
     */
    public LyftPublicApiRx getUncheckedLyftPublicApiRx() {
        Retrofit retrofitPublicApi = getRetrofitBuilder(getPublicOkHttpClient(), false)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
        return retrofitPublicApi.create(LyftPublicApiRx.class);
    }

    /**
     * @return An implementation of Lyft's User API endpoints that REQUIRE a user access token.
     * The return type of API calls will be {@link retrofit2.Call}. Used by the LyftButton.
     *
     * The Retrofit client will throw a {@link com.lyft.networking.exceptions.PartialResponseException}
     * if the response returned by the server has missing information (i.e. non-null values returned as null).
     *
     * THE CALLER MUST be able to handle {@link com.lyft.networking.exceptions.PartialResponseException}.
     * Unhandled exceptions will cause a runtime crash.
     *
     * To explicitly disallow this, invoke #getUncheckedLyftUserApi().
     */
    public LyftUserApi getLyftUserApi() {
        Retrofit retrofitUserApi = getRetrofitBuilder(getUserOkHttpClient(), true).build();
        return retrofitUserApi.create(LyftUserApi.class);
    }

    /**
     * @return An implementation of Lyft's User API endpoints that REQUIRE a user access token.
     * The return type of API calls will be {@link retrofit2.Call}. Used by the LyftButton.
     *
     * The Retrofit client will not massage the response passed by the server. It is the responsibility of the caller
     * to verify that the required contents of each payload is non-null before access.
     */
    public LyftUserApi getUncheckedLyftUserApi() {
        Retrofit retrofitUserApi = getRetrofitBuilder(getUserOkHttpClient(), false).build();
        return retrofitUserApi.create(LyftUserApi.class);
    }

    /**
     * @return An implementation of Lyft's User API endpoints that REQUIRE a user access token.
     * The return type of API calls will be {@link rx.Observable}. Used by the LyftButton.
     *
     * The Retrofit client will throw a {@link com.lyft.networking.exceptions.PartialResponseException}
     * if the response returned by the server has missing information (i.e. non-null values returned as null).
     *
     * THE CALLER MUST be able to handle {@link com.lyft.networking.exceptions.PartialResponseException}.
     * Unhandled exceptions will cause a runtime crash.
     *
     * To explicitly disallow this, invoke #getLyftUncheckedUserApiRx().
     */
    public LyftUserApiRx getLyftUserApiRx() {
        Retrofit retrofitUserApi = getRetrofitBuilder(getUserOkHttpClient(), true)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
        return retrofitUserApi.create(LyftUserApiRx.class);
    }


    /**
     * @return An implementation of Lyft's User API endpoints that REQUIRE a user access token.
     * The return type of API calls will be {@link rx.Observable}. Used by the LyftButton.
     *
     * The Retrofit client will not massage the response passed by the server. It is the responsibility of the caller
     * to verify that the required contents of each payload is non-null before access.
     */
    public LyftUserApiRx getUncheckedLyftUserApiRx() {
        Retrofit retrofitUserApi = getRetrofitBuilder(getUserOkHttpClient(), false)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
        return retrofitUserApi.create(LyftUserApiRx.class);
    }

    private Retrofit.Builder getRetrofitBuilder(OkHttpClient client, boolean requiresCompleteResponse) {
        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(LyftPublicApi.API_ROOT)
                .client(client);

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

    private OkHttpClient getUserOkHttpClient()
    {
        return new OkHttpClient.Builder()
                .addInterceptor(new RequestInterceptor(apiConfig.getUserAccessToken()))
                .build();
    }
}
