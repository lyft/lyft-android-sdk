package com.lyft.lyftbutton;

public enum RideTypeEnum {
    LINE("lyft_line"),
    CLASSIC("lyft"),
    PLUS("lyft_plus"),
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
        if (this == LINE) {
            return "Lyft Line";
        } else if (this == CLASSIC) {
            return "Lyft";
        } else if (this == PLUS) {
            return "Lyft Plus";
        }
        return "";
    }
}
