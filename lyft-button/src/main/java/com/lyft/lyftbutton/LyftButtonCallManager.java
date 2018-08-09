package com.lyft.lyftbutton;

import android.content.Context;

import com.lyft.deeplink.DeepLink;
import com.lyft.deeplink.DeepLinkParams;
import com.lyft.networking.ApiConfig;
import com.lyft.networking.apiObjects.CostEstimate;
import com.lyft.networking.apiObjects.CostEstimateResponse;
import com.lyft.networking.apiObjects.Eta;
import com.lyft.networking.apiObjects.EtaEstimateResponse;
import com.lyft.networking.apiObjects.GoogleGeocodeResponse;
import com.lyft.networking.apiObjects.GoogleGeocodeResult;
import com.lyft.networking.apiObjects.GoogleLatLng;
import com.lyft.networking.apis.internal.GoogleApi;
import com.lyft.networking.apis.LyftPublicApi;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import org.jetbrains.annotations.Nullable;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

class LyftButtonCallManager {

    private final LyftPublicApi publicApi;
    private final String clientId;
    private final GoogleApi googleApi;
    private final Set<Call> callSet = Collections.newSetFromMap(new ConcurrentHashMap<Call, Boolean>());
    private final ExecutorService executorService;
    private RideParams rideParams;

    LyftButtonCallManager(String clientId, LyftPublicApi publicApi, GoogleApi googleApi, ExecutorService executorService) {
        this.clientId = clientId;
        this.publicApi = publicApi;
        this.googleApi = googleApi;
        this.executorService = executorService;
    }

    void setRideParams(RideParams rideParams) {
        this.rideParams = rideParams;
    }

    void load(final LyftButton.ResultLoadedCallback callback) {
        executorService.submit(new Runnable() {
            @Override
            public void run() {
                rideParams = refreshRideParams();

                if (rideParams.isPickupLatLngSet()) {
                    loadEta(callback);

                    if (rideParams.isDropoffLatLngSet()) {
                        loadCost(callback);
                    }
                }
            }
        });
    }

    void launchLyftApp(Context context) {
        DeepLinkParams deepLinkParams = new DeepLinkParams.Builder()
                .setClientId(clientId)
                .setRideType(rideParams.getRideTypeEnum().toString())
                .setPickupLocation(rideParams.getPickupLat(), rideParams.getPickupLng())
                .setDropoffLocation(rideParams.getDropoffLat(), rideParams.getDropoffLng())
                .setPromoCode(rideParams.getPromoCode())
                .build();

        DeepLink.launchLyftApp(context, deepLinkParams);
    }

    void clearCalls() {
        for (Call call : callSet) {
            call.cancel();
        }
        callSet.clear();
        executorService.shutdownNow();
    }

    private RideParams refreshRideParams() {
        try {
            RideParams.Builder builder = rideParams.newBuilder();

            Future<GoogleLatLng> futurePickup = null;
            if (rideParams.shouldForwardGeocodePickup()) {
                futurePickup = executorService.submit(createCallableForAddress(rideParams.getPickupAddr()));
            }

            Future<GoogleLatLng> futureDropoff = null;
            if (rideParams.shouldForwardGeocodeDropoff()) {
                futureDropoff = executorService.submit(createCallableForAddress(rideParams.getDropoffAddr()));
            }

            if (futurePickup != null && futurePickup.get() != null) {
                GoogleLatLng result = futurePickup.get();
                builder.setPickupLocation(result.lat, result.lng);
            }

            if (futureDropoff != null && futureDropoff.get() != null) {
                GoogleLatLng result = futureDropoff.get();
                builder.setDropoffLocation(result.lat, result.lng);
            }

            return builder.build();
        } catch (InterruptedException | ExecutionException e) {}
        return rideParams;
    }

    private Callable<GoogleLatLng> createCallableForAddress(final String address) {
        return new Callable<GoogleLatLng>() {
            @Override
            public GoogleLatLng call() {
                GoogleLatLng latLng = null;
                Call<GoogleGeocodeResponse> call = googleApi.forwardGeocode(address);
                callSet.add(call);
                try {
                    GoogleGeocodeResponse googleGeocodeResponse = call.execute().body();
                    latLng = parseLatLngFromResponse(googleGeocodeResponse);
                } catch (IOException e) {} finally {
                    callSet.remove(call);
                }
                return latLng;
            }
        };
    }

    @Nullable
    private static GoogleLatLng parseLatLngFromResponse(GoogleGeocodeResponse googleGeocodeResponse) {
        if (googleGeocodeResponse != null) {
            List<GoogleGeocodeResult> results = googleGeocodeResponse.results;
            if (results != null && !results.isEmpty()) {
                GoogleGeocodeResult googleGeocodeResult = results.get(0);
                if (googleGeocodeResult != null && googleGeocodeResult.geometry != null) {
                    return googleGeocodeResult.geometry.location;
                }
            }
        }
        return null;
    }

    private void loadEta(final LyftButton.ResultLoadedCallback callback) {
        final Call<EtaEstimateResponse> etaCall =
                publicApi.getEtas(rideParams.getPickupLat(), rideParams.getPickupLng(), RideTypeEnum.ALL.toString());
        callSet.add(etaCall);
        etaCall.enqueue(new Callback<EtaEstimateResponse>() {
            @Override
            public void onResponse(Call<EtaEstimateResponse> call, Response<EtaEstimateResponse> response) {
                Eta eta = getEtaForRideType(response.body(), rideParams.getRideTypeEnum().toString());

                if (eta != null && eta.eta_seconds != null) {
                    callback.onSuccess(eta);
                } else {
                    onFailure(call, new NullPointerException("EtaEstimateResponse is null or empty."));
                }
                callSet.remove(etaCall);
            }

            @Override
            public void onFailure(Call<EtaEstimateResponse> call, Throwable t) {
                callback.onFailure(t);
                callSet.remove(etaCall);
            }
        });
    }

    private void loadCost(final LyftButton.ResultLoadedCallback callback) {
        final Call<CostEstimateResponse> costCall =
                publicApi.getCosts(rideParams.getPickupLat(), rideParams.getPickupLng(), RideTypeEnum.ALL.toString(),
                        rideParams.getDropoffLat(),
                        rideParams.getDropoffLng());
        callSet.add(costCall);
        costCall.enqueue(new Callback<CostEstimateResponse>() {
            @Override
            public void onResponse(Call<CostEstimateResponse> call, Response<CostEstimateResponse> response) {
                CostEstimate costEstimate = getCostForRideType(response.body(), rideParams.getRideTypeEnum().toString());
                if (costEstimate != null) {
                    callback.onSuccess(costEstimate);
                } else {
                    onFailure(call, new NullPointerException("CostEstimateResponse is null or empty."));
                }
                callSet.remove(costCall);
            }

            @Override
            public void onFailure(Call<CostEstimateResponse> call, Throwable t) {
                callback.onFailure(t);
                callSet.remove(costCall);
            }
        });
    }

    @Nullable
    private static Eta getEtaForRideType(EtaEstimateResponse etaEstimateResponse, String desiredRideType) {
        Eta desiredEta = null;

        if (etaEstimateResponse != null && etaEstimateResponse.eta_estimates != null) {
            for (Eta eta : etaEstimateResponse.eta_estimates) {
                if (RideTypeEnum.CLASSIC.toString().equals(eta.ride_type)) {
                    desiredEta = eta;
                }

                if (desiredRideType.equals(eta.ride_type)) {
                    desiredEta = eta;
                    break;
                }
            }
        }
        return desiredEta;
    }

    @Nullable
    private static CostEstimate getCostForRideType(CostEstimateResponse costEstimateResponse, String desiredRideType) {
        CostEstimate desiredCostEstimate = null;

        if (costEstimateResponse != null && costEstimateResponse.cost_estimates != null) {
            for (CostEstimate costEstimate : costEstimateResponse.cost_estimates) {
                if (RideTypeEnum.CLASSIC.toString().equals(costEstimate.ride_type)) {
                    desiredCostEstimate = costEstimate;
                }

                if (desiredRideType.equals(costEstimate.ride_type)) {
                    desiredCostEstimate = costEstimate;
                    break;
                }
            }
        }
        return desiredCostEstimate;
    }
}
