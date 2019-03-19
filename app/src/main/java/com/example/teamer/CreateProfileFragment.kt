package com.example.teamer


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
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
            vm.addProfileData(username.toString(), ArrayList(), ArrayList())

            Navigation.findNavController(view).navigate(R.id.action_createProfileFragment_to_viewProfileFragment)
        }

        // Inflate the layout for this fragment
        return v
    }

}
