package com.ceph.brainybot.data.repository

import com.ceph.brainybot.feature_chat.data.dto.Candidate
import com.ceph.brainybot.feature_chat.data.dto.Content
import com.ceph.brainybot.feature_chat.data.dto.GeminiRequestDto
import com.ceph.brainybot.feature_chat.data.dto.GeminiResponseDto
import com.ceph.brainybot.feature_chat.data.dto.Part
import com.ceph.brainybot.feature_chat.data.remote.ApiService
import com.ceph.brainybot.feature_chat.domain.model.GeminiResponse
import com.ceph.brainybot.feature_chat.data.repository.ApiRepositoryImpl
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class ApiRepositoryImplTest {

    private lateinit var repository: ApiRepositoryImpl
    private val apiService: ApiService = mockk()

    @Before
    fun setUp() {
        repository = ApiRepositoryImpl(apiService)

    }

    @Test
    fun `generatedResponse returns mapped domain object`() = runTest {

        val request = GeminiRequestDto(
            contents = listOf(
                Content(parts = listOf(Part("Hello AI")))
            )
        )
        val fakeDto = GeminiResponseDto(
            candidates = listOf(
                Candidate(
                    content = Content(parts = listOf(Part("Hello from Gemini")))
                )
            )
        )
        val expectedDomain = GeminiResponse(
            text = "Hello from Gemini"
        )

        coEvery { apiService.generateResponse(request = request) } returns fakeDto

        val result = repository.generateResponse(request).first()

        assertEquals(expectedDomain, result)
    }

    @Test
    fun `generateResponse throws error when request fails`() = runTest {
        val request = GeminiRequestDto(listOf())

        coEvery { apiService.generateResponse(request = request) } throws RuntimeException("Network error")

        assertThrows(RuntimeException::class.java) {
            runTest {
                repository.generateResponse(request).first()
            }
        }
    }


}