package com.lyft.networking.apiObjects;

import com.google.gson.annotations.SerializedName;

/**
 * A Location model with eta meta data ssociated.
 */
public class EtaLocation extends LyftLocation
{
    /**
     * Estimated time for the driver to arrive at the location.
     * Available after ride status changes to accepted and until arrived.
     */
    @SerializedName("eta_seconds")
    public int eta_seconds;

}
