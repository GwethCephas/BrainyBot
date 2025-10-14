package com.ceph.brainybot.feature_chat.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ceph.brainybot.feature_chat.domain.repository.AnimationRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AnimationViewModel(
    private val animationRepository: AnimationRepository
): ViewModel() {
    private val _alreadyAnimatedMessage = MutableStateFlow<Set<String>>(emptySet())
    val alreadyAnimatedMessage = _alreadyAnimatedMessage.asStateFlow()

    init {
        viewModelScope.launch {
            animationRepository.getAllAnimatedMessages().collect { animatedSet ->
                _alreadyAnimatedMessage.value = animatedSet
            }
        }
    }

    fun markMessageAsAnimated(messageId: String) {
        viewModelScope.launch {
            animationRepository.setMessageAnimated(messageId)
        }
    }
}