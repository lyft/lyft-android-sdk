package com.lyft.networking.apiObjects;

import com.google.gson.annotations.SerializedName;
import com.lyft.networking.apiObjects.internal.Validatable;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Response model for requests for User Ride History on the Lyft Platform
 */

public class UserRideHistoryResponse implements Validatable {

    @NotNull
    @SerializedName("ride_history")
    public List<RideHistory> ride_history;

    @Override
    public boolean isValid() {
        for (RideHistory rideHistory : ride_history) {
            if (!rideHistory.isValid()) {
                return false;
            }
        }

        return true;
    }
}
