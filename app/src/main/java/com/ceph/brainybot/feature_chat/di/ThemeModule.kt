package com.ceph.brainybot.feature_chat.di

import com.ceph.brainybot.feature_profile.data.ThemeDataStoreManager
import com.ceph.brainybot.feature_profile.profile.ThemeViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val themeModule = module {
    single { ThemeDataStoreManager(androidContext()) }

    viewModel { ThemeViewModel(get()) }
}