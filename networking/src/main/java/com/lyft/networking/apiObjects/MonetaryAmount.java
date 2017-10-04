package com.lyft.networking.apiObjects;

import com.google.gson.annotations.SerializedName;

/**
 * Model representing monetary amount and currency
 */

public class MonetaryAmount
{
    /**
     * The monetary amount
     */
    @SerializedName("amount")
    public int amount;

    /**
     * The ISO 4217 currency code for the amount (e.g. USD).
     */
    @SerializedName("currency")
    public String currency;
}
