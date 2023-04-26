package com.example.booksriver

import android.content.Context
import android.content.Intent
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.booksriver.data.K
import com.example.booksriver.repository.BookRepository
import com.example.booksriver.session.SessionManager
import com.example.booksriver.util.createNotification
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class NotificationWorker @AssistedInject constructor(
    @Assisted private val appContext: Context,
    @Assisted workerParams: WorkerParameters,
    private val bookRepository: BookRepository,
    private val sessionManager: SessionManager
) : CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result {
        if(sessionManager.getUser() != null){
            val response = bookRepository.getUserBooksByStatus(K.BookStatus.READING.status)
            return response.onSuccess { data ->
                data.firstOrNull()?.let { book ->
                    val notificationIntent = Intent(appContext, SplashActivity::class.java)
                    notificationIntent.putExtra("bookId", book.id)
                    notificationIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP)

                    createNotification(
                        notificationIntent,
                        appContext,
                        "Hey, keep reading!",
                        "'${book.title}' is missing you!"
                    )
                }
            }.run {
                Result.success()
            }
        }else{
            return Result.success()
        }
    }
}