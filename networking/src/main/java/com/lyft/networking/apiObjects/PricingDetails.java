package com.lyft.networking.apiObjects;

import com.google.gson.annotations.SerializedName;
import com.lyft.networking.apiObjects.internal.Validatable;

import org.jetbrains.annotations.NotNull;

public class PricingDetails implements Validatable {

    @NotNull
    @SerializedName("base_charge")
    public final Integer base_charge;

    @NotNull
    @SerializedName("cancel_penalty_amount")
    public final Integer cancel_penalty_amount;

    @NotNull
    @SerializedName("cost_minimum")
    public final Integer cost_minimum;

    @NotNull
    @SerializedName("cost_per_mile")
    public final Integer cost_per_mile;

    @NotNull
    @SerializedName("cost_per_minute")
    public final Integer cost_per_minute;

    @NotNull
    @SerializedName("currency")
    public final String currency;

    @NotNull
    @SerializedName("trust_and_service")
    public final Integer trust_and_service;

    public PricingDetails(Integer base_charge, Integer cancel_penalty_amount, Integer cost_minimum, Integer cost_per_mile,
            Integer cost_per_minute, String currency, Integer trust_and_service) {
        this.base_charge = base_charge;
        this.cancel_penalty_amount = cancel_penalty_amount;
        this.cost_minimum = cost_minimum;
        this.cost_per_mile = cost_per_mile;
        this.cost_per_minute = cost_per_minute;
        this.currency = currency;
        this.trust_and_service = trust_and_service;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class PricingDetails {\n");

        sb.append("  base_charge: ").append(base_charge).append("\n");
        sb.append("  cancel_penalty_amount: ").append(cancel_penalty_amount).append("\n");
        sb.append("  cost_minimum: ").append(cost_minimum).append("\n");
        sb.append("  cost_per_mile: ").append(cost_per_mile).append("\n");
        sb.append("  cost_per_minute: ").append(cost_per_minute).append("\n");
        sb.append("  currency: ").append(currency).append("\n");
        sb.append("  trust_and_service: ").append(trust_and_service).append("\n");
        sb.append("}\n");
        return sb.toString();
    }

    @Override
    public boolean isValid() {
        return base_charge != null
                && cancel_penalty_amount != null
                && cost_minimum != null
                && cost_per_mile != null
                && cost_per_minute != null
                && currency != null
                && trust_and_service != null;
    }
}
