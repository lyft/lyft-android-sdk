package com.lyft.networking.apis;

import com.lyft.networking.apiObjects.CostEstimateResponse;
import com.lyft.networking.apiObjects.EtaEstimateResponse;
import com.lyft.networking.apiObjects.NearbyDriversResponse;
import com.lyft.networking.apiObjects.RideTypesResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface LyftPublicApi {

    String API_ROOT = "https://api.lyft.com";

    /**
    * Cost estimates
    * Estimate the cost of taking a Lyft between two points. 
       * @param startLat Latitude of the starting location (required)
       * @param startLng Longitude of the starting location (required)
       * @param rideType ID of a ride type (optional)
       * @param endLat Latitude of the ending location (optional)
       * @param endLng Longitude of the ending location (optional)
       * @return Call&lt;CostEstimateResponse&gt;
    */
    
    @GET("/v1/cost")
    Call<CostEstimateResponse> getCosts(@Query("start_lat") Double startLat, @Query("start_lng") Double startLng, @Query("ride_type") String rideType, @Query("end_lat") Double endLat, @Query("end_lng") Double endLng);

    /**
    * Available drivers nearby
    * The drivers endpoint returns a list of nearby drivers&#39; lat and lng at a given location. 
       * @param lat Latitude of a location (required)
       * @param lng Longitude of a location (required)
       * @return Call&lt;NearbyDriversResponse&gt;
    */
    
    @GET("/v1/drivers")
    Call<NearbyDriversResponse> getDrivers(@Query("lat") Double lat, @Query("lng") Double lng);

    /***
     * Returns the estimated time in seconds it will take for the nearest driver to reach the
     * specified location
     * @param lat   Pick up location latitude
     * @param lng   Pick up location longitude
     * @return Retrofit Call of {@link EtaEstimateResponse} type.
     */
    @GET("/v1/eta")
    Call<EtaEstimateResponse> getEtas(@Query("lat") double lat, @Query("lng") double lng);


    /**
    * Pickup ETAs
    * The ETA endpoint lets you know how quickly a Lyft driver can come get you 
       * @param lat Latitude of a location (required)
       * @param lng Longitude of a location (required)
       * @param rideType ID of a ride type (optional)
       * @return Call&lt;EtaEstimateResponse&gt;
    */
    
    @GET("/v1/eta")
    Call<EtaEstimateResponse> getEtas(@Query("lat") Double lat, @Query("lng") Double lng, @Query("ride_type") String rideType);

    /***
     * Returns the estimated time in seconds it will take for the nearest driver to reach the
     * specified location
     * @param lat               Pick up location latitude
     * @param lng               Pick up location longitude
     * @param rideType          The id of the desired ride type that driver eta is
     *                          being requested for.
     * @param destination_lat   Destination location latitude
     * @param destination_lng   Destination location longitude
     * @return Retrofit Call of {@link EtaEstimateResponse} type.
     */
    @GET("/v1/eta")
    Call<EtaEstimateResponse> getEtas(@Query("lat") double lat, @Query("lng") double lng, @Query("ride_type") String rideType, @Query("destination_lat") Double destination_lat, @Query("destination_lng") Double destination_lng);

    /**
    * Types of rides
    * The ride types endpoint returns information about what kinds of Lyft rides you can request at a given location. 
       * @param lat Latitude of a location (required)
       * @param lng Longitude of a location (required)
       * @param rideType ID of a ride type (optional)
       * @return Call&lt;RideTypesResponse&gt;
    */
    
    @GET("/v1/ridetypes")
    Call<RideTypesResponse> getRidetypes(@Query("lat") Double lat, @Query("lng") Double lng, @Query("ride_type") String rideType);

}
