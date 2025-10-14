package com.ceph.brainybot.feature_chat.data.datastore

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


class AnimatedMessageStoreManager(
    private val context: Context
) {
    companion object {
        private val Context.dataStore by preferencesDataStore(name = "chat_preferences")
        private val ALREADY_ANIMATED = stringSetPreferencesKey("already_animated_message_ids")
    }

    suspend fun markMessageAsAnimated(messageId: String) {
        context.dataStore.edit { prefs ->
            val current = prefs[ALREADY_ANIMATED] ?: emptySet()
            prefs[ALREADY_ANIMATED] = current + messageId
        }
    }

    fun getAllAnimatedMessages(): Flow<Set<String>> {
        return context.dataStore.data.map { prefs ->
            prefs[ALREADY_ANIMATED] ?: emptySet()
        }
    }
}