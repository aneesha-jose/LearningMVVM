package com.aneeshajose.trending.localdata

import android.content.Context
import com.aneeshajose.trending.base.qualifiers.ApplicationContext
import com.aneeshajose.trending.base.scopes.ApplicationScope
import com.aneeshajose.trending.common.VALID_DATA_TIME_DIFF_IN_MS
import com.aneeshajose.trending.models.Repo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * Created by Aneesha Jose on 2020-03-24.
 */
@ApplicationScope
class LocalDataSource @Inject constructor(@ApplicationContext context: Context) {

    private val appDatabase = AppDatabase.invoke(context)

    suspend fun getValidRepos(): List<Repo> = withContext(Dispatchers.IO) {
        return@withContext appDatabase.getRepositoryDao()
            .fetchValidRepos(System.currentTimeMillis() - VALID_DATA_TIME_DIFF_IN_MS)
    }

    private fun storeRepos(repos: List<Repo>) {
        appDatabase.getRepositoryDao().insert(repos)
    }

    suspend fun refreshRepositoriesData(repos: List<Repo>) = withContext(Dispatchers.IO) {
        appDatabase.getRepositoryDao().clearExistingData()
        storeRepos(repos.onEach { it.createdDate = System.currentTimeMillis() })
    }

}