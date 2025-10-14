package com.ceph.brainybot.app.data

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.util.Log
import com.ceph.brainybot.app.domain.model.NetworkStatus
import com.ceph.brainybot.app.domain.repository.NetworkConnectivityObserver
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.stateIn

class NetworkConnectivityObserverImpl(
    context: Context,
    coroutineScope: CoroutineScope
) : NetworkConnectivityObserver {

    val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager


    private val _networkStatus = observer().stateIn(
        scope = coroutineScope,
        started = SharingStarted.Companion.WhileSubscribed(5000),
        initialValue = NetworkStatus.Disconnected
    )
    override val networkStatus: StateFlow<NetworkStatus> = _networkStatus


    fun observer(): Flow<NetworkStatus> {
        return callbackFlow {
            val networkCallback = object : ConnectivityManager.NetworkCallback() {
                override fun onAvailable(network: Network) {
                    super.onAvailable(network)
                    trySend(NetworkStatus.Connected)
                    Log.d("NetworkStatus", "Connected")
                }

                override fun onLost(network: Network) {
                    super.onLost(network)
                    trySend(NetworkStatus.Disconnected)
                    Log.d("NetworkStatus", "Disconnected")
                }
            }

            val networkRequest = NetworkRequest.Builder()
                .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
                .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
                .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
                .build()

            connectivityManager.registerNetworkCallback(networkRequest, networkCallback)

            awaitClose {
                connectivityManager.unregisterNetworkCallback(networkCallback)
            }

        }
            .distinctUntilChanged()
    }
}