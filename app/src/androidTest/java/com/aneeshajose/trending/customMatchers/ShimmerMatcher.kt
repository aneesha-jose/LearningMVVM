package com.aneeshajose.trending.customMatchers

import android.view.View
import com.facebook.shimmer.ShimmerFrameLayout
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.TypeSafeMatcher

/**
 * Created by Aneesha Jose on 2020-03-28.
 */
class ShimmerMatcher(private val shouldShimmer: Boolean = false) :
    TypeSafeMatcher<View>(ShimmerFrameLayout::class.java) {

    override fun describeTo(description: Description?) {
        description?.appendText(" should shimmer : ")?.appendValue(shouldShimmer)
    }

    override fun matchesSafely(item: View?): Boolean {
        if (item !is ShimmerFrameLayout) return false

        return item.isShimmerStarted && item.isShimmerVisible == shouldShimmer
    }
}

fun isShimmering(): Matcher<View> {
    return ShimmerMatcher(true)
}

fun isNotShimmering(): Matcher<View> {
    return ShimmerMatcher()
}