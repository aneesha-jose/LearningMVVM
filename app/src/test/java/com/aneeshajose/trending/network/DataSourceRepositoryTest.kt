package com.aneeshajose.trending.network

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.aneeshajose.trending.assets.repo_success_json_unit
import com.aneeshajose.trending.base.TestCoroutineContextProvider
import com.aneeshajose.trending.base.TestCoroutineRule
import com.aneeshajose.trending.localdata.LocalDataSource
import com.aneeshajose.trending.models.Repo
import com.aneeshajose.trending.models.ResponseWrapper
import com.aneeshajose.trending.utility.scheduleUpdateRepoWorker
import com.google.common.truth.Truth.assertThat
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import io.mockk.*
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.test.runBlockingTest
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import retrofit2.Response
import retrofit2.mock.Calls

/**
 * Created by Aneesha Jose on 2020-03-27.
 */
@RunWith(AndroidJUnit4::class)
class DataSourceRepositoryTest {

    @get:Rule
    val testRule = InstantTaskExecutorRule()

    @get:Rule
    val coroutineTestRule = TestCoroutineRule()

    private val response: List<Repo?> = Gson().fromJson<List<Repo?>>(
        repo_success_json_unit,
        object : TypeToken<List<Repo?>>() {}.type
    )

    private val context = ApplicationProvider.getApplicationContext<Context>()

    private val coroutineProvider = TestCoroutineContextProvider()

    lateinit var dataSourceRepository: DataSourceRepository

    @MockK
    lateinit var localDataSource: LocalDataSource

    @MockK
    lateinit var apiService: ApiService

    private val testLiveData = MutableLiveData<ResponseWrapper<List<Repo>>>()

    @Before
    @Throws(Exception::class)
    fun setUp() {
        MockKAnnotations.init(this)
        dataSourceRepository =
            DataSourceRepository(context, coroutineProvider, apiService, localDataSource)
        mockkStatic("com.aneeshajose.trending.utility.ActivityUtilsKt")
        every { scheduleUpdateRepoWorker(any()) } returns Unit
    }

    @Test
    fun test_EmptyData() {
        coroutineTestRule.runBlockingTest {
            every { apiService.fetchRepositories() } answers {
                Calls.response(
                    Response.success(
                        emptyList()
                    )
                )
            }
            coEvery { localDataSource.getValidRepos() } coAnswers { emptyList() }
            dataSourceRepository.getRepository(testLiveData)
            assertThat(testLiveData.value?.body).isEmpty()
        }
    }

    @Test
    fun test_DbDataPresent_FailedServerCall() {
        coroutineTestRule.runBlockingTest {
            val failureMessage = "Not Found"
            val okhttpResponse = mockkClass(okhttp3.Response::class, relaxed = true)
            every { okhttpResponse.message } returns failureMessage
            every { apiService.fetchRepositories() } answers {
                Calls.response(
                    Response.error(
                        failureMessage.toResponseBody("application/json".toMediaTypeOrNull()),
                        okhttpResponse
                    )
                )
            }
            val dbResponse = response.filterNotNull()
            coEvery { localDataSource.getValidRepos() } coAnswers { dbResponse }
            dataSourceRepository.getRepository(testLiveData)
            assertThat(testLiveData.value?.body).isNotEmpty()
            assertThat(testLiveData.value?.body).isSameAs(dbResponse)
        }
    }

    @Test
    fun test_NoDbData_SuccessfulServerCall() {
        coroutineTestRule.runBlockingTest {
            every { apiService.fetchRepositories() } answers {
                Calls.response(
                    Response.success(
                        response
                    )
                )
            }
            coEvery { localDataSource.getValidRepos() } coAnswers { emptyList() }
            coEvery { localDataSource.refreshRepositoriesData(any()) } coAnswers {}
            dataSourceRepository.getRepository(testLiveData)
            assertThat(testLiveData.value?.body).isNotEmpty()
            assertThat(testLiveData.value?.body).isEqualTo(response)
            val slot = CapturingSlot<List<Repo>>()
            verify { dataSourceRepository.saveInLocalDb(capture(slot)) }
            assertThat(slot.captured).isEqualTo(response)
            slot.clear()
            verify { runBlockingTest { localDataSource.refreshRepositoriesData(capture(slot)) } }
            assertThat(slot.captured).isEqualTo(response)
        }
    }

    @Test
    fun test_DifferentDbAndserverData() {
        coroutineTestRule.runBlockingTest {
            val dbData = response.filterNotNull().subList(0, response.size / 2)
            val serverData = response.subList(response.size / 2, response.size)
            every { apiService.fetchRepositories() } answers {
                Calls.response(
                    Response.success(
                        serverData
                    )
                )
            }
            coEvery { localDataSource.getValidRepos() } coAnswers { dbData }
            coEvery { localDataSource.refreshRepositoriesData(any()) } coAnswers {}
            dataSourceRepository.getRepository(testLiveData)
            assertThat(testLiveData.value?.body).isNotEmpty()
            assertThat(testLiveData.value?.body).isEqualTo(serverData)
            val slot = CapturingSlot<List<Repo>>()
            verify { dataSourceRepository.saveInLocalDb(capture(slot)) }
            assertThat(slot.captured).isEqualTo(serverData)
            slot.clear()
            verify { runBlockingTest { localDataSource.refreshRepositoriesData(capture(slot)) } }
            assertThat(slot.captured).isEqualTo(serverData)
        }
    }
}