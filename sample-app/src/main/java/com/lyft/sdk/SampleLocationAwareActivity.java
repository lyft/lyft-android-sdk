package com.lyft.sdk;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.lyft.deeplink.RideTypeEnum;
import com.lyft.lyftbutton.LyftButton;
import com.lyft.lyftbutton.RideParams;
import com.lyft.networking.ApiConfig;
import com.lyft.networking.LyftApiFactory;
import com.lyft.networking.apiObjects.RideType;
import com.lyft.networking.apiObjects.RideTypesResponse;
import com.lyft.networking.apis.LyftApi;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SampleLocationAwareActivity extends Activity {

    private static final int PERMISSION_REQUEST_CODE = 1336;
    private static final long LOCATION_UPDATE_INTERVAL_MS = 15000;
    private static final float LOCATION_MIN_DISPLACEMENT_METERS = 10;
    private final Set<Call> callSet = new HashSet<>();
    private Spinner rideTypeSpinner;
    private ArrayAdapter<String> adapter;
    private LyftApi lyftApi;
    private LyftButton lyftButton;
    private GoogleApiClient googleApiClient;
    private double currentLat = 0.0;
    private double currentLng = 0.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sample_location_aware);

        ApiConfig apiConfig = new ApiConfig.Builder()
                .setClientId("your_client_id")
                .setClientToken("your_client_token")
                .build();

        lyftButton = (LyftButton) findViewById(R.id.lyft_button);
        lyftButton.setApiConfig(apiConfig);
        lyftApi = new LyftApiFactory(apiConfig).getLyftPublicApi();

        initializeRideTypeSpinner();
        setupLocationApi();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        googleApiClient.disconnect();
        for (Call call : callSet) {
            call.cancel();
        }
        callSet.clear();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == PERMISSION_REQUEST_CODE && isLocationPermissionGranted()) {
            googleApiClient.connect();
        }
    }

    private void initializeRideTypeSpinner() {
        rideTypeSpinner = (Spinner) findViewById(R.id.ride_type_dropdown);
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapter.add(getResources().getString(R.string.loading_text));
        rideTypeSpinner.setAdapter(adapter);
        rideTypeSpinner.setSelection(0, false);
        rideTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                refreshButton((String) rideTypeSpinner.getSelectedItem());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    @SuppressWarnings({"MissingPermission"})
    private void setupLocationApi() {
        googleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
                    @Override
                    public void onConnected(@Nullable Bundle bundle) {
                        LocationRequest locationRequest = LocationRequest.create();
                        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
                        locationRequest.setInterval(LOCATION_UPDATE_INTERVAL_MS);
                        locationRequest.setSmallestDisplacement(LOCATION_MIN_DISPLACEMENT_METERS);
                        LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest,
                                new com.google.android.gms.location.LocationListener() {
                                    @Override
                                    public void onLocationChanged(Location location) {
                                        currentLat = location.getLatitude();
                                        currentLng = location.getLongitude();
                                        getRideTypesAtCurrentLocation();
                                    }
                                });
                    }

                    @Override
                    public void onConnectionSuspended(int i) {
                    }
                })
                .addOnConnectionFailedListener(new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                    }
                })
                .build();

        if (isLocationPermissionGranted()) {
            googleApiClient.connect();
        } else {
            ActivityCompat.requestPermissions(this, new String[] { Manifest.permission.ACCESS_FINE_LOCATION }, PERMISSION_REQUEST_CODE);
        }
    }

    private void getRideTypesAtCurrentLocation() {
        Call<RideTypesResponse> call = lyftApi.getRidetypes(currentLat, currentLng, null);
        callSet.add(call);
        call.enqueue(new Callback<RideTypesResponse>() {
            @Override
            public void onResponse(Call<RideTypesResponse> call, Response<RideTypesResponse> response) {
                callSet.remove(call);
                adapter.clear();
                RideTypesResponse rideTypesResponse = response.body();
                if (isLyftAvailable(rideTypesResponse)) {
                    List<RideType> rideTypes = rideTypesResponse.ride_types;
                    for (RideType rideType : rideTypes) {
                        adapter.add(rideType.display_name);
                    }
                    rideTypeSpinner.setSelection(adapter.getPosition(RideTypeEnum.STANDARD.getDisplayName()));
                } else {
                    adapter.add("LYFT N/A");
                }
            }

            @Override
            public void onFailure(Call<RideTypesResponse> call, Throwable t) {
                callSet.remove(call);
            }
        });
    }

    private boolean isLocationPermissionGranted() {
        return ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    private void refreshButton(String selectedRideType) {
        RideTypeEnum rideTypeEnum = RideTypeEnum.STANDARD;

        for (RideTypeEnum rte : RideTypeEnum.values()) {
            if (rte.getDisplayName().equals(selectedRideType)) {
                rideTypeEnum = rte;
            }
        }

        RideParams.Builder rideParamsBuilder = new RideParams.Builder()
                .setPickupLocation(currentLat, currentLng)
                .setDropoffLocation(37.759234, -122.4135125);
        rideParamsBuilder.setRideTypeEnum(rideTypeEnum);

        lyftButton.setRideParams(rideParamsBuilder.build());
        lyftButton.load();
    }

    private static boolean isLyftAvailable(RideTypesResponse rideTypesResponse) {
        return rideTypesResponse != null && rideTypesResponse.ride_types != null && !rideTypesResponse.ride_types.isEmpty();
    }
}
