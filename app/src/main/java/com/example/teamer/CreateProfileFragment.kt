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

class CreateProfileFragment : Fragment() {

    private lateinit var vm : TeamerVM

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        vm = activity.run {
            ViewModelProviders.of(this!!).get(TeamerVM::class.java)
        }


        val v = inflater.inflate(R.layout.fragment_create_profile, container, false)

        // navigate to the view profile fragment when done
        v.findViewById<Button>(R.id.f_create_profile_done_btn).setOnClickListener { view ->
            val username = v.findViewById<TextView>(R.id.f_create_profile_username_et).text

            val platforms = ArrayList<String>()
            val games = ArrayList<String>()

            if (v.findViewById<CheckBox>(R.id.f_create_profile_pc_cb).isChecked) platforms.add("PC")
            if (v.findViewById<CheckBox>(R.id.f_create_profile_ps4_cb).isChecked) platforms.add("PS4")
            if (v.findViewById<CheckBox>(R.id.f_create_profile_xbox_cb).isChecked) platforms.add("XBOX ONE")
            if (v.findViewById<CheckBox>(R.id.f_create_profile_switch_cb).isChecked) platforms.add("Nintendo Switch")

            if (v.findViewById<CheckBox>(R.id.f_create_profile_csgo_cb).isChecked) games.add("CS:GO")
            if (v.findViewById<CheckBox>(R.id.f_create_profile_apex_legends_cb).isChecked) games.add("Apex Legends")
            if (v.findViewById<CheckBox>(R.id.f_create_profile_rocket_league_cb).isChecked) games.add("Rocket League")

            vm.addProfileData(username.toString(), platforms, games)

            Navigation.findNavController(view).navigate(R.id.action_createProfileFragment_to_viewProfileFragment)
        }

//        val data = vm.getCurrentUserData().value
        vm.getCurrentUserData().observe(this@CreateProfileFragment, Observer { data ->
            // if the profile is being edited
            if (!data?.username?.isEmpty()!!) {
                v.findViewById<TextView>(R.id.f_create_profile_title_tv).text = "Modify Profile"
                v.findViewById<EditText>(R.id.f_create_profile_username_et).setText(data.username)
                for (platform in data.platforms) {
                    when (platform) {
                        "PC" -> v.findViewById<CheckBox>(R.id.f_create_profile_pc_cb).isChecked = true
                        "PS4" -> v.findViewById<CheckBox>(R.id.f_create_profile_ps4_cb).isChecked = true
                        "XBOX ONE" -> v.findViewById<CheckBox>(R.id.f_create_profile_xbox_cb).isChecked = true
                        "Nintendo Switch" -> v.findViewById<CheckBox>(R.id.f_create_profile_switch_cb).isChecked = true
                    }
                }

                for (game in data.games) {
                    when (game) {
                        "CS:GO" -> v.findViewById<CheckBox>(R.id.f_create_profile_csgo_cb).isChecked = true
                        "Apex Legends" -> v.findViewById<CheckBox>(R.id.f_create_profile_apex_legends_cb).isChecked = true
                        "Rocket League" -> v.findViewById<CheckBox>(R.id.f_create_profile_rocket_league_cb).isChecked = true
                    }
                }
            }
            else {
                v.findViewById<TextView>(R.id.f_create_profile_title_tv).text = "Create Profile"
                v.findViewById<EditText>(R.id.f_create_profile_username_et).setText("")

                v.findViewById<CheckBox>(R.id.f_create_profile_pc_cb).isChecked = false
                v.findViewById<CheckBox>(R.id.f_create_profile_ps4_cb).isChecked = false
                v.findViewById<CheckBox>(R.id.f_create_profile_xbox_cb).isChecked = false
                v.findViewById<CheckBox>(R.id.f_create_profile_switch_cb).isChecked = false
                v.findViewById<CheckBox>(R.id.f_create_profile_csgo_cb).isChecked = false
                v.findViewById<CheckBox>(R.id.f_create_profile_apex_legends_cb).isChecked = false
                v.findViewById<CheckBox>(R.id.f_create_profile_rocket_league_cb).isChecked = false
            }
        })

        // Inflate the layout for this fragment
        return v
    }

}
