package com.ceph.brainybot.app.di

import android.content.Context
import com.ceph.brainybot.app.domain.repository.NetworkConnectivityObserver
import com.ceph.brainybot.app.data.NetworkConnectivityObserverImpl
import kotlinx.coroutines.CoroutineScope
import org.koin.dsl.module

val appModule = module {

    // Network Connectivity Observer Instance
    factory<NetworkConnectivityObserver> { (context: Context, coroutineScope: CoroutineScope) ->
        NetworkConnectivityObserverImpl(context, coroutineScope)
    }
}