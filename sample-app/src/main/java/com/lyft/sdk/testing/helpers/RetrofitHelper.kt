package com.lyft.sdk.testing.helpers

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

fun <T> retrofitCallBack(success: (Response<T>) -> Unit, failure: (Throwable) -> Unit): Callback<T> {
    return object: Callback<T> {
        override fun onFailure(call: Call<T>, t: Throwable) {
            failure(t)
        }

        override fun onResponse(call: Call<T>, response: Response<T>) {
            success(response)
        }

    }
}