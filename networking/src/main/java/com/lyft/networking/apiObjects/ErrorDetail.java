package com.lyft.networking.apiObjects;

import com.google.gson.annotations.SerializedName;

/**
 * An object with a single key-value pair, where the key is the name of the invalid parameter, and the value is a description of the error.
 **/
public class ErrorDetail {

    @SerializedName("field_name")
    public final String field_name;

    public ErrorDetail(String field_name) {
        this.field_name = field_name;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class ErrorDetail {\n");

        sb.append("  field_name: ").append(field_name).append("\n");
        sb.append("}\n");
        return sb.toString();
    }
}
