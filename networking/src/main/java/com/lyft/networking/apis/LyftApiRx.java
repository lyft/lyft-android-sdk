package com.lyft.networking.apis;

import com.lyft.networking.apiObjects.CostEstimateResponse;
import com.lyft.networking.apiObjects.EtaEstimateResponse;
import com.lyft.networking.apiObjects.NearbyDriversResponse;
import com.lyft.networking.apiObjects.RideTypesResponse;
import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface LyftApiRx {

    /**
     * Cost estimates
     * Estimate the cost of taking a Lyft between two points.
     *
     * @param startLat Latitude of the starting location (required)
     * @param startLng Longitude of the starting location (required)
     * @param rideType ID of a ride type (optional)
     * @param endLat Latitude of the ending location (optional)
     * @param endLng Longitude of the ending location (optional)
     * @return Call&lt;CostEstimateResponse&gt;
     */

    @GET("/v1/cost")
    Observable<CostEstimateResponse> getCosts(@Query("start_lat") Double startLat, @Query("start_lng") Double startLng,
            @Query("ride_type") String rideType, @Query("end_lat") Double endLat, @Query("end_lng") Double endLng);

    /**
     * Available drivers nearby
     * The drivers endpoint returns a list of nearby drivers&#39; lat and lng at a given location.
     *
     * @param lat Latitude of a location (required)
     * @param lng Longitude of a location (required)
     * @return Call&lt;NearbyDriversResponse&gt;
     */

    @GET("/v1/drivers")
    Observable<NearbyDriversResponse> getDrivers(@Query("lat") Double lat, @Query("lng") Double lng);

    /**
     * Pickup ETAs
     * The ETA endpoint lets you know how quickly a Lyft driver can come get you
     *
     * @param lat Latitude of a location (required)
     * @param lng Longitude of a location (required)
     * @param rideType ID of a ride type (optional)
     * @return Call&lt;EtaEstimateResponse&gt;
     */

    @GET("/v1/eta")
    Observable<EtaEstimateResponse> getEtas(@Query("lat") Double lat, @Query("lng") Double lng, @Query("ride_type") String rideType);

    /**
     * Types of rides
     * The ride types endpoint returns information about what kinds of Lyft rides you can request at a given location.
     *
     * @param lat Latitude of a location (required)
     * @param lng Longitude of a location (required)
     * @param rideType ID of a ride type (optional)
     * @return Call&lt;RideTypesResponse&gt;
     */

    @GET("/v1/ridetypes")
    Observable<RideTypesResponse> getRidetypes(@Query("lat") Double lat, @Query("lng") Double lng, @Query("ride_type") String rideType);
}
