package com.aneeshajose.trending.utils

import android.app.Activity
import android.view.View
import androidx.annotation.IdRes
import androidx.recyclerview.widget.RecyclerView
import androidx.test.rule.ActivityTestRule

/**
 * Created by Aneesha Jose on 2020-03-28.
 */

fun <T : Activity> getRecyclerViewItemCount(@IdRes rvId: Int, testRule: ActivityTestRule<T>): Int? {
    val view = testRule.activity.findViewById(rvId) as View
    return if (view is RecyclerView)
        view.adapter?.itemCount
    else
        null
}