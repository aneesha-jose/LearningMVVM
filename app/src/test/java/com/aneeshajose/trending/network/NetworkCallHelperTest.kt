package com.aneeshajose.trending.network

import com.aneeshajose.trending.assets.repo_succes_json_unit
import com.aneeshajose.trending.base.TestCoroutineContextProvider
import com.aneeshajose.trending.base.TestCoroutineRule
import com.aneeshajose.trending.models.Repo
import com.aneeshajose.trending.network.utils.NetworkResponse
import com.aneeshajose.trending.network.utils.makeNetworkCall
import com.google.common.truth.Truth.assertThat
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import io.mockk.*
import io.mockk.impl.annotations.MockK
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import retrofit2.Response
import retrofit2.mock.Calls

/**
 * Created by Aneesha Jose on 2020-03-26.
 */

@RunWith(JUnit4::class)
class NetworkCallHelperTest {

    @get:Rule
    val coroutineTestRule = TestCoroutineRule()

    private val coroutineProvider = TestCoroutineContextProvider()

    @MockK
    lateinit var apiService: ApiService

    private val success = mockk<NetworkResponse<List<Repo?>?>>(relaxed = true)
    private val failure = mockk<NetworkResponse<String>>(relaxed = true)
    private val error = mockk<NetworkResponse<Throwable>>(relaxed = true)

    @Before
    @Throws(Exception::class)
    fun setUp() {
        MockKAnnotations.init(this)
    }

    @Test
    fun test_dataReturnsNull() {
        every { apiService.fetchRepositories() } answers { Calls.response(Response.success(null as List<Repo?>?)) }
        coroutineTestRule.runBlockingTest {
            makeNetworkCall(
                apiService.fetchRepositories(),
                FETCH_REPOSITORIES,
                coroutineProvider,
                success,
                failure
            )
            verify { success.onNetworkResponse(null, FETCH_REPOSITORIES) }
        }
    }

    @Test
    fun test_dataReturnsEmpty() {
        val emptyList = emptyList<Repo?>()
        every { apiService.fetchRepositories() } answers { Calls.response(Response.success(emptyList)) }
        coroutineTestRule.runBlockingTest {
            makeNetworkCall(
                apiService.fetchRepositories(),
                FETCH_REPOSITORIES,
                coroutineProvider,
                success,
                failure
            )
            verify { success.onNetworkResponse(emptyList, FETCH_REPOSITORIES) }
        }
    }

    @Test
    fun test_dataReturnValidSuccess() {
        val capturedResponse = slot<List<Repo?>>()
        val response = Gson().fromJson<List<Repo?>>(
            repo_succes_json_unit,
            object : TypeToken<List<Repo?>>() {}.type
        )
        every { apiService.fetchRepositories() } answers {
            Calls.response(
                Response.success(response)
            )
        }
        coroutineTestRule.runBlockingTest {
            makeNetworkCall(
                apiService.fetchRepositories(),
                FETCH_REPOSITORIES,
                coroutineProvider,
                success,
                failure
            )
            verify {
                success.onNetworkResponse(
                    capture(capturedResponse),
                    FETCH_REPOSITORIES
                )
            }
            assertThat(capturedResponse.captured).isNotEmpty()
            assertThat(capturedResponse.captured == response).isTrue()
        }
    }

    @Test
    fun test_dataReturnFailure() {
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
        coroutineTestRule.runBlockingTest {
            makeNetworkCall(
                apiService.fetchRepositories(),
                FETCH_REPOSITORIES,
                coroutineProvider,
                success,
                failure
            )
            verify { failure.onNetworkResponse(failureMessage, FETCH_REPOSITORIES) }
        }
    }

    @Test
    fun test_dataReturnError() {
        val throwable = Throwable("error")
        every { apiService.fetchRepositories() } answers {
            Calls.failure(throwable)
        }
        coroutineTestRule.runBlockingTest {
            makeNetworkCall(
                apiService.fetchRepositories(),
                FETCH_REPOSITORIES,
                coroutineProvider,
                success,
                failure,
                error
            )
            verify { error.onNetworkResponse(throwable, FETCH_REPOSITORIES) }
        }
    }
}