package com.aneeshajose.trending.displayrepos

import android.os.Bundle
import androidx.lifecycle.*
import androidx.savedstate.SavedStateRegistryOwner
import com.aneeshajose.trending.common.DEFAULT_SEL_ITEM_POSITION
import com.aneeshajose.trending.common.SS_SELCTED_ITEM_POSITION
import com.aneeshajose.trending.models.Repo
import com.aneeshajose.trending.models.ResponseWrapper
import com.aneeshajose.trending.network.DataSourceRepository

/**
 * Created by Aneesha Jose on 2020-03-25.
 */
class TrendingReposViewModel(
    private val dataSourceRepository: DataSourceRepository,
    private val handle: SavedStateHandle
) : ViewModel() {

    private val repos: MutableLiveData<ResponseWrapper<List<Repo>>> by lazy {
        MutableLiveData<ResponseWrapper<List<Repo>>>().also {
            dataSourceRepository.getRepository(it)
        }
    }

    fun loadRepos() {
        dataSourceRepository.getRepositoriesFromServer(repos, true)
    }

    fun getRepos(): LiveData<ResponseWrapper<List<Repo>>> = repos

    fun setclickedItemPosition(position: Int) {
        handle[SS_SELCTED_ITEM_POSITION] = position
    }

    fun getclickedItemPosition(): Int {
        return handle[SS_SELCTED_ITEM_POSITION] ?: DEFAULT_SEL_ITEM_POSITION
    }

    fun sortReposByName(repoList: List<Repo>) {
        repos.postValue(ResponseWrapper(repoList.sortedBy { it.name }))
    }

    fun sortReposByStars(repoList: List<Repo>) {
        repos.postValue(ResponseWrapper(repoList.sortedBy { it.stars }))
    }
}

class TrendingReposViewModelFactory(
    private val dataSourceRepository: DataSourceRepository,
    savedStateRegistryOwner: SavedStateRegistryOwner,
    defaultArgs: Bundle?
) : AbstractSavedStateViewModelFactory(savedStateRegistryOwner, defaultArgs) {

    override fun <T : ViewModel?> create(
        key: String,
        modelClass: Class<T>,
        handle: SavedStateHandle
    ): T {
        return TrendingReposViewModel(dataSourceRepository, handle) as T
    }

}