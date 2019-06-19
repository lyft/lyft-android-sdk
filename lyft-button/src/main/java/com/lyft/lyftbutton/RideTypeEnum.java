package com.lyft.lyftbutton;

public enum RideTypeEnum {
    SHARED("lyft_line"),
    STANDARD("lyft"),
    XL("lyft_plus"),
    ALL(null);

    private final String name;

    RideTypeEnum(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }

    public String getDisplayName() {
        if (this == SHARED) {
            return "Lyft Shared";
        } else if (this == STANDARD) {
            return "Lyft";
        } else if (this == XL) {
            return "Lyft XL";
        }
        return "";
    }
}
