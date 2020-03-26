package com.aneeshajose.trending.base.modules

import com.aneeshajose.trending.BuildConfig
import com.aneeshajose.trending.base.qualifiers.BaseUrl
import com.aneeshajose.trending.base.scopes.ApplicationScope
import com.aneeshajose.trending.network.ApiService
import com.aneeshajose.trending.network.RestClient
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Created by Aneesha Jose on 2020-03-24.
 */
@Module
class NetworkModule(val providedBaseUrl: String) {

    @ApplicationScope
    @Provides
    fun getOkHttpClient(loggingInterceptor: HttpLoggingInterceptor): OkHttpClient {
        return OkHttpClient.Builder().addInterceptor(loggingInterceptor).build()
    }

    @ApplicationScope
    @Provides
    fun httpLoggingInterceptor(level: HttpLoggingInterceptor.Level): HttpLoggingInterceptor {
        val interceptor = HttpLoggingInterceptor()
        interceptor.setLevel(level)
        return interceptor
    }

    @ApplicationScope
    @Provides
    fun logLevel(): HttpLoggingInterceptor.Level {
        return if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE
    }

    @ApplicationScope
    @Provides
    fun gsonConverterFactory(): GsonConverterFactory {
        return GsonConverterFactory.create(GsonBuilder().create())
    }

    @ApplicationScope
    @Provides
    fun getApiService(restClient: RestClient): ApiService {
        return restClient.createApiService()
    }

    @ApplicationScope
    @Provides
    @BaseUrl
    fun getBaseUrl(): String {
        return providedBaseUrl
    }

}