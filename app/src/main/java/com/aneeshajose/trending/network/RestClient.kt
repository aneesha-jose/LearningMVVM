package com.aneeshajose.trending.network

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Inject

/**
 * Created by Aneesha Jose on 2020-03-22.
 */
class RestClient @Inject constructor(
    client: OkHttpClient,
    gsonConverterFactory: GsonConverterFactory
) {

    private val retrofit: Retrofit = Retrofit.Builder()
        .client(client)
        .baseUrl(BASE_URL)
        .addConverterFactory(gsonConverterFactory)
        .build()

    fun createApiService(): ApiService {
        return retrofit.create(ApiService::class.java)
    }

}