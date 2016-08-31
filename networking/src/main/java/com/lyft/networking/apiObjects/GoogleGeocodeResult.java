package com.lyft.networking.apiObjects;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class GoogleGeocodeResult {

    @SerializedName("address_components")
    public final List<GoogleAddressComponent> addressComponents;

    @SerializedName("formatted_address")
    public final String formattedAddress;

    @SerializedName("geometry")
    public final GoogleGeometry geometry;

    @SerializedName("types")
    public final List<String> types;

    public GoogleGeocodeResult(List<GoogleAddressComponent> addressComponents, String formattedAddress, GoogleGeometry geometry,
            List<String> types) {
        this.addressComponents = addressComponents;
        this.formattedAddress = formattedAddress;
        this.geometry = geometry;
        this.types = types;
    }

    @Override
    public String toString() {
        return "GoogleGeocodeResult{" +
                "addressComponents=" + addressComponents +
                ", formattedAddress='" + formattedAddress + '\'' +
                ", geometry=" + geometry +
                ", types=" + types +
                '}';
    }
}
