package com.lyft.networking.apiObjects;

import com.google.gson.annotations.SerializedName;
import com.lyft.networking.apiObjects.internal.Validatable;
import org.jetbrains.annotations.NotNull;

/**
 * Estimated Time of Arrival
 **/
public class Eta implements Validatable {

    @NotNull
    @SerializedName("ride_type")
    public final String ride_type;

    @NotNull
    @SerializedName("display_name")
    public final String display_name;

    @NotNull
    @SerializedName("eta_seconds")
    public final Integer eta_seconds;

    public Eta(String ride_type, String display_name, Integer eta_seconds) {
        this.ride_type = ride_type;
        this.display_name = display_name;
        this.eta_seconds = eta_seconds;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class Eta {\n");

        sb.append("  ride_type: ").append(ride_type).append("\n");
        sb.append("  display_name: ").append(display_name).append("\n");
        sb.append("  eta_seconds: ").append(eta_seconds).append("\n");
        sb.append("}\n");
        return sb.toString();
    }


    @Override
    public boolean isValid() {
        return ride_type != null
                && display_name != null
                && eta_seconds != null;
    }
}
