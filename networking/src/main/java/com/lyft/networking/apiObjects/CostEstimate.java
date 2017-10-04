package com.lyft.networking.apiObjects;

import com.google.gson.annotations.SerializedName;

/**
 * A non-guaranteed estimate of price
 **/
public class CostEstimate {

    /**
     * Ride type one of: (lyft, lyft_plus, lyft_line, lyft_premier, lyft_lux, lyft_luxsuv)
     */
    @SerializedName("ride_type")
    public final String ride_type;

    /**
     * A human readable description of the ride type.
     */
    @SerializedName("display_name")
    public final String display_name;

    /***
     *ISO 4217 currency code for the amount (e.g. USD).
     */
    @SerializedName("currency")
    public final String currency;

    /**
     * Estimated lower bound for trip cost, in minor units (cents). Estimates are not guaranteed,
     * and only provide a reasonable range based on current conditions.
     */
    @SerializedName("estimated_cost_cents_min")
    public final Integer estimated_cost_cents_min;

    /**
     * Estimated upper bound for trip cost, in minor units (cents). Estimates are not guaranteed,
     * and only provide a reasonable range based on current conditions.
     */
    @SerializedName("estimated_cost_cents_max")
    public final Integer estimated_cost_cents_max;

    /**
     * Estimated distance for this ride as expressed in miles.
     */
    @SerializedName("estimated_distance_miles")
    public final Double estimated_distance_miles;

    /**
     * Estimated time to get from the start location to the end as expressed in seconds.
     */
    @SerializedName("estimated_duration_seconds")
    public final Integer estimated_duration_seconds;

    /**
     * The current primetime value
     */
    @SerializedName("primetime_percentage")
    public final String primetime_percentage;

    /**
     * A token that should be used to confirm that the user has accepted current Prime Time
     * and/or fixed price charges. See note above.
     */
    @SerializedName("cost_token")
    public final String cost_token;

    /**
     * A token that should be used to confirm that the user has accepted current Prime Time pricing.
     */
    @SerializedName("primetime_confirmation_token")
    public final String primetime_confirmation_token;

    public CostEstimate(String ride_type, String display_name, String currency, Integer estimated_cost_cents_min,
                        Integer estimated_cost_cents_max, Double estimated_distance_miles, Integer estimated_duration_seconds,
                        String primetime_percentage, String primetime_confirmation_token, String cost_token) {
        this.ride_type = ride_type;
        this.display_name = display_name;
        this.currency = currency;
        this.estimated_cost_cents_min = estimated_cost_cents_min;
        this.estimated_cost_cents_max = estimated_cost_cents_max;
        this.estimated_distance_miles = estimated_distance_miles;
        this.estimated_duration_seconds = estimated_duration_seconds;
        this.primetime_percentage = primetime_percentage;
        this.primetime_confirmation_token = primetime_confirmation_token;
        this.cost_token = cost_token;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class CostEstimate {\n");

        sb.append("  ride_type: ").append(ride_type).append("\n");
        sb.append("  display_name: ").append(display_name).append("\n");
        sb.append("  currency: ").append(currency).append("\n");
        sb.append("  estimated_cost_cents_min: ").append(estimated_cost_cents_min).append("\n");
        sb.append("  estimated_cost_cents_max: ").append(estimated_cost_cents_max).append("\n");
        sb.append("  estimated_distance_miles: ").append(estimated_distance_miles).append("\n");
        sb.append("  estimated_duration_seconds: ").append(estimated_duration_seconds).append("\n");
        sb.append("  primetime_percentage: ").append(primetime_percentage).append("\n");
        sb.append("  primetime_confirmation_token: ").append(primetime_confirmation_token).append("\n");
        sb.append("  cost_token: ").append(cost_token).append("\n");
        sb.append("}\n");
        return sb.toString();
    }
}
