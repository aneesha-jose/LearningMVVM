package com.aneeshajose.trending.customMatchers

import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.graphics.drawable.Drawable
import android.view.View
import android.widget.TextView
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.TypeSafeMatcher

class DrawableColorMatcher(
    private val color: Int,
    private val mode: PorterDuff.Mode,
    private val forAll: Boolean
) :
    TypeSafeMatcher<View>(View::class.java) {

    override fun matchesSafely(item: View): Boolean {
        return if (item !is TextView) {
            isDrawableColorSameForDrawable(item.background)
        } else {
            isDrawableColorSameForCompound(item)
        }
    }

    private fun isDrawableColorSameForCompound(item: TextView): Boolean {
        return if (forAll) item.compoundDrawables.all { isDrawableColorSameForDrawable(it) }
        else item.compoundDrawables.any { isDrawableColorSameForDrawable(it) }
    }

    private fun isDrawableColorSameForDrawable(background: Drawable): Boolean {
        return PorterDuffColorFilter(
            color,
            mode
        ) == background.colorFilter
    }

    override fun describeTo(description: Description) {
        description.appendText("with drawable color filter is : ").appendValue(color)
    }

}

fun withColorFilter(
    color: Int,
    mode: PorterDuff.Mode,
    forAll: Boolean = false
): Matcher<View> {
    return DrawableColorMatcher(color, mode, forAll)
}