@file:Suppress("LeakingThis")

package com.aneeshajose.trending.base

import androidx.arch.core.executor.testing.CountingTaskExecutorRule
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.platform.app.InstrumentationRegistry
import com.aneeshajose.trending.base.activity.BaseActivity
import com.aneeshajose.trending.localdata.AppDatabase
import io.appflate.restmock.RESTMockServer
import org.junit.After
import org.junit.Before
import org.junit.Rule

/**
 * Created by Aneesha Jose on 2020-03-27.
 */
abstract class BaseActivityTest<T : BaseActivity> {

    var TAG = getClassOfActivity().simpleName

    @get:Rule
    var activityRule = IntentsTestRule(getClassOfActivity(), false, false)

    @get:Rule
    var testRule = CountingTaskExecutorRule()

    abstract fun getClassOfActivity(): Class<T>

    @Before
    fun clearDbData() {
        AppDatabase.invoke(InstrumentationRegistry.getInstrumentation().targetContext)
            .clearAllTables()
        before()
    }

    open fun before() {}

    @After
    @Throws(Exception::class)
    open fun tearDown() {
        RESTMockServer.reset()
        activityRule.finishActivity()
        onTearDown()
    }

    open fun onTearDown() {}
}