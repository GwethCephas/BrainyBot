package com.ceph.brainybot.feature_chat.di

import android.app.Activity
import com.ceph.brainybot.feature_chat.data.remote.ApiService
import com.ceph.brainybot.feature_chat.data.repository.ApiRepositoryImpl
import com.ceph.brainybot.feature_chat.data.repository.ChatRepositoryImpl
import com.ceph.brainybot.feature_chat.data.utils.Constants.BASE_URL
import com.ceph.brainybot.feature_chat.domain.repository.ApiRepository
import com.ceph.brainybot.feature_chat.domain.repository.ChatRepository
import com.ceph.brainybot.feature_auth.data.auth.GoogleAuthClient
import com.ceph.brainybot.feature_chat.data.firestoreChatManager.FirestoreChatManager
import com.ceph.brainybot.feature_chat.presentation.home.ChatViewModel
import com.google.firebase.firestore.FirebaseFirestore
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

val chatModule = module {

    // Retrofit Instance
    single {

        val interceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        val client = OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .build()

        Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
            .client(client)
            .build()
    }
    single { get<Retrofit>().create(ApiService::class.java) }


    // Api Repository Instance
    single<ApiRepository> { ApiRepositoryImpl(get()) }

    // Firebase Firestore Instance
    single { FirebaseFirestore.getInstance() }

    // FirestoreChatManager Instance
    factory { (activity: Activity) ->
        FirestoreChatManager(get<FirebaseFirestore>(), GoogleAuthClient(activity))
    }

    // Chat Repository Instance
    factory<ChatRepository> { (firestoreChatManager: FirestoreChatManager) ->
        ChatRepositoryImpl(firestoreChatManager)
    }

    // ViewModel Instance
    viewModel { ( chatRepository: ChatRepository) ->
        ChatViewModel(
            get(),
            chatRepository
        )
    }


}