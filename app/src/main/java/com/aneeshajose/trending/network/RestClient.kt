package com.aneeshajose.trending.network

import com.aneeshajose.trending.base.scopes.ApplicationScope
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import javax.inject.Inject

/**
 * Created by Aneesha Jose on 2020-03-22.
 */
@ApplicationScope
class RestClient @Inject constructor(client: OkHttpClient) {

    private val retrofit: Retrofit = Retrofit.Builder()
        .client(client)
        .baseUrl(BASE_URL)
        .build()

    fun createApiService(): ApiService {
        return retrofit.create(ApiService::class.java)
    }

}