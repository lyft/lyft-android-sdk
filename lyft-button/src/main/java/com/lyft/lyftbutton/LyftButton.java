package com.lyft.lyftbutton;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.lyft.networking.ApiConfig;
import com.lyft.networking.GoogleApiBuilder;
import com.lyft.networking.LyftApiFactory;
import com.lyft.networking.Preconditions;
import com.lyft.networking.apiObjects.CostEstimate;
import com.lyft.networking.apiObjects.Eta;
import java.util.concurrent.Executors;
import org.jetbrains.annotations.NotNull;

public class LyftButton extends LinearLayout {

    private final ImageView lyftIcon;
    private final TextView getRideLabel;
    private final TextView rideTypeEtaText;
    private final ImageView primeTimeIcon;
    private final TextView costText;
    private final View costContainer;
    private LyftButtonCallManager callManager;
    private ApiConfig apiConfig;
    private RideParams rideParams;
    private ResultLoadedCallback resultLoadedCallback;

    public LyftButton(Context context) {
        this(context, null);
    }

    public LyftButton(final Context context, AttributeSet attrs) {
        super(context, attrs);

        setupAndInflate();

        lyftIcon = findViewById(R.id.lyft_icon);
        getRideLabel = findViewById(R.id.get_ride_label);
        rideTypeEtaText = findViewById(R.id.ridetype_eta_text);
        primeTimeIcon = findViewById(R.id.prime_time_icon);
        costText = findViewById(R.id.cost_text);
        costContainer = findViewById(R.id.cost_container);

        applyLyftStyle(context, attrs);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (callManager != null) {
            callManager.clearCalls();
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        if (widthMode == MeasureSpec.AT_MOST) {
            widthMeasureSpec = MeasureSpec.makeMeasureSpec((int) getResources().getDimension(R.dimen.button_width), MeasureSpec.EXACTLY);
        }

        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        if (heightMode == MeasureSpec.AT_MOST) {
            heightMeasureSpec = MeasureSpec.makeMeasureSpec((int) getResources().getDimension(R.dimen.button_height), MeasureSpec.EXACTLY);
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    private void applyLyftStyle(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.getTheme().obtainStyledAttributes(attrs, R.styleable.LyftButton, 0, 0);
        int selectedStyleId;
        try {
            selectedStyleId = typedArray.getInt(R.styleable.LyftButton_lyftStyle, LyftStyle.DEFAULT.getStyleId());
        } finally {
            typedArray.recycle();
        }

        LyftStyle lyftStyle = LyftStyle.fromStyleId(selectedStyleId);
        setLyftStyle(lyftStyle);
    }

    private void setupAndInflate() {
        setOrientation(HORIZONTAL);
        setGravity(Gravity.CENTER);
        int padding = (int) getResources().getDimension(R.dimen.button_padding);
        setPadding(padding, padding, padding, padding);

        inflate(getContext(), R.layout.lyft_button, this);
    }

    /**
     * Required in order to access Lyft's API Endpoints.
     */
    public void setApiConfig(@NotNull ApiConfig apiConfig) {
        Preconditions.checkNotNull(apiConfig, "ApiConfig must not be null.");
        this.apiConfig = apiConfig;
        callManager = new LyftButtonCallManager(apiConfig.getClientId(), new LyftApiFactory(apiConfig).getLyftApi(),
                new GoogleApiBuilder().build(), Executors.newCachedThreadPool());
    }

    /**
     * Required.
     *
     * @param rideParams Details about the ride.
     */
    public void setRideParams(@NotNull RideParams rideParams) {
        Preconditions.checkNotNull(rideParams, "RideParams cannot be null.");
        this.rideParams = rideParams;
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                callManager.launchLyftApp(getContext());
            }
        });
        rideTypeEtaText.setVisibility(VISIBLE);
        rideTypeEtaText.setText(rideParams.getRideTypeEnum().getDisplayName());
    }

    /**
     * Optional. The default is {@link LyftStyle#LAUNCHER}. Can also be set in XML via the lyft:lyftStyle attribute.
     */
    public void setLyftStyle(@NotNull LyftStyle lyftStyle) {
        setBackgroundResource(lyftStyle.getBackgroundDrawableId());
        lyftIcon.setImageDrawable(getResources().getDrawable(lyftStyle.getLyftIconDrawableId()));
        getRideLabel.setTextColor(getResources().getColor(lyftStyle.getTextColorId()));
        rideTypeEtaText.setTextColor(getResources().getColor(lyftStyle.getTextColorId()));
        primeTimeIcon.setImageDrawable(getResources().getDrawable(lyftStyle.getPrimeTimeIconId()));
        costText.setTextColor(getResources().getColor(lyftStyle.getTextColorId()));
    }

    /**
     * Optional. Useful in order to know when eta/cost is about to be displayed on the button, or if either API call failed.
     */
    public void setResultLoadedCallback(ResultLoadedCallback resultLoadedCallback) {
        this.resultLoadedCallback = resultLoadedCallback;
    }

    public void load() {
        Preconditions.checkNotNull(apiConfig, "You must call setApiConfig before calling load.");
        Preconditions.checkNotNull(rideParams, "You must call setRideParams before calling load.");
        callManager.setRideParams(rideParams);

        callManager.load(new ResultLoadedCallback() {
            @Override
            public void onSuccess(Eta eta) {
                displayEta(eta);
                if (resultLoadedCallback != null) {
                    resultLoadedCallback.onSuccess(eta);
                }
            }

            @Override
            public void onSuccess(CostEstimate costEstimate) {
                displayCost(costEstimate);
                if (resultLoadedCallback != null) {
                    resultLoadedCallback.onSuccess(costEstimate);
                }
            }

            @Override
            public void onFailure(Throwable t) {
                if (resultLoadedCallback != null) {
                    resultLoadedCallback.onFailure(t);
                }
            }
        });
    }

    private void displayEta(Eta eta) {
        int etaMinutes = eta.eta_seconds / 60;
        rideTypeEtaText.setText(String.valueOf(
                getResources().getString(R.string.ridetype_eta_text, eta.display_name, etaMinutes)));
    }

    private void displayCost(CostEstimate costEstimate) {
        costContainer.setVisibility(VISIBLE);

        if (TextUtils.isEmpty(costEstimate.primetime_percentage) || "0%".equals(costEstimate.primetime_percentage)) {
            primeTimeIcon.setVisibility(GONE);
        } else {
            primeTimeIcon.setVisibility(VISIBLE);
        }

        float minCost = costEstimate.estimated_cost_cents_min / 100f;
        float maxCost = costEstimate.estimated_cost_cents_max / 100f;
        if (minCost == maxCost) {
            costText.setText(String.valueOf(getResources().getString(R.string.fixed_cost_amount, minCost)));
        } else {
            costText.setText(
                    String.valueOf(getResources().getString(R.string.cost_estimate_range, (int) minCost, (int) maxCost)));
        }
    }

    public interface ResultLoadedCallback {

        void onSuccess(Eta eta);

        void onSuccess(CostEstimate costEstimate);

        void onFailure(Throwable t);
    }
}
