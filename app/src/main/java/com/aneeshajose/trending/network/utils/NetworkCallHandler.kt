package com.aneeshajose.trending.network.utils

import androidx.core.util.Consumer

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
fun <T> makeNetworkCall(call: Call<T>, onSuccess: Consumer<T>, onFailure: Consumer<String>, onError: Consumer<Throwable>? = null ){
    call.enqueue(object : Callback<T>{

        override fun onResponse(call: Call<T>, response: Response<T>) {
            if(response.isSuccessful) {
                onSuccess.accept(response.body())
            } else {
                onFailure.accept(response.message())
            }
        }

        override fun onFailure(call: Call<T>, t: Throwable) {
            onError?.accept(t)
        }

    })
}
