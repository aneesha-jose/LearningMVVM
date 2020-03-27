package com.aneeshajose.trending.models

/**
 * Created by Aneesha Jose on 2020-03-25.
 */
data class ResponseWrapper<T>(
    val body: T? = null,
    val msg: String? = null,
    val throwable: Throwable? = null
) {
    fun hasNoData(): Boolean {
        return body == null && msg == null && throwable == null
    }
}