package com.lyft.networking.apiObjects;

import com.google.gson.annotations.SerializedName;
import com.lyft.networking.apiObjects.internal.Validatable;
import org.jetbrains.annotations.NotNull;

/**
 * Created by awaht on 10/4/2017.
 */

public class RideCancellation extends MonetaryAmount implements Validatable {
    /**
     * Token used to confirm the fee when cancelling a request.
     */
    @NotNull
    @SerializedName("token")
    public String token;

    /**
     * How long, in seconds, before the token expires.
     */
    @SerializedName("token_duration")
    public int token_duration;

    @Override
    public boolean isValid() {
        return token != null && super.isValid();
    }
}
