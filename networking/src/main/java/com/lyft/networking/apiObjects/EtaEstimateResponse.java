package com.lyft.networking.apiObjects;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class EtaEstimateResponse {

    @SerializedName("eta_estimates")
    public final List<Eta> eta_estimates;

    public EtaEstimateResponse(List<Eta> eta_estimates) {
        this.eta_estimates = eta_estimates;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class EtaEstimateResponse {\n");

        sb.append("  eta_estimates: ").append(eta_estimates).append("\n");
        sb.append("}\n");
        return sb.toString();
    }
}
