package com.aneeshajose.trending.network.utils

import com.aneeshajose.trending.base.CoroutineContextProvider
import com.aneeshajose.trending.network.ApiCallTag
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


/**
 * Created by Aneesha Jose on 2020-03-22.
 * A class that holds utility functions that aid in making network calls easier
 */
val apiCallTracker = mutableListOf<String>()

fun <T> makeNetworkCall(
    call: Call<T>,
    @ApiCallTag apiCallTag: String,
    coroutineContext: CoroutineContextProvider,
    onSuccess: NetworkResponse<T>,
    onFailure: NetworkResponse<String>,
    onError: NetworkResponse<Throwable>? = null
) {
    apiCallTracker.add(apiCallTag)
    CoroutineScope(coroutineContext.IO).launch {
        call.enqueue(object : Callback<T> {
            override fun onResponse(call: Call<T>, response: Response<T>) {
                apiCallTracker.remove(apiCallTag)
                if (response.isSuccessful) {
                    onSuccess.onNetworkResponse(response.body(), apiCallTag)
                } else {
                    onFailure.onNetworkResponse(response.message(), apiCallTag)
                }
            }

            override fun onFailure(call: Call<T>, t: Throwable) {
                apiCallTracker.remove(apiCallTag)
                onError?.onNetworkResponse(t, apiCallTag)
            }

        })
    }
}

interface NetworkResponse<T> {
    fun onNetworkResponse(t: T?, @ApiCallTag callTag: String)
}
