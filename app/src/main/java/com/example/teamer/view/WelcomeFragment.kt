package com.example.teamer.view


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.Navigation
import com.example.teamer.R


class WelcomeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val viewF : View = inflater.inflate(R.layout.fragment_welcome, container, false)

        val signInBtn : Button = viewF.findViewById(R.id.f_welcome_sign_in_btn)
        val signUpBtn : Button = viewF.findViewById(R.id.f_welcome_sign_up_btn)

        signInBtn.setOnClickListener {
            Navigation.findNavController(viewF).navigate(R.id.action_welcomeFragment_to_signInFragment)
        }

        signUpBtn.setOnClickListener {
            Navigation.findNavController(viewF).navigate(R.id.action_welcomeFragment_to_signUpFragment)
        }

        return viewF
    }
}
