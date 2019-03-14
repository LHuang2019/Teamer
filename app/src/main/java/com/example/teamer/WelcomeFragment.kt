package com.example.teamer


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.Navigation


class WelcomeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var viewF : View = inflater.inflate(R.layout.fragment_welcome, container, false)

        var signInBtn : Button = viewF.findViewById(R.id.f_welcome_sign_in_btn)
        var signUpBtn : Button = viewF.findViewById(R.id.f_welcome_sign_up_btn)

        signInBtn.setOnClickListener {
            Navigation.findNavController(viewF).navigate(R.id.action_welcomeFragment_to_signInFragment)
        }

        signUpBtn.setOnClickListener {
            Navigation.findNavController(viewF).navigate(R.id.action_welcomeFragment_to_signUpFragment)
        }

        return viewF
    }
}
