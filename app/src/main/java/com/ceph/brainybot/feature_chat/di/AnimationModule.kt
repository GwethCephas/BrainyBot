package com.ceph.brainybot.feature_chat.di

import com.ceph.brainybot.feature_chat.data.datastore.AnimatedMessageStoreManager
import com.ceph.brainybot.feature_chat.data.repository.AnimationRepositoryImpl
import com.ceph.brainybot.feature_chat.domain.repository.AnimationRepository
import com.ceph.brainybot.feature_chat.presentation.home.AnimationViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val animationModule = module {

    single { AnimatedMessageStoreManager(androidContext()) }

    single<AnimationRepository> { AnimationRepositoryImpl(get()) }

    viewModel { AnimationViewModel(get()) }
}