package com.aneeshajose.trending.utility

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.aneeshajose.trending.base.App
import com.aneeshajose.trending.displayrepos.TrendingReposViewModel
import com.aneeshajose.trending.displayrepos.TrendingReposViewModelFactory

/**
 * Created by Aneesha Jose on 2020-03-25.
 */
fun getTrendingReposViewModel(
    activity: AppCompatActivity,
    args: Bundle?,
    app: App
): TrendingReposViewModel {
    return TrendingReposViewModelFactory(
        getDataSourceRepositoryFromApp(app),
        activity,
        args
    ).create(TrendingReposViewModel::class.java)
}