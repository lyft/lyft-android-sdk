package com.lyft.networking.apiObjects;

import com.google.gson.annotations.SerializedName;
import com.lyft.networking.apiObjects.internal.Validatable;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class RideType implements Validatable {

    @NotNull
    @SerializedName("ride_type")
    public final String ride_type;

    @NotNull
    @SerializedName("display_name")
    public final String display_name;

    @NotNull
    @SerializedName("seats")
    public final Integer seats;

    @Nullable
    @SerializedName("image_url")
    public final String image_url;

    @NotNull
    @SerializedName("pricing_details")
    public final PricingDetails pricing_details;

    public RideType(String ride_type,
                    String display_name,
                    Integer seats,
                    String image_url,
                    PricingDetails pricing_details) {
        this.ride_type = ride_type;
        this.display_name = display_name;
        this.seats = seats;
        this.image_url = image_url;
        this.pricing_details = pricing_details;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class RideType {\n");

        sb.append("  ride_type: ").append(ride_type).append("\n");
        sb.append("  display_name: ").append(display_name).append("\n");
        sb.append("  seats: ").append(seats).append("\n");
        sb.append("  image_url: ").append(image_url).append("\n");
        sb.append("  pricing_details: ").append(pricing_details).append("\n");
        sb.append("}\n");
        return sb.toString();
    }

    @Override
    public boolean isValid() {
        return ride_type != null
                && display_name != null
                && seats != null
                && pricing_details != null && pricing_details.isValid();
    }
}
