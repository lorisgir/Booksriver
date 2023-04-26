package com.example.booksriver.util

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.app.TaskStackBuilder
import com.example.booksriver.R
import com.example.booksriver.data.K
import com.example.booksriver.data.response.ErrorResponse
import kotlin.math.ceil
import kotlin.random.Random

fun calculatePercentage(pagesRead: Int, bookPages: Int): Float =
    pagesRead.toFloat() / (bookPages).toFloat()

fun isLandscapeOrTablet(configuration: Configuration): Boolean {
    return isLandscape(configuration) || isTablet(configuration)
}

fun isTablet(configuration: Configuration): Boolean {
    return configuration.screenWidthDp > 450
}

fun isLandscape(configuration: Configuration): Boolean {
    return configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
}

fun getResponsiveColumnCount(configuration: Configuration, maxWidth: Int): Int {
    return ceil(configuration.screenWidthDp.toFloat() / maxWidth.toFloat()).toInt().coerceAtLeast(1)
}

fun getDetailsOrMessage(errorResponse: ErrorResponse): String {
    return if (errorResponse.details.isNotEmpty()) errorResponse.details.first() else errorResponse.message
}

fun createNotification(intent: Intent, context: Context, title: String, content: String) {
    val pending: PendingIntent = TaskStackBuilder.create(context).run {
        addNextIntentWithParentStack(intent)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getPendingIntent(
                123,
                PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
            )!!
        } else {
            getPendingIntent(123, PendingIntent.FLAG_UPDATE_CURRENT)!!
        }
    }


    val builder = NotificationCompat.Builder(context, K.CHANNEL_ID)
        .setSmallIcon(R.drawable.ic_launcher_foreground)
        .setContentTitle(title)
        .setContentText(content)
        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        .setContentIntent(pending)
        .setAutoCancel(true)
    with(NotificationManagerCompat.from(context)) {
        notify(Random.nextInt(9999), builder.build())
    }
}