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
     * By default, the Retrofit client will throw a {@link com.lyft.networking.exceptions.PartialResponseException}
     * if the response returned by the server has missing information (i.e. non-null values returned as null).
     *
     * THE CALLER MUST be able to handle {@link com.lyft.networking.exceptions.PartialResponseException}.
     * Unhandled exceptions will cause a runtime crash.
     *
     * To explicitly disallow this, invoke #getLyftPublicApi(boolean).
     */
    public LyftPublicApi getLyftPublicApi() {
        return getLyftPublicApi(true);
    }

    /**
     * Creates an implementation of Lyft's Public API, with an option of generating {@link com.lyft.networking.exceptions.PartialResponseException}
     * should the server return incomplete response models.
     *
     * @param requiresCompleteResponse A boolean flag to indicate whether an exception should be thrown
     *                                 if the server returns partial data. If set true,
     *                                 the Retrofit client will throw a {@link com.lyft.networking.exceptions.PartialResponseException}
     *                                 when the response returned by the server has missing information (i.e. non-null values returned as null).
     *
     *                                 ONLY SET AS FALSE if the consumer is responsible for manually handling null checks.
     *
     * @return An implementation of Lyft's Public API endpoints that do not require a user.
     * The return type of API calls will be {@link retrofit2.Call}. Used by the LyftButton.
     */
    public LyftPublicApi getLyftPublicApi(boolean requiresCompleteResponse) {
        Retrofit retrofitPublicApi = getRetrofitBuilder(getPublicOkHttpClient(), requiresCompleteResponse).build();
        return retrofitPublicApi.create(LyftPublicApi.class);
    }

    /**
     * @return An implementation of Lyft's Public API endpoints that do not require a user.
     * The return type of API calls will be {@link rx.Observable}.
     *
     * By default, the Retrofit client will throw a {@link com.lyft.networking.exceptions.PartialResponseException}
     * if the response returned by the server has missing information (i.e. non-null values returned as null).
     *
     * THE CALLER MUST be able to handle {@link com.lyft.networking.exceptions.PartialResponseException}.
     * Unhandled exceptions will cause a runtime crash.
     *
     * To explicitly disallow this, invoke #getLyftPublicApiRx(boolean).
     */
    public LyftPublicApiRx getLyftPublicApiRx() {
        return getLyftPublicApiRx(true);
    }

    /**
     * @param requiresCompleteResponse A boolean flag to indicate whether an exception should be thrown
     *                                 if the server returns partial data. If set true,
     *                                 the Retrofit client will throw a {@link com.lyft.networking.exceptions.PartialResponseException}
     *                                 when the response returned by the server has missing information (i.e. non-null values returned as null).
     *
     *                                 ONLY SET AS FALSE if the consumer is responsible for manually handling null checks.
     *
     * @return An implementation of Lyft's Public API endpoints that do not require a user.
     * The return type of API calls will be {@link rx.Observable}.
     */
    public LyftPublicApiRx getLyftPublicApiRx(boolean requiresCompleteResponse) {
        Retrofit retrofitPublicApi = getRetrofitBuilder(getPublicOkHttpClient(), requiresCompleteResponse)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
        return retrofitPublicApi.create(LyftPublicApiRx.class);
    }

    /**
     * @return An implementation of Lyft's User API endpoints that REQUIRE a user access token.
     * The return type of API calls will be {@link retrofit2.Call}. Used by the LyftButton.
     *
     * By default, the Retrofit client will throw a {@link com.lyft.networking.exceptions.PartialResponseException}
     * if the response returned by the server has missing information (i.e. non-null values returned as null).
     *
     * THE CALLER MUST be able to handle {@link com.lyft.networking.exceptions.PartialResponseException}.
     * Unhandled exceptions will cause a runtime crash.
     *
     * To explicitly disallow this, invoke #getLyftUserApi(boolean).
     */
    public LyftUserApi getLyftUserApi() {
        return getLyftUserApi(true);
    }

    /**
     * @param requiresCompleteResponse A boolean flag to indicate whether an exception should be thrown
     *                                 if the server returns partial data. If set true,
     *                                 the Retrofit client will throw a {@link com.lyft.networking.exceptions.PartialResponseException}
     *                                 when the response returned by the server has missing information (i.e. non-null values returned as null).
     *
     *                                 ONLY SET AS FALSE if the consumer is responsible for manually handling null checks.
     *
     * @return An implementation of Lyft's User API endpoints that REQUIRE a user access token.
     * The return type of API calls will be {@link retrofit2.Call}. Used by the LyftButton.
     */
    public LyftUserApi getLyftUserApi(boolean requiresCompleteResponse) {
        Retrofit retrofitUserApi = getRetrofitBuilder(getUserOkHttpClient(), requiresCompleteResponse).build();
        return retrofitUserApi.create(LyftUserApi.class);
    }

    /**
     * @return An implementation of Lyft's User API endpoints that REQUIRE a user access token.
     * The return type of API calls will be {@link rx.Observable}. Used by the LyftButton.
     *
     * By default, the Retrofit client will throw a {@link com.lyft.networking.exceptions.PartialResponseException}
     * if the response returned by the server has missing information (i.e. non-null values returned as null).
     *
     * THE CALLER MUST be able to handle {@link com.lyft.networking.exceptions.PartialResponseException}.
     * Unhandled exceptions will cause a runtime crash.
     *
     * To explicitly disallow this, invoke #getLyftUserApiRx(boolean).
     */
    public LyftUserApiRx getLyftUserApiRx() {
        return getLyftUserApiRx(true);
    }


    /**
     * @param requiresCompleteResponse A boolean flag to indicate whether an exception should be thrown
     *                                 if the server returns partial data. If set true,
     *                                 the Retrofit client will throw a {@link com.lyft.networking.exceptions.PartialResponseException}
     *                                 when the response returned by the server has missing information (i.e. non-null values returned as null).
     *
     *                                 ONLY SET AS FALSE if the consumer is responsible for manually handling null checks.
     *
     * @return An implementation of Lyft's User API endpoints that REQUIRE a user access token.
     * The return type of API calls will be {@link rx.Observable}. Used by the LyftButton.
     */
    public LyftUserApiRx getLyftUserApiRx(boolean requiresCompleteResponse) {
        Retrofit retrofitUserApi = getRetrofitBuilder(getUserOkHttpClient(), requiresCompleteResponse)
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
