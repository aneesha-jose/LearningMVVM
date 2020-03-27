package com.aneeshajose.trending.base

import com.aneeshajose.trending.base.scopes.ApplicationScope
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

/**
 * Created by Aneesha Jose on 2020-03-27.
 */
@ApplicationScope
open class CoroutineContextProvider @Inject constructor() {
    open val Main: CoroutineContext by lazy { Dispatchers.Main }
    open val IO: CoroutineContext by lazy { Dispatchers.IO }
}