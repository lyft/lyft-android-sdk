package com.lyft.networking;

import com.lyft.networking.apis.LyftPublicApi;
import com.lyft.networking.apis.LyftPublicApiRx;
import com.lyft.networking.apis.LyftUserApi;
import com.lyft.networking.apis.LyftUserApiRx;

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
     * See: <a href="http://petstore.swagger.io/?url=https://api.lyft.com/v1/spec#!/Public/">http://petstore.swagger.io/?url=https://api.lyft.com/v1/spec#!/Public/</a>
     */
    public LyftPublicApi getLyftPublicApi() {
        Retrofit retrofitPublicApi = getRetrofitBuilder(getPublicOkHttpClient()).build();
        return retrofitPublicApi.create(LyftPublicApi.class);
    }

    /**
     * @return An implementation of Lyft's Public API endpoints that do not require a user.
     * The return type of API calls will be {@link rx.Observable}.
     * See: <a href="http://petstore.swagger.io/?url=https://api.lyft.com/v1/spec#!/Public/">http://petstore.swagger.io/?url=https://api.lyft.com/v1/spec#!/Public/</a>
     */
    public LyftPublicApiRx getLyftPublicApiRx() {
        Retrofit retrofitPublicApi = getRetrofitBuilder(getPublicOkHttpClient())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
        return retrofitPublicApi.create(LyftPublicApiRx.class);
    }

    /**
     * @return An implementation of Lyft's User API endpoints that REQUIRE a user access token.
     * The return type of API calls will be {@link retrofit2.Call}. Used by the LyftButton.
     */

    public LyftUserApi getLyftUserApi()
    {
        Retrofit retrofitUserApi = getRetrofitBuilder(getUserOkHttpClient()).build();
        return retrofitUserApi.create(LyftUserApi.class);
    }

    /**
     * @return An implementation of Lyft's User API endpoints that REQUIRE a user access token.
     * The return type of API calls will be {@link rx.Observable}. Used by the LyftButton.
     */
    public LyftUserApiRx getLyftUserApiRx() {
        Retrofit retrofitUserApi = getRetrofitBuilder(getUserOkHttpClient())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
        return retrofitUserApi.create(LyftUserApiRx.class);
    }

    private Retrofit.Builder getRetrofitBuilder(OkHttpClient client) {
        return new Retrofit.Builder()
                .baseUrl(LyftPublicApi.API_ROOT)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create());
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
