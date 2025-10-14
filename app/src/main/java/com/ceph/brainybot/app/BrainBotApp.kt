package com.ceph.brainybot.app

import android.app.Application
import android.content.Context
import androidx.datastore.preferences.preferencesDataStore
import com.ceph.brainybot.app.di.appModule
import com.ceph.brainybot.feature_auth.di.authModule
import com.ceph.brainybot.feature_chat.di.animationModule
import com.ceph.brainybot.feature_chat.di.chatModule
import com.ceph.brainybot.feature_chat.di.themeModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class BrainBotApp : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@BrainBotApp)
            modules(
                chatModule, appModule, authModule, animationModule, themeModule
            )
        }

    }
}