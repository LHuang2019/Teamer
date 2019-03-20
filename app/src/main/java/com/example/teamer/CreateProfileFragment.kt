package com.example.teamer


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.TextView
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
        v.findViewById<Button>(R.id.f_create_profile_btn).setOnClickListener { view ->
            val username = v.findViewById<TextView>(R.id.username_text_edit).text

            val platforms = ArrayList<String>()
            val games = ArrayList<String>()

            if (v.findViewById<CheckBox>(R.id.f_PC_checkbox).isChecked) platforms.add("PC")
            if (v.findViewById<CheckBox>(R.id.f_PS4_checkbox).isChecked) platforms.add("PS4")
            if (v.findViewById<CheckBox>(R.id.f_XBOX_ONE_checkbox).isChecked) platforms.add("XBOX ONE")
            if (v.findViewById<CheckBox>(R.id.f_Switch_checkbox).isChecked) platforms.add("Nintendo Switch")

            if (v.findViewById<CheckBox>(R.id.f_CSGO_checkbox).isChecked) games.add("CS:GO")
            if (v.findViewById<CheckBox>(R.id.f_apex_legends_checkbox).isChecked) games.add("Apex Legends")
            if (v.findViewById<CheckBox>(R.id.f_rocket_league_checkbox).isChecked) games.add("Rocket League")

            vm.addProfileData(username.toString(), platforms, games)

            Navigation.findNavController(view).navigate(R.id.action_createProfileFragment_to_viewProfileFragment)
        }

        // Inflate the layout for this fragment
        return v
    }

}
