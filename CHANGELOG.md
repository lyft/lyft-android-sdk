Version 2.0.1 *(2020-09-20)*
----------------------------
* Fixed a bug that caused an NPE when attempting to display a price on the Lyft Button.

Version 2.0.1 *(2019-11-18)*
----------------------------
* Fixed a bug that prevented promo codes from being applied to the Lyft Button.

Version 2.0.0 *(2019-07-12)*
----------------------------
* Update Gradle version from `2.14.1` to `4.10.1`, and Android Gradle Plugin from `2.2.2` to `3.3.2`
* The compile SDK target for Android has been bumped to API 28.
* The following dependencies have been updated:
  * Android Build Tools `23.0.3` -> `28.0.3`
  * Android Support Libraries `23.4.0` -> `28.0.0`
  * Google Play Services `9.4.0` -> `17.0.0`
  * Mockito `1.10.19` -> `2.25.0`
  * Retrofit `2.1.0` -> `2.6.0`
* `LyftUserApi`, `LyftUserApiRx` and all endpoints that require a Lyft User OAuth token **have been removed**. External clients will no longer have access to features that require user information.
* `LyftPublicApi` and `LyftPublicApiRx` have been renamed to `LyftApi` and `LyftApiRx` respectively, to reflect new changes regarding the UserApi client.
* The RxJava call adapter for Retrofit has been updated to RxJava2. Users of the RxJava Retrofit interface will have to update their codebase to take RxJava2 Observables.
* All models returned by network calls have been documented with JetBrain `@Nullable` and `@NotNull` annotations.
* `LyftApiFactory` now by default generates a `LyftApi` that **throws a `PartialResponseException`** when the server response doesn't abide by the `@Nullable` and `@NotNull` annotations of the parsed model. The client must be able to handle a `PartialResponseException` when calling for Lyft endpoints.
  * If the client wishes to handle null checking manually, call `LyftApiFactory#getUncheckedLyftApi` to get a Retrofit client that does not massage responses.
* `RideTypeEnum` has been moved from the Lyft Button module to the Deeplink module, with the following changes:
  * `toString()` is no longer valid when fetching for a ride type key. Use `RideTypeEnum#getRideTypeKey()` instead.
  * `getDisplayName()` is still valid for fetching human readable text for each ride type.
* `RideTypeEnum` has been updated to reflect the current ride offerings  provided by Lyft:
  * `LINE` has been replaced by `SHARED`
  * `CLASSIC` has been replaced by `STANDARD`
  * `PLUS` has been replaced by `XL`
  * `LUX`, `LUX_BLACK`, `LUX_BLACK_XL` has been added into `RideTypeEnum`
  * `null` can be used in place of `RideTypeEnum.ALL` in existing usages 
* `DeepLinkParams` and its builder now takes in `RideTypeEnum` to specify a ride type, instead of a `String`.

Version 1.0.3 *(2017-02-21)*
----------------------------
Rename couponCode to promoCode
Add promoCode to RideParams
Make ResultLoadedCallback interface public

Version 1.0.2 *(2016-11-22)*
----------------------------
Coupon code parameter in ridetype deeplink
Expose address parameters in deeplink

Version 1.0.1 *(2016-11-15)*
----------------------------
Small deeplink fixes.
Avoid NPE if eta_seconds is null.

Version 1.0.0 *(2016-09-01)*
----------------------------
Initial release.
