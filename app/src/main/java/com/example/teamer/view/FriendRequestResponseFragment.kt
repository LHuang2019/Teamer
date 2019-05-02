package com.example.teamer.view


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import com.example.teamer.R
import com.example.teamer.data.FriendRequest
import com.example.teamer.model.TeamerVM

class FriendRequestResponseFragment : Fragment() {

    lateinit var vm : TeamerVM

    lateinit var viewF : View

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        vm = this.run {
            ViewModelProviders.of(this).get(TeamerVM::class.java)
        }

        viewF = inflater.inflate(R.layout.fragment_friend_request_response, container, false)

        val friendRequest = this.arguments?.getSerializable("friend_request") as? FriendRequest
        val userData = friendRequest?.sender

        viewF.findViewById<TextView>(R.id.f_response_username_tv).text = userData?.username

        val platformStr = userData?.platforms?.joinToString { platform -> platform }
        viewF.findViewById<TextView>(R.id.f_response_platform_list_tv).text = platformStr

        val gameStr = userData?.games?.joinToString { game -> game }
        viewF.findViewById<TextView>(R.id.f_response_game_list_tv).text = gameStr


        viewF.findViewById<Button>(R.id.f_response_accept_btn).setOnClickListener {
            friendRequest?.uid?.let { uid -> vm.removeFriendRequest(uid) }
            friendRequest?.recipientUid?.let { recipientUid -> vm.addFriend(recipientUid, friendRequest.sender) }

            Navigation.findNavController(viewF).navigate(
                R.id.action_friendRequestResponseFragment_to_friendListFragment
            )
        }

        viewF.findViewById<Button>(R.id.f_response_decline_btn).setOnClickListener {
            friendRequest?.uid?.let { uid -> vm.removeFriendRequest(uid) }

            Navigation.findNavController(viewF).navigate(
                R.id.action_friendRequestResponseFragment_to_friendListFragment
            )
        }

        return viewF
    }


}
