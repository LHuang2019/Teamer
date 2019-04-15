package com.example.teamer.view


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.teamer.MainActivity
import com.example.teamer.R
import com.example.teamer.model.FriendListViewAdapter

class FriendListFragment : Fragment() {

    private lateinit var viewF : View
    private lateinit var recyclerView : RecyclerView

    private lateinit var profileBtn : Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewF =  inflater.inflate(R.layout.fragment_friend_list, container, false)

        profileBtn = viewF.findViewById(R.id.f_friend_list_profile_btn)
        profileBtn.setOnClickListener {
            Navigation.findNavController(viewF).navigate(
                R.id.action_friendListFragment_to_viewProfileFragment
            )
        }

        recyclerView = viewF.findViewById(R.id.friendlist_recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        val viewAdapter =
            FriendListViewAdapter(activity as MainActivity)
        recyclerView.adapter = viewAdapter

        return viewF
    }
}
