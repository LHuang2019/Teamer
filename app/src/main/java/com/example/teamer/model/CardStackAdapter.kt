package com.example.teamer.model

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.teamer.R
import com.example.teamer.data.UserData

class CardStackAdapter(
    private var profiles: List<UserData> = emptyList()
) : RecyclerView.Adapter<CardStackAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ViewHolder(inflater.inflate(R.layout.cardstack_view_profile, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val profile = profiles[position]
        holder.username.text = profile.username

        holder.platformList.text = profile.platforms.joinToString { platform -> platform }
        holder.gamesList.text = profile.games.joinToString { game -> game }

    }

    override fun getItemCount(): Int {
        return profiles.size
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val username: TextView = view.findViewById(R.id.f_view_profile_username_tv)
        val platformList: TextView = view.findViewById(R.id.f_view_profile_platform_list_tv)
        val gamesList: TextView = view.findViewById(R.id.f_view_profile_game_list_tv)
    }

}