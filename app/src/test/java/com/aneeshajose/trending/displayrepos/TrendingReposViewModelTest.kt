package com.aneeshajose.trending.displayrepos

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import androidx.lifecycle.SavedStateHandle
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.aneeshajose.trending.assets.repo_succes_json_unit
import com.aneeshajose.trending.base.TestCoroutineContextProvider
import com.aneeshajose.trending.base.TestCoroutineRule
import com.aneeshajose.trending.localdata.LocalDataSource
import com.aneeshajose.trending.models.Repo
import com.aneeshajose.trending.models.ResponseWrapper
import com.aneeshajose.trending.network.ApiService
import com.aneeshajose.trending.network.DataSourceRepository
import com.aneeshajose.trending.utility.scheduleUpdateRepoWorker
import com.google.common.truth.Truth.assertThat
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import io.mockk.*
import io.mockk.impl.annotations.MockK
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import retrofit2.mock.Calls


/**
 * Created by Aneesha Jose on 2020-03-26.
 */
@RunWith(AndroidJUnit4::class)
class TrendingReposViewModelTest {

    @get:Rule
    val testRule = InstantTaskExecutorRule()

    @get:Rule
    val coroutineTestRule = TestCoroutineRule()

    private val response: List<Repo?> = Gson().fromJson<List<Repo?>>(
        repo_succes_json_unit,
        object : TypeToken<List<Repo?>>() {}.type
    )

    private val coroutineProvider = TestCoroutineContextProvider()

    private val context = ApplicationProvider.getApplicationContext<Context>()

    private lateinit var viewModel: TrendingReposViewModel

    lateinit var dataSourceRepo: DataSourceRepository

    @MockK
    lateinit var localDataSource: LocalDataSource

    @MockK
    lateinit var apiService: ApiService

    @MockK
    lateinit var handle: SavedStateHandle

    @MockK
    lateinit var observer: Observer<ResponseWrapper<List<Repo>>>

    @Before
    @Throws(Exception::class)
    fun setUp() {
        MockKAnnotations.init(this)
        dataSourceRepo = DataSourceRepository(
            context,
            coroutineProvider,
            apiService,
            localDataSource
        )
        viewModel = TrendingReposViewModel(dataSourceRepo, handle)
        mockkStatic("com.aneeshajose.trending.utility.ActivityUtilsKt")
        every { scheduleUpdateRepoWorker(any()) } returns Unit
    }

    @Test
    fun test_dataReturnsNull() {
        coroutineTestRule.runBlockingTest {
            coEvery { localDataSource.getValidRepos() }.coAnswers { emptyList() }
            val failureMessage = "Not Found"
            val okhttpResponse = mockkClass(Response::class, relaxed = true)
            every { okhttpResponse.message } returns failureMessage
            every { apiService.fetchRepositories() } answers {
                Calls.response(
                    retrofit2.Response.error(
                        failureMessage.toResponseBody("application/json".toMediaTypeOrNull()),
                        okhttpResponse
                    )
                )
            }
            val liveData = viewModel.getRepos()
            assertThat(liveData.value).isNotNull()
            assertThat(liveData.value?.body).isNull()
            assertThat(liveData.value?.msg).isSameAs(failureMessage)
        }
    }

    @Test
    fun test_DbDataPresent_FailedServerCall() {
        coroutineTestRule.runBlockingTest {
            val failureMessage = "Not Found"
            val okhttpResponse = mockkClass(Response::class, relaxed = true)
            every { okhttpResponse.message } returns failureMessage
            every { apiService.fetchRepositories() } answers {
                Calls.response(
                    retrofit2.Response.error(
                        failureMessage.toResponseBody("application/json".toMediaTypeOrNull()),
                        okhttpResponse
                    )
                )
            }
            val dbResponse = response.filterNotNull()
            coEvery { localDataSource.getValidRepos() } coAnswers { dbResponse }
            val liveData = viewModel.getRepos()
            assertThat(liveData.value?.body).isNotEmpty()
            assertThat(liveData.value?.body).isSameAs(dbResponse)
        }
    }

    @Test
    fun test_NoDbData_SuccessfulServerCall() {
        coroutineTestRule.runBlockingTest {
            every { apiService.fetchRepositories() } answers {
                Calls.response(
                    retrofit2.Response.success(
                        response
                    )
                )
            }
            coEvery { localDataSource.getValidRepos() } coAnswers { emptyList() }
            coEvery { localDataSource.refreshRepositoriesData(any()) } coAnswers {}
            val liveData = viewModel.getRepos()
            assertThat(liveData.value?.body).isNotEmpty()
            assertThat(liveData.value?.body).isEqualTo(response)
        }
    }

    @Test
    fun test_DifferentDbAndserverData() {
        coroutineTestRule.runBlockingTest {
            val dbData = response.filterNotNull().subList(0, response.size / 2)
            val serverData = response.subList(response.size / 2, response.size)
            every { apiService.fetchRepositories() } answers {
                Calls.response(
                    retrofit2.Response.success(
                        serverData
                    )
                )
            }
            coEvery { localDataSource.getValidRepos() } coAnswers { dbData }
            coEvery { localDataSource.refreshRepositoriesData(any()) } coAnswers {}
            val liveData = viewModel.getRepos()
            assertThat(liveData.value?.body).isNotEmpty()
            assertThat(liveData.value?.body).isEqualTo(serverData)
        }
    }


}