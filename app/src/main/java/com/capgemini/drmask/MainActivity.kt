package com.capgemini.drmask

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.capgemini.drmask.authetication.LoginActivity
import com.capgemini.drmask.retrofitdatabase.PollutionDbInterface
import com.capgemini.drmask.retrofitdatabase.PollutionDetails
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {


    private lateinit var auth: FirebaseAuth
    lateinit var ref:DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        auth = FirebaseAuth.getInstance()
        ref = FirebaseDatabase.getInstance().getReference("users").child(auth.currentUser.uid)


        //POLLUTION INDEX RETROFIT
        val request = PollutionDbInterface.getInstance().getPollutionDetails("https://api.openweathermap.org/data/2.5/air_pollution?lat=13.1&lon=80.2707&appid=9ff634aced5342a274e1882a50f3960f")
        request.enqueue(object : Callback<PollutionDetails>{
            override fun onResponse(call: Call<PollutionDetails>, response: Response<PollutionDetails>) {
                if(response.isSuccessful){
                    Log.d("Pollution","Success ${response.body()}")
                    val pollutionList = response.body()!!.list[0]
                    val aqi = pollutionList.main.aqi
                    val co =  String.format("%.2f", pollutionList.components.co)
                    val no = String.format("%.2f", pollutionList.components.no)
                    val pm = String.format("%.2f", pollutionList.components.pm2_5)

                    //   Where 1 = Good, 2 = Fair, 3 = Moderate, 4 = Poor, 5 = Very Poor.
                    var msg=""
                    when(aqi){
                        1->{
                            msg = "Today, The air quality is good.. Have a good day"
                        }
                        2->{
                            msg = "Today, The air quality is fair.. Have a good day"
                        }
                        3->{
                            msg = "Today, The air quality is moderate.. Take necessary precautions while going out"
                        }
                        4->{
                            msg = "Today, The air quality is Poor.. We recommend not to head outside without a mask or air filter"
                        }
                        5->{
                            msg = "Today, The air quality is Very Poor.. Stay indoors as it is harmful levels"
                        }
                    }
                    mainAqiT.text = msg
                    "$co μg/m3\n\nCO".also { mainCoT.text = it }
                    "$no μg/m3\n\nNO".also { mainNoT.text = it }
                    "$pm μg/m3\n\nPM2.5".also { mainPmT.text = it }
                }
            }

            override fun onFailure(call: Call<PollutionDetails>, t: Throwable) {
                Log.d("Pollution","${t.message}")
            }

        })


        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists())
                {
                    val name = snapshot.child("name").value
                    "Welcome $name".also { mainNameT.text = it }
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
            R.id.mainLogoutB->{
                val currentUser = auth.currentUser
                if(currentUser!=null)
                {
                    Toast.makeText(this, "Logging Out", Toast.LENGTH_SHORT).show()
                    auth.signOut()
                }
                startActivity(Intent(this,LoginActivity::class.java))
                finish()
            }
            R.id.mainAccountB->{
                startActivity(Intent(this,AccountActivity::class.java))
            }
            R.id.dataB->{
                startActivity(Intent(this,FireBasePlotActivity::class.java))
            }
        }
    }
}