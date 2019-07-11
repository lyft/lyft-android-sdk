package com.lyft.deeplink;

public enum RideTypeEnum {
    SHARED("lyft_line", "Shared"),
    STANDARD("lyft", "Lyft"),
    XL("lyft_plus", "Lyft XL"),
    LUX("lyft_premier", "Lux"),
    LUX_BLACK("lyft_lux", "Lux Black"),
    LUX_BLACK_XL("lyft_luxsuv", "Lux Black XL");

    private String rideTypeKey;
    private String displayName;

    RideTypeEnum(String rideTypeKey, String displayName) {
        this.rideTypeKey = rideTypeKey;
        this.displayName = displayName;
    }

    public String getRideTypeKey() {
        return rideTypeKey;
    }

    public String getDisplayName() {
        return displayName;
    }
}

