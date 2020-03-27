package com.aneeshajose.trending.base

import android.content.Context
import com.aneeshajose.trending.base.modules.AppModule
import com.aneeshajose.trending.base.modules.GlideModule
import com.aneeshajose.trending.base.modules.NetworkModule
import com.aneeshajose.trending.base.qualifiers.ApplicationContext
import com.aneeshajose.trending.base.scopes.ApplicationScope
import com.aneeshajose.trending.localdata.LocalDataSource
import com.aneeshajose.trending.network.ApiService
import com.aneeshajose.trending.network.DataSourceRepository
import com.bumptech.glide.RequestManager
import dagger.Component

/**
 * Created by Aneesha Jose on 2020-03-25.
 */
@ApplicationScope
@Component(modules = [AppModule::class, NetworkModule::class, GlideModule::class])
interface AppComponent {

    @ApplicationContext
    fun getContext(): Context

    fun getApiService(): ApiService

    fun getLocalDataSource(): LocalDataSource

    fun getDataSourceRepository(): DataSourceRepository

    fun glideRequestManager(): RequestManager

    fun coroutineContext(): CoroutineContextProvider
}