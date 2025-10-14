package com.ceph.brainybot.feature_auth.di

import android.content.Context
import com.ceph.brainybot.feature_auth.data.repository.AuthRepositoryImpl
import com.ceph.brainybot.feature_auth.data.auth.GoogleAuthClient
import com.ceph.brainybot.feature_auth.domain.repository.AuthRepository
import com.ceph.brainybot.feature_auth.presentation.viewModel.AuthViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.core.parameter.parametersOf
import org.koin.dsl.module


val authModule = module {

    factory<AuthRepository> { (context: Context) ->
        AuthRepositoryImpl(GoogleAuthClient(context))
    }
    viewModel { (context: Context) ->
        AuthViewModel(get { parametersOf(context) })
    }

}

