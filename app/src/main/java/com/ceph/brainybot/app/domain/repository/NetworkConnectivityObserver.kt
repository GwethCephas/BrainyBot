package com.ceph.brainybot.app.domain.repository

import com.ceph.brainybot.app.domain.model.NetworkStatus
import kotlinx.coroutines.flow.StateFlow

interface NetworkConnectivityObserver {

    val networkStatus: StateFlow<NetworkStatus>

}