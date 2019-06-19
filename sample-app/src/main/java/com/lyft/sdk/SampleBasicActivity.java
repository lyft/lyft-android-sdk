package com.lyft.sdk;

import android.app.Activity;
import android.os.Bundle;
import com.lyft.deeplink.RideTypeEnum;
import com.lyft.lyftbutton.LyftButton;
import com.lyft.lyftbutton.RideParams;
import com.lyft.networking.ApiConfig;

public class SampleBasicActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_sample_basic);

        ApiConfig apiConfig = new ApiConfig.Builder()
                .setClientId("your_client_id")
                .setClientToken("your_client_token")
                .build();

        LyftButton lyftButton = (LyftButton) findViewById(R.id.lyft_button);
        lyftButton.setApiConfig(apiConfig);

        RideParams.Builder rideParamsBuilder = new RideParams.Builder()
                .setPickupLocation(37.7766048, -122.3943629)
                .setDropoffLocation(37.759234, -122.4135125);
        rideParamsBuilder.setRideTypeEnum(RideTypeEnum.STANDARD);

        lyftButton.setRideParams(rideParamsBuilder.build());
        lyftButton.load();
    }
}
