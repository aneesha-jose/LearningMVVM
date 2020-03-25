package com.aneeshajose.trending.base.modules

import android.content.Context
import com.aneeshajose.trending.base.qualifiers.ApplicationContext
import com.aneeshajose.trending.base.scopes.ApplicationScope
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import dagger.Module
import dagger.Provides

/**
 * Created by Aneesha Jose on 2020-03-25.
 */
@Module
class GlideModule {

    @ApplicationScope
    @Provides
    fun glideRequestManager(@ApplicationContext context: Context): RequestManager {
        return Glide.with(context)
    }
}