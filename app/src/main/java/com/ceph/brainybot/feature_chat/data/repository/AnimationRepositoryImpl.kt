package com.ceph.brainybot.feature_chat.data.repository

import com.ceph.brainybot.feature_chat.data.datastore.AnimatedMessageStoreManager
import com.ceph.brainybot.feature_chat.domain.repository.AnimationRepository
import kotlinx.coroutines.flow.Flow

class AnimationRepositoryImpl(
    private val dataStoreManager: AnimatedMessageStoreManager
) : AnimationRepository {

    override fun getAllAnimatedMessages(): Flow<Set<String>> {
        return dataStoreManager.getAllAnimatedMessages()
    }

    override suspend fun setMessageAnimated(messageId: String) {
        dataStoreManager.markMessageAsAnimated(messageId)
    }


}