package com.lyft.networking.apiObjects;

import com.google.gson.annotations.SerializedName;

public class GoogleGeometry {

    @SerializedName("location")
    public final GoogleLatLng location;

    public GoogleGeometry(GoogleLatLng location) {
        this.location = location;
    }

    @Override
    public String toString() {
        return "GoogleGeometry{" +
                "location=" + location +
                '}';
    }
}
