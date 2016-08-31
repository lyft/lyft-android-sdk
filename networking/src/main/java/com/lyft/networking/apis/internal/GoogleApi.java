package com.lyft.networking.apis.internal;

import com.lyft.networking.apiObjects.GoogleGeocodeResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface GoogleApi {

    String API_ROOT = "https://maps.googleapis.com";

    @GET("/maps/api/geocode/json")
    Call<GoogleGeocodeResponse> forwardGeocode(@Query("address") String address);
}
