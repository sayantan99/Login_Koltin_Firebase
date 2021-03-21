package com.example.login_kotlin

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import com.facebook.login.LoginManager
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_dashboard.*

class Dashboard : AppCompatActivity() {

    private lateinit var mAuth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient
    lateinit var gso: GoogleSignInOptions

    override fun onStart() {
        super.onStart()
        gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)
        mAuth = FirebaseAuth.getInstance()




        sout.setOnClickListener {
            logout()

        }
    }
    fun logout()
    {
        mAuth.signOut()

        googleSignInClient.signOut().addOnCompleteListener { task: Task<Void> ->
            if (task.isSuccessful) {
                val intent = Intent(this, MainActivity::class.java)

                startActivity(intent)
                finish()
            }
        }

        // Facebook session out
        LoginManager.getInstance().logOut()
        finish()
    }
}
