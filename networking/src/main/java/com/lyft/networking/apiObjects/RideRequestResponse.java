package com.lyft.networking.apiObjects;

import com.google.gson.annotations.SerializedName;

/**
 * Response model for requests for Rides on the Lyft Platform
 */

public class RideRequestResponse
{
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

}
