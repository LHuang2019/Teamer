package com.example.teamer.view


import android.app.AlertDialog
import android.content.DialogInterface
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
import com.example.teamer.data.UserData
import com.example.teamer.model.TeamerVM

class FriendProfileFragment : Fragment() {

    private lateinit var vm : TeamerVM

    private lateinit var viewF : View

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        vm = this.run {
            ViewModelProviders.of(this).get(TeamerVM::class.java)
        }

        viewF = inflater.inflate(R.layout.fragment_friend_profile, container, false)

        val userData = this.arguments?.getSerializable("user") as? UserData

        viewF.findViewById<TextView>(R.id.f_friend_profile_username_tv).text = userData?.username

        val platformStr = userData?.platforms?.joinToString { platform -> platform }
        viewF.findViewById<TextView>(R.id.f_friend_profile_platform_list_tv).text = platformStr

        val gameStr = userData?.games?.joinToString { game -> game }
        viewF.findViewById<TextView>(R.id.f_friend_profile_game_list_tv).text = gameStr

        viewF.findViewById<Button>(R.id.f_friend_profile_remove_btn).setOnClickListener {
            val dialogBuilder = AlertDialog.Builder(context)
            dialogBuilder.setTitle("Remove " + userData?.username)
            dialogBuilder.setMessage("Are you sure to remove " + userData?.username + "?")

            dialogBuilder.setPositiveButton("YES"
            ) { _, which ->
                if (which == DialogInterface.BUTTON_POSITIVE) {
                    userData?.uid?.let { uid -> vm.removeFriend(uid) }
                    Navigation.findNavController(viewF).navigate(
                        R.id.action_friendProfileFragment_to_friendListFragment
                    )
                }
            }
            dialogBuilder.setNeutralButton("CANCEL") { _, _ -> }

            dialogBuilder.create().show()
        }

        return viewF
    }
}
