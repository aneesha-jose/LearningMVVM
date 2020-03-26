package com.aneeshajose.trending.base

import com.aneeshajose.trending.base.modules.AppModule
import com.aneeshajose.trending.base.modules.NetworkModule
import io.appflate.restmock.RESTMockServer

/**
 * Created by Aneesha Jose on 2020-03-26.
 */
class TestApp : App() {
    override fun buildAppComponent() {
        component = DaggerAppComponent.builder()
            .appModule(AppModule(this))
            .networkModule(NetworkModule(RESTMockServer.getUrl()))
            .build()
    }
}