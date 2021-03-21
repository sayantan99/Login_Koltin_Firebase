package com.example.login_kotlin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.util.Patterns
import android.widget.Toast
import androidx.core.os.HandlerCompat.postDelayed
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_dash.*
import kotlinx.android.synthetic.main.activity_register_session.*

class Register_session : AppCompatActivity() {

    lateinit var mauth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dash)

        //val user= auth.currentUser;

        mauth = FirebaseAuth.getInstance()

        re_but1.setOnClickListener {
            signupUser()
        }

       /* if(user != null)
        {
            Toast.makeText(this,"Already logged in",Toast.LENGTH_LONG).show();
        }




        Handler().postDelayed({

        }, 2000);*/


    }

    fun signupUser() {

        if (email_reg.text.toString().isEmpty()) {
            email_reg.error = "pLEASE The valid  password"
            //email_reg.requestFocus()
        }
        if (pass_reg.text.toString().isEmpty() || pass_reg.text.toString().length < 6 ) {
           pass_reg.error = "Please enter a valid password with more than 6 letters"
           pass_reg.requestFocus()
        }




        mauth.createUserWithEmailAndPassword(email_reg.text.toString(),pass_reg.text.toString() )
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {

                    // Sign in success, update UI with the signed-in user's information
                    //Log.d(applicationContext, "createUserWithEmail:success")

                    mauth.currentUser?.sendEmailVerification()
                        ?.addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                val user = mauth.currentUser
                                startActivity(Intent(this, MainActivity::class.java))
                                //finish()
                            }
                        }
                    //finish()
                } else {
                    // If sign in fails, display a message to the user.
                    //Log.w(TAG, "createUserWithEmail:failure", task.exception)
                    Toast.makeText(
                        baseContext, "Authentication failed.",
                        Toast.LENGTH_SHORT
                    ).show()

                }

                // ...
            }
    }
}
