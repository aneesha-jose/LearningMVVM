package com.aneeshajose.trending.network

import androidx.annotation.StringDef

/**
 * Created by Aneesha Jose on 2020-03-24.
 */

const val FETCH_REPOSITORIES = "FetchRepositories"

@Retention(AnnotationRetention.SOURCE)
@StringDef(FETCH_REPOSITORIES)
annotation class ApiCallTag