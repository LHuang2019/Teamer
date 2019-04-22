package com.example.teamer;

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service;
import android.content.Context
import android.content.Intent
import android.os.Binder
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.firebase.database.*
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot

class MessagingService: Service() {

    inner class MyBinder: Binder(){
        fun getService():MessagingService{
            return this@MessagingService
        }
    }

    private val iBinder = MyBinder()

    override fun onBind(intent: Intent?): IBinder {
        Log.w("test", "Service bound")

        createNotificationChannel()

        // TODO: clear pending friend requests when notification is tapped
        FirebaseFirestore.getInstance().collection("pending_friend_requests")
            .whereEqualTo("recipient_id", intent?.getStringExtra("user_uid"))
            .addSnapshotListener(EventListener<QuerySnapshot> { value, e ->
                if (e != null) {
                    Log.w("Test", "Listen failed.", e)
                    return@EventListener
                }

                val requests = ArrayList<String>()
                for (doc in value!!) {
                    if (doc.get("sender_id") != null) {
                        requests.add(doc.getString("sender_id")!!)
                    }
                }

                if (requests.size > 0) {
                    // Create an explicit intent for an Activity in your app
                    sendFriendRequestNotification()
                }
            })

        return iBinder
    }

    val CHANNEL_ID = "friend_request_channel"
    var notificationId = 1

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

    private fun sendFriendRequestNotification() {
        val intent = Intent(Intent.ACTION_SEND).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent: PendingIntent = PendingIntent.getActivity(this, 0, intent, 0)

        val builder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.friend_notif_icon)
            .setContentTitle("Friend Request")
            .setContentText("A new user would like to add you as a friend.")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            // Set the intent that will fire when the user taps the notification
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        with(NotificationManagerCompat.from(applicationContext)) {
            // notificationId is a unique int for each notification that you must define
            notify(notificationId, builder.build())
        }
        notificationId++
    }

    override fun onCreate() {
        super.onCreate()
    }

    fun sendFriendRequest(recipient_id: String, sender_id: String) {
        val data = HashMap<String, Any>()
        data["recipient_id"] = recipient_id
        data["sender_id"] = sender_id

        FirebaseFirestore.getInstance().collection("pending_friend_requests").add(data)
    }
}
