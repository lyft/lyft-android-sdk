package com.lyft.networking.apiObjects;

import com.google.gson.annotations.SerializedName;
import com.lyft.networking.apiObjects.internal.Validatable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Response model for requests for Rides on the Lyft Platform
 */

public class RideRequestResponse implements Validatable {
    /**
     * Requested ride ID.
     */
    @NotNull
    @SerializedName("ride_id")
    public String ride_id;

    /**
     * Ride type will correspond to the ride_type request body parameter.
     */
    @NotNull
    @SerializedName("ride_type")
    public String ride_type;

    /**
     * Ride status for a newly requested ride will be returned as pending.
     */
    @NotNull
    @SerializedName("status")
    public String status;

    /**
     * Requested location for passenger pickup.
     */
    @Nullable
    @SerializedName("origin")
    public LyftLocation origin;

    /**
     * Requested location for passenger drop off.
     */
    @Nullable
    @SerializedName("destination")
    public LyftLocation destination;

    /**
     * The passenger
     */
    @Nullable
    @SerializedName("passenger")
    public LyftUser passenger;

    @Override
    public boolean isValid() {
        return ride_id != null
                && ride_type != null
                && status != null;
    }
}
