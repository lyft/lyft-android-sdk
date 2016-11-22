package com.lyft.deeplink;

import org.jetbrains.annotations.Nullable;

public class DeepLinkParams {

    private final String clientId;
    private final String rideType;
    private final String couponCode;
    private final String pickupAddr;
    private final Double pickupLat;
    private final Double pickupLng;
    private final String dropoffAddr;
    private final Double dropoffLat;
    private final Double dropoffLng;

    private DeepLinkParams(String clientId, String rideType, String couponCode, String pickupAddr, Double pickupLat, Double pickupLng,
            String dropoffAddr, Double dropoffLat, Double dropoffLng) {
        this.clientId = clientId;
        this.rideType = rideType;
        this.couponCode = couponCode;
        this.pickupAddr = pickupAddr;
        this.pickupLat = pickupLat;
        this.pickupLng = pickupLng;
        this.dropoffAddr = dropoffAddr;
        this.dropoffLat = dropoffLat;
        this.dropoffLng = dropoffLng;
    }

    @Nullable
    public String getClientId() {
        return clientId;
    }

    @Nullable
    public String getRideType() {
        return rideType;
    }

    @Nullable
    public String getCouponCode() {
        return couponCode;
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

    public boolean isPickupLatLngSet() {
        return pickupLat != null && pickupLng != null;
    }

    public boolean isPickupAddressSet() {
        return pickupAddr != null && !pickupAddr.isEmpty();
    }

    public boolean isDropoffLatLngSet() {
        return dropoffLat != null && dropoffLng != null;
    }

    public boolean isDropoffAddressSet() {
        return dropoffAddr != null && !dropoffAddr.isEmpty();
    }

    public static class Builder {

        private String clientId;
        private String rideType = "lyft";
        private String couponCode;
        private String pickupAddr;
        private Double pickupLat;
        private Double pickupLng;
        private String dropoffAddr;
        private Double dropoffLat;
        private Double dropoffLng;

        public DeepLinkParams build() {
            return new DeepLinkParams(clientId, rideType, couponCode, pickupAddr, pickupLat, pickupLng, dropoffAddr, dropoffLat, dropoffLng);
        }

        public Builder setClientId(String clientId) {
            this.clientId = clientId;
            return this;
        }

        public Builder setRideType(String rideType) {
            this.rideType = rideType;
            return this;
        }

        public Builder setCouponCode(String couponCode) {
            this.couponCode = couponCode;
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

        public Builder setPickupLocation(Double pickupLat, Double pickupLng) {
            this.pickupLat = pickupLat;
            this.pickupLng = pickupLng;
            return this;
        }

        public Builder setDropoffLocation(Double dropoffLat, Double dropoffLng) {
            this.dropoffLat = dropoffLat;
            this.dropoffLng = dropoffLng;
            return this;
        }
    }
}
