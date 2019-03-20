package com.example.teamer


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import org.w3c.dom.Text

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
            v.findViewById<TextView>(R.id.f_profile_username_tv).text = userData.username

            var platform_string = ""
            for (index in userData.platforms.indices) {
                if (index > 0) {
                    platform_string += ", "
                }
                platform_string += userData.platforms[index]
            }


            v.findViewById<TextView>(R.id.platforms_list_tv).text = platform_string

            var games_string = ""

            for (index in userData.games.indices) {
                if (index > 0) {
                    games_string += ", "
                }
                games_string += userData.games[index]
            }

            v.findViewById<TextView>(R.id.games_list_tv).text = games_string
        })

        return v
    }
}
