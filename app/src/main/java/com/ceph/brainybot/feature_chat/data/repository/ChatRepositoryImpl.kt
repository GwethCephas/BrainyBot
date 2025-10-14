package com.ceph.brainybot.feature_chat.data.repository

import com.ceph.brainybot.feature_chat.data.firestoreChatManager.FirestoreChatManager
import com.ceph.brainybot.feature_chat.domain.model.ChatMessage
import com.ceph.brainybot.feature_chat.domain.model.ChatRoom
import com.ceph.brainybot.feature_chat.domain.repository.ChatRepository
import kotlinx.coroutines.flow.Flow


class ChatRepositoryImpl(
    private val firestoreChatManager: FirestoreChatManager
) : ChatRepository {

    override suspend fun sendMessage(
        roomId: String?,
        message: ChatMessage,
    ) {
        firestoreChatManager.sendMessage(roomId, message)
    }

    override fun observeMessages(
        roomId: String?,
    ): Flow<List<ChatMessage>> {
        return firestoreChatManager.observeMessages(roomId)
    }

    override suspend fun createChatRoom(
        roomId: String?, name: String
    ) {
        firestoreChatManager.createChatRoom(roomId, name)
    }

    override fun observeChatRooms(): Flow<List<ChatRoom>> {
        return firestoreChatManager.observeChatRooms()
    }

    override suspend fun roomExists(roomId: String?): Boolean {
        return firestoreChatManager.roomExists(roomId)
    }

    override suspend fun searchRoomByNames(query: String): List<ChatRoom> {
        return firestoreChatManager.searchRoomByNames(query)
    }

    override suspend fun deleteChatRoom(roomId: String?): Result<Unit> {
        return firestoreChatManager.deleteChatRoom(roomId)
    }
}