package com.aneeshajose.trending.network

import com.aneeshajose.trending.base.qualifiers.BaseUrl
import com.aneeshajose.trending.base.scopes.ApplicationScope
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Inject

/**
 * Created by Aneesha Jose on 2020-03-22.
 */
@ApplicationScope
class RestClient @Inject constructor(
    client: OkHttpClient,
    gsonConverterFactory: GsonConverterFactory,
    @BaseUrl baseUrl: String
) {

    private val retrofit: Retrofit = Retrofit.Builder()
        .client(client)
        .baseUrl(baseUrl)
        .addConverterFactory(gsonConverterFactory)
        .build()

    fun createApiService(): ApiService {
        return retrofit.create(ApiService::class.java)
    }

}