package com.lyft.networking.apiObjects;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class GoogleGeocodeResponse {

    @SerializedName("status")
    public final String status;

    @SerializedName("results")
    public final List<GoogleGeocodeResult> results;

    public GoogleGeocodeResponse(String status, List<GoogleGeocodeResult> results) {
        this.status = status;
        this.results = results;
    }

    @Override
    public String toString() {
        return "GoogleGeocodeResponse{" +
                "status='" + status + '\'' +
                ", results=" + results +
                '}';
    }
}
