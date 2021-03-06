package com.aneeshajose.trending.base.coroutineHandling

import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.idling.CountingIdlingResource
import kotlinx.coroutines.CoroutineDispatcher
import kotlin.coroutines.CoroutineContext

/**
 * Created by Aneesha Jose on 2020-03-28.
 */
class EspressoTrackedDispatcher(private val wrappedCoroutineDispatcher: CoroutineDispatcher) :
    CoroutineDispatcher() {
    private val counter: CountingIdlingResource =
        CountingIdlingResource("EspressoTrackedDispatcher for $wrappedCoroutineDispatcher")

    init {
        IdlingRegistry.getInstance().register(counter)
    }

    override fun dispatch(context: CoroutineContext, block: Runnable) {
        counter.increment()
        val blockWithDecrement = Runnable {
            try {
                block.run()
            } finally {
                counter.decrement()
            }
        }
        wrappedCoroutineDispatcher.dispatch(context, blockWithDecrement)
    }

    fun cleanUp() {
        IdlingRegistry.getInstance().unregister(counter)
    }
}