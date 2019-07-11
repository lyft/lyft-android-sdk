package com.lyft.sdk.testing

import com.lyft.networking.ApiConfig
import com.lyft.networking.LyftApiFactory
import com.lyft.networking.apis.LyftApi
import com.lyft.networking.apis.LyftApiRx

class TestLyftApiFactory {
    companion object {
        const val LYFT_CLIENT_ID = "your_id_here"
        const val LYFT_CLIENT_TOKEN = "your_token_here"
    }

    private val lyftApiFactory: LyftApiFactory

    init {
        val lyftApiConfig = ApiConfig.Builder()
                .setClientId(LYFT_CLIENT_ID)
                .setClientToken(LYFT_CLIENT_TOKEN)
                .build()

        lyftApiFactory = LyftApiFactory(lyftApiConfig)
    }

    fun getRetrofitClient(): LyftApi {
        return lyftApiFactory.lyftApi
    }

    fun getRetrofitRxClient(): LyftApiRx {
        return lyftApiFactory.lyftApiRx
    }
}