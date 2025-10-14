package com.ceph.brainybot.feature_chat.data.firestoreChatManager

import android.util.Log
import com.ceph.brainybot.feature_auth.data.auth.GoogleAuthClient
import com.ceph.brainybot.feature_chat.domain.model.ChatMessage
import com.ceph.brainybot.feature_chat.domain.model.ChatRoom
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class FirestoreChatManager(
    private val firestore: FirebaseFirestore,
    private val googleAuthClient: GoogleAuthClient
) {

    private val currentUserId: String
        get() = googleAuthClient.getCurrentUser()?.userId
            ?: throw IllegalStateException("No authenticated user found")

    private val userCollection
        get() = firestore.collection(currentUserId)


    suspend fun sendMessage(
        roomId: String?,
        message: ChatMessage,
    ) {
        roomId?.let {
            userCollection
                .document(it)
                .collection("messages")
                .add(message)
                .await()
        }

    }

    fun observeMessages(
        roomId: String?,
    ): Flow<List<ChatMessage>> = callbackFlow {

        val listener = roomId?.let {
            userCollection
                .document(it)
                .collection("messages")
                .orderBy("timestamp", Query.Direction.ASCENDING)
                .addSnapshotListener { snapshot, error ->
                    if (error != null) {
                        Log.e("ChatRepositoryImpl", "observeMessages: ${error.message}")
                        return@addSnapshotListener
                    }
                    val messages = snapshot?.toObjects(ChatMessage::class.java) ?: emptyList()
                    trySend(messages)


                }
        }
        awaitClose { listener?.remove() }

    }

    suspend fun createChatRoom(
        roomId: String?, name: String
    ) {

        val room = ChatRoom(
            roomId = roomId,
            name = name,
            createdAt = System.currentTimeMillis()
        )
        roomId?.let {
            userCollection
                .document(it)
                .set(room)
                .await()
        }

    }

    fun observeChatRooms(): Flow<List<ChatRoom>> = callbackFlow {
        val subscription = userCollection
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    Log.e("ChatRepositoryImpl", "observeChatRooms: ${error.message}")
                    return@addSnapshotListener
                }
                val chatRooms = snapshot?.documents?.mapNotNull { documentSnapshot ->
                    documentSnapshot.toObject(ChatRoom::class.java)
                        ?.copy(roomId = documentSnapshot.id)
                }.orEmpty()

                trySend(chatRooms)

            }
        awaitClose { subscription.remove() }
    }

    suspend fun roomExists(roomId: String?): Boolean {
        val doc = roomId?.let {
            userCollection
                .document(it)
                .get()
                .await()
        }

        return doc?.exists() ?: false
    }

    suspend fun searchRoomByNames(query: String): List<ChatRoom> {
        val rooms = userCollection
            .whereEqualTo("name", query)
            .get()
            .await()
        return rooms.toObjects(ChatRoom::class.java)
    }

    suspend fun deleteChatRoom(roomId: String?): Result<Unit> {
        roomId?.let {
            userCollection
                .document(it)
                .delete()
                .await()
        }

        return Result.success(Unit)

    }

}