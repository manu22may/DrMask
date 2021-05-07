package com.capgemini.drmask.authetication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.capgemini.drmask.MainActivity
import com.capgemini.drmask.R
import com.capgemini.drmask.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_sign_up.*

class SignUpActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var ref :DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        auth = FirebaseAuth.getInstance()
        ref = FirebaseDatabase.getInstance().getReference("users")

        signUpBtn.setOnClickListener {
            signUpUser()
        }

        existingUserT.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }

    private fun signUpUser() {
        if(signupEmailE.text.isEmpty()){
            signupEmailE.error ="Please enter email"
            signupEmailE.requestFocus()
            return
        }
        if(signupPasswordE.text.isEmpty()){
            signupPasswordE.error ="Please enter password"
            signupPasswordE.requestFocus()
            return
        }
        if(nameE.text.isEmpty()){
            nameE.error ="Please enter name"
            nameE.requestFocus()
            return
        }

        //----CREATE USER---
        auth.createUserWithEmailAndPassword(signupEmailE.text.toString(), signupPasswordE.text.toString())
            .addOnCompleteListener{ task ->
                if (task.isSuccessful) {

                    val user = auth.currentUser
                    saveUser()
                    updateUI(user)
                } else {
                    Toast.makeText(this, "Authentication failed. Try Again After some time",
                        Toast.LENGTH_SHORT).show()
                    updateUI(null)
                }
            }
    }


    private fun saveUser() {
        val userid =ref.push().key!! //generate new key(primary)
        val user = User(userid,nameE.text.toString(),null,null)
        ref.child(userid).setValue(user).addOnCompleteListener {
           Log.d("SignUp","UserAdded")
        }
    }


    private fun updateUI(user: FirebaseUser?) {
        Toast.makeText(this, "Added", Toast.LENGTH_SHORT).show()
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }
}