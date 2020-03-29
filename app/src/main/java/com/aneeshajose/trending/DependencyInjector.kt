package com.aneeshajose.trending

import android.content.Context
import com.aneeshajose.trending.base.AppComponent
import com.aneeshajose.trending.base.CoroutineContextProvider
import com.aneeshajose.trending.base.activity.ActivityModule
import com.aneeshajose.trending.base.qualifiers.ActivityContext
import com.aneeshajose.trending.base.qualifiers.ApplicationContext
import com.aneeshajose.trending.base.scopes.ActivityScope
import com.aneeshajose.trending.displayrepos.DisplayTrendingReposActivity
import com.aneeshajose.trending.localdata.LocalDataSource
import com.aneeshajose.trending.network.ApiService
import com.aneeshajose.trending.network.DataSourceRepository
import com.aneeshajose.trending.splash.SplashActivity
import com.bumptech.glide.RequestManager
import dagger.Component

/**
 * Created by Aneesha Jose on 2020-03-25.
 */
@ActivityScope
@Component(modules = [ActivityModule::class], dependencies = [AppComponent::class])
interface DependencyInjector {

    @ApplicationContext
    fun getContext(): Context

    @ActivityContext
    fun getActivityContext(): Context

    fun getApiService(): ApiService

    fun getLocalDataSource(): LocalDataSource

    fun getDataSourceRepository(): DataSourceRepository

    fun glideRequestManager(): RequestManager

    fun coroutineContext(): CoroutineContextProvider

    fun injectDependencies(activity: SplashActivity)
    fun injectDependencies(activity: DisplayTrendingReposActivity)
}