package com.example.teamer.view


import android.os.Bundle
import android.os.Message
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.teamer.MainActivity

import com.example.teamer.R
import com.example.teamer.data.*
import com.example.teamer.model.FriendListViewAdapter
import com.example.teamer.model.MessagingViewAdapter
import com.example.teamer.model.PendingRequestListViewAdapter
import com.example.teamer.model.TeamerVM
import com.google.firebase.firestore.FirebaseFirestore


class MessagingFragment : Fragment() {

    private lateinit var vm : TeamerVM

    private lateinit var viewF : View
    private lateinit var recyclerView : RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        vm = activity.run {
            ViewModelProviders.of(this!!).get(TeamerVM::class.java)
        }

        viewF =  inflater.inflate(R.layout.fragment_messaging, container, false)


        viewF.findViewById<TextView>(R.id.partnerMessage_tv).text = arguments?.getString("username")

        recyclerView = viewF.findViewById(R.id.messages_recyclerview)

        recyclerView.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)

        val viewAdapter = MessagingViewAdapter(activity as MainActivity, vm.getCurrentUserData().value?.uid!!)
        recyclerView.adapter = viewAdapter


        val db = FirebaseFirestore.getInstance()

        val DB_message_ref = db.collection("messages")

        var firstListenerCompleted = false

        val messages: MutableList<MessageData> = mutableListOf()


        fun updateMessages() {
            // check for messages sent by the current user
            DB_message_ref.whereEqualTo("sender_uid", vm.getCurrentUserData().value?.uid)
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {

                    val data = document.data

                    // skip the irrelevant messages to this conversation
                    if (data.getValue("recipient_uid").toString() != arguments?.getString("uid")) {
                        continue
                    }

                    val m = MessageData(data.getValue("sender_uid").toString(),
                        data.getValue("recipient_uid").toString(),
                        data.getValue("messageText").toString(),
                        data.getValue("timestamp") as Long
                    )

                    messages.add(m)
                }
                if (firstListenerCompleted) {
                    messages.sortBy{
                        it.timestamp
                    }
                    viewAdapter.setMessages(messages)
                }

                firstListenerCompleted = true
            }
            .addOnFailureListener { exception ->
                Log.w("Error", "Error getting documents: ", exception)
            }

            // check for messages going to the current user
            DB_message_ref.whereEqualTo("recipient_uid", vm.getCurrentUserData().value?.uid)
                .get()
                .addOnSuccessListener { documents ->
                    for (document in documents) {

                        val data = document.data

                        // skip the irrelevant messages to this conversation
                        if (data.getValue("sender_uid").toString() != arguments?.getString("uid")) {
                            continue
                        }

                        val m = MessageData(data.getValue("sender_uid").toString(),
                            data.getValue("recipient_uid").toString(),
                            data.getValue("messageText").toString(),
                            data.getValue("timestamp") as Long
                        )

                        messages.add(m)
                    }
                    if (firstListenerCompleted) {
                        messages.sortBy{
                            it.timestamp
                        }
                        viewAdapter.setMessages(messages)
                    }

                    firstListenerCompleted = true
                }
                .addOnFailureListener { exception ->
                    Log.w("Error", "Error getting documents: ", exception)
                }
        }


        // send button
        viewF.findViewById<Button>(R.id.send_btn).setOnClickListener{
            val editText = viewF.findViewById<EditText>(R.id.chat_edittext)

            val message = MessageData(
                vm.getCurrentUserData().value?.uid!!,
                arguments?.getString("uid")!!,
                editText.text.toString(),
                System.currentTimeMillis()
            )

            if (message.messageText.trim() != "") {
                viewAdapter.addMessage(message)
                editText.text.clear()

                val messageHash = HashMap<String, Any>()
                messageHash["sender_uid"] = message.sender_uid
                messageHash["recipient_uid"] = message.recipient_uid
                messageHash["messageText"] = message.messageText
                messageHash["timestamp"] = message.timestamp

                DB_message_ref.add(messageHash)

                fun addPendingMessage(recipientUid : String, recipientToken : String, sender : UserData) {
                    db.collection("users")
                        .document(recipientUid).collection("pending_messages").whereEqualTo("sender.uid", sender.uid)
                        .get().addOnSuccessListener {
                            if (it.isEmpty) {
                                val ref = db.collection("users").document(recipientUid)
                                    .collection("pending_messages").document()
                                val messageNotif = MessageNotification(ref.id, sender, recipientUid, recipientToken)
                                ref.set(messageNotif)
                            }
                        }
                }

                addPendingMessage(message.recipient_uid, arguments?.getString("token")!!, vm?.getCurrentUserData().value!!)

            }
        }

        DB_message_ref.addSnapshotListener { querySnapshot, firebaseFirestoreException ->
            messages.clear()
            updateMessages()
        }

        return viewF
    }


}
