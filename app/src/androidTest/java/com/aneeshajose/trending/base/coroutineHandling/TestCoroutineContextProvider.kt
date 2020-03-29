package com.aneeshajose.trending.base.coroutineHandling

import com.aneeshajose.trending.base.CoroutineContextProvider
import kotlinx.coroutines.Dispatchers
import kotlin.coroutines.CoroutineContext

/**
 * Created by Aneesha Jose on 2020-03-27.
 */
class TestCoroutineContextProvider : CoroutineContextProvider() {
    override var Main: CoroutineContext = Dispatchers.Main
    override var IO: CoroutineContext = EspressoTrackedDispatcher(Dispatchers.IO)
}

val testCoroutineProviderInstance = TestCoroutineContextProvider()