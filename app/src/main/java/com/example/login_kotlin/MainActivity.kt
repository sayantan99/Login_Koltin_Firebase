package com.example.login_kotlin

import android.content.Context
import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.util.Patterns
import android.view.View
import android.widget.Toast
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_register_session.*
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginBehavior
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.GoogleAuthProvider
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.util.*
import java.util.Base64.getEncoder


class MainActivity : AppCompatActivity() {
    companion object {
        private const val RC_SIGN_IN = 120
    }


    private lateinit var googleSignInClient: GoogleSignInClient
    lateinit var gso: GoogleSignInOptions
    lateinit var auth: FirebaseAuth
    var highScore: Int = 0
    var callbackManager = CallbackManager.Factory.create();

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)





        auth = FirebaseAuth.getInstance()

        signup.setOnClickListener {
            startActivity(Intent(this, Register_session::class.java))
            finish()
        }
        login.setOnClickListener {
            if ((email.text.toString()).isNotEmpty() && pass.text.toString().isNotEmpty()) {
                dologin()


            } else {

                Toast.makeText(this, "Please fill up the above fields", Toast.LENGTH_LONG).show()

            }

        }
        /*if(user != null)
       {
           Toast.makeText(this,"Already logged in",Toast.LENGTH_LONG).show();
       }
       else
       {
           startActivity(Intent(this,Signin_google::class.java))
       }



       Handler().postDelayed({

       }, 2000);*/

        gs.setOnClickListener {
            initalize()
            signIn()
            //printHashKey(this)

        }
        face.setOnClickListener{
            facebookLogin()
        }
        pre.setOnClickListener {
            val intent = Intent(this, Reset_Pssword::class.java)
            startActivity(intent)
            finish()
        }



    }


    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        updateUI(currentUser)
    }

    private fun updateUI(currentUser: FirebaseUser?) {

        if (currentUser != null) {
            //startActivity(Intent(this, dash::class.java))
        } else {

        }

    }
    override fun onResume() {
        super.onResume()
        moveNextPage()
    }
    fun moveNextPage(){
        var currentUser = FirebaseAuth.getInstance().currentUser
        if(currentUser != null){
            startActivity(Intent(this,Dashboard::class.java))
        }
    }


    fun dologin() {
        if ((email.text.toString()).isEmpty()) {
            email.error = "pLEASE The valid  password"
            //email.requestFocus()
        }

        if (pass.text.toString().isEmpty()) {
            pass.error = "pLEASE The valid  password"
            //pass.requestFocus()
        }

        auth.signInWithEmailAndPassword(email.text.toString(), pass.text.toString())
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {

                    Toast.makeText(this, "Successfully logged in", Toast.LENGTH_LONG).show()

                    val user = auth.currentUser
                    updateUI(user)
                    moveNextPage()

                } else {
                    // If sign in fails, display a message to the user.

                    Toast.makeText(
                        baseContext, "Authentication failed.",
                        Toast.LENGTH_SHORT
                    ).show()
                    updateUI(null)
                    // ...
                }

                // ...
            }


    }


    private fun initalize() {
        gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)


    }

    private fun signIn() {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, MainActivity.RC_SIGN_IN)
    }

    fun facebookLogin()
    {
        LoginManager.getInstance().loginBehavior = LoginBehavior.WEB_VIEW_ONLY
        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile","email"))
        LoginManager.getInstance().registerCallback(callbackManager,object :
            FacebookCallback<LoginResult> {
            override fun onSuccess(result: LoginResult?) {
                firebaseAuthWithFacebook(result)
            }

            override fun onCancel() {

            }

            override fun onError(error: FacebookException?) {

            }

        })

    }
    fun firebaseAuthWithFacebook(result: LoginResult?){
        var credential = FacebookAuthProvider.getCredential(result?.accessToken?.token!!)
        FirebaseAuth.getInstance().signInWithCredential(credential).addOnCompleteListener { task ->
            if(task.isSuccessful){
              Toast.makeText(this,"Login Successful",Toast.LENGTH_LONG).show()
                moveNextPage()
            }
        }
    }



    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        callbackManager.onActivityResult(requestCode,resultCode,data)
       if (requestCode == RC_SIGN_IN) {

            val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)
           val account=task.getResult(ApiException::class.java)
           if (account != null) {
               firebasAuthGoogle(account)
           }
        }
    }

    fun firebasAuthGoogle(acct: GoogleSignInAccount)
    {
        var credent= GoogleAuthProvider.getCredential(acct?.idToken,null)
        FirebaseAuth.getInstance().signInWithCredential(credent).addOnCompleteListener { task ->
            if(task.isSuccessful){
                Toast.makeText(this,"Login Successful",Toast.LENGTH_LONG).show()
                moveNextPage()
            }
        }


    }

    private fun handleResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account: GoogleSignInAccount? = completedTask.getResult(ApiException::class.java)
            //updateUI (account)
            startActivity(Intent(this, Dashboard::class.java))
            //Toast.makeText(this, "Successfully signed in from your google account", Toast.LENGTH_LONG).show()
        } catch (e: ApiException) {
            Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show()
        }
    }

  /* private fun signout() {
        googleSignInClient.signOut().addOnCompleteListener { task: Task<Void> ->
            if (task.isSuccessful) {
                highScore = 0
            }

        }

    }*/
}

