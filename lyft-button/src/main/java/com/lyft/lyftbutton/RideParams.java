package com.lyft.lyftbutton;

import com.lyft.deeplink.RideTypeEnum;
import org.jetbrains.annotations.Nullable;

public class RideParams {

    private final RideTypeEnum rideTypeEnum;
    private final String pickupAddr;
    private final Double pickupLat;
    private final Double pickupLng;
    private final String dropoffAddr;
    private final Double dropoffLat;
    private final Double dropoffLng;
    private final String promoCode;

    private RideParams(RideTypeEnum rideTypeEnum, String pickupAddr, Double pickupLat, Double pickupLng, String dropoffAddr,
            Double dropoffLat, Double dropoffLng, String promoCode) {
        this.rideTypeEnum = rideTypeEnum;
        this.pickupAddr = pickupAddr;
        this.pickupLat = pickupLat;
        this.pickupLng = pickupLng;
        this.dropoffAddr = dropoffAddr;
        this.dropoffLat = dropoffLat;
        this.dropoffLng = dropoffLng;
        this.promoCode = promoCode;
    }

    public RideTypeEnum getRideTypeEnum() {
        return rideTypeEnum;
    }

    @Nullable
    public String getPickupAddr() {
        return pickupAddr;
    }

    @Nullable
    public Double getPickupLat() {
        return pickupLat;
    }

    @Nullable
    public Double getPickupLng() {
        return pickupLng;
    }

    @Nullable
    public String getDropoffAddr() {
        return dropoffAddr;
    }

    @Nullable
    public Double getDropoffLat() {
        return dropoffLat;
    }

    @Nullable
    public Double getDropoffLng() {
        return dropoffLng;
    }

    @Nullable
    public String getPromoCode() {
        return promoCode;
    }

    public boolean isPickupLatLngSet() {
        return pickupLat != null && pickupLng != null;
    }

    public boolean isDropoffLatLngSet() {
        return dropoffLat != null && dropoffLng != null;
    }

    boolean shouldForwardGeocodePickup() {
        return pickupAddr != null && (pickupLat == null || pickupLng == null);
    }

    boolean shouldForwardGeocodeDropoff() {
        return dropoffAddr != null && (dropoffLat == null || dropoffLng == null);
    }

    public Builder newBuilder() {
        return new Builder(this);
    }

    public static class Builder {

        private RideTypeEnum rideTypeEnum = RideTypeEnum.STANDARD;
        private String pickupAddr;
        private Double pickupLat;
        private Double pickupLng;
        private String dropoffAddr;
        private Double dropoffLat;
        private Double dropoffLng;
        private String promoCode;

        public RideParams build() {
            return new RideParams(rideTypeEnum, pickupAddr, pickupLat, pickupLng, dropoffAddr, dropoffLat, dropoffLng, promoCode);
        }

        public Builder() {}

        Builder(RideParams rideParams) {
            this.rideTypeEnum = rideParams.rideTypeEnum;
            this.pickupAddr = rideParams.pickupAddr;
            this.pickupLat = rideParams.pickupLat;
            this.pickupLng = rideParams.pickupLng;
            this.dropoffAddr = rideParams.dropoffAddr;
            this.dropoffLat = rideParams.dropoffLat;
            this.dropoffLng = rideParams.dropoffLng;
            this.promoCode = rideParams.promoCode;
        }

        public Builder setRideTypeEnum(RideTypeEnum rideTypeEnum) {
            this.rideTypeEnum = rideTypeEnum;
            return this;
        }

        public Builder setPickupAddress(String pickupAddr) {
            this.pickupAddr = pickupAddr;
            return this;
        }

        public Builder setDropoffAddress(String dropoffAddr) {
            this.dropoffAddr = dropoffAddr;
            return this;
        }

        public Builder setPickupLocation(double pickupLat, double pickupLng) {
            this.pickupLat = pickupLat;
            this.pickupLng = pickupLng;
            return this;
        }

        public Builder setDropoffLocation(double dropoffLat, double dropoffLng) {
            this.dropoffLat = dropoffLat;
            this.dropoffLng = dropoffLng;
            return this;
        }

        public Builder setPromoCode(String promoCode) {
            this.promoCode = promoCode;
            return this;
        }
    }
}
