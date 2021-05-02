package com.capgemini.drmask

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    val ref = FirebaseDatabase.getInstance().getReference("Humidity").child("Value")
    lateinit var humidityDataValues : MutableList<String>
    val SAMPLE_SIZE =50

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        humidityDataValues = mutableListOf()
        humidityDataValues.clear()

        //FIREBASE
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists())
                {
                    val data= snapshot.value.toString()
                    if(humidityDataValues.size>=SAMPLE_SIZE)
                        humidityDataValues.removeFirst()
                    humidityDataValues.add(data)
                    checkingFirebaseT.text = humidityDataValues.toString()
                }
            }

            override fun onCancelled(error: DatabaseError) {
            }

        })

    }


    fun onButtonClicked(view: View) {
        when(view.id)
        {
            R.id.startB->{
                Toast.makeText(this,"Starting...",Toast.LENGTH_LONG).show()
                startActivity(Intent(this,ActivityStart::class.java))
            }
            R.id.contactB->{
                Toast.makeText(this,"Contact us",Toast.LENGTH_LONG).show()
                startActivity(Intent(this,ActivityContact::class.java))
            }
        }
    }
}