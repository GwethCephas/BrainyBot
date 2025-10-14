package com.ceph.brainybot.feature_profile.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ceph.brainybot.feature_profile.data.ThemeDataStoreManager
import com.ceph.brainybot.feature_profile.model.ThemeMode
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ThemeViewModel(
    private val themeDataStoreManager: ThemeDataStoreManager
): ViewModel() {

    val themeMode: Flow<ThemeMode> = themeDataStoreManager.themeMode
        .stateIn(viewModelScope, SharingStarted.Eagerly, ThemeMode.SYSTEM)

    fun setThemeMode(themeMode: ThemeMode) {
        viewModelScope.launch {
            themeDataStoreManager.setThemeMode(themeMode)
        }

    }
}