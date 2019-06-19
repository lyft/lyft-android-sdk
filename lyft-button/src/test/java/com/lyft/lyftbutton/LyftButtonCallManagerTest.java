package com.lyft.lyftbutton;

import com.lyft.networking.apiObjects.CostEstimate;
import com.lyft.networking.apiObjects.CostEstimateResponse;
import com.lyft.networking.apiObjects.Eta;
import com.lyft.networking.apiObjects.EtaEstimateResponse;
import com.lyft.networking.apiObjects.GoogleLatLng;
import com.lyft.networking.apis.LyftPublicApi;
import com.lyft.networking.apis.internal.GoogleApi;
import com.lyft.testutils.MockGoogleApi;
import com.lyft.testutils.MockLyftPublicApi;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import retrofit2.Retrofit;
import retrofit2.mock.BehaviorDelegate;
import retrofit2.mock.MockRetrofit;
import retrofit2.mock.NetworkBehavior;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyDouble;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;

public class LyftButtonCallManagerTest {

    private static final Double PICKUP_LAT = 1.0;
    private static final Double PICKUP_LNG = 2.0;
    private static final Double DROPOFF_LAT = 3.0;
    private static final Double DROPOFF_LNG = 4.0;
    private static final String PICKUP_ADDR = "185 Berry St, San Francisco, CA 94107";
    private static final String DROPOFF_ADDR = "2300 Harrison St, San Francisco, CA 94110";
    private static final long TIMEOUT_MS = 100;

    private final ArgumentCaptor<Eta> etaArgumentCaptor = ArgumentCaptor.forClass(Eta.class);
    private final ArgumentCaptor<CostEstimate> costEstimateArgumentCaptor = ArgumentCaptor.forClass(CostEstimate.class);
    private LyftButtonCallManager callManager;
    private MockLyftPublicApi lyftPublicApi;
    private MockGoogleApi googleApi;

    @Mock
    LyftButton.ResultLoadedCallback mockCallback;

    @Before
    public void setup() {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(LyftPublicApi.API_ROOT)
                .build();

        NetworkBehavior behavior = NetworkBehavior.create();
        behavior.setDelay(1, TimeUnit.MILLISECONDS);
        behavior.setVariancePercent(0);
        behavior.setFailurePercent(0);
        MockRetrofit mockRetrofit = new MockRetrofit.Builder(retrofit)
                .networkBehavior(behavior)
                .build();
        BehaviorDelegate<LyftPublicApi> lyftApidelegate = mockRetrofit.create(LyftPublicApi.class);
        lyftPublicApi = spy(new MockLyftPublicApi(lyftApidelegate));

        BehaviorDelegate<GoogleApi> googleApiDelegate = mockRetrofit.create(GoogleApi.class);
        googleApi = spy(new MockGoogleApi(googleApiDelegate));
        HashMap<String, GoogleLatLng> addressToLatLngMap = new HashMap<>();
        addressToLatLngMap.put(PICKUP_ADDR, new GoogleLatLng(PICKUP_LAT, PICKUP_LNG));
        addressToLatLngMap.put(DROPOFF_ADDR, new GoogleLatLng(DROPOFF_LAT, DROPOFF_LNG));
        googleApi.setAddressToLatLngMap(addressToLatLngMap);

        mockCallback = mock(LyftButton.ResultLoadedCallback.class);
        callManager = new LyftButtonCallManager("clientId", lyftPublicApi, googleApi, new MockExecutorService());
    }

    @Test
    public void loadTest_dontLoadAnythingWithEmptyRideParams() {
        RideParams rideParams = new RideParams.Builder()
                .build();

        callManager.setRideParams(rideParams);
        callManager.load(mockCallback);

        verify(lyftPublicApi, never()).getEtas(anyDouble(), anyDouble(), anyString());
        verify(lyftPublicApi, never()).getCosts(anyDouble(), anyDouble(), anyString(), anyDouble(), anyDouble());
        verifyZeroInteractions(mockCallback);
    }

    @Test
    public void loadTest_dontLoadAnythingWithoutPickup() {
        RideParams rideParams = new RideParams.Builder()
                .setDropoffLocation(DROPOFF_LAT, DROPOFF_LNG)
                .build();

        callManager.setRideParams(rideParams);
        callManager.load(mockCallback);

        verify(lyftPublicApi, never()).getEtas(anyDouble(), anyDouble(), anyString());
        verify(lyftPublicApi, never()).getCosts(anyDouble(), anyDouble(), anyString(), anyDouble(), anyDouble());
        verifyZeroInteractions(mockCallback);
    }

    @Test
    public void loadTest_onlyLoadEtaWithPickup() throws InterruptedException {
        RideParams rideParams = new RideParams.Builder()
                .setPickupLocation(PICKUP_LAT, PICKUP_LNG)
                .build();

        callManager.setRideParams(rideParams);

        CountDownLatch countDownLatch = new CountDownLatch(1);
        LyftButton.ResultLoadedCallback callback = spy(new Callback(countDownLatch, true, false, false));
        callManager.load(callback);

        verify(lyftPublicApi).getEtas(PICKUP_LAT, PICKUP_LNG, null);
        verify(lyftPublicApi, never()).getCosts(anyDouble(), anyDouble(), anyString(), anyDouble(), anyDouble());

        assertTrue(countDownLatch.await(TIMEOUT_MS, TimeUnit.MILLISECONDS));
        verify(callback).onSuccess(etaArgumentCaptor.capture());
        assertEquals(RideTypeEnum.STANDARD.toString(), etaArgumentCaptor.getValue().ride_type);
    }

    @Test
    public void loadTest_loadEtaAndCostWithPickupAndDropoff() throws InterruptedException {
        RideParams rideParams = new RideParams.Builder()
                .setPickupLocation(PICKUP_LAT, PICKUP_LNG)
                .setDropoffLocation(DROPOFF_LAT, DROPOFF_LNG)
                .build();

        etaAndCostSuccessConditions(rideParams);
    }

    @Test
    public void loadTest_loadEtaAndCostForLyftLine() throws InterruptedException {
        RideParams rideParams = new RideParams.Builder()
                .setRideTypeEnum(RideTypeEnum.SHARED)
                .setPickupLocation(PICKUP_LAT, PICKUP_LNG)
                .setDropoffLocation(DROPOFF_LAT, DROPOFF_LNG)
                .build();

        etaAndCostSuccessConditions(rideParams);
    }

    @Test
    public void loadTest_loadEtaAndCostForLyftPlus() throws InterruptedException {
        RideParams rideParams = new RideParams.Builder()
                .setRideTypeEnum(RideTypeEnum.XL)
                .setPickupLocation(PICKUP_LAT, PICKUP_LNG)
                .setDropoffLocation(DROPOFF_LAT, DROPOFF_LNG)
                .build();

        etaAndCostSuccessConditions(rideParams);
    }

    @Test
    public void loadTest_loadEtaAndCostWithPickupAndDropoffAddress() throws InterruptedException {
        RideParams rideParams = new RideParams.Builder()
                .setPickupAddress(PICKUP_ADDR)
                .setDropoffAddress(DROPOFF_ADDR)
                .build();

        etaAndCostSuccessConditions(rideParams);
    }

    @Test
    public void loadTest_loadEtaAndCostWithPickupLatLngAndDropoffAddress() throws InterruptedException {
        RideParams rideParams = new RideParams.Builder()
                .setPickupLocation(PICKUP_LAT, PICKUP_LNG)
                .setDropoffAddress(DROPOFF_ADDR)
                .build();

        etaAndCostSuccessConditions(rideParams);
    }

    @Test
    public void loadTest_loadEtaAndCostWithPickupAddressAndDropoffLatLng() throws InterruptedException {
        RideParams rideParams = new RideParams.Builder()
                .setPickupAddress(PICKUP_ADDR)
                .setDropoffLocation(DROPOFF_LAT, DROPOFF_LNG)
                .build();

        etaAndCostSuccessConditions(rideParams);
    }

    private void etaAndCostSuccessConditions(RideParams rideParams) throws InterruptedException {
        callManager.setRideParams(rideParams);

        CountDownLatch countDownLatch = new CountDownLatch(2);
        LyftButton.ResultLoadedCallback callback = spy(new Callback(countDownLatch, true, true, false));
        callManager.load(callback);

        verify(lyftPublicApi).getEtas(PICKUP_LAT, PICKUP_LNG, null);
        verify(lyftPublicApi).getCosts(PICKUP_LAT, PICKUP_LNG, null, DROPOFF_LAT, DROPOFF_LNG);

        assertTrue(countDownLatch.await(TIMEOUT_MS, TimeUnit.MILLISECONDS));
        verify(callback).onSuccess(etaArgumentCaptor.capture());
        assertEquals(rideParams.getRideTypeEnum().toString(), etaArgumentCaptor.getValue().ride_type);

        verify(callback).onSuccess(costEstimateArgumentCaptor.capture());
        assertEquals(rideParams.getRideTypeEnum().toString(), costEstimateArgumentCaptor.getValue().ride_type);
    }

    @Test
    public void loadTest_loadEtaWithFailedForwardGeocode() throws InterruptedException {
        RideParams rideParams = new RideParams.Builder()
                .setPickupLocation(PICKUP_LAT, PICKUP_LNG)
                .setDropoffAddress(DROPOFF_ADDR)
                .build();

        callManager.setRideParams(rideParams);

        googleApi.forceNullResponse(true);

        CountDownLatch countDownLatch = new CountDownLatch(1);
        LyftButton.ResultLoadedCallback callback = spy(new Callback(countDownLatch, true, false, false));
        callManager.load(callback);

        verify(lyftPublicApi).getEtas(PICKUP_LAT, PICKUP_LNG, null);
        verify(lyftPublicApi, never()).getCosts(anyDouble(), anyDouble(), anyString(), anyDouble(), anyDouble());

        assertTrue(countDownLatch.await(TIMEOUT_MS, TimeUnit.MILLISECONDS));
        verify(callback).onSuccess(etaArgumentCaptor.capture());
        assertEquals(RideTypeEnum.STANDARD.toString(), etaArgumentCaptor.getValue().ride_type);
    }

    @Test
    public void loadTest_etaFailureNullResponse() throws InterruptedException {
        RideParams rideParams = new RideParams.Builder()
                .setRideTypeEnum(RideTypeEnum.XL)
                .setPickupLocation(PICKUP_LAT, PICKUP_LNG)
                .setDropoffLocation(DROPOFF_LAT, DROPOFF_LNG)
                .build();
        callManager.setRideParams(rideParams);

        lyftPublicApi.setCustomEtaEstimateResponse(null);

        CountDownLatch countDownLatch = new CountDownLatch(2);
        LyftButton.ResultLoadedCallback callback = spy(new Callback(countDownLatch, false, true, true));
        callManager.load(callback);

        assertTrue(countDownLatch.await(TIMEOUT_MS, TimeUnit.MILLISECONDS));
        verify(callback).onSuccess(any(CostEstimate.class));
        verify(callback).onFailure(any(Throwable.class));
    }

    @Test
    public void loadTest_etaFailureNullList() throws InterruptedException {
        RideParams rideParams = new RideParams.Builder()
                .setRideTypeEnum(RideTypeEnum.XL)
                .setPickupLocation(PICKUP_LAT, PICKUP_LNG)
                .setDropoffLocation(DROPOFF_LAT, DROPOFF_LNG)
                .build();
        callManager.setRideParams(rideParams);

        lyftPublicApi.setCustomEtaEstimateResponse(new EtaEstimateResponse(null));
        CountDownLatch countDownLatch = new CountDownLatch(2);
        LyftButton.ResultLoadedCallback callback = spy(new Callback(countDownLatch, false, true, true));
        callManager.load(callback);

        assertTrue(countDownLatch.await(TIMEOUT_MS, TimeUnit.MILLISECONDS));
        verify(callback).onSuccess(any(CostEstimate.class));
        verify(callback).onFailure(any(Throwable.class));
    }

    @Test
    public void loadTest_etaFailureEmptyList() throws InterruptedException {
        RideParams rideParams = new RideParams.Builder()
                .setRideTypeEnum(RideTypeEnum.XL)
                .setPickupLocation(PICKUP_LAT, PICKUP_LNG)
                .setDropoffLocation(DROPOFF_LAT, DROPOFF_LNG)
                .build();
        callManager.setRideParams(rideParams);

        lyftPublicApi.setCustomEtaEstimateResponse(new EtaEstimateResponse(new ArrayList<Eta>()));

        CountDownLatch countDownLatch = new CountDownLatch(2);
        LyftButton.ResultLoadedCallback callback = spy(new Callback(countDownLatch, false, true, true));
        callManager.load(callback);

        assertTrue(countDownLatch.await(TIMEOUT_MS, TimeUnit.MILLISECONDS));
        verify(callback).onSuccess(any(CostEstimate.class));
        verify(callback).onFailure(any(Throwable.class));
    }

    @Test
    public void loadTest_etaFailureNullEtaSeconds() throws InterruptedException {
        RideParams rideParams = new RideParams.Builder()
                .setRideTypeEnum(RideTypeEnum.XL)
                .setPickupLocation(PICKUP_LAT, PICKUP_LNG)
                .setDropoffLocation(DROPOFF_LAT, DROPOFF_LNG)
                .build();
        callManager.setRideParams(rideParams);

        List<Eta> etas = new ArrayList<>();
        Eta eta = new Eta(RideTypeEnum.XL.toString(), RideTypeEnum.XL.getDisplayName(), null);
        etas.add(eta);
        lyftPublicApi.setCustomEtaEstimateResponse(new EtaEstimateResponse(etas));

        CountDownLatch countDownLatch = new CountDownLatch(2);
        LyftButton.ResultLoadedCallback callback = spy(new Callback(countDownLatch, false, true, true));
        callManager.load(callback);

        assertTrue(countDownLatch.await(TIMEOUT_MS, TimeUnit.MILLISECONDS));
        verify(callback).onSuccess(any(CostEstimate.class));
        verify(callback).onFailure(any(Throwable.class));
    }

    @Test
    public void loadTest_costFailureNullResponse() throws InterruptedException {
        RideParams rideParams = new RideParams.Builder()
                .setRideTypeEnum(RideTypeEnum.XL)
                .setPickupLocation(PICKUP_LAT, PICKUP_LNG)
                .setDropoffLocation(DROPOFF_LAT, DROPOFF_LNG)
                .build();
        callManager.setRideParams(rideParams);

        lyftPublicApi.setCustomCostEstimateResponse(null);

        CountDownLatch countDownLatch = new CountDownLatch(2);
        LyftButton.ResultLoadedCallback callback = spy(new Callback(countDownLatch, true, false, true));
        callManager.load(callback);

        assertTrue(countDownLatch.await(TIMEOUT_MS, TimeUnit.MILLISECONDS));
        verify(callback).onSuccess(any(Eta.class));
        verify(callback).onFailure(any(Throwable.class));
    }

    @Test
    public void loadTest_costFailureNullList() throws InterruptedException {
        RideParams rideParams = new RideParams.Builder()
                .setRideTypeEnum(RideTypeEnum.XL)
                .setPickupLocation(PICKUP_LAT, PICKUP_LNG)
                .setDropoffLocation(DROPOFF_LAT, DROPOFF_LNG)
                .build();
        callManager.setRideParams(rideParams);

        lyftPublicApi.setCustomCostEstimateResponse(new CostEstimateResponse(null));

        CountDownLatch countDownLatch = new CountDownLatch(2);
        LyftButton.ResultLoadedCallback callback = spy(new Callback(countDownLatch, true, false, true));
        callManager.load(callback);

        assertTrue(countDownLatch.await(TIMEOUT_MS, TimeUnit.MILLISECONDS));
        verify(callback).onSuccess(any(Eta.class));
        verify(callback).onFailure(any(Throwable.class));
    }

    @Test
    public void loadTest_costFailureEmptyList() throws InterruptedException {
        RideParams rideParams = new RideParams.Builder()
                .setRideTypeEnum(RideTypeEnum.XL)
                .setPickupLocation(PICKUP_LAT, PICKUP_LNG)
                .setDropoffLocation(DROPOFF_LAT, DROPOFF_LNG)
                .build();
        callManager.setRideParams(rideParams);

        lyftPublicApi.setCustomCostEstimateResponse(new CostEstimateResponse(new ArrayList<CostEstimate>()));

        CountDownLatch countDownLatch = new CountDownLatch(2);
        LyftButton.ResultLoadedCallback callback = spy(new Callback(countDownLatch, true, false, true));
        callManager.load(callback);

        assertTrue(countDownLatch.await(TIMEOUT_MS, TimeUnit.MILLISECONDS));
        verify(callback).onSuccess(any(Eta.class));
        verify(callback).onFailure(any(Throwable.class));
    }

    private static class Callback implements LyftButton.ResultLoadedCallback {

        private final CountDownLatch countDownLatch;
        private final boolean etaSuccess;
        private final boolean costSuccess;
        private final boolean failure;

        Callback(CountDownLatch countDownLatch, boolean etaSuccess, boolean costSuccess, boolean failure) {
            this.countDownLatch = countDownLatch;
            this.etaSuccess = etaSuccess;
            this.costSuccess = costSuccess;
            this.failure = failure;
        }

        @Override
        public void onSuccess(Eta eta) {
            if (etaSuccess) {
                countDownLatch.countDown();
            }
        }

        @Override
        public void onSuccess(CostEstimate costEstimate) {
            if (costSuccess) {
                countDownLatch.countDown();
            }
        }

        @Override
        public void onFailure(Throwable t) {
            if (failure) {
                countDownLatch.countDown();
            }
        }
    }
}
