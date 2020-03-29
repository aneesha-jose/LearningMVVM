package com.aneeshajose.trending.test

import android.graphics.Color
import android.graphics.PorterDuff
import android.view.View
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.doesNotExist
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.aneeshajose.trending.R
import com.aneeshajose.trending.assets.repo_succes_json
import com.aneeshajose.trending.base.BaseActivityTest
import com.aneeshajose.trending.base.success
import com.aneeshajose.trending.customMatchers.nthChildOf
import com.aneeshajose.trending.customMatchers.withColorFilter
import com.aneeshajose.trending.displayrepos.DisplayTrendingReposActivity
import com.aneeshajose.trending.displayrepos.VH_Repo
import com.aneeshajose.trending.models.Repo
import com.aneeshajose.trending.utils.getRecyclerViewItemCount
import com.google.common.truth.Truth
import io.appflate.restmock.RESTMockServer
import io.appflate.restmock.utils.RequestMatchers
import okhttp3.mockwebserver.MockResponse
import org.hamcrest.CoreMatchers.allOf
import org.hamcrest.CoreMatchers.not
import org.hamcrest.Matcher
import org.junit.Test
import org.junit.runner.RunWith
import kotlin.random.Random

/**
 * Created by Aneesha Jose on 2020-03-27.
 */
@RunWith(AndroidJUnit4::class)
@LargeTest
class DisplayTrendingRepoSuccessTest : BaseActivityTest<DisplayTrendingReposActivity>() {

    override fun getClassOfActivity(): Class<DisplayTrendingReposActivity> {
        return DisplayTrendingReposActivity::class.java
    }

    @Test
    fun test_SuccessResponseFromServer() {
        RESTMockServer.whenGET(RequestMatchers.pathEndsWith("repositories"))
            .thenReturn(
                MockResponse().setResponseCode(200).setBody(
                    repo_succes_json
                )
            )
        activityRule.launchActivity(null)

        Thread.sleep(4000)

        onView(withId(R.id.shimmerLayout)).check(matches(withEffectiveVisibility(Visibility.GONE)))
        onView(withId(R.id.rvRepoList)).check(matches(withEffectiveVisibility(Visibility.VISIBLE)))

        Truth.assertThat(getRecyclerViewItemCount(R.id.rvRepoList, activityRule))
            .isEqualTo(success.size)

//        assertHandlingOfLastItemInRV()

        val position = Random.nextInt(0, success.size - 1)
        onView(withId(R.id.rvRepoList)).perform(
            RecyclerViewActions.scrollToPosition<VH_Repo>(
                position
            )
        )
        val childItem = success[position]
        Truth.assertThat(childItem).isNotNull()
        val childView = nthChildOf(withId(R.id.rvRepoList), position)
        Truth.assertThat(childView).isNotNull()
        assertHandlingOfRandomItemInRV(position, childView!!, childItem!!)

    }

    private fun assertHandlingOfLastItemInRV() {
        val lastPosition = success.size - 1
        val childItem = success[lastPosition]
        Truth.assertThat(childItem).isNotNull()
        val childView = nthChildOf(withId(R.id.rvRepoList), (lastPosition))
        Truth.assertThat(childView).isNotNull()

        onView(childView).check(doesNotExist())
        onView(withId(R.id.rvRepoList)).perform(
            RecyclerViewActions.scrollToPosition<VH_Repo>(
                lastPosition
            )
        )
        onView(childView).check(matches(isCompletelyDisplayed()))

        assertNonExpandedViewOfChild(childView!!, childItem!!)

        assertHandlingOfRandomItemInRV(lastPosition, childView, childItem)

    }

    private fun assertHandlingOfRandomItemInRV(
        position: Int,
        childView: Matcher<View?>,
        childItem: Repo
    ) {

        assertNonExpandedViewOfChild(childView, childItem)

        onView(withId(R.id.rvRepoList)).perform(
            RecyclerViewActions.actionOnItemAtPosition<VH_Repo>(
                position
                , click()
            )
        )

        Thread.sleep(2000)
        onView(withId(R.id.rvRepoList)).perform(
            RecyclerViewActions.scrollToPosition<VH_Repo>(
                position
            )
        )

        assertExpandedViewOfChild(childView, childItem)
    }

    private fun assertNonExpandedViewOfChild(childView: Matcher<View?>, childItem: Repo) {
        //check displayed data
        childView.matches(hasDescendant(withId(R.id.cvCircularImage)))


        childView.matches(hasDescendant(withId(R.id.tvItemAuthor)))
        onView(withText(childItem.author)).check(matches(isDisplayed()))

        childView.matches(hasDescendant(withId(R.id.tvItemHeader)))
        onView(withText(childItem.name)).check(matches(isDisplayed()))

        //should be not displayed
        childView.matches(not(hasDescendant(withId(R.id.tvItemDesc))))
        onView(withText(childItem.description)).check(matches(not(isDisplayed())))

        childView.matches(not(hasDescendant(withId(R.id.tvLanguage))))
        childView.matches(not(hasDescendant(withId(R.id.tvStars))))
        childView.matches(not(hasDescendant(withId(R.id.tvForks))))

    }

    private fun assertExpandedViewOfChild(childView: Matcher<View?>, childItem: Repo) {
        childView.matches(isCompletelyDisplayed())

        //check displayed data
        childView.matches(hasDescendant(withId(R.id.cvCircularImage)))

        childView.matches(hasDescendant(withId(R.id.tvItemAuthor)))
        childView.matches(hasDescendant(withText(childItem.author)))

        childView.matches(hasDescendant(withId(R.id.tvItemHeader)))
        childView.matches(hasDescendant(withText(childItem.name)))

        childView.matches(hasDescendant(withId(R.id.tvItemDesc)))
        childView.matches(hasDescendant(withText(childItem.description)))

        childView.matches(hasDescendant(withId(R.id.tvLanguage)))
        childView.matches(hasDescendant(withText(childItem.language)))
        onView(allOf(withText(childItem.language), isDescendantOfA(childView))).check(
            matches(
                withColorFilter(
                    Color.parseColor(
                        childItem.languageColor ?: ""
                    ), PorterDuff.Mode.SRC_ATOP
                )
            )
        )

        childView.matches(hasDescendant(withId(R.id.tvStars)))
        childView.matches(hasDescendant(withText(childItem.stars.toString())))


        childView.matches(hasDescendant(withId(R.id.tvForks)))
        childView.matches(hasDescendant(withText(childItem.forks.toString())))
    }

}