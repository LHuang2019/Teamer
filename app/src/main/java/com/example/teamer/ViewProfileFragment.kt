package com.example.teamer


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation

class ViewProfileFragment : Fragment() {

    private lateinit var vm : TeamerVM
    private lateinit var viewF : View

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        vm = activity.run {
            ViewModelProviders.of(this!!).get(TeamerVM::class.java)
        }

        viewF = inflater.inflate(R.layout.fragment_view_profile, container, false)

        vm.getCurrentUserData().observe(this@ViewProfileFragment, Observer { userData ->
            viewF.findViewById<TextView>(R.id.f_view_profile_username_tv).text = userData.username

            val platformStr = userData.platforms.joinToString { platform -> platform }
            viewF.findViewById<TextView>(R.id.f_view_profile_platform_list_tv).text = platformStr

            val gameStr = userData.games.joinToString { game -> game }
            viewF.findViewById<TextView>(R.id.f_view_profile_game_list_tv).text = gameStr
        })

        viewF.findViewById<Button>(R.id.f_view_profile_edit_profile_btn).setOnClickListener {
            Navigation.findNavController(viewF).navigate(
                R.id.action_viewProfileFragment_to_createProfileFragment
            )
        }

        return viewF
    }
}
