package com.lyft.networking.apiObjects;

import com.google.gson.annotations.SerializedName;
import com.lyft.networking.apiObjects.internal.Validatable;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class NearbyDriver implements Validatable {

    @NotNull
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

    @Override
    public boolean isValid() {
        if (locations == null) {
            return false;
        }

        for (LatLng location : locations) {
            if (!location.isValid()) {
                return false;
            }
        }
        return true;
    }
}
