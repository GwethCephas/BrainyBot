package com.ceph.brainybot.feature_chat.domain.repository

import kotlinx.coroutines.flow.Flow

interface AnimationRepository {

    fun getAllAnimatedMessages(): Flow<Set<String>>
    suspend fun setMessageAnimated(messageId: String)
}