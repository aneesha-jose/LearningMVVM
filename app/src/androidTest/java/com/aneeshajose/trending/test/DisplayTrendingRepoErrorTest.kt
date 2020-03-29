package com.aneeshajose.trending.test

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.aneeshajose.trending.R
import com.aneeshajose.trending.base.BaseActivityTest
import com.aneeshajose.trending.customMatchers.DrawableMatcher
import com.aneeshajose.trending.customMatchers.isRefreshing
import com.aneeshajose.trending.displayrepos.DisplayTrendingReposActivity
import io.appflate.restmock.RESTMockServer
import io.appflate.restmock.utils.RequestMatchers
import org.junit.Test
import org.junit.runner.RunWith
import java.util.concurrent.TimeUnit

/**
 * Created by Aneesha Jose on 2020-03-27.
 */
@RunWith(AndroidJUnit4::class)
@LargeTest
class DisplayTrendingRepoErrorTest : BaseActivityTest<DisplayTrendingReposActivity>() {

    override fun getClassOfActivity(): Class<DisplayTrendingReposActivity> {
        return DisplayTrendingReposActivity::class.java
    }

    @Test
    fun test_FailureResponseFromServer() {
        RESTMockServer.whenGET(RequestMatchers.pathEndsWith("repositories"))
            .thenReturnFile(404, "Server Error")
        activityRule.launchActivity(null)

        Thread.sleep(3000)

        onView(withId(R.id.rlErrorLayout)).check(matches(withEffectiveVisibility(Visibility.VISIBLE)))

        onView(withId(R.id.ivError)).check(matches(isCompletelyDisplayed()))
        onView(withId(R.id.ivError)).check(matches(DrawableMatcher.withDrawable(R.drawable.nointernet_connection)))

        onView(withId(R.id.tvErrorHeader)).check(matches(isCompletelyDisplayed()))
        onView(withId(R.id.tvErrorHeader)).check(matches(withText(R.string.something_went_wrong)))

        onView(withId(R.id.tvErrorDescription)).check(matches(isCompletelyDisplayed()))
        onView(withId(R.id.tvErrorDescription)).check(matches(withText("Server Error")))

    }

    @Test
    fun test_ErrorResponseFromServer() {
        RESTMockServer.whenGET(RequestMatchers.pathEndsWith("repositories"))
            .delayBody(TimeUnit.SECONDS, 2)
            .thenReturnString("123456")// error string

        activityRule.launchActivity(null)

        Thread.sleep(3000)

        onView(withId(R.id.rlErrorLayout)).check(matches(withEffectiveVisibility(Visibility.VISIBLE)))

        onView(withId(R.id.ivError)).check(matches(isCompletelyDisplayed()))
        onView(withId(R.id.ivError)).check(matches(DrawableMatcher.withDrawable(R.drawable.nointernet_connection)))

        onView(withId(R.id.tvErrorHeader)).check(matches(isCompletelyDisplayed()))
        onView(withId(R.id.tvErrorHeader)).check(matches(withText(R.string.something_went_wrong)))

        onView(withId(R.id.tvErrorDescription)).check(matches(isCompletelyDisplayed()))
        onView(withId(R.id.tvErrorDescription)).check(matches(withText(R.string.alien_blocking_signal)))

        onView(withId(R.id.btnRetry)).check(matches(isCompletelyDisplayed()))
        onView(withId(R.id.btnRetry)).perform(click())

        //Validating call is triggered
        onView(withId(R.id.swipeRefresh)).check(matches(isRefreshing()))

    }

}