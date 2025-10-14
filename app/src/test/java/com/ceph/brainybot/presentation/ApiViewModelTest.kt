package com.ceph.brainybot.presentation

import app.cash.turbine.test
import com.ceph.brainybot.feature_chat.data.dto.GeminiRequestDto
import com.ceph.brainybot.feature_chat.domain.model.GeminiResponse
import com.ceph.brainybot.feature_chat.domain.repository.ApiRepository
import com.ceph.brainybot.feature_chat.presentation.home.ChatViewModel
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class ApiViewModelTest {
    private lateinit var repository: ApiRepository
    private lateinit var vieModel: ChatViewModel

    @Before
    fun setUp() {
        repository = mockk()
        vieModel = ChatViewModel(repository)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `generateResponse updates the stateflow`() = runTest {

        val request = GeminiRequestDto(
            emptyList()
        )
        val expectedResponse = GeminiResponse("Hello from Gemini")

        coEvery { repository.generateResponse(request) } returns flowOf(expectedResponse)


        vieModel.response.test {

            // Check the initial value
            assertEquals(GeminiResponse(""), awaitItem())

            //   update the response
            vieModel.generateResponse(request)
            advanceUntilIdle()

            // Check the updated value
            assertEquals(expectedResponse, awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

}