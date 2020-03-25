package com.aneeshajose.trending.utility

import android.content.Context
import android.graphics.drawable.Drawable
import androidx.core.graphics.drawable.DrawableCompat
import androidx.work.BackoffPolicy
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import com.aneeshajose.trending.base.App
import com.aneeshajose.trending.common.UPDATE_REPO_WORKER
import com.aneeshajose.trending.network.DataSourceRepository
import com.aneeshajose.trending.network.UpdateRepoWorker
import java.util.concurrent.TimeUnit


/**
 * Created by Aneesha Jose on 2020-03-25.
 */

fun getDataSourceRepositoryFromApp(app: App): DataSourceRepository {
    return app.component.getDataSourceRepository()
}

fun setTint(d: Drawable, color: Int): Drawable {
    val wrappedDrawable = DrawableCompat.wrap(d)
    DrawableCompat.setTint(wrappedDrawable, color)
    return wrappedDrawable
}

fun scheduleUpdateRepoWorker(context: Context) {
    val request = OneTimeWorkRequest.Builder(UpdateRepoWorker::class.java)
        .setBackoffCriteria(BackoffPolicy.EXPONENTIAL, 30, TimeUnit.MINUTES)
        .setInitialDelay(2, TimeUnit.HOURS)
        .build()
    WorkManager.getInstance(context).enqueueUniqueWork(
        UPDATE_REPO_WORKER,
        ExistingWorkPolicy.REPLACE,
        request
    )
}