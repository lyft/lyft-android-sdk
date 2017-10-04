package com.lyft.testutils;

import com.lyft.networking.apiObjects.CostEstimate;
import com.lyft.networking.apiObjects.CostEstimateResponse;
import com.lyft.networking.apiObjects.Eta;
import com.lyft.networking.apiObjects.EtaEstimateResponse;
import com.lyft.networking.apiObjects.NearbyDriversResponse;
import com.lyft.networking.apiObjects.RideType;
import com.lyft.networking.apiObjects.RideTypesResponse;
import com.lyft.networking.apis.LyftPublicApi;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.http.Query;
import retrofit2.mock.BehaviorDelegate;

public class MockLyftPublicApi implements LyftPublicApi {

    private static final String LYFT_LINE = "lyft_line";
    private static final String LYFT = "lyft";
    private static final String LYFT_PLUS = "lyft_plus";
    private static final String LYFT_PREMIER = "lyft_premier";
    private static final String LYFT_LUX = "lyft_lux";
    private static final String LYFT_LUX_SUV = "lyft_luxsuv";

    private final BehaviorDelegate<LyftPublicApi> delegate;
    private CostEstimateResponse costEstimateResponse;
    private EtaEstimateResponse etaEstimateResponse;
    private boolean useCustomCostResponse = false;
    private boolean useCustomEtaResponse = false;

    public MockLyftPublicApi(BehaviorDelegate<LyftPublicApi> delegate) {
        this.delegate = delegate;
    }

    @Override
    public Call<CostEstimateResponse> getCosts(@Query("start_lat") Double startLat, @Query("start_lng") Double startLng,
                                               @Query("ride_type") String rideType, @Query("end_lat") Double endLat, @Query("end_lng") Double endLng) {
        if (!useCustomCostResponse) {
            List<CostEstimate> costEstimates = new ArrayList<>();
            if (rideType == null) {
                costEstimates.add(createCostEstimateForRideType(LYFT_LINE));
                costEstimates.add(createCostEstimateForRideType(LYFT));
                costEstimates.add(createCostEstimateForRideType(LYFT_PLUS));
                costEstimates.add(createCostEstimateForRideType(LYFT_PREMIER));
                costEstimates.add(createCostEstimateForRideType(LYFT_LUX));
                costEstimates.add(createCostEstimateForRideType(LYFT_LUX_SUV));
            } else if (isValidRideType(rideType)) {
                costEstimates.add(createCostEstimateForRideType(rideType));
            }

            costEstimateResponse = new CostEstimateResponse(costEstimates);
        }

        return delegate.returningResponse(costEstimateResponse).getCosts(startLat, startLng, rideType, endLat, endLng);
    }

    @Override
    public Call<NearbyDriversResponse> getDrivers(@Query("lat") Double lat, @Query("lng") Double lng) {
        return null;
    }

    @Override
    public Call<EtaEstimateResponse> getEtas(@Query("lat") Double lat, @Query("lng") Double lng, @Query("ride_type") String rideType) {
        if (!useCustomEtaResponse) {
            List<Eta> etas = new ArrayList<>();
            if (rideType == null) {
                etas.add(new Eta(LYFT_LINE, getDisplayNameForRideType(LYFT_LINE), 180));
                etas.add(new Eta(LYFT, getDisplayNameForRideType(LYFT), 60));
                etas.add(new Eta(LYFT_PLUS, getDisplayNameForRideType(LYFT_PLUS), 300));
            } else if (isValidRideType(rideType)) {
                etas.add(new Eta(rideType, getDisplayNameForRideType(rideType), 60));
            }

            etaEstimateResponse = new EtaEstimateResponse(etas);
        }

        return delegate.returningResponse(etaEstimateResponse).getEtas(lat, lng, rideType);
    }

    @Override
    public Call<RideTypesResponse> getRidetypes(@Query("lat") Double lat, @Query("lng") Double lng,
                                                @Query("ride_type") String rideType) {
        List<RideType> rideTypes = new ArrayList<>();
        if (rideType == null) {
            rideTypes.add(new RideType(LYFT_LINE, getDisplayNameForRideType(LYFT_LINE), 4, null, null, null));
            rideTypes.add(new RideType(LYFT, getDisplayNameForRideType(LYFT), 4, null, null, null));
            rideTypes.add(new RideType(LYFT_PLUS, getDisplayNameForRideType(LYFT_PLUS), 6, null, null, null));
            rideTypes.add(new RideType(LYFT_PREMIER, getDisplayNameForRideType(LYFT_PREMIER), 4, null, null, null));
            rideTypes.add(new RideType(LYFT_LUX, getDisplayNameForRideType(LYFT_LUX), 4, null, null, null));
            rideTypes.add(new RideType(LYFT_LUX_SUV, getDisplayNameForRideType(LYFT_LUX_SUV), 6, null, null, null));
        } else {
            rideTypes.add(new RideType(rideType, getDisplayNameForRideType(rideType), 6, null, null, null));
        }
        RideTypesResponse rideTypesResponse = new RideTypesResponse(rideTypes);
        return delegate.returningResponse(rideTypesResponse).getRidetypes(lat, lng, rideType);
    }

    public void setCustomCostEstimateResponse(CostEstimateResponse costEstimateResponse) {
        this.costEstimateResponse = costEstimateResponse;
        this.useCustomCostResponse = true;
    }

    public void setCustomEtaEstimateResponse(EtaEstimateResponse etaEstimateResponse) {
        this.etaEstimateResponse = etaEstimateResponse;
        this.useCustomEtaResponse = true;
    }

    private static CostEstimate createCostEstimateForRideType(String rideType) {
        return new CostEstimate(rideType, getDisplayNameForRideType(rideType), "USD", null, null, null, null, null, null, null);
    }

    private static boolean isValidRideType(String rideType) {
        return LYFT_LINE.equals(rideType)
                || LYFT.equals(rideType)
                || LYFT_PLUS.equals(rideType)
                || LYFT_PREMIER.equals(rideType)
                || LYFT_LUX.equals(rideType)
                || LYFT_LUX_SUV.equals(rideType);
    }

    private static String getDisplayNameForRideType(String rideType) {
        if (LYFT_LINE.equals(rideType)) {
            return "Lyft Line";
        }

        if (LYFT_PLUS.equals(rideType)) {
            return "Lyft Plus";
        }

        if(LYFT_PREMIER.equals(rideType)) {
            return "Lyft Premier";
        }

        if(LYFT_LUX.equals(rideType)) {
            return "Lyft Lux";
        }

        if(LYFT_LUX_SUV.equals(rideType)) {
            return "Lyft Lux SUV";
        }

        return "Lyft";
    }
}
