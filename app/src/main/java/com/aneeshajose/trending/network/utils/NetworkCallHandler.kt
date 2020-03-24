package com.aneeshajose.trending.network.utils

import com.aneeshajose.trending.network.ApiCallTag
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * Created by Aneesha Jose on 2020-03-22.
 * A class that holds utility functions that aid in making network calls easier
 */

/**
 *
 */
fun <T> makeNetworkCall(
    call: Call<T>,
    @ApiCallTag apiCallTag: String,
    onSuccess: NetworkResponse<T>,
    onFailure: NetworkResponse<String>,
    onError: NetworkResponse<Throwable>? = null
) {
    GlobalScope.launch(Dispatchers.IO) {
        call.enqueue(object : Callback<T> {

            override fun onResponse(call: Call<T>, response: Response<T>) {
                if (response.isSuccessful) {
                    onSuccess.onNetworkResponse(response.body(), apiCallTag)
                } else {
                    onFailure.onNetworkResponse(response.message(), apiCallTag)
                }
            }

            override fun onFailure(call: Call<T>, t: Throwable) {
                onError?.onNetworkResponse(t, apiCallTag)
            }

        })
    }
}

interface NetworkResponse<T> {
    fun onNetworkResponse(t: T?, @ApiCallTag callTag: String)
}
