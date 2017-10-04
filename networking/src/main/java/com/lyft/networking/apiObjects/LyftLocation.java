package com.lyft.networking.apiObjects;

import com.google.gson.annotations.SerializedName;

/**
 * Model containing information about a Lyft Location.
 * The model can be used to represent the destination or origin
 * location of rides on the Lyft Platform.
 */

public class LyftLocation extends LatLng
{
    /**
     * Display address at/near the given location.
     */
    @SerializedName("address")
    public String address = null;
    public LyftLocation(){ this(0, 0);}
    public LyftLocation(double lat, double lng) { this(lat, lng, null); }
    public LyftLocation(double lat, double lng, String address)
    {
        super(lat, lng);
        this.address = address;
    }

}
