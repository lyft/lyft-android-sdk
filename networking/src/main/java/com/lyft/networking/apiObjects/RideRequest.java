package com.lyft.networking.apiObjects;

/**
 * Model containing information necessary to request a
 * ride on the Lyft Platfrom
 */
public class RideRequest
{
    /**
     * Required: Pickup location
     */
    public LyftLocation origin;

    /**
     * Drop off location
     */
    public LyftLocation destination;

    /**
     * Required: Ride type; supported values depend on your location, check
     * Availability - Ride Types for acceptable values (possible examples
     * include: lyft, lyft_plus, etc).
     */
    public String  ride_type;

    /**
     * Prime Time Confirmation token
     * Deprecated, use {@link RideRequest#cost_token} instead.
     */
    @Deprecated
    public String primetime_confirmation_token;

    /**
     * If Prime Time is active, the response will include a Cost Token under
     * the key cost_token. It is your application's responsibility to confirm
     * the user's acceptance of Prime Time pricing. Once the user has accepted
     * the pricing, your application should repeat the request with the
     * confirmation token to confirm the ride request. The Cost Token locks in
     * pricing for one minute. Note that this is the same cost_token that is
     * also in the Availability - Ride Estimates response when querying that
     * endpoint with a user context.
     */
    public String cost_token;

    public RideRequest(LyftLocation origin, LyftLocation destination, String ride_type) { this(origin, destination, ride_type, null);}
    public RideRequest(LyftLocation origin, LyftLocation destination, String ride_type, String cost_token)
    {
        this.origin         = origin;
        this.ride_type      = ride_type;
        this.cost_token     = cost_token;
        this.destination    = destination;

    }

}
