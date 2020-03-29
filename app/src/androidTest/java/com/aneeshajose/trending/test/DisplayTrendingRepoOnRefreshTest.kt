package com.aneeshajose.trending.test

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.aneeshajose.trending.R
import com.aneeshajose.trending.assets.repo_succes_json
import com.aneeshajose.trending.base.BaseActivityTest
import com.aneeshajose.trending.base.success
import com.aneeshajose.trending.customMatchers.isRefreshing
import com.aneeshajose.trending.customMatchers.isShimmering
import com.aneeshajose.trending.displayrepos.DisplayTrendingReposActivity
import com.aneeshajose.trending.utils.getRecyclerViewItemCount
import com.google.common.truth.Truth
import io.appflate.restmock.RESTMockServer
import io.appflate.restmock.utils.RequestMatchers
import okhttp3.mockwebserver.MockResponse
import org.junit.Test
import org.junit.runner.RunWith
import java.util.concurrent.TimeUnit

/**
 * Created by Aneesha Jose on 2020-03-27.
 */
@RunWith(AndroidJUnit4::class)
@LargeTest
class DisplayTrendingRepoOnRefreshTest : BaseActivityTest<DisplayTrendingReposActivity>() {

    override fun getClassOfActivity(): Class<DisplayTrendingReposActivity> {
        return DisplayTrendingReposActivity::class.java
    }

    @Test
    fun test_SuccessOnRefresh() {
        RESTMockServer.whenGET(RequestMatchers.pathEndsWith("repositories"))
            .delayHeaders(TimeUnit.SECONDS, 2)
            .delayBody(TimeUnit.SECONDS, 2)
            .thenReturn(
                MockResponse().setResponseCode(200).setBody(
                    repo_succes_json
                )
            )
        activityRule.launchActivity(null)

        onView(withId(R.id.swipeRefresh)).check(matches(withEffectiveVisibility(Visibility.VISIBLE)))
        onView(withId(R.id.swipeRefresh)).perform(ViewActions.swipeDown())

        onView(withId(R.id.swipeRefresh)).check(matches(isRefreshing()))
        onView(withId(R.id.shimmerLayout)).check(matches(isShimmering()))
        onView(withId(R.id.shimmerLayout)).check(matches(withEffectiveVisibility(Visibility.VISIBLE)))

        Thread.sleep(4000)

        Truth.assertThat(getRecyclerViewItemCount(R.id.rvRepoList, activityRule))
            .isEqualTo(success.size)

    }

    @Test
    fun test_FailureOnRefresh() {
        RESTMockServer.whenGET(RequestMatchers.pathEndsWith("repositories"))
            .thenReturnEmpty(500)
        activityRule.launchActivity(null)

        onView(withId(R.id.rlErrorLayout)).check(matches(withEffectiveVisibility(Visibility.VISIBLE)))
    }

}