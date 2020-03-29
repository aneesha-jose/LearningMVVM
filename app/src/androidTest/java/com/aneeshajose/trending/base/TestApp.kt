package com.aneeshajose.trending.base

import com.aneeshajose.trending.assets.repo_succes_json
import com.aneeshajose.trending.base.modules.NetworkModule
import com.aneeshajose.trending.models.Repo
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import io.appflate.restmock.RESTMockServer

/**
 * Created by Aneesha Jose on 2020-03-26.
 */
class TestApp : App() {

    override fun buildAppComponent() {
        component = DaggerAppComponent.builder()
            .appModule(TestAppModule(this))
            .networkModule(NetworkModule(RESTMockServer.getUrl()))
            .build()
    }
}

val success = Gson().fromJson<List<Repo?>>(
    repo_succes_json,
    object : TypeToken<List<Repo?>>() {}.type
)