package com.lyft.deeplink;

import org.junit.Test;

import static junit.framework.Assert.assertEquals;

public class DeepLinkTest {

    private static final String CLIENT_ID = "clientId";
    private static final String RIDETYPE = "ridetype";
    private static final Double PICKUP_LAT = 1.0;
    private static final Double PICKUP_LNG = 2.0;
    private static final Double DROPOFF_LAT = 3.0;
    private static final Double DROPOFF_LNG = 4.0;
    private static final String PICKUP_ADDR = "185 Berry Street, San Francisco, CA";
    private static final String DROPOFF_ADDR = "2300 Harrison Street, San Francisco, CA";

    @Test
    public void createDeepLinkStringTest_full() {
        DeepLinkParams deepLinkParams = new DeepLinkParams.Builder()
                .setClientId(CLIENT_ID)
                .setRideType(RIDETYPE)
                .setPickupLocation(PICKUP_LAT, PICKUP_LNG)
                .setDropoffLocation(DROPOFF_LAT, DROPOFF_LNG)
                .setPickupAddress(PICKUP_ADDR)
                .setDropoffAddress(DROPOFF_ADDR)
                .build();

        String deeplinkString = DeepLink.createDeepLinkString(deepLinkParams);
        assertEquals("lyft://ridetype?id=" + RIDETYPE + "&pickup[latitude]=" + PICKUP_LAT + "&pickup[longitude]=" +
                PICKUP_LNG + "&pickup[address]=" + PICKUP_ADDR + "&destination[latitude]=" + DROPOFF_LAT +
                "&destination[longitude]=" + DROPOFF_LNG + "&destination[address]=" + DROPOFF_ADDR + "&clientId=" + CLIENT_ID,
                deeplinkString);
    }

    @Test
    public void createDeepLinkStringTest_noClientId() {
        DeepLinkParams deepLinkParams = new DeepLinkParams.Builder()
                .setRideType(RIDETYPE)
                .setPickupLocation(PICKUP_LAT, PICKUP_LNG)
                .setDropoffLocation(DROPOFF_LAT, DROPOFF_LNG)
                .build();

        String deeplinkString = DeepLink.createDeepLinkString(deepLinkParams);
        assertEquals("lyft://ridetype?id=" + RIDETYPE + "&pickup[latitude]=" + PICKUP_LAT + "&pickup[longitude]=" + PICKUP_LNG
                        + "&destination[latitude]=" + DROPOFF_LAT + "&destination[longitude]=" + DROPOFF_LNG,
                deeplinkString);
    }

    @Test
    public void createDeepLinkStringTest_noPickupLocation() {
        DeepLinkParams deepLinkParams = new DeepLinkParams.Builder()
                .setRideType(RIDETYPE)
                .setPickupLocation(PICKUP_LAT, null)
                .setDropoffLocation(DROPOFF_LAT, DROPOFF_LNG)
                .build();

        String deeplinkString = DeepLink.createDeepLinkString(deepLinkParams);
        assertEquals("lyft://ridetype?id=" + RIDETYPE + "&destination[latitude]=" + DROPOFF_LAT + "&destination[longitude]=" + DROPOFF_LNG,
                deeplinkString);
    }

    @Test
    public void createDeepLinkStringTest_addressOnly() {
        DeepLinkParams deepLinkParams = new DeepLinkParams.Builder()
                .setClientId(CLIENT_ID)
                .setRideType(RIDETYPE)
                .setPickupAddress(PICKUP_ADDR)
                .setDropoffAddress(DROPOFF_ADDR)
                .build();

        String deeplinkString = DeepLink.createDeepLinkString(deepLinkParams);
        assertEquals("lyft://ridetype?id=" + RIDETYPE + "&pickup[address]=" + PICKUP_ADDR +
                "&destination[address]=" + DROPOFF_ADDR + "&clientId=" + CLIENT_ID,
                deeplinkString);
    }

    @Test
    public void createDeepLinkStringTest_noParams() {
        DeepLinkParams deepLinkParams = new DeepLinkParams.Builder()
                .build();

        String deeplinkString = DeepLink.createDeepLinkString(deepLinkParams);
        assertEquals("lyft://ridetype?id=lyft", deeplinkString);
    }
}
