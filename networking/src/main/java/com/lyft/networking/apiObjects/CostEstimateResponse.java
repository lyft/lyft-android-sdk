package com.lyft.networking.apiObjects;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class CostEstimateResponse {

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
}
