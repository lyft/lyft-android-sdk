package com.lyft.sdk;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.lyft.deeplink.RideTypeEnum;
import com.lyft.lyftbutton.LyftButton;
import com.lyft.lyftbutton.RideParams;
import com.lyft.networking.ApiConfig;
import com.lyft.sdk.testing.TestLyftApiFactory;
import com.lyft.sdk.testing.ui.SpinnerPairItem;

import java.util.Arrays;
import java.util.List;

public class SampleBasicActivity extends Activity {

    private Spinner rideTypeSpinner;
    private LyftButton lyftButton;

    private List<SpinnerPairItem<RideTypeEnum>> spinnerList = Arrays.asList(
            new SpinnerPairItem<>(RideTypeEnum.SHARED.getDisplayName(), RideTypeEnum.SHARED),
            new SpinnerPairItem<>(RideTypeEnum.STANDARD.getDisplayName(), RideTypeEnum.STANDARD),
            new SpinnerPairItem<>(RideTypeEnum.XL.getDisplayName(), RideTypeEnum.XL),
            new SpinnerPairItem<>(RideTypeEnum.LUX.getDisplayName(), RideTypeEnum.LUX),
            new SpinnerPairItem<>(RideTypeEnum.LUX_BLACK.getDisplayName(), RideTypeEnum.LUX_BLACK),
            new SpinnerPairItem<>(RideTypeEnum.LUX_BLACK_XL.getDisplayName(), RideTypeEnum.LUX_BLACK_XL)
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_sample_basic);
        setupSpinner();

        ApiConfig apiConfig = new ApiConfig.Builder()
                .setClientId("your_client_id")
                .setClientToken("your_client_token")
                .build();

        lyftButton = findViewById(R.id.lyft_button);
        lyftButton.setApiConfig(apiConfig);

        loadButtonWithRideType(RideTypeEnum.STANDARD);
    }

    private void setupSpinner() {
        rideTypeSpinner = findViewById(R.id.sample_basic_activity_spinner);

        ArrayAdapter<SpinnerPairItem<RideTypeEnum>> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                spinnerList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        rideTypeSpinner.setAdapter(adapter);
        rideTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                loadButtonWithRideType(spinnerList.get(position).getData());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //Do nothing
            }
        });
    }

    private void loadButtonWithRideType(RideTypeEnum rideTypeEnum) {
        RideParams.Builder rideParamsBuilder = new RideParams.Builder()
                .setPickupLocation(37.7766048, -122.3943629)
                .setDropoffLocation(37.759234, -122.4135125);
        rideParamsBuilder.setRideTypeEnum(rideTypeEnum);

        lyftButton.setRideParams(rideParamsBuilder.build());
        lyftButton.load();
    }
}
