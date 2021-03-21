package com.example.login_kotlin

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_reset__pssword.*

class Reset_Pssword : AppCompatActivity() {

        private lateinit var auth: FirebaseAuth;
        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_reset__pssword)

            auth = FirebaseAuth.getInstance()

            pass_res.setOnClickListener {
                val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                inputMethodManager.hideSoftInputFromWindow(currentFocus!!.windowToken, 0)
                if (res_email.text.toString().isNullOrEmpty())
                    Toast.makeText(this, "Email Address is not provided", Toast.LENGTH_LONG).show()
                else {
                    auth.sendPasswordResetEmail(
                        res_email.text.toString())
                        .addOnCompleteListener(this) { task ->
                            if (task.isSuccessful) {
                                val user = auth.currentUser

                                Toast.makeText(this, "Reset Password Link is mailed", Toast.LENGTH_LONG).show()
                            } else

                                Toast.makeText(this, "Password Reset mail could not be sent", Toast.LENGTH_LONG).show()
                        }
                }
            }
        }



    }