package com.lyft.networking.apiObjects;

import com.google.gson.annotations.SerializedName;

/**
 * Extension of the LyftUser model with
 * phone number meta data
 */

public class LyftDriver extends LyftUser
{
    /**
     * Driver phone number
     */
    @SerializedName("phone_number")
    public String phone_number;
}
