package com.ceph.brainybot.feature_chat.data.remote

import com.ceph.brainybot.feature_chat.data.dto.GeminiRequestDto
import com.ceph.brainybot.feature_chat.data.dto.GeminiResponseDto
import com.ceph.brainybot.feature_chat.data.utils.Constants.API_KEY
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiService {

    @POST("models/gemini-2.0-flash:generateContent")
    suspend fun generateResponse(
        @Query("key") apiKey: String = API_KEY,
        @Body request: GeminiRequestDto
    ): GeminiResponseDto

}