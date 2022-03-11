# defect/package-visibility-in-android

- **Issue**: `intent.resolveActivity(context.getPackageManager())` evaluated to `null` when targetSDK is 30 and above
<img width="1251" alt="Screen Shot 2022-03-11 at 2 05 36 PM" src="https://user-images.githubusercontent.com/83723306/157934972-f81074d6-4eca-4996-9b79-21a1733dc42f.png">

- **Cause**: Android 11 introduced changes related to package visibility. (https://developer.android.com/about/versions/11/privacy/package-visibility)

- **Fix**: 
  - Updated Gradle version from `4.10.1` to `5.4.1`, and Android Gradle Plugin from `3.3.2` to `3.5.4`
  - Updated AndroidTargetSDK from `23` to `30`
  - Added a <queries> element in the Android manifest

- **Before Fix Sample Video:** 

https://user-images.githubusercontent.com/83723306/157935785-0d32181c-20d4-42b9-98af-2212bd3a696d.mp4

- **After Fix Sample Video:** 

https://user-images.githubusercontent.com/83723306/157935477-3695dd17-5ad3-422e-8b1f-a882f125ad9d.mp4

https://user-images.githubusercontent.com/83723306/157935497-d20b923e-00d1-4680-991a-0a2595383f6f.mp4

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
    compile 'com.lyft:lyft-android-sdk:2.0.2'
}
```

If you only want to use the [Lyft API Wrapper](https://github.com/lyft/lyft-android-sdk#lyft-api-wrapper) or [Deeplink](https://github.com/lyft/lyft-android-sdk#deeplinking) portion of the SDK, you can pull them individually.
```gradle
compile 'com.lyft:lyft-android-sdk-networking:2.0.2'  // Lyft API Wrapper
compile 'com.lyft:lyft-android-sdk-deeplink:2.0.2'    // Deeplink
```

### Maven:
```xml
<dependency>
  <groupId>com.lyft</groupId>
  <artifactId>lyft-android-sdk</artifactId>
  <version>2.0.2</version>
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
rideParamsBuilder.setRideTypeEnum(RideTypeEnum.STANDARD);

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
Lyft is growing very quickly and is currently available in [these cities](https://www.lyft.com/cities). Please keep in mind that some ride types (such as Lyft Line) are not yet available in all Lyft cities. If you set the ride type of the button  and it happens to be unavailable, the button will default to the Lyft Standard ride type. You can utilize the [`/v1/ridetypes`](https://developer.lyft.com/docs/availability-ride-types) endpoint to get a list of the available ride types in an area.

### Button styles
To specify the button style via XML, use the `lyft:lyftStyle` attribute or set it programmatically:
```java
lyftButton.setStyle(LyftStyle.MULBERRY_DARK);
```

There are 5 styles to pick from:

![lyft-styles](https://cloud.githubusercontent.com/assets/13209348/17683300/88f86446-6306-11e6-81e6-bc42fc77650e.png)

## Lyft API Wrapper
The SDK provides wrapper methods around Lyft's REST APIs - this can be helpful when you want to build a more custom integration with the Lyft platform vs making HTTP requests directly.

The SDK uses Square's [Retrofit 2](http://square.github.io/retrofit/) networking library. For each Lyft API endpoint, there is a corresponding Java method. The return type is a `Call` object which can be executed synchronously or asynchronously. See [LyftApi.java](https://github.com/lyft/lyft-android-sdk/blob/master/networking/src/main/java/com/lyft/networking/apis/LyftApi.java) for all the API methods.

```java
LyftApi lyftPublicApi = new LyftApiFactory(apiConfig).getLyftApi();
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
If you already use RxJava (your app must have it as a dependency), then you can obtain an `Observable` instead of a `Call` object. Simply use [LyftApiRx](https://github.com/lyft/lyft-android-sdk/blob/master/networking/src/main/java/com/lyft/networking/apis/LyftApiRx.java) instead of LyftApi.

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
        .setRideType(RideTypeEnum.SHARED)
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
