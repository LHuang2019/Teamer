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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val v = inflater.inflate(R.layout.fragment_view_profile, container, false)

        vm = activity.run {
            ViewModelProviders.of(this!!).get(TeamerVM::class.java)
        }

        vm.getCurrentUserData().observe(this@ViewProfileFragment, Observer { userData ->
            v.findViewById<TextView>(R.id.f_view_profile_username_tv).text = userData.username

            var platform_string = ""
            for (index in userData.platforms.indices) {
                if (index > 0) {
                    platform_string += ", "
                }
                platform_string += userData.platforms[index]
            }


            v.findViewById<TextView>(R.id.f_view_profile_platform_list_tv).text = platform_string

            var games_string = ""

            for (index in userData.games.indices) {
                if (index > 0) {
                    games_string += ", "
                }
                games_string += userData.games[index]
            }

            v.findViewById<TextView>(R.id.f_view_profile_game_list_tv).text = games_string
        })

        v.findViewById<Button>(R.id.f_view_profile_edit_profile_btn).setOnClickListener { view ->
            Navigation.findNavController(v).navigate(
                R.id.action_viewProfileFragment_to_createProfileFragment
            )
        }

        return v
    }
}
