package com.ceph.brainybot.feature_chat.domain.model

data class ChatRoom(
    val roomId: String? = null,
    val name: String = "",
    val createdAt: Long = System.currentTimeMillis()
)
