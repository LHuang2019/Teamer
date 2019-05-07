package com.example.teamer.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Message
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.teamer.MainActivity
import com.example.teamer.R
import com.example.teamer.data.FirestoreDatabase
import com.example.teamer.data.FriendRequest
import com.example.teamer.data.MessageNotification
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class FriendRequestMessagingService : FirebaseMessagingService() {

    private var notificationId = 0

    companion object {
        private const val CHANNEL_ID = "friend_request_channel"
        private const val CHANNEL_NAME = "Friend Request"
        private const val CHANNEL_DESCRIPTION = "Channel for notifying friend request"

        const val FRIEND_REQUEST_INTENT = "friend_request"

        private const val USERS_COLLECTION = "users"
        private const val FRIEND_REQUESTS_COLLECTION = "friend_requests"

        private const val PENDING_MESSAGE_COLLECTION = "pending_messages"
        private const val PENDING_MESSAGE_INTENT = "pending_message"

    }

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
    }

    private fun createNotificationChannel() {
        val channel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT)
            .apply {
                description = CHANNEL_DESCRIPTION
            }

        val notificationManager: NotificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }

    override fun onMessageReceived(remoteMessage : RemoteMessage?) {
        if (remoteMessage?.data != null) {
            Log.d("hi", "received")
            showNotification(remoteMessage.data)
        }
    }

    private fun showNotification(data : MutableMap<String?, String?>) {

        val type = data["type"]
        val documentId = data["documentId"]
        val recipientId = data["recipientId"]

        if (documentId != null && recipientId != null && type != null) {
            if (type == "friendRequest") {
                // if it is a friend request
                FirestoreDatabase.getDatabase().collection(USERS_COLLECTION).document(recipientId).
                    collection(FRIEND_REQUESTS_COLLECTION).document(documentId).get()
                    .addOnSuccessListener { document ->
                        if (document.exists()) {
                            val friendRequest = document.toObject(FriendRequest::class.java)

                            val intent = Intent(this, MainActivity::class.java)
                            intent.putExtra(FRIEND_REQUEST_INTENT, friendRequest)

                            val resultPendingIntent: PendingIntent? = PendingIntent.getActivity(this, 0, intent,
                                PendingIntent.FLAG_ONE_SHOT)

                            val builder = NotificationCompat.Builder(this, CHANNEL_ID)
                                .setSmallIcon(R.drawable.friend_notif_icon)
                                .setContentTitle(friendRequest?.sender?.username + " would like to add you as friend.")
                                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                                .setContentIntent(resultPendingIntent)
                                .setAutoCancel(true)

                            with(NotificationManagerCompat.from(applicationContext)) {
                                notify(notificationId, builder.build())
                            }

                            notificationId++
                        }
                    }
                    .addOnFailureListener { }
            } else {
                // if it is a message notification
                FirestoreDatabase.getDatabase().collection(USERS_COLLECTION).document(recipientId).
                    collection(PENDING_MESSAGE_COLLECTION).document(documentId).get()
                    .addOnSuccessListener { document ->
                        if (document.exists()) {
                            val pendingMessage = document.toObject(MessageNotification::class.java)

                            val intent = Intent(this, MainActivity::class.java)
                            intent.putExtra(PENDING_MESSAGE_INTENT, pendingMessage)

                            val resultPendingIntent: PendingIntent? = PendingIntent.getActivity(this, 0, intent,
                                PendingIntent.FLAG_ONE_SHOT)

                            val builder = NotificationCompat.Builder(this, CHANNEL_ID)
                                .setSmallIcon(R.drawable.friend_notif_icon)
                                .setContentTitle(pendingMessage?.sender?.username + " has sent you a message.")
                                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                                .setContentIntent(resultPendingIntent)
                                .setAutoCancel(true)

                            with(NotificationManagerCompat.from(applicationContext)) {
                                notify(notificationId, builder.build())
                            }

                            notificationId++
                        }
                    }
                    .addOnFailureListener { }
            }
        }
    }
}