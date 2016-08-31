package com.lyft.networking.apiObjects;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class NearbyDriversByRideType {

    @SerializedName("ride_type")
    public final String ride_type;

    @SerializedName("drivers")
    public final List<NearbyDriver> drivers;

    public NearbyDriversByRideType(String ride_type, List<NearbyDriver> drivers) {
        this.ride_type = ride_type;
        this.drivers = drivers;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class NearbyDriversByRideType {\n");

        sb.append("  ride_type: ").append(ride_type).append("\n");
        sb.append("  drivers: ").append(drivers).append("\n");
        sb.append("}\n");
        return sb.toString();
    }
}
