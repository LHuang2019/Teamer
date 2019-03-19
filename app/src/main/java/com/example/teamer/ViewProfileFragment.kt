package com.example.teamer


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.ViewModelProviders
import org.w3c.dom.Text


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
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

        v.findViewById<TextView>(R.id.f_profile_username_tv).text = vm.currentUserData?.username

        var platform_string = ""

        for (index in vm.currentUserData?.platforms!!.indices) {
            if (index > 0) {
                platform_string += ", "
            }
            platform_string += vm.currentUserData?.platforms!![index]
        }

        v.findViewById<TextView>(R.id.platforms_list_tv).text = platform_string

        var games_string = ""

        for (index in vm.currentUserData?.games!!.indices) {
            if (index > 0) {
                games_string += ", "
            }
            games_string += vm.currentUserData?.games!![index]
        }

        v.findViewById<TextView>(R.id.games_list_tv).text = games_string


        return v
    }


}
