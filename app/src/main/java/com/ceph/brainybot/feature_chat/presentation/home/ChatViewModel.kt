package com.ceph.brainybot.feature_chat.presentation.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ceph.brainybot.feature_chat.data.dto.GeminiRequestDto
import com.ceph.brainybot.feature_chat.domain.model.ChatMessage
import com.ceph.brainybot.feature_chat.domain.model.ChatRoom
import com.ceph.brainybot.feature_chat.domain.repository.ApiRepository
import com.ceph.brainybot.feature_chat.domain.repository.ChatRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ChatViewModel(
    private val apiRepository: ApiRepository,
    private val chatRepository: ChatRepository
) : ViewModel() {


    private var _uiState = MutableStateFlow(UiState())
    val uiState = _uiState.asStateFlow()


    private val _messages = MutableStateFlow<List<ChatMessage>>(emptyList())
    val messages = _messages.asStateFlow()


    private val _chatRooms = MutableStateFlow<List<ChatRoom>>(emptyList())
    val chatRooms = _chatRooms.asStateFlow()

    private val _currentRoomId = MutableStateFlow<String?>(null)
    val currentRoomId = _currentRoomId.asStateFlow()

    fun setCurrentRoomId(roomId: String?) {
        _currentRoomId.value = roomId
        if (roomId == null) {
            _messages.value = emptyList()
        } else observeMessages(roomId)
    }
    fun generateResponse(
        userRequest: String,
        request: GeminiRequestDto,
        roomId: String,
        currentUser: String
    ) {
        viewModelScope.launch {
            try {
                _uiState.value = UiState(isLoading = true)

                sendMessage(
                    roomId = roomId,
                    message = ChatMessage(
                        text = userRequest,
                        senderId = currentUser
                    )
                )
                apiRepository.generateResponse(request).collect { response ->
                    val response = response.text
                    _uiState.value = UiState(isLoading = false)


                    sendMessage(
                        roomId = roomId,
                        message = ChatMessage(
                            text = response,
                            senderId = "bot"
                        )
                    )

                }
            } catch (e: Exception) {
                Log.e("ApiViewModel", "generateResponse: ${e.message}")
                _uiState.value = UiState(errorMessage = e.message ?: "Unknown error")
            }

        }
    }

    fun createChat(roomId: String, name: String) {
        viewModelScope.launch {
            chatRepository.createChatRoom(roomId, name)
            setCurrentRoomId(roomId)
        }
    }

    fun sendMessage(roomId: String, message: ChatMessage) {
        viewModelScope.launch {
            chatRepository.sendMessage(roomId, message)
        }
    }

    fun observeMessages(roomId: String?) {
        viewModelScope.launch {
            chatRepository.observeMessages(roomId).collect { messages ->
                _messages.value = messages
            }
        }
    }

    fun observeChatRooms() {
        viewModelScope.launch {
            chatRepository.observeChatRooms().collect { rooms ->
                _chatRooms.value = rooms
            }
        }

    }


    fun deleteChatRoom(roomId: String) {
        viewModelScope.launch {
            chatRepository.deleteChatRoom(roomId)
        }

    }
}