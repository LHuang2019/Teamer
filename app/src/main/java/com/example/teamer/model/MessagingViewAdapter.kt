package com.example.teamer.model

import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.os.bundleOf
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.example.teamer.R
import com.example.teamer.data.MessageData

class MessagingViewAdapter internal constructor(context : Context, val currentUserUID: String) :
    RecyclerView.Adapter<MessagingViewAdapter.MessagingViewHolder>() {

    private val inflater : LayoutInflater = LayoutInflater.from(context)
    private var messages: MutableList<MessageData> = mutableListOf()

    inner class MessagingViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        fun bindItem(message : MessageData) {
            val textView = itemView.findViewById<TextView>(R.id.message_tv)
            textView.text = message.messageText

            val params = textView.layoutParams as LinearLayout.LayoutParams

            if (message.sender_uid == currentUserUID) {
                textView.gravity = Gravity.RIGHT
                params.gravity = Gravity.RIGHT
                textView.setBackgroundResource(R.drawable.message_border)

            } else {
                textView.gravity = Gravity.LEFT
                params.gravity = Gravity.LEFT
                textView.setBackgroundResource(R.drawable.message_border2)
            }

            textView.requestLayout()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessagingViewHolder {
        val itemView = inflater.inflate(R.layout.recyclerview_message, parent, false)
        return MessagingViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MessagingViewHolder, position: Int) {
        holder.bindItem(messages[position])
    }

    internal fun setMessages(messages : MutableList<MessageData>) {
        this.messages = messages
        notifyDataSetChanged()
    }

    internal fun addMessage(message : MessageData) {
        this.messages.add(message)
        notifyDataSetChanged()
    }

    override fun getItemCount() = messages.size
}