package com.aneeshajose.trending.customMatchers

import android.view.View
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.TypeSafeMatcher

/**
 * Created by Aneesha Jose on 2020-03-28.
 */
class SwipeRefreshMatcher(private val shuuldBeRefresing: Boolean = false) :
    TypeSafeMatcher<View>(SwipeRefreshLayout::class.java) {

    override fun describeTo(description: Description?) {
        description?.appendText(" should shimmer : ")?.appendValue(shuuldBeRefresing)
    }

    override fun matchesSafely(item: View?): Boolean {
        if (item !is SwipeRefreshLayout) return false

        return item.isRefreshing == shuuldBeRefresing
    }
}

fun isRefreshing(): Matcher<View> {
    return SwipeRefreshMatcher(true)
}

fun isNotRefreshing(): Matcher<View> {
    return SwipeRefreshMatcher()
}