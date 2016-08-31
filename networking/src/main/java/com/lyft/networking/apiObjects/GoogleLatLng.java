package com.lyft.networking.apiObjects;

import com.google.gson.annotations.SerializedName;

public class GoogleLatLng {

    @SerializedName("lat")
    public final Double lat;

    @SerializedName("lng")
    public final Double lng;

    public GoogleLatLng(Double lat, Double lng) {
        this.lat = lat;
        this.lng = lng;
    }

    @Override
    public String toString() {
        return "GoogleLatLng{" +
                "lat=" + lat +
                ", lng=" + lng +
                '}';
    }
}
