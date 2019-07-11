package com.lyft.deeplink;

public enum RideTypeEnum {
    SHARED("lyft_line", "Lyft Shared"),
    STANDARD("lyft", "Lyft"),
    XL("lyft_plus", "Lyft XL"),
    LUX("lyft_premier", "Lyft Lux"),
    LUX_BLACK("lyft_lux", "Lyft Lux Black"),
    LUX_BLACK_XL("lyft_luxsuv", "Lyft Lux Black XL");

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

