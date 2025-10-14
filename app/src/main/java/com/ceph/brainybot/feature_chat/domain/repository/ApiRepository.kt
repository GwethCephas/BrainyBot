package com.ceph.brainybot.feature_chat.domain.repository

import com.ceph.brainybot.feature_chat.data.dto.GeminiRequestDto
import com.ceph.brainybot.feature_chat.domain.model.GeminiResponse
import kotlinx.coroutines.flow.Flow

interface ApiRepository {
    suspend fun generateResponse(request: GeminiRequestDto): Flow<GeminiResponse>

}