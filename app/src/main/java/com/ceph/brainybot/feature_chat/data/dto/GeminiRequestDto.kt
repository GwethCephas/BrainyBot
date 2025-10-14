package com.ceph.brainybot.feature_chat.data.dto

data class GeminiRequestDto(
    val contents: List<Content>

)

data class Content(
    val parts: List<Part>
)

data class Part(
    val text: String
)


