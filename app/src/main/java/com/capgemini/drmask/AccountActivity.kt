package com.capgemini.drmask

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.widget.Toast
import com.capgemini.drmask.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_account.*

class AccountActivity : AppCompatActivity() {

    var new =false
    private lateinit var auth: FirebaseAuth
    private lateinit var ref : DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_account)

        new =intent.getBooleanExtra("new",false)

        auth = FirebaseAuth.getInstance()
        Log.d("AccountActivity","Current uid ${auth.currentUser.uid} ${auth.currentUser.email}")

        ref = FirebaseDatabase.getInstance().getReference("users").child(auth.currentUser.uid)
        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                Log.d("AccountActivity","Snapshot: $snapshot")
                if(snapshot.exists())
                {
                    val user = snapshot.getValue(User::class.java)!!
                    settingsNameT.setText(user.name.toString())
                    settingsAgeT.setText(user.age.toString())
                    settingsHeightT.setText(if(user.height.toString()=="") "" else "${user.height.toString()}")
                    settingsWeightT.setText(if(user.weight.toString()=="") "" else "${user.weight.toString()}")
                    settingsEmergencyPhoneT.setText(user.emergencyPhone.toString())
                }
            }

            override fun onCancelled(error: DatabaseError) {
            }

        })

        settingsConfirmB.setOnClickListener {
            if(check(settingsNameT.text) || check(settingsAgeT.text) || check(settingsHeightT.text)|| check(settingsWeightT.text)|| settingsEmergencyPhoneT.text.toString().length<10)
                Toast.makeText(this,"Fill all details",Toast.LENGTH_LONG).show()
            else{
                ref.child("name").setValue(settingsNameT.text.toString())
                ref.child("height").setValue(settingsHeightT.text.toString())
                ref.child("weight").setValue(settingsWeightT.text.toString())
                ref.child("age").setValue(settingsAgeT.text.toString())
                ref.child("emergencyPhone").setValue(settingsEmergencyPhoneT.text.toString())

                Toast.makeText(this,"Details Updated",Toast.LENGTH_LONG).show()

                if(new){
                    startActivity(Intent(this,MainActivity::class.java))
                    finish()
                }
                else{
                    finish()
                }
            }
        }

    }

    private fun check(x: Editable?): Boolean {
        return x.toString().isNullOrEmpty()
    }
}