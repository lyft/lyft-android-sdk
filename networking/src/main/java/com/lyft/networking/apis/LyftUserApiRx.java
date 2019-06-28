package com.lyft.networking.apis;

import com.lyft.networking.ApiConfig;
import com.lyft.networking.apiObjects.EtaEstimateResponse;
import com.lyft.networking.apiObjects.RideRequest;
import com.lyft.networking.apiObjects.RideRequestResponse;
import com.lyft.networking.apiObjects.UserRideHistoryResponse;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Retrofit API interface for requests made on behalf of the user on the Lyft Platform.
 * Important note: every method in this interface requires the call to be made with an
 * actual oAuth access_token for the user. Your application would have needed to go through
 * the 3-legged oAuth process flow to acquire the access_token from the user. With the
 * access token in hand you can call the {@link ApiConfig#setUserAccessToken(String)} method
 * to set the user's access token prior to making any calls from a Retrofit Api class built
 * from this interface. For more information on how to acquire the user access token, and the
 * 3-legged oAuth process flow, see {@link "https://developer.lyft.com/v1/docs/authentication"}
 */

public interface LyftUserApiRx
{
    /**
     * Request a ride on behalf of the user. The user's payment credentials
     * on file will be charged for the ride.
     * @param rideRequest Details about the ride being requested
     * @return Retrofit Call of {@link RideRequestResponse} type.
     */
    @POST("/v1/rides")
    Observable<RideRequestResponse> requestRide(@Body RideRequest rideRequest);

    /***
     * Cancels a specified ride on behalf of a user.
     * @param ride_id   The Id of the ride to cancel
     * @return Retrofit Call of {@link ResponseBody} type. In some cases, there may be a
     * cancellation fee — which the user must pay — in order to cancel the ride. If there
     * is a cancellation fee the call to this method will result in an HTTP-400 response with
     * the response body containing error description meta data (in the form of JSON). The returned
     * error meta data will include a token, which is valid for token_duration seconds.
     *
     * Using this token, it is your application's responsibility to confirm the user's acceptance
     * of the cancellation fee. Once the user has accepted the fee, your application should repeat
     * the request with the cancel_confirmation_token by using the method call {@link LyftUserApiRx#cancelRide(String, String)}
     *
     */
    @POST("/v1/rides/{ride_id}/cancel")
    Observable<ResponseBody> cancelRide(@Path("ride_id") String ride_id);

    /***
     * Cancels a specified ride on behalf of a user (with the use of a
     * cancellation confirmation token). For more information about cancellation
     * confirmation tokens see {@link LyftUserApiRx#cancelRide(String)} and {@link "https://developer.lyft.com/reference#ride-request-cancel"}
     * @param ride_id                   The Id of the ride to cancel
     * @param cancel_confirmation_token Cancellation confirmation token
     * @return Retrofit Call of {@link ResponseBody} type.
     */
    @POST("/v1/rides/{ride_id}/cancel")
    Observable<ResponseBody> cancelRide(@Path("ride_id") String ride_id, @Body String cancel_confirmation_token);

    /***
     * Returns the estimated time in seconds it will take for the nearest driver to reach the
     * specified location
     * @param lat   Pick up location latitude
     * @param lng   Pick up location longitude
     * @return Retrofit Call of {@link EtaEstimateResponse} type.
     */
    @GET("/v1/eta")
    Observable<EtaEstimateResponse> getEtas(@Query("lat") double lat, @Query("lng") double lng);

    /***
     * Returns the estimated time in seconds it will take for the nearest driver to reach the
     * specified location
     * @param lat               Pick up location latitude
     * @param lng               Pick up location longitude
     * @param ride_type         The id of the desired ride type that driver eta is
     *                          being requested for.
     * @param destination_lat   Destination location latitude
     * @param destination_lng   Destination location longitude
     * @return Retrofit Call of {@link EtaEstimateResponse} type.
     */
    @GET("/v1/eta")
    Observable<EtaEstimateResponse> getEtas(@Query("lat") double lat, @Query("lng") double lng,
                                         @Query("ride_type") String ride_type, @Query
                                                 ("destination_lat") double destination_lat,
                                         @Query("destination_lng") String destination_lng);

    /***
     * Returns the estimated time in seconds it will take for the nearest driver to reach the
     * specified location
     * @param lat       Pick up location latitude
     * @param lng       Pick up location longitude
     * @param ride_type The id of the desired ride type that driver eta is being requested for.
     * @return Retrofit Call of {@link EtaEstimateResponse} type.
     */
    @GET("/v1/eta")
    Observable<EtaEstimateResponse> getEtas(@Query("lat") double lat, @Query("lng") double lng, @Query("ride_type") String ride_type);

    /***
     * Returns a list of current and past rides for a given, authenticated passenger. If there's
     * a ride in progress, location will have the ride's current location.
     * This method overload will return no more than 10 rides from the user's ride history.
     * If more than 10 rides are required, please use {@link LyftUserApiRx#getUserRideHistory(String, int)}
     * @param start_time The ISO8601 start time to filter rides.
     *                   The earliest supported date is 2015-01-01T00:00:00Z
     * @return Retrofit Call of {@link UserRideHistoryResponse} type.
     */
    @GET("/v1/rides")
    Observable<UserRideHistoryResponse> getUserRideHistory(@Query("start_time") String start_time);

    /***
     * Returns a list of current and past rides for a given, authenticated passenger. If there's
     * a ride in progress, location will have the ride's current location.
     * @param start_time    The ISO8601 start time to filter rides.
     *                      The earliest supported date is 2015-01-01T00:00:00Z
     * @param limit         The Maximum number of rides to return. The maximum possible value is 50
     * @return Retrofit Call of {@link UserRideHistoryResponse} type.
     */
    @GET("/v1/rides")
    Observable<UserRideHistoryResponse> getUserRideHistory(@Query("start_time") String start_time, @Query("limit") int limit);

    /***
     * Returns a list of current and past rides for a given, authenticated passenger. If there's
     * a ride in progress, location will have the ride's current location.
     * This method overload will return no more than 10 rides from the user's ride history.
     * If more than 10 rides are required, please use {@link LyftUserApiRx#getUserRideHistory(String, String, int)}
     * @param start_time    The ISO8601 start time to filter rides.
     *                      The earliest supported date is 2015-01-01T00:00:00Z
     * @param end_time      The ISO8601 end time to filter rides.
     *                      The earliest supported date is 2015-01-01T00:00:00Z
     * @return Retrofit Call of {@link UserRideHistoryResponse} type.
     */
    @GET("/v1/rides")
    Observable<UserRideHistoryResponse> getUserRideHistory(@Query("start_time") String start_time, @Query("end_time") String end_time);

    /***
     * Returns a list of current and past rides for a given, authenticated passenger. If there's
     * a ride in progress, location will have the ride's current location.
     * @param start_time    The ISO8601 start time to filter rides.
     *                      The earliest supported date is 2015-01-01T00:00:00Z
     * @param end_time      The ISO8601 end time to filter rides.
     *                      The earliest supported date is 2015-01-01T00:00:00Z
     * @param limit         The Maximum number of rides to return. The maximum possible value is 50
     * @return Retrofit Call of {@link UserRideHistoryResponse} type.
     */
    @GET("/v1/rides")
    Observable<UserRideHistoryResponse> getUserRideHistory(@Query("start_time") String start_time, @Query("end_time") String end_time, @Query("limit") int limit);

}
