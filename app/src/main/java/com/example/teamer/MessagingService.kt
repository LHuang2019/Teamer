package com.example.teamer;

import android.app.Service;
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import android.util.Log
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
        return iBinder
    }

    override fun onCreate() {
        super.onCreate()

        FirebaseFirestore.getInstance().collection("pending_friend_requests")
            .whereEqualTo("recipient_id", "EOBY9buZBTePLYtIONIO1NGoGuf1")
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
                Log.d("Test", "Friend Requests from: $requests")
            })
    }

    fun sendFriendRequest(recipient_id: String, sender_id: String) {
        val data = HashMap<String, Any>()
        data["recipient_id"] = recipient_id
        data["sender_id"] = sender_id

        FirebaseFirestore.getInstance().collection("pending_friend_requests").add(data)
    }
}
