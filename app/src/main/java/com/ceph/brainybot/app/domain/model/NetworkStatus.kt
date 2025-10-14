package com.ceph.brainybot.app.domain.model

sealed class NetworkStatus {
    data object Connected: NetworkStatus()
    data object Disconnected: NetworkStatus()
}