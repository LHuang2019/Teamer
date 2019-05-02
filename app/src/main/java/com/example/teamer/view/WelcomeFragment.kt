package com.example.teamer.view


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import com.example.teamer.R
import com.example.teamer.data.UserLogin
import com.example.teamer.model.TeamerVM


class WelcomeFragment : Fragment() {

    private lateinit var vm : TeamerVM


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        vm = activity.run {
            ViewModelProviders.of(this!!).get(TeamerVM::class.java)
        }
        val viewF : View = inflater.inflate(R.layout.fragment_welcome, container, false)

        val userLogin : UserLogin? = vm.getUserLogin()

        if (userLogin != null) {
            val auth = vm.getAuth()

            auth.signInWithEmailAndPassword(userLogin.email, userLogin.password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        vm.getCurrentUserData().observe(this@WelcomeFragment, Observer {
                            Navigation.findNavController(viewF).navigate(
                                R.id.action_welcomeFragment_to_friendListFragment
                            )
                        })
                    }
                }
        }
        else {
            val signInBtn: Button = viewF.findViewById(R.id.f_welcome_sign_in_btn)
            val signUpBtn: Button = viewF.findViewById(R.id.f_welcome_sign_up_btn)

            signInBtn.visibility = View.VISIBLE
            signUpBtn.visibility = View.VISIBLE

            signInBtn.setOnClickListener {
                Navigation.findNavController(viewF).navigate(R.id.action_welcomeFragment_to_signInFragment)
            }

            signUpBtn.setOnClickListener {
                Navigation.findNavController(viewF).navigate(R.id.action_welcomeFragment_to_signUpFragment)
            }
        }

        return viewF
    }
}
