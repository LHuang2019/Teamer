package com.example.teamer.view


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import com.example.teamer.R
import com.example.teamer.model.TeamerVM


class SignUpFragment : Fragment() {

    private lateinit var vm : TeamerVM

    private lateinit var userNameEt : EditText
    private lateinit var passwordEt : EditText
    private lateinit var passwordConfirmEt : EditText
    private lateinit var signUpBtn : Button

    private lateinit var viewF : View

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        vm = activity.run {
            ViewModelProviders.of(this!!).get(TeamerVM::class.java)
        }

        viewF = inflater.inflate(R.layout.fragment_sign_up, container, false)

        userNameEt = viewF.findViewById(R.id.f_sign_up_email_et)
        passwordEt = viewF.findViewById(R.id.f_sign_up_password_et)
        passwordConfirmEt = viewF.findViewById(R.id.f_sign_up_password_confirm_et)
        signUpBtn = viewF.findViewById(R.id.f_sign_up_btn)

        signUpBtn.setOnClickListener {
            val email = userNameEt.text.toString()
            val password = passwordEt.text.toString()
            val passwordRepeat = passwordConfirmEt.text.toString()

            when {
                email.isBlank() || password.isBlank() -> {
                    val toast = Toast.makeText(viewF.context,
                        "Please enter both an email and password.", Toast.LENGTH_LONG)
                    toast.show()
                }
                passwordRepeat.isBlank() -> passwordConfirmEt.error = "Please confirm your password."
                passwordRepeat != password -> passwordConfirmEt.error = "Password does not match."
                else -> signUp(email, password)
            }
        }

        return viewF
    }

    private fun signUp(email : String, password : String) {
        val auth = vm.getAuth()

        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    vm.addNewUser(auth.currentUser!!)
                    vm.setCurrentUser(auth.currentUser!!)
                    Navigation.findNavController(viewF).navigate(R.id.action_signUpFragment_to_createProfileFragment)
                }
                else {
                    val toast = Toast.makeText(viewF.context,
                        "Account creation failed: " + task.exception?.message, Toast.LENGTH_LONG)
                    toast.show()
                }
            }
    }
}
