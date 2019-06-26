package com.lyft.networking.apiObjects;

import com.google.gson.annotations.SerializedName;
import com.lyft.networking.apiObjects.internal.ICompleteData;
import org.jetbrains.annotations.Nullable;

/**
 * Model containing information about a Lyft Location.
 * The model can be used to represent the destination or origin
 * location of rides on the Lyft Platform.
 */

public class LyftLocation extends LatLng implements ICompleteData {
    /**
     * Display address at/near the given location.
     */
    @Nullable
    @SerializedName("address")
    public String address = null;

    public LyftLocation() {
        this(0, 0);
    }

    public LyftLocation(double lat, double lng) {
        this(lat, lng, null);
    }

    public LyftLocation(double lat, double lng, String address) {
        super(lat, lng);
        this.address = address;
    }


}
