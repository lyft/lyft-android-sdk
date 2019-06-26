package com.lyft.networking.apiObjects;

import com.google.gson.annotations.SerializedName;
import com.lyft.networking.apiObjects.internal.ICompleteData;

/**
 * Response model for requests for Rides on the Lyft Platform
 */

public class RideRequestResponse implements ICompleteData {
    /**
     * Requested ride ID.
     */
    @SerializedName("ride_id")
    public String ride_id;

    /**
     * Ride type will correspond to the ride_type request body parameter.
     */
    @SerializedName("ride_type")
    public String ride_type;

    /**
     * Ride status for a newly requested ride will be returned as pending.
     */
    @SerializedName("status")
    public String status;

    /**
     * Requested location for passenger pickup.
     */
    @SerializedName("origin")
    public LyftLocation origin;

    /**
     * Requested location for passenger drop off.
     */
    @SerializedName("destination")
    public LyftLocation destination;

    /**
     * The passenger
     */
    @SerializedName("passenger")
    public LyftUser passenger;

    @Override
    public boolean isValid() {
        return ride_id != null
                && ride_type != null
                && status != null
                && origin.isValid()
                && destination.isValid()
                && passenger.isValid();
    }
}
