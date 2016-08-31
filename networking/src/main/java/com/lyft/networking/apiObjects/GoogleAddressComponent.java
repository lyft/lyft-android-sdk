package com.lyft.networking.apiObjects;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class GoogleAddressComponent {

    @SerializedName("long_name")
    public final String longName;

    @SerializedName("short_name")
    public final String shortName;

    @SerializedName("types")
    public final List<String> types;

    public GoogleAddressComponent(String longName, String shortName, List<String> types) {
        this.longName = longName;
        this.shortName = shortName;
        this.types = types;
    }

    @Override
    public String toString() {
        return "GoogleAddressComponent{" +
                "longName='" + longName + '\'' +
                ", shortName='" + shortName + '\'' +
                ", types=" + types +
                '}';
    }
}
