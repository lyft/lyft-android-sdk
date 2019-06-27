package com.lyft.networking.apiObjects;

import com.google.gson.annotations.SerializedName;
import com.lyft.networking.apiObjects.internal.Validatable;
import org.jetbrains.annotations.NotNull;

/**
 * Model representing monetary amount and currency
 */

public class MonetaryAmount implements Validatable {
    /**
     * The monetary amount
     */
    @NotNull
    @SerializedName("amount")
    public int amount;

    /**
     * The ISO 4217 currency code for the amount (e.g. USD).
     */
    @NotNull
    @SerializedName("currency")
    public String currency;

    @Override
    public boolean isValid() {
        return currency != null;
    }
}
