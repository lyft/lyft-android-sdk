package com.lyft.networking.apiObjects;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Response model for requests for User Ride History on the Lyft Platform
 */

public class UserRideHistoryResponse
{
    @SerializedName("ride_history")
    public List<RideHistory> ride_history;

}
