package com.aneeshajose.trending.network

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.aneeshajose.trending.assets.repo_succes_json_unit
import com.aneeshajose.trending.base.TestCoroutineContextProvider
import com.aneeshajose.trending.base.TestCoroutineRule
import com.aneeshajose.trending.localdata.LocalDataSource
import com.aneeshajose.trending.models.Repo
import com.aneeshajose.trending.models.ResponseWrapper
import com.google.common.truth.Truth.assertThat
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockkClass
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
class DataSourceRepositoryUnitTest {

    @get:Rule
    val testRule = InstantTaskExecutorRule()

    @get:Rule
    val coroutineTestRule = TestCoroutineRule()

    private val context = ApplicationProvider.getApplicationContext<Context>()

    private val coroutineProvider = TestCoroutineContextProvider()

    lateinit var dataSourceRepository: DataSourceRepository

    @MockK
    lateinit var localDataSource: LocalDataSource

    @MockK
    lateinit var apiService: ApiService

    @MockK
    lateinit var observer: Observer<ResponseWrapper<List<Repo>>>

    private val testLiveData = MutableLiveData<ResponseWrapper<List<Repo>>>()

    @Before
    @Throws(Exception::class)
    fun setUp() {
        MockKAnnotations.init(this)
        dataSourceRepository =
            DataSourceRepository(context, coroutineProvider, apiService, localDataSource)
        testLiveData.observeForever { observer }
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
            assertThat(testLiveData.hasActiveObservers()).isTrue()
            assertThat(testLiveData.hasObservers()).isTrue()
            assertThat(testLiveData.value?.body).isEmpty()
        }
    }

    @Test
    fun test_DbDataOnFailedServerCall() {
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
            val response = Gson().fromJson<List<Repo>>(
                repo_succes_json_unit,
                object : TypeToken<List<Repo>>() {}.type
            )
            coEvery { localDataSource.getValidRepos() } coAnswers { response }
            dataSourceRepository.getRepository(testLiveData)
            assertThat(testLiveData.value?.body).isNotEmpty()
            assertThat(testLiveData.value?.body).isSameAs(response)
        }
    }
}