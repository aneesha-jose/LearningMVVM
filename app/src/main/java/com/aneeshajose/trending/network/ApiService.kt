package com.aneeshajose.trending.network

import com.aneeshajose.trending.models.Repo
import retrofit2.Call
import retrofit2.http.GET

/**
 * Created by Aneesha Jose on 2020-03-22.
 */
interface ApiService {

    @GET("repositories")
    fun fetchRepositories(): Call<List<Repo?>?>

}