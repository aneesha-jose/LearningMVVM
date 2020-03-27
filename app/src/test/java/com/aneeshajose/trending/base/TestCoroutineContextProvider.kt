package com.aneeshajose.trending.base

import kotlinx.coroutines.Dispatchers
import kotlin.coroutines.CoroutineContext

/**
 * Created by Aneesha Jose on 2020-03-27.
 */
class TestCoroutineContextProvider : CoroutineContextProvider() {
    override val Main: CoroutineContext = Dispatchers.Unconfined
    override val IO: CoroutineContext = Dispatchers.Unconfined
}