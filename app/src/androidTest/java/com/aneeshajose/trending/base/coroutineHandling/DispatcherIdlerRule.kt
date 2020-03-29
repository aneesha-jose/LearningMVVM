package com.aneeshajose.trending.base.coroutineHandling

import kotlinx.coroutines.Dispatchers
import org.junit.rules.TestRule
import org.junit.runner.Description
import org.junit.runners.model.Statement

/**
 * Created by Aneesha Jose on 2020-03-28.
 */
class DispatcherIdlerRule : TestRule {
    override fun apply(base: Statement?, description: Description?): Statement =
        object : Statement() {
            override fun evaluate() {
                val espressoTrackedDispatcherIO =
                    EspressoTrackedDispatcher(
                        Dispatchers.IO
                    )
                testCoroutineProviderInstance.IO = espressoTrackedDispatcherIO
                try {
                    base?.evaluate()
                } finally {
                    espressoTrackedDispatcherIO.cleanUp()
                    testCoroutineProviderInstance.IO = Dispatchers.IO
                }
            }
        }
}