package com.ceph.brainybot.feature_chat.data.repository

import android.util.Log
import com.ceph.brainybot.feature_chat.data.dto.GeminiRequestDto
import com.ceph.brainybot.feature_chat.data.mappers.toGeminiResponse
import com.ceph.brainybot.feature_chat.data.remote.ApiService
import com.ceph.brainybot.feature_chat.domain.model.GeminiResponse
import com.ceph.brainybot.feature_chat.domain.repository.ApiRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class ApiRepositoryImpl(
    private val apiService: ApiService
): ApiRepository {

    override suspend fun generateResponse(
        request: GeminiRequestDto
    ): Flow<GeminiResponse>{
        return flow {
            try {
                val response  = apiService.generateResponse(request = request)
                Log.d("ApiRepositoryImpl", "generateResponse: $response")
                emit( response.toGeminiResponse())

            } catch (e: Exception) {
                Log.e("ApiRepositoryImpl", "generateResponse: ${e.message}")
                throw e
            }
        }

    }
}