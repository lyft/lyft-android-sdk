package com.lyft.lyftbutton;

public enum LyftStyle {
    MULBERRY_DARK(0, R.drawable.btn_gradient_purple_rounded, R.drawable.wordmark_thin_white, R.color.white, R.drawable.prime_time_white),
    MULBERRY_LIGHT(1, R.drawable.btn_white_rounded, R.drawable.wordmark_thin_mulberry, R.color.mulberry, R.drawable.prime_time_mulberry),
    HOT_PINK(2, R.drawable.btn_hotpink_rounded, R.drawable.wordmark_thin_white, R.color.white, R.drawable.prime_time_white),
    MULTI_COLOR(3, R.drawable.btn_white_rounded, R.drawable.wordmark_thin_pink, R.color.black, R.drawable.prime_time_charcoal),
    LAUNCHER(4, R.drawable.btn_white_rounded, R.drawable.app_icon, R.color.mulberry, R.drawable.prime_time_mulberry);

    public static final LyftStyle DEFAULT = LAUNCHER;
    private final int styleId;
    private final int backgroundDrawableId;
    private final int lyftIconDrawableId;
    private final int textColorId;
    private final int primeTimeIconId;

    LyftStyle(int styleId, int backgroundDrawableId, int lyftIconDrawableId, int textColorId, int primeTimeIconId) {
        this.styleId = styleId;
        this.backgroundDrawableId = backgroundDrawableId;
        this.lyftIconDrawableId = lyftIconDrawableId;
        this.textColorId = textColorId;
        this.primeTimeIconId = primeTimeIconId;
    }

    static LyftStyle fromStyleId(int styleId) {
        for (LyftStyle lyftStyle : values()) {
            if (lyftStyle.getStyleId() == styleId) {
                return lyftStyle;
            }
        }
        return DEFAULT;
    }

    int getStyleId() {
        return styleId;
    }

    int getBackgroundDrawableId() {
        return backgroundDrawableId;
    }

    int getLyftIconDrawableId() {
        return lyftIconDrawableId;
    }

    int getTextColorId() {
        return textColorId;
    }

    int getPrimeTimeIconId() {
        return primeTimeIconId;
    }
}
