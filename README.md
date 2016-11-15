# Lyft Android SDK

The Official Lyft Android SDK makes it easy to integrate Lyft into your app. More specifically, it provides:
- An easily configurable Lyft button which can display cost, ETA, and ride type. Tapping the button deeplinks into the Lyft app with pre-filled pickup/destination/ridetype.
- A Java interface for making sync/async calls to Lyft's REST APIs
- Two sample Android Activities that show how to use the SDK components.

## Registration
- You must first create a Lyft Developer account [here](https://www.lyft.com/developers).
- Once registered, you will be assigned a Client ID and will be able to generate Client Tokens.

## Setup and Installation

### Gradle:

```gradle
repositories {
  mavenCentral() // or jcenter()
}

dependencies {
    compile 'com.lyft:lyft-android-sdk:1.0.1'
}
```

If you only want to use the [Lyft API Wrapper](https://github.com/lyft/lyft-android-sdk#lyft-api-wrapper) or [Deeplink](https://github.com/lyft/lyft-android-sdk#deeplinking) portion of the SDK, you can pull them individually.
```gradle
compile 'com.lyft:lyft-android-sdk-networking:1.0.1'  // Lyft API Wrapper
compile 'com.lyft:lyft-android-sdk-deeplink:1.0.1'    // Deeplink
```

### Maven:
```xml
<dependency>
  <groupId>com.lyft</groupId>
  <artifactId>lyft-android-sdk</artifactId>
  <version>1.0.1</version>
  <type>aar</type>
</dependency>
```

## Lyft Button

Adding the Lyft Button in an XML layout is as simple as:
```xml
<com.lyft.lyftbutton.LyftButton
    android:id="@+id/lyft_button"
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    lyft:lyftStyle="lyftMulberryDark"
    />
```
We recommend setting the width/height to `wrap_content`, which results in a width of 260dp and a height of 50dp.
Otherwise, please keep in mind that a smaller width/height may result in undesirable UI, such as overlapping text.

To load ETA/cost:
```java
ApiConfig apiConfig = new ApiConfig.Builder()
        .setClientId("your_client_id")
        .setClientToken("your_client_token")
        .build();

LyftButton lyftButton = (LyftButton) findViewById(R.id.lyft_button);
lyftButton.setApiConfig(apiConfig);

RideParams.Builder rideParamsBuilder = new RideParams.Builder()
        .setPickupLocation(37.7766048, -122.3943576)
        .setDropoffLocation(37.759234, -122.4135125);
rideParamsBuilder.setRideTypeEnum(RideTypeEnum.CLASSIC);

lyftButton.setRideParams(rideParamsBuilder.build());
lyftButton.load();
```

Addresses can also be used for pickup/dropoff locations. Each address results in an extra API call to obtain the corresponding lat/lng pair. Therefore, we recommend using lat/lng values directly if they are available. 
```java
RideParams.Builder rideParamsBuilder = new RideParams.Builder()
        .setPickupAddress("185 Berry St, San Francisco, CA 94107")
        .setDropoffAddress("2300 Harrison St, San Francisco, CA 94110");
```

### Ride types
Lyft is growing very quickly and is currently available in [these cities](https://www.lyft.com/cities). Please keep in mind that some ride types (such as Lyft Line) are not yet available in all Lyft cities. If you set the ride type of the button  and it happens to be unavailable, the button will default to the Lyft Classic ride type. You can utilize the [`/v1/ridetypes`](https://developer.lyft.com/docs/availability-ride-types) endpoint to get a list of the available ride types in an area.

### Button styles
To specify the button style via XML, use the `lyft:lyftStyle` attribute or set it programmatically:
```java
lyftButton.setStyle(LyftStyle.MULBERRY_DARK);
```

There are 5 styles to pick from:

![lyft-styles](https://cloud.githubusercontent.com/assets/13209348/17683300/88f86446-6306-11e6-81e6-bc42fc77650e.png)

## Lyft API Wrapper
The SDK provides wrapper methods around Lyft's REST APIs - this can be helpful when you want to build a more custom integration with the Lyft platform vs making HTTP requests directly.

The SDK uses Square's [Retrofit 2](http://square.github.io/retrofit/) networking library. For each [Lyft Public API endpoint](http://petstore.swagger.io/?url=https://api.lyft.com/v1/spec#!/Public/), there is a corresponding Java method. The return type is a `Call` object which can be executed synchronously or asynchronously. See [LyftPublicApi.java](https://github.com/lyft/lyft-android-sdk/blob/master/networking/src/main/java/com/lyft/networking/apis/LyftPublicApi.java) for all the API methods.

```java
LyftPublicApi lyftPublicApi = new LyftApiFactory(apiConfig).getLyftPublicApi();
Call<EtaEstimateResponse> etaCall = lyftPublicApi.getEtas(37.7766048, -122.3943576, "lyft");
```
Asynchronous:
```java
etaCall.enqueue(new Callback<EtaEstimateResponse>() {
    @Override
    public void onResponse(Call<EtaEstimateResponse> call, Response<EtaEstimateResponse> response) {
        EtaEstimateResponse etaEstimateResponse = response.body();
        Eta eta = etaEstimateResponse.eta_estimates.get(0);
    }

    @Override
    public void onFailure(Call<EtaEstimateResponse> call, Throwable t) {
        Log.d("MyApp", t.toString());
    }
});
```

Synchronous:
```java
EtaEstimateResponse etaEstimateResponse = etaCall.execute().body();
Eta eta = etaEstimateResponse.eta_estimates.get(0);
```

### RxJava Observables
If you already use RxJava (your app must have it as a dependency), then you can obtain an `Observable` instead of a `Call` object. Simply use [LyftPublicApiRx](https://github.com/lyft/lyft-android-sdk/blob/master/networking/src/main/java/com/lyft/networking/apis/LyftPublicApiRx.java) instead of LyftPublicApi.

```java
LyftPublicApiRx lyftPublicApiRx = new LyftApiFactory(apiConfig).getLyftPublicApiRx();
Observable<EtaEstimateResponse> etaObservable = lyftPublicApiRx.getEtas(37.7766048, -122.3943576, "lyft");
```

### ProGuard
If you are directly using the Lyft Wrapper API without using the `Lyft Button`, then you may need to add the the ProGuard rules for Retrofit. Please see [proguard-rules.pro](https://github.com/lyft/lyft-android-sdk/blob/master/lyft-button/proguard-rules.pro). This only applies if you are using ProGuard.

## Deeplinking
The SDK provides direct [deeplinking](https://developer.lyft.com/docs/deeplinking) to the Lyft app for those developers who prefer to handle their own custom deeplinking vs relying on the Lyft Button.. The `deeplink` module of the SDK includes this logic and makes it easy to launch the Lyft app.
```java
DeepLinkParams deepLinkParams = new DeepLinkParams.Builder()
        .setClientId("your_client_id")
        .setRideType("lyft_line")
        .setPickupLocation(37.7766048, -122.3943576)
        .setDropoffLocation(37.759234, -122.4135125)
        .build();

DeepLink.launchLyftApp(getContext(), deepLinkParams);
```

## Sample Activities
Checkout the sample activites which are included in the SDK, in the `sample-app` module:
  - `SampleBasicActivity`: Includes minimal code to set up the Lyft Button.
  - `SampleLocationAwareActivity`: Gets the device's current GPS location and calls the `/v1/ridetypes` endpoint to get a list of the available ride types in the area (i.e. Lyft Line, Lyft Plus, etc). The user is able to select an available ride type via a dropdown. The Lyft Button then displays the current ETA and cost of that ride type to a specified destination.

You can specify which activity to launch in the `sample-app`'s [AndroidManifest.xml](https://github.com/lyft/lyft-android-sdk/blob/master/sample-app/src/main/AndroidManifest.xml).

## Support

If you're looking for help configuring or using the SDK, or if you have general questions related to our APIs, the Lyft Developer Platform team provides support through our [forum](https://developer.lyft.com/discuss) as well as on Stack Overflow (using the `lyft-api` tag)

## Reporting security vulnerabilities

If you've found a vulnerability or a potential vulnerability in the Lyft Android SDK,
please let us know at security@lyft.com. We'll send a confirmation email to
acknowledge your report, and we'll send an additional email when we've
identified the issue positively or negatively.
