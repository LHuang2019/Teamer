package com.example.teamer.model

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.example.teamer.R
import com.example.teamer.data.FriendRequest

class PendingRequestListViewAdapter internal constructor(context : Context) :
    RecyclerView.Adapter<PendingRequestListViewAdapter.PendingRequestListViewHolder>() {

    private val inflater : LayoutInflater = LayoutInflater.from(context)
    private var friendRequests = emptyList<FriendRequest>()

    inner class PendingRequestListViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        fun bindItem(request : FriendRequest) {
            itemView.findViewById<TextView>(R.id.recyleview_username_tv).text = request.sender.username

            itemView.setOnClickListener {
                Navigation.findNavController(itemView).navigate(
                    R.id.action_friendListFragment_to_friendRequestResponseFragment,
                    bundleOf("friend_request" to request)
                )
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PendingRequestListViewHolder {
        val itemView = inflater.inflate(R.layout.recyclerview_user, parent, false)
        return PendingRequestListViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: PendingRequestListViewHolder, position: Int) {
        holder.bindItem(friendRequests[position])
    }

    internal fun setFriendRequests(friendRequests : List<FriendRequest>) {
        this.friendRequests = friendRequests
        notifyDataSetChanged()
    }

    override fun getItemCount() = friendRequests.size
}