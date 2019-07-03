package com.lyft.networking.apiObjects;

import com.google.gson.annotations.SerializedName;
import com.lyft.networking.apiObjects.internal.Validatable;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class NearbyDriversByRideType implements Validatable {

    @NotNull
    @SerializedName("ride_type")
    public final String ride_type;

    @NotNull
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

    @Override
    public boolean isValid() {
        if (drivers == null) {
            return false;
        }

        for (NearbyDriver driver : drivers) {
            if (!driver.isValid()) {
                return false;
            }
        }
        return ride_type != null;
    }
}
