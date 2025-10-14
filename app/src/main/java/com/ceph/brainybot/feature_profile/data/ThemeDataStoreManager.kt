package com.ceph.brainybot.feature_profile.data

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.ceph.brainybot.feature_profile.model.ThemeMode
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.themeDataStore by preferencesDataStore("theme_prefs")


class ThemeDataStoreManager(
    private val context: Context
) {
    private val themePreferenceKey = stringPreferencesKey("theme_key")

    val themeMode: Flow<ThemeMode> = context.themeDataStore.data.map { prefs ->
        val mode = prefs[themePreferenceKey] ?: "SYSTEM"
        ThemeMode.entries.find { it.name == mode } ?: ThemeMode.SYSTEM
    }

    suspend fun setThemeMode(mode: ThemeMode) {
        context.themeDataStore.edit { prefs ->
            prefs[themePreferenceKey] = mode.name

        }
    }

}