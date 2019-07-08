package com.lyft.networking.apiObjects;

import com.google.gson.annotations.SerializedName;
import com.lyft.networking.apiObjects.internal.Validatable;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class CostEstimateResponse implements Validatable {

    @NotNull
    @SerializedName("cost_estimates")
    public final List<CostEstimate> cost_estimates;

    public CostEstimateResponse(List<CostEstimate> cost_estimates) {
        this.cost_estimates = cost_estimates;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class CostEstimateResponse {\n");

        sb.append("  cost_estimates: ").append(cost_estimates).append("\n");
        sb.append("}\n");
        return sb.toString();
    }

    @Override
    public boolean isValid() {
        if (cost_estimates == null) {
            return false;
        }

        for (CostEstimate costEstimate : cost_estimates) {
            if (!costEstimate.isValid()) {
                return false;
            }
        }

        return true;
    }
}
