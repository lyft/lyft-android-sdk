package com.lyft.testutils;

import com.lyft.networking.apiObjects.GoogleAddressComponent;
import com.lyft.networking.apiObjects.GoogleGeocodeResponse;
import com.lyft.networking.apiObjects.GoogleGeocodeResult;
import com.lyft.networking.apiObjects.GoogleGeometry;
import com.lyft.networking.apiObjects.GoogleLatLng;
import com.lyft.networking.apis.internal.GoogleApi;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import retrofit2.Call;
import retrofit2.http.Query;
import retrofit2.mock.BehaviorDelegate;

public class MockGoogleApi implements GoogleApi {

    private final BehaviorDelegate<GoogleApi> delegate;
    private Map<String, GoogleLatLng> addressToLatLngMap = new HashMap<>();
    private boolean nullResponse;

    public MockGoogleApi(BehaviorDelegate<GoogleApi> delegate) {
        this.delegate = delegate;
    }

    @Override
    public Call<GoogleGeocodeResponse> forwardGeocode(@Query("address") String address) {
        if (nullResponse) {
            return delegate.returningResponse(null).forwardGeocode(address);
        }

        GoogleLatLng googleLatLng;
        if (addressToLatLngMap.containsKey(address)) {
            googleLatLng = addressToLatLngMap.get(address);
        } else {
            googleLatLng = new GoogleLatLng(13.333337, 73.333331);
        }

        GoogleGeometry googleGeometry = new GoogleGeometry(googleLatLng);
        GoogleGeocodeResult googleGeocodeResult =
                new GoogleGeocodeResult(Collections.<GoogleAddressComponent>emptyList(), "", googleGeometry,
                        Collections.<String>emptyList());
        List<GoogleGeocodeResult> results = new ArrayList<>();
        results.add(googleGeocodeResult);
        GoogleGeocodeResponse googleGeocodeResponse = new GoogleGeocodeResponse("status", results);
        return delegate.returningResponse(googleGeocodeResponse).forwardGeocode(address);
    }

    public void setAddressToLatLngMap(Map<String, GoogleLatLng> addressToLatLngMap) {
        this.addressToLatLngMap = addressToLatLngMap;
    }

    public void forceNullResponse(boolean nullResponse) {
        this.nullResponse = nullResponse;
    }
}
