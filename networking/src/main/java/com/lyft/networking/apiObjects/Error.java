package com.lyft.networking.apiObjects;

import com.google.gson.annotations.SerializedName;
import java.util.List;

/**
 * Details about why a request failed, such as missing or invalid parameters
 **/
public class Error {

    @SerializedName("error")
    public final String error;

    @SerializedName("error_detail")
    public final List<ErrorDetail> error_detail;

    @SerializedName("error_description")
    public final String error_description;

    public Error(String error, List<ErrorDetail> error_detail, String error_description) {
        this.error = error;
        this.error_detail = error_detail;
        this.error_description = error_description;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class Error {\n");

        sb.append("  error: ").append(error).append("\n");
        sb.append("  error_detail: ").append(error_detail).append("\n");
        sb.append("  error_description: ").append(error_description).append("\n");
        sb.append("}\n");
        return sb.toString();
    }
}
