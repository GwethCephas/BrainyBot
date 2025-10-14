package com.ceph.brainybot.feature_chat.data.utils

import androidx.datastore.preferences.core.stringSetPreferencesKey
import com.ceph.brainybot.BuildConfig

object Constants {

    const val BASE_URL = "https://generativelanguage.googleapis.com/v1beta/"
    const val API_KEY = BuildConfig.API_KEY

    val ANIMATED_MESSAGE_IDS = stringSetPreferencesKey("animated_message_ids")
}