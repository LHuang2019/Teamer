package com.example.teamer.view


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.RadioGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.teamer.MainActivity
import com.example.teamer.R
import com.example.teamer.model.FriendListViewAdapter
import com.example.teamer.model.PendingRequestListViewAdapter
import com.example.teamer.model.TeamerVM

class FriendListFragment : Fragment() {
    private lateinit var vm : TeamerVM

    private lateinit var viewF : View
    private lateinit var recyclerView : RecyclerView

    private lateinit var discoverBtn : Button
    private lateinit var profileBtn : Button
    private lateinit var logoutBtn : Button

    private lateinit var listGroup : RadioGroup

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        vm = activity.run {
            ViewModelProviders.of(this!!).get(TeamerVM::class.java)
        }

        viewF =  inflater.inflate(R.layout.fragment_friend_list, container, false)

        profileBtn = viewF.findViewById(R.id.f_friend_list_profile_btn)
        profileBtn.setOnClickListener {
            Navigation.findNavController(viewF).navigate(
                R.id.action_friendListFragment_to_viewProfileFragment
            )
        }

        discoverBtn = viewF.findViewById(R.id.f_friend_list_discover_btn)
        discoverBtn.setOnClickListener {
            Navigation.findNavController(viewF).navigate(
                R.id.action_friendListFragment_to_discoverFragment
            )
        }

        logoutBtn = viewF.findViewById(R.id.f_friend_list_logout_btn)
        logoutBtn.setOnClickListener {
            vm.logout()
            Navigation.findNavController(viewF).navigate(
                R.id.action_friendListFragment_to_welcomeFragment
            )
        }

        listGroup = viewF.findViewById(R.id.f_friend_list_type_rg)
        recyclerView = viewF.findViewById(R.id.list_recycler_view)

        recyclerView.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)

        listGroup.check(R.id.f_friend_list_friends_rb)

        listGroup.setOnCheckedChangeListener { _, checkedId ->
            if (checkedId == R.id.f_friend_list_friends_rb) {
                val viewAdapter = FriendListViewAdapter(activity as MainActivity)
                recyclerView.adapter = viewAdapter
                vm.getCurrentUserFriendList().observe(this@FriendListFragment, Observer {
                    viewAdapter.setFriendList(it)
                })
            }
            else {
                val viewAdapter = PendingRequestListViewAdapter(activity as MainActivity)
                recyclerView.adapter = viewAdapter
                vm.getCurrentUserPendingRequests().observe(this@FriendListFragment, Observer {
                    viewAdapter.setFriendRequests(it)
                })
            }
        }

        return viewF
    }
}
