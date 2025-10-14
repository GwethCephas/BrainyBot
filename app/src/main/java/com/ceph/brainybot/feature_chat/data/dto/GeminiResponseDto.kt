package com.ceph.brainybot.feature_chat.data.dto

data class GeminiResponseDto(
    val candidates: List<Candidate>
)
data class Candidate(
    val content: Content
)