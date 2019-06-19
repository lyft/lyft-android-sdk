package com.lyft.deeplink;

public enum RideType {
    SHARED("lyft_line"),
    STANDARD("lyft"),
    XL("lyft_plus"),
    LUX("lyft_premier"),
    LUX_BLACK("lyft_lux"),
    LUX_BLACK_XL("lyft_lux_suv");

    private String rideTypeKey;

    RideType(String rideTypeKey) {
        this.rideTypeKey = rideTypeKey;
    }

    public String getRideTypeKey() {
        return rideTypeKey;
    }
}

