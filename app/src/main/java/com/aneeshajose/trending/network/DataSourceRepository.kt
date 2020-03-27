package com.aneeshajose.trending.network

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.aneeshajose.trending.R
import com.aneeshajose.trending.base.CoroutineContextProvider
import com.aneeshajose.trending.base.qualifiers.ApplicationContext
import com.aneeshajose.trending.base.scopes.ApplicationScope
import com.aneeshajose.trending.localdata.LocalDataSource
import com.aneeshajose.trending.models.Repo
import com.aneeshajose.trending.models.ResponseWrapper
import com.aneeshajose.trending.network.utils.NetworkResponse
import com.aneeshajose.trending.network.utils.apiCallTracker
import com.aneeshajose.trending.network.utils.makeNetworkCall
import com.aneeshajose.trending.utility.scheduleUpdateRepoWorker
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * Created by Aneesha Jose on 2020-03-24.
 */
@ApplicationScope
class DataSourceRepository @Inject constructor(
    @ApplicationContext val context: Context,
    private val coroutineContextProvider: CoroutineContextProvider,
    private val apiService: ApiService,
    private val localDataSource: LocalDataSource
) {

    fun getRepository(liveData: MutableLiveData<ResponseWrapper<List<Repo>>>) {
        if (!apiCallTracker.contains(FETCH_REPOSITORIES))
            getRepositoriesFromServer(liveData, false)
        getRepositoriesFromLocalData(liveData)
    }

    fun getRepositoriesFromServer(
        liveData: MutableLiveData<ResponseWrapper<List<Repo>>>,
        isForceFetch: Boolean
    ) {
        makeNetworkCall(apiService.fetchRepositories(),
            FETCH_REPOSITORIES,
            coroutineContextProvider,
            onSuccess = object : NetworkResponse<List<Repo?>?> {
                override fun onNetworkResponse(t: List<Repo?>?, callTag: String) {
                    var data = t?.filterNotNull()
                    when {
                        data?.isNotEmpty() == true -> saveInLocalDb(data)
                        //to handle retention of local data in case of not force fetching data
                        !isForceFetch && liveData.value?.body != null -> data = liveData.value?.body
                    }
                    liveData.postValue(ResponseWrapper(data))
                }
            },
            onFailure = object : NetworkResponse<String> {
                override fun onNetworkResponse(t: String?, callTag: String) {
                    liveData.postValue(
                        ResponseWrapper(
                            getDefaultLiveDataValue(
                                liveData,
                                isForceFetch
                            ), t
                        )
                    )
                }

            },
            onError = object : NetworkResponse<Throwable> {
                override fun onNetworkResponse(t: Throwable?, callTag: String) {
                    liveData.postValue(
                        ResponseWrapper(
                            getDefaultLiveDataValue(liveData, isForceFetch),
                            context.getString(R.string.alien_blocking_signal),
                            t
                        )
                    )
                }

            })
    }

    private fun getDefaultLiveDataValue(
        liveData: LiveData<ResponseWrapper<List<Repo>>>,
        forceFetch: Boolean
    ): List<Repo>? {
        return if (forceFetch) null else liveData.value?.body
    }

    private fun getRepositoriesFromLocalData(liveData: MutableLiveData<ResponseWrapper<List<Repo>>>) {
        CoroutineScope(coroutineContextProvider.IO).launch {
            val localData = withContext(coroutineContextProvider.IO) {
                localDataSource.getValidRepos()
            }
            if (localData.isNotEmpty() || liveData.value == null)
                launch(coroutineContextProvider.Main) {
                    liveData.postValue(
                        ResponseWrapper(
                            localData
                        )
                    )
                }
        }
    }

    fun saveInLocalDb(repos: List<Repo>) {
        CoroutineScope(coroutineContextProvider.IO).launch {
            localDataSource.refreshRepositoriesData(repos)
            scheduleUpdateRepoWorker(context)
        }
    }
}