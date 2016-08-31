package com.lyft.networking.apiObjects;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class NearbyDriversResponse {

    @SerializedName("nearby_drivers")
    public final List<NearbyDriversByRideType> nearby_drivers;

    public NearbyDriversResponse(List<NearbyDriversByRideType> nearby_drivers) {
        this.nearby_drivers = nearby_drivers;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class NearbyDriversResponse {\n");

        sb.append("  nearby_drivers: ").append(nearby_drivers).append("\n");
        sb.append("}\n");
        return sb.toString();
    }
}
