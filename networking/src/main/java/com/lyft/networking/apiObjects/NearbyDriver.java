package com.lyft.networking.apiObjects;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class NearbyDriver {

    @SerializedName("locations")
    public final List<LatLng> locations;

    public NearbyDriver(List<LatLng> locations) {
        this.locations = locations;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class NearbyDriver {\n");

        sb.append("  locations: ").append(locations).append("\n");
        sb.append("}\n");
        return sb.toString();
    }
}
