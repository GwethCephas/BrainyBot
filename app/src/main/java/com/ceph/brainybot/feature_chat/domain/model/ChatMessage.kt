package com.ceph.brainybot.feature_chat.domain.model

import java.util.UUID

data class ChatMessage(
    val id: String = UUID.randomUUID().toString(),
    val senderId: String = "",
    val text: String = "",
    val timestamp: Long = System.currentTimeMillis()

)
