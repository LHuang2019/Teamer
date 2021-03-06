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
import com.example.teamer.data.UserData

class FriendListViewAdapter internal constructor(context : Context) :
    RecyclerView.Adapter<FriendListViewAdapter.FriendListViewHolder>() {

    private val inflater : LayoutInflater = LayoutInflater.from(context)
    private var friendList = emptyList<UserData>()

    inner class FriendListViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        fun bindItem(user : UserData) {
            itemView.findViewById<TextView>(R.id.recyleview_username_tv).text = user.username

            itemView.setOnClickListener {
                Navigation.findNavController(itemView).navigate(
                    R.id.action_friendListFragment_to_friendProfileFragment,
                    bundleOf("user" to user)
                )
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FriendListViewHolder {
        val itemView = inflater.inflate(R.layout.recyclerview_user, parent, false)
        return FriendListViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: FriendListViewHolder, position: Int) {
        holder.bindItem(friendList[position])
    }

    internal fun setFriendList(friendList : List<UserData>) {
        this.friendList = friendList
        notifyDataSetChanged()
    }

    override fun getItemCount() = friendList.size
}