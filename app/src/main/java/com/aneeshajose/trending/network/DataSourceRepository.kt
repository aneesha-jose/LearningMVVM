package com.aneeshajose.trending.network

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.aneeshajose.trending.R
import com.aneeshajose.trending.base.qualifiers.ApplicationContext
import com.aneeshajose.trending.base.scopes.ApplicationScope
import com.aneeshajose.trending.localdata.LocalDataSource
import com.aneeshajose.trending.models.Repo
import com.aneeshajose.trending.models.ResponseWrapper
import com.aneeshajose.trending.network.utils.NetworkResponse
import com.aneeshajose.trending.network.utils.makeNetworkCall
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * Created by Aneesha Jose on 2020-03-24.
 */
@ApplicationScope
class DataSourceRepository @Inject constructor(
    @ApplicationContext val context: Context,
    private val apiService: ApiService,
    private val localDataSource: LocalDataSource
) {

    private val apiCallTracker = mutableListOf<String>()

    fun getRepository(): MutableLiveData<ResponseWrapper<List<Repo>>> {
        val liveData = MutableLiveData<ResponseWrapper<List<Repo>>>()
        if (!apiCallTracker.contains(FETCH_REPOSITORIES))
            getRepositoriesFromServer(liveData)
        getRepositoriesFromLocalData(liveData)
        return liveData
    }

    private fun getRepositoriesFromServer(liveData: MutableLiveData<ResponseWrapper<List<Repo>>>) {
        apiCallTracker.add(FETCH_REPOSITORIES)
        makeNetworkCall(apiService.fetchRepositories(), FETCH_REPOSITORIES,
            onSuccess = object : NetworkResponse<List<Repo?>?> {
                override fun onNetworkResponse(t: List<Repo?>?, callTag: String) {
                    apiCallTracker.remove(callTag)
                    val data = t?.filterNotNull()
                    if (data?.isNotEmpty() == true) {
                        saveInLocalDb(data)
                    }
                    liveData.postValue(ResponseWrapper(data))
                }
            },
            onFailure = object : NetworkResponse<String> {
                override fun onNetworkResponse(t: String?, callTag: String) {
                    apiCallTracker.remove(callTag)
                    liveData.postValue(ResponseWrapper(null, t))
                }

            },
            onError = object : NetworkResponse<Throwable> {
                override fun onNetworkResponse(t: Throwable?, callTag: String) {
                    apiCallTracker.remove(callTag)
                    liveData.postValue(
                        ResponseWrapper(
                            null,
                            context.getString(R.string.alien_blocking_signal),
                            t
                        )
                    )
                }

            })
    }

    private fun getRepositoriesFromLocalData(liveData: MutableLiveData<ResponseWrapper<List<Repo>>>) {
        GlobalScope.launch {
            val localData = withContext(Dispatchers.Default) {
                localDataSource.getValidRepos()
            }
            if (localData.isNotEmpty()) liveData.value = ResponseWrapper(localData)
        }
    }

    private fun saveInLocalDb(repos: List<Repo>) {
        GlobalScope.launch {
            localDataSource.refreshRepositoriesData(repos)
        }
    }
}