package com.autoever.everp.data.repository

import com.autoever.everp.data.datasource.remote.firebase.FirebaseDataSource
import com.autoever.everp.domain.repository.PushNotificationRepository
import kotlinx.coroutines.tasks.await
import timber.log.Timber
import javax.inject.Inject

class PushNotificationRepositoryImpl @Inject constructor(
    private val firebaseDataSource: FirebaseDataSource,
) : PushNotificationRepository {

    fun getAndSyncToken() {
        firebaseDataSource.messaging.token
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val token = task.result
                    Timber.tag(TAG).i("Token: $token")
                    // 여기서 토큰을 서버와 동기화하는 로직을 호출할 수 있습니다.
                } else {
                    Timber.tag(TAG).e(task.exception, "Fetching FCM token failed")
                }
            }
    }

    override suspend fun getToken(): String {
        try {
            val token: String = firebaseDataSource.messaging.token.await()
            Timber.tag(TAG).i("Token: $token")
            return token
        } catch (e: Exception) {
            Timber.tag(TAG).e(e, "Fetching FCM token failed")
            throw e
        }
    }

    override suspend fun syncTokenWithServer(token: String) {
        TODO("Not yet implemented")
    }

    companion object {
        private const val TAG = "Firebase(FCM)"
    }
}
