package com.aneeshajose.trending.base.activity

import android.content.Context
import com.aneeshajose.trending.base.qualifiers.ActivityContext
import com.aneeshajose.trending.base.scopes.ActivityScope
import dagger.Module
import dagger.Provides

/**
 * Created by Aneesha Jose on 2020-03-25.
 */
@Module
class ActivityModule(private val activity: BaseActivity) {

    @ActivityScope
    @ActivityContext
    @Provides
    fun activityContext(): Context {
        return activity
    }
}