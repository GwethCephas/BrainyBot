package com.ceph.brainybot.data.mappers

import com.ceph.brainybot.feature_chat.data.dto.Candidate
import com.ceph.brainybot.feature_chat.data.dto.Content
import com.ceph.brainybot.feature_chat.data.dto.GeminiResponseDto
import com.ceph.brainybot.feature_chat.data.dto.Part
import com.ceph.brainybot.feature_chat.domain.model.GeminiResponse
import com.ceph.brainybot.feature_chat.data.mappers.toGeminiResponse
import org.junit.Assert.*
import org.junit.Test

class MapperTest {

    @Test
    fun `toGeminiResponse maps correctly` () {

        val dto = GeminiResponseDto(
            candidates = listOf(
                Candidate(
                    content = Content(parts = listOf(Part("Hello from Gemini")))
                )
            )
        )
        val expectedDomain = GeminiResponse(
            text = "Hello from Gemini"
        )
        val result = dto.toGeminiResponse()

        assertEquals(expectedDomain, result)

    }

    @Test
    fun `toGeminiResponse handles empty response`() {
       val dto = GeminiResponseDto(candidates = emptyList())
        val expectedDomain = GeminiResponse(text = "")

        val result  = dto.toGeminiResponse()

        assertEquals(expectedDomain, result)
    }

}