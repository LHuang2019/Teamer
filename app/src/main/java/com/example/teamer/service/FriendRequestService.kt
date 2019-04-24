package com.example.teamer.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Binder
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.app.TaskStackBuilder
import com.example.teamer.R
import com.example.teamer.FriendRequestResponseActivity
import com.example.teamer.data.FirestoreDatabase
import com.example.teamer.data.FriendRequest
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.QuerySnapshot

class FriendRequestService: Service() {
    private var notificationId = 1

    companion object {
        private val GROUP_KEY = "friend_request_group"
        private val SUMMARY_ID = 0

        private val CHANNEL_ID = "friend_request_channel"

        const val USER_INTENT = "user_id"
        const val FRIEND_REQUEST_INTENT = "friend_request"

        private const val FRIEND_REQUESTS_COLLECTION = "friend_requests"
        private const val RECIPIENT_FIELD = "recipient"
    }

    private val serviceBinder = ServiceBinder()

    inner class ServiceBinder: Binder(){
        fun getService(): FriendRequestService {
            return this@FriendRequestService
        }
    }

    override fun onBind(intent: Intent?): IBinder {
        createNotificationChannel()

        FirestoreDatabase.getDatabase().collection(FRIEND_REQUESTS_COLLECTION)
            .whereEqualTo(RECIPIENT_FIELD, intent?.getStringExtra(USER_INTENT))
            .addSnapshotListener(EventListener<QuerySnapshot> { query, e ->
                if (e != null || query == null) {
                    return@EventListener
                }

                val requests = ArrayList<FriendRequest>()
                for (doc in query) {
                    requests.add(doc.toObject(FriendRequest::class.java))
                }

                if (requests.size > 0) {
                    // Create an explicit intent for an Activity in your app
                    sendFriendRequestNotification(requests)
                }
            })

        return serviceBinder
    }

    private fun createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Friend Request"
            val descriptionText = "Channel for notifying user of friend requests"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            // Register the channel with the system
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun sendFriendRequestNotification(requests : List<FriendRequest>) {
        for (request in requests) {
            val intent = Intent(this, FriendRequestResponseActivity::class.java)
            intent.putExtra(FRIEND_REQUEST_INTENT, request)

            val resultPendingIntent: PendingIntent? = TaskStackBuilder.create(this).run {
                // Add the intent, which inflates the back stack
                addNextIntentWithParentStack(intent)
                // Get the PendingIntent containing the entire back stack
                getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT)
            }

            val builder = NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.friend_notif_icon)
                .setContentTitle(request.sender.username)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(resultPendingIntent)
                .setAutoCancel(true)
                .setGroup(GROUP_KEY)

            with(NotificationManagerCompat.from(applicationContext)) {
                // notificationId is a unique int for each notification that you must define
                notify(notificationId, builder.build())
            }
            notificationId++
        }

        val summaryNotification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.friend_notif_icon)
            .setContentTitle("New Friend Requests")
            .setContentText(requests.size.toString() + " new user(s) would like to add you as a friend.")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
            .setGroup(GROUP_KEY)
            .setGroupSummary(true).build()

        with(NotificationManagerCompat.from(applicationContext)) {
            // notificationId is a unique int for each notification that you must define
            notify(SUMMARY_ID, summaryNotification)
        }
    }
}
