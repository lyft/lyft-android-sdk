package com.lyft.sdk.testing.ui

data class SpinnerPairItem<S>(
        val humanReadableText: String,
        val data: S
) {
    override fun toString(): String {
        return humanReadableText
    }
}