package com.aneeshajose.trending.base.modules

import android.app.Application
import android.content.Context
import com.aneeshajose.trending.base.qualifiers.ApplicationContext
import com.aneeshajose.trending.base.scopes.ApplicationScope
import dagger.Module
import dagger.Provides

/**
 * Created by Aneesha Jose on 2020-03-24.
 */
@Module
class AppModule(val app: Application) {

    @ApplicationScope
    @ApplicationContext
    @Provides
    fun getContext():Context{
        return app
    }

}