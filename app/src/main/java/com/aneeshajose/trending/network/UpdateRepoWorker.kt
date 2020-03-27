package com.aneeshajose.trending.network

import android.content.Context
import androidx.concurrent.futures.CallbackToFutureAdapter
import androidx.work.ListenableWorker
import androidx.work.WorkerParameters
import com.aneeshajose.trending.base.App
import com.aneeshajose.trending.models.Repo
import com.aneeshajose.trending.network.utils.NetworkResponse
import com.aneeshajose.trending.network.utils.apiCallTracker
import com.aneeshajose.trending.network.utils.makeNetworkCall
import com.google.common.util.concurrent.ListenableFuture

/**
 * Created by Aneesha Jose on 2020-03-25.
 */

class UpdateRepoWorker(val context: Context, params: WorkerParameters) :
    ListenableWorker(context, params) {

    override fun startWork(): ListenableFuture<Result> {
        return CallbackToFutureAdapter.getFuture {
            work(it)
        }

    }

    private fun work(futureResult: CallbackToFutureAdapter.Completer<Result>) {
        if (apiCallTracker.contains(FETCH_REPOSITORIES)) {
            futureResult.set(Result.success())
            return
        }
        val component = (context.applicationContext as App).component
        makeNetworkCall(
            component.getApiService().fetchRepositories(),
            FETCH_REPOSITORIES,
            component.coroutineContext(),
            onSuccess = object : NetworkResponse<List<Repo?>?> {
                override fun onNetworkResponse(t: List<Repo?>?, callTag: String) {
                    val data = t?.filterNotNull()
                    if (data?.isNotEmpty() == true) {
                        component.getDataSourceRepository().saveInLocalDb(data)
                        futureResult.set(Result.success())
                    }
                }
            },
            onFailure = object : NetworkResponse<String> {
                override fun onNetworkResponse(t: String?, callTag: String) {
                    futureResult.set(Result.retry())
                }

            },
            onError = object : NetworkResponse<Throwable> {
                override fun onNetworkResponse(t: Throwable?, callTag: String) {
                    futureResult.set(Result.retry())
                }
            })
    }
}