package com.capgemini.drmask.authetication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.capgemini.drmask.MainActivity
import com.capgemini.drmask.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        auth = FirebaseAuth.getInstance()
        loginB.setOnClickListener {
            doLogin()
        }

        newUserT.setOnClickListener {
            startActivity(Intent(this, SignUpActivity::class.java))
            finish()
        }

    }

    private fun doLogin() {
        if(loginEmailE.text.isEmpty()){
            loginEmailE.error ="Please enter email"
            loginEmailE.requestFocus()
            return
        }
        if(loginPasswordE.text.isEmpty()){
            loginPasswordE.error ="Please enter password"
            loginPasswordE.requestFocus()
            return
        }

        //----LOGIN USER---
        auth.signInWithEmailAndPassword(loginEmailE.text.toString(), loginPasswordE.text.toString())
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        val user = auth.currentUser
                        Toast.makeText(this, "Authentication Successful", Toast.LENGTH_SHORT).show()
                        updateUI(user)
                    } else { //wrong details
                        Toast.makeText(this, "Authentication failed.", Toast.LENGTH_SHORT).show()
                    }
                }
    }

    public override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        if(currentUser!=null)
        {
            Toast.makeText(this, "Automatically Signing you in", Toast.LENGTH_SHORT).show()
            updateUI(currentUser)
        }
    }

    private fun updateUI(currentUser : FirebaseUser?){
        startActivity(Intent(this,MainActivity::class.java))
        finish()
    }

}