package com.capgemini.drmask

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
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