package com.example.teamer


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

        // navigate to the view profile fragment when done
        viewF.findViewById<Button>(R.id.f_create_profile_done_btn).setOnClickListener { view ->
            val username = viewF.findViewById<TextView>(R.id.f_create_profile_username_et).text

            val platforms = LinkedList<Platform>()
            val games = LinkedList<Game>()

            if (viewF.findViewById<CheckBox>(R.id.f_create_profile_pc_cb).isChecked) platforms.add(Platform.PC)
            if (viewF.findViewById<CheckBox>(R.id.f_create_profile_ps4_cb).isChecked) platforms.add(Platform.PS4)
            if (viewF.findViewById<CheckBox>(R.id.f_create_profile_xbox_cb).isChecked) platforms.add(Platform.XBOX_ONE)
            if (viewF.findViewById<CheckBox>(R.id.f_create_profile_switch_cb).isChecked) platforms.add(Platform.SWITCH)

            if (viewF.findViewById<CheckBox>(R.id.f_create_profile_csgo_cb).isChecked) games.add(Game.CSGO)
            if (viewF.findViewById<CheckBox>(R.id.f_create_profile_apex_legends_cb).isChecked) games.add(Game.APEX_LEGENDS)
            if (viewF.findViewById<CheckBox>(R.id.f_create_profile_rocket_league_cb).isChecked) games.add(Game.ROCKET_LEAGUE)

            vm.addProfileData(username.toString(), platforms, games)

            Navigation.findNavController(view).navigate(R.id.action_createProfileFragment_to_viewProfileFragment)
        }

        vm.getCurrentUserData().observe(this@CreateProfileFragment, Observer { data ->
            // if the profile is being edited
            if (!data?.username?.isEmpty()!!) {
                viewF.findViewById<TextView>(R.id.f_create_profile_title_tv).text = "Modify Profile"
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
            }
            else {
                viewF.findViewById<TextView>(R.id.f_create_profile_title_tv).text = "Create Profile"
                viewF.findViewById<EditText>(R.id.f_create_profile_username_et).setText("")

                viewF.findViewById<CheckBox>(R.id.f_create_profile_pc_cb).isChecked = false
                viewF.findViewById<CheckBox>(R.id.f_create_profile_ps4_cb).isChecked = false
                viewF.findViewById<CheckBox>(R.id.f_create_profile_xbox_cb).isChecked = false
                viewF.findViewById<CheckBox>(R.id.f_create_profile_switch_cb).isChecked = false

                viewF.findViewById<CheckBox>(R.id.f_create_profile_csgo_cb).isChecked = false
                viewF.findViewById<CheckBox>(R.id.f_create_profile_apex_legends_cb).isChecked = false
                viewF.findViewById<CheckBox>(R.id.f_create_profile_rocket_league_cb).isChecked = false
            }
        })

        // Inflate the layout for this fragment
        return viewF
    }
}
