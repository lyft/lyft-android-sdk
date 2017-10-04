package com.lyft.networking.apiObjects;

import com.google.gson.annotations.SerializedName;

/**
 * Created by awaht on 10/4/2017.
 */

public class RideCancellation extends MonetaryAmount
{
    /**
     * Token used to confirm the fee when cancelling a request.
     */
    @SerializedName("token")
    public String token ;

    /**
     * How long, in seconds, before the token expires.
     */
    @SerializedName("token_duration")
    public int token_duration ;
}
