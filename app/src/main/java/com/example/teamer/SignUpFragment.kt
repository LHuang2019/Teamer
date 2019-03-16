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

            if (email.isBlank() || password.isBlank()) {
                val toast = Toast.makeText(viewF.context,
                    "Please enter both an email and password.", Toast.LENGTH_SHORT)
                toast.show()
            }
            else {
                signUp(email, password)
            }
        }

        return viewF
    }

    private fun signUp(email : String, password : String) {
        var auth = vm.getAuth()

        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    vm.addNewUser(auth.currentUser!!)
                    vm.setCurrentUser(auth.currentUser!!)
                }
                else {
                    val toast = Toast.makeText(viewF.context, "Account creation failed.", Toast.LENGTH_SHORT)
                    toast.show()
                }
            }
    }
}
