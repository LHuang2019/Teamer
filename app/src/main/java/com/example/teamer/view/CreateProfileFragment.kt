package com.example.teamer.view


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import com.example.teamer.misc.Game
import com.example.teamer.misc.Platform
import com.example.teamer.R
import com.example.teamer.model.TeamerVM
import java.util.*

class CreateProfileFragment : Fragment() {

    private lateinit var vm : TeamerVM
    private lateinit var viewF : View

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        vm = activity.run {
            ViewModelProviders.of(this!!).get(TeamerVM::class.java)
        }

        viewF = inflater.inflate(R.layout.fragment_create_profile, container, false)

        vm.getCurrentUserData().observe(this@CreateProfileFragment, Observer { data ->
            // if the profile is being edited
            if (!data?.username?.isEmpty()!!) {
                viewF.findViewById<TextView>(R.id.f_create_profile_title_tv).text = getString(R.string.f_create_profile_title_modify_tv)
                viewF.findViewById<EditText>(R.id.f_create_profile_username_et).setText(data.username)

                for (platform in data.platforms) {
                    when (platform) {
                        Platform.PC.title -> viewF.findViewById<CheckBox>(R.id.f_create_profile_pc_cb).isChecked = true
                        Platform.PS4.title -> viewF.findViewById<CheckBox>(R.id.f_create_profile_ps4_cb).isChecked = true
                        Platform.XBOX_ONE.title -> viewF.findViewById<CheckBox>(R.id.f_create_profile_xbox_cb).isChecked = true
                        Platform.SWITCH.title  -> viewF.findViewById<CheckBox>(R.id.f_create_profile_switch_cb).isChecked = true
                    }
                }

                for (game in data.games) when (game) {
                    Game.CSGO.title -> viewF.findViewById<CheckBox>(R.id.f_create_profile_csgo_cb).isChecked = true
                    Game.APEX_LEGENDS.title -> viewF.findViewById<CheckBox>(R.id.f_create_profile_apex_legends_cb).isChecked = true
                    Game.ROCKET_LEAGUE.title -> viewF.findViewById<CheckBox>(R.id.f_create_profile_rocket_league_cb).isChecked = true
                }

                viewF.findViewById<CheckBox>(R.id.f_create_profile_location_cb).isChecked = data.locationIsPublic
            }
        })

        // navigate to the view profile fragment when done
        viewF.findViewById<Button>(R.id.f_create_profile_done_btn).setOnClickListener { view ->
            val username = viewF.findViewById<TextView>(R.id.f_create_profile_username_et).text

            val platforms = LinkedList<Platform>()
            val games = LinkedList<Game>()

            if (viewF.findViewById<CheckBox>(R.id.f_create_profile_pc_cb).isChecked) platforms.add(
                Platform.PC
            )
            if (viewF.findViewById<CheckBox>(R.id.f_create_profile_ps4_cb).isChecked) platforms.add(
                Platform.PS4
            )
            if (viewF.findViewById<CheckBox>(R.id.f_create_profile_xbox_cb).isChecked) platforms.add(
                Platform.XBOX_ONE
            )
            if (viewF.findViewById<CheckBox>(R.id.f_create_profile_switch_cb).isChecked) platforms.add(
                Platform.SWITCH
            )

            if (viewF.findViewById<CheckBox>(R.id.f_create_profile_csgo_cb).isChecked) games.add(Game.CSGO)
            if (viewF.findViewById<CheckBox>(R.id.f_create_profile_apex_legends_cb).isChecked) games.add(
                Game.APEX_LEGENDS
            )
            if (viewF.findViewById<CheckBox>(R.id.f_create_profile_rocket_league_cb).isChecked) games.add(
                Game.ROCKET_LEAGUE
            )

            val locationIsPublic =  viewF.findViewById<CheckBox>(R.id.f_create_profile_location_cb).isChecked
            val updateLocation =  viewF.findViewById<CheckBox>(R.id.f_create_profile_update_loc_cb).isChecked

            vm.addProfileData(username.toString(), platforms, games, locationIsPublic, updateLocation)

            Navigation.findNavController(view).navigate(R.id.action_createProfileFragment_to_viewProfileFragment)
        }

        return viewF
    }
}
