package com.capgemini.drmask

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast

class ActivityContact : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contact)
    }

    fun onButtonClicked(view: View) {
        when(view.id){
            R.id.smsB->{
                val i = Intent(Intent.ACTION_SENDTO, Uri.parse("sms:9952928460"))
                Toast.makeText(this, "SMSing ....", Toast.LENGTH_LONG).show()
                startActivity(i)
            }
            R.id.callB -> {
//                val i =Intent(Intent.ACTION_CALL,Uri.parse("tel:12345678"))
                val i = Intent(Intent.ACTION_DIAL, Uri.parse("tel:9952928460"))
                Toast.makeText(this, "Calling ....", Toast.LENGTH_LONG).show()
                startActivity(i)
            }
            R.id.emailB -> {
                val send = Intent(Intent.ACTION_SENDTO,Uri.parse("mailto:swethaaravilla@gmail.com"))
                Toast.makeText(this, "Emailing ....", Toast.LENGTH_LONG).show()
                startActivity(Intent.createChooser(send, "Send mail..."))
            }
            R.id.visitB -> {
                val i = Intent(Intent.ACTION_VIEW, Uri.parse("http://capgemini.com"))
                Toast.makeText(this, "Visiting us ....", Toast.LENGTH_LONG).show()
                startActivity(i)
            }
            R.id.locateB->{
                val i =Intent(Intent.ACTION_VIEW, Uri.parse("geo:0,0?q=Chennai%2C%20Tamil%20Nadu%20600127geo%3A0%2C0%3Fq%3DKelambakkam%2520-%2520Vandalur%2520Rd%252C%2520Rajan%2520Nagar%252C%2520Chennai%252C%2520Tamil%2520Nadu%2520600127"))
                Toast.makeText(this, "Locating us ....", Toast.LENGTH_LONG).show()
                startActivity(i)
            }
        }
    }
}