package com.aneeshajose.trending.test

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.aneeshajose.trending.R
import com.aneeshajose.trending.base.BaseActivityTest
import com.aneeshajose.trending.common.SPLASH_DELAY
import com.aneeshajose.trending.splash.SplashActivity
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Created by Aneesha Jose on 2020-03-27.
 */
@RunWith(AndroidJUnit4::class)
@LargeTest
class SplashActivityTest : BaseActivityTest<SplashActivity>() {

    override fun getClassOfActivity(): Class<SplashActivity> {
        return SplashActivity::class.java
    }

    @Test
    fun test_MovesToNextActivityAfterSplashDelay() {
        activityRule.launchActivity(null)

        Thread.sleep(SPLASH_DELAY)

        //To check if next activity started
        onView(withId(R.id.tbBaseToolbar)).check(matches(withEffectiveVisibility(Visibility.VISIBLE)))

    }

}