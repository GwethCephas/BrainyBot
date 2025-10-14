package com.ceph.brainybot.feature_chat.domain.repository

import com.ceph.brainybot.feature_chat.domain.model.ChatRoom
import com.ceph.brainybot.feature_chat.domain.model.ChatMessage
import kotlinx.coroutines.flow.Flow

interface ChatRepository {

    suspend fun sendMessage(roomId: String?, message: ChatMessage)
    fun observeMessages(roomId: String?): Flow<List<ChatMessage>>

    suspend fun createChatRoom(roomId: String?, name: String)

    fun observeChatRooms(): Flow<List<ChatRoom>>


    suspend fun roomExists(roomId: String?): Boolean

    suspend fun searchRoomByNames(query: String): List<ChatRoom>

    suspend fun deleteChatRoom(roomId: String?): Result<Unit>


}