package com.ceph.brainybot.feature_chat.data.mappers

import com.ceph.brainybot.feature_chat.data.dto.GeminiResponseDto
import com.ceph.brainybot.feature_chat.domain.model.GeminiResponse

fun GeminiResponseDto.toGeminiResponse(): GeminiResponse {
    val candidateText = candidates
        .firstOrNull()
        ?.content?.parts?.firstOrNull()
        ?.text ?: ""
    return GeminiResponse(text = candidateText)
}