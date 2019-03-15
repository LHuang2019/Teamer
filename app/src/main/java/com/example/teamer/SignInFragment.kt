package com.example.teamer


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.lifecycle.ViewModelProviders

class SignInFragment : Fragment() {

    private lateinit var vm : TeamerVM

    private lateinit var userNameEt : EditText
    private lateinit var passwordEt : EditText
    private lateinit var signInBtn : Button

    private lateinit var viewF : View

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        vm = activity.run {
            ViewModelProviders.of(this!!).get(TeamerVM::class.java)
        }

        viewF = inflater.inflate(R.layout.fragment_sign_in, container, false)

        userNameEt = viewF.findViewById(R.id.f_sign_in_username_et)
        passwordEt = viewF.findViewById(R.id.f_sign_in_password_et)
        signInBtn = viewF.findViewById(R.id.f_sign_in_btn)

        signInBtn.setOnClickListener {
            val email = userNameEt.text.toString()
            val password = passwordEt.text.toString()

            if (email.isBlank() || password.isBlank()) {
                val toastMsg = "Please enter both an email and password."
                val toast = Toast.makeText(viewF.context, toastMsg, Toast.LENGTH_SHORT)
                toast.show()
            }
            else {
                signIn(email, password)
            }
        }

        return viewF
    }

    fun signIn(email : String, password : String) {
        var auth = vm.getAuth()

        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    vm.setCurrentUser(auth.currentUser)
                }
                else {
                    val toastMsg = "Authentication failed."
                    val toast = Toast.makeText(viewF.context, toastMsg, Toast.LENGTH_SHORT)
                    toast.show()
                }
            }
    }
}