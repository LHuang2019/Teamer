package com.example.teamer.view


import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.teamer.MainActivity

import com.example.teamer.R
import com.example.teamer.data.MessageData
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
                    viewAdapter.setMessages(messages)
                }

                firstListenerCompleted = true
            }
            .addOnFailureListener { exception ->
                Log.w("Error", "Error getting documents: ", exception)
            }


        return viewF
    }


}
