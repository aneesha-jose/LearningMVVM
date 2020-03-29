package com.aneeshajose.trending.base

import com.aneeshajose.trending.base.coroutineHandling.testCoroutineProviderInstance
import com.aneeshajose.trending.base.modules.AppModule
import dagger.Module

/**
 * Created by Aneesha Jose on 2020-03-28.
 */
@Module
class TestAppModule(app: App) : AppModule(app) {

    override fun getCoroutineContext(): CoroutineContextProvider {
        return testCoroutineProviderInstance
    }
}
