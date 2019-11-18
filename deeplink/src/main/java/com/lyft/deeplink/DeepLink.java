package com.lyft.deeplink;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;

public class DeepLink {

    private static final String LYFT_PACKAGE_NAME = "me.lyft.android";
    private static final String SDK_VERSION = "2.0.1";

    /**
     * @return true if Lyft app is installed on the device.
     */
    public static boolean isLyftInstalled(Context context) {
        PackageManager packageManager = context.getPackageManager();
        try {
            packageManager.getPackageInfo(LYFT_PACKAGE_NAME, PackageManager.GET_ACTIVITIES);
            return true;
        } catch (NameNotFoundException e) {
            return false;
        }
    }

    /**
     * Attempts to deeplink to the Lyft app. If Lyft is not installed on the device,
     * the Play Store will be launched to the Lyft app details page.
     *
     * @param context Required for deeplinking.
     * @param deepLinkParams Used to construct the deeplink {@link Uri}.
     * @return true if Lyft app launched successfully.
     */
    public static boolean launchLyftApp(Context context, DeepLinkParams deepLinkParams) {
        if (!isLyftInstalled(context)) {
            String url = "https://www.lyft.com/signup/SDKSIGNUP?clientId=" + deepLinkParams.getClientId()
                    + "&sdkName=android&sdkVersion=" + SDK_VERSION;
            Uri webpage = Uri.parse(url);
            Intent intent = new Intent(Intent.ACTION_VIEW, webpage);
            if (intent.resolveActivity(context.getPackageManager()) != null) {
                context.startActivity(intent);
            }
            return false;
        }

        PackageManager packageManager = context.getPackageManager();

        Intent i = packageManager.getLaunchIntentForPackage(LYFT_PACKAGE_NAME);
        if (i == null) {
            return false;
        }

        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        String deepLinkString = createDeepLinkString(deepLinkParams);
        i.setData(Uri.parse(deepLinkString));
        context.startActivity(i);

        return true;
    }

    static String createDeepLinkString(DeepLinkParams deepLinkParams) {
        StringBuilder sb = new StringBuilder();
        sb.append("lyft://ridetype?id=");

        if (deepLinkParams.isRideTypeSet()) {
            sb.append(deepLinkParams.getRideTypeEnum().getRideTypeKey());
        } else {
            sb.append(RideTypeEnum.STANDARD.getRideTypeKey());
        }

        if (deepLinkParams.isPickupLatLngSet()) {
            sb.append("&pickup[latitude]=");
            sb.append(String.valueOf(deepLinkParams.getPickupLat()));
            sb.append("&pickup[longitude]=");
            sb.append(String.valueOf(deepLinkParams.getPickupLng()));
        }

        if (deepLinkParams.isPickupAddressSet()) {
            sb.append("&pickup[address]=");
            sb.append(deepLinkParams.getPickupAddr());
        }

        if (deepLinkParams.isDropoffLatLngSet()) {
            sb.append("&destination[latitude]=");
            sb.append(String.valueOf(deepLinkParams.getDropoffLat()));
            sb.append("&destination[longitude]=");
            sb.append(String.valueOf(deepLinkParams.getDropoffLng()));
        }

        if (deepLinkParams.isDropoffAddressSet()) {
            sb.append("&destination[address]=");
            sb.append(deepLinkParams.getDropoffAddr());
        }

        if (deepLinkParams.getClientId() != null) {
            sb.append("&partner=");
            sb.append(deepLinkParams.getClientId());
        }

        if (deepLinkParams.getPromoCode() != null) {
            sb.append("&credits=");
            sb.append(deepLinkParams.getPromoCode());
        }

        return sb.toString();
    }
}
