package com.lyft.networking.apiObjects;

import com.google.gson.annotations.SerializedName;
import com.lyft.networking.apiObjects.internal.ICompleteData;
import org.jetbrains.annotations.Nullable;

/**
 * A model for an instance of a user's RideHistory
 */

public class RideHistory implements ICompleteData {
    /**
     * Ride id
     */
    @SerializedName("ride_id")
    public String ride_id;
    /**
     * Ride type expressed as one of (lyft, lyft_plus, lyft_line, etc).
     */
    @SerializedName("ride_type")
    public String ride_type;
    /**
     * Ride status expressed as one of (pending, accepted, arrived,
     * pickedUp, droppedOff, canceled, unknown).
     */
    @SerializedName("status")
    public String status;

    /**
     * Prime Time percentage applied to the base price
     */
    @SerializedName("primetime_percentage")
    public String primetime_percentage;

    /**
     * The ISO8601 Timestamp of when the request was made.
     */
    @SerializedName("requested_at")
    public String requested_at;

    /**
     * Indicates whether the ride was requested from the business profile or personal
     * profile of the passenger.
     */
    @Nullable
    @SerializedName("ride_profile")
    public String ride_profile;

    /**
     * Amp color HEX code, eg. #FFFFFF.
     */
    @Nullable
    @SerializedName("beacon_color")
    public String beacon_color;

    /**
     * Link to a web view showing the pricing structure for the geographic area where
     * the ride was taken.
     */
    @Nullable
    @SerializedName("pricing_details_url")
    public String pricing_details_url;

    /**
     * Link to a web view showing the passenger, driver, and route information for a ride.
     * This field will only be present for rides created via the API, or that have been
     * shared through the "Share my Route" feature.
     */
    @Nullable
    @SerializedName("route_url")
    public String route_url;

    /**
     * The role of user who canceled the ride (if applicable).
     */
    @Nullable
    @SerializedName("canceled_by")
    public String canceled_by;

    /**
     * The written feedback the user left for this ride.
     */
    @Nullable
    @SerializedName("feedback")
    public String feedback;
    /**
     * The array of actors who may cancel the ride at this point (driver, passenger, dispatcher).
     */
    @Nullable
    @SerializedName("can_cancel")
    public String[] can_cancel;

    /**
     * Actual location of passenger pickup.
     */
    @SerializedName("pickup")
    public LyftLocation pickup;

    /**
     * Requested location for passenger drop off.
     */
    @SerializedName("destination")

    public LyftLocation destination;
    /**
     * Actual location of passenger drop off
    */
    @SerializedName("dropoff")
    public LyftLocation dropoff;

    /**
     * Current location of the vehicle. Available after ride status changes
     * to pickedUp and until droppedOff.
     */
    @Nullable
    @SerializedName("location")
    public LyftLocation location;

    /**
     * Requested location for passenger pickup.
     */
    @SerializedName("origin")
    public EtaLocation origin;

    /**
     * The passenger meta data
     */
    @SerializedName("passenger")
    public LyftUser passenger;

    /**
     * Driver meta data
     */
    @SerializedName("driver")
    public LyftDriver driver;
    /**
     * Total cost for the ride. Available after ride status changes to droppedOff and
     * the passenger has rated and paid for the ride, plus some processing time.
     */
    @SerializedName("price")
    public RidePrice price;

    /**
     * The cost of cancellation if there would be a penalty
     */
    @SerializedName("cancellation_price")
    public RideCancellation cancellation_price;
    /**
     * Vehicle meta data
     */
    @SerializedName("vehicle")
    public Vehicle vehicle;

    @SerializedName("distance_miles")
    public double distance_miles;

    @SerializedName("duration_seconds")
    public double duration_seconds;

    @SerializedName("rating")
    public int rating;

    /**
     * Helper method that returns a boolean flag indicating whether or not the Ride represented by
     * this instance of RideHistory is in a pending state.
     * @return
     */
    public boolean isPending() { return "pending".equalsIgnoreCase(status) || "unknown".equalsIgnoreCase(status); }


    @Override
    public boolean isValid() {
        return ride_id != null
                && ride_type != null
                && status != null
                && primetime_percentage != null
                && requested_at != null
                && pickup.isValid()
                && destination.isValid()
                && dropoff.isValid()
                && origin.isValid()
                && passenger.isValid()
                && driver.isValid()
                && vehicle.isValid();
    }
}
