package com.capgemini.drmask.authetication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.capgemini.drmask.AccountActivity
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
                    Toast.makeText(this, "${task.exception?.message}",
                        Toast.LENGTH_SHORT).show()
                }
            }
    }


    private fun saveUser() {
        val userid = auth.currentUser.uid
        val user = User(id = userid,name = nameE.text.toString())
        ref.child(userid).setValue(user).addOnCompleteListener {
           Log.d("SignUp","UserAdded")
        }
    }


    private fun updateUI(user: FirebaseUser?) {
        Toast.makeText(this, "Added", Toast.LENGTH_SHORT).show()
        val intent = Intent(this, AccountActivity::class.java)
        intent.putExtra("new",true)
        startActivity(intent)
        finish()
    }
}