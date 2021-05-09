package com.capgemini.drmask

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_fire_base_plot.*
import kotlinx.android.synthetic.main.activity_main.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class FireBasePlotActivity : AppCompatActivity() {

    lateinit var humidityDataValues : MutableList<String>
    lateinit var bpmDataValues : MutableList<String>
    lateinit var spo2DataValues : MutableList<String>
    var SAMPLE_SIZE = 20


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fire_base_plot)
        val ref1 = FirebaseDatabase.getInstance().getReference("BPM").child("Value")
        val ref2 = FirebaseDatabase.getInstance().getReference("Humidity").child("Value")
        val ref3 = FirebaseDatabase.getInstance().getReference("SPO2").child("Value")

        humidityDataValues = mutableListOf()
        bpmDataValues = mutableListOf()
        spo2DataValues = mutableListOf()

        ref1.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists())
                {
                    val data  = snapshot.value.toString()
                    //CONSTRAINT
                    if(data.toDouble()<40 ||data.toDouble()>200) {
                        Toast.makeText(
                            this@FireBasePlotActivity,
                            "$data BPM Abnormality",
                            Toast.LENGTH_SHORT
                        ).show()
                        sendNotification("$data BPM Abnormality",1)
                    }
                    bpmDataValues.add(data)
                    bpmT.text = bpmDataValues.toString()
                    if(bpmDataValues.size>=SAMPLE_SIZE)
                        bpmDataValues.removeFirst()
                }
            }
            override fun onCancelled(error: DatabaseError) {
            }
        })


        ref2.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists())
                {
                    val data  = snapshot.value.toString()
                    humidityDataValues.add(data)
                    humidityT.text =humidityDataValues.toString()
                    if(humidityDataValues.size>=SAMPLE_SIZE)
                        humidityDataValues.removeFirst()
                }
            }
            override fun onCancelled(error: DatabaseError) {
            }
        })


        ref3.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists())
                {
                    val data  = snapshot.value.toString()
                    //CONSTRAINT
                    if(data.toDouble()<94){
                        Toast.makeText(this@FireBasePlotActivity,"$data Spo2 Abnormality",Toast.LENGTH_SHORT).show()
                        sendNotification("$data SPO2 Abnormality",3)
                    }
                    spo2DataValues.add(data)
                    spo2T.text=spo2DataValues.toString()
                    if(spo2DataValues.size>=SAMPLE_SIZE)
                        spo2DataValues.removeFirst()
                }
            }
            override fun onCancelled(error: DatabaseError) {
            }
        })
    }

    private fun sendNotification(msg: String, id: Int) {
        var notifyMessage =msg
        val nManager =getSystemService(NOTIFICATION_SERVICE) as NotificationManager
                val builder: Notification.Builder = if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {//checks version
                    val channel = NotificationChannel("test", "Reminder Done", NotificationManager.IMPORTANCE_DEFAULT)
                    nManager.createNotificationChannel(channel)

                    val date = LocalDateTime.now()
                    val time = DateTimeFormatter
                        .ofPattern("dd-MM HH:mm:ss").format(date)
                    notifyMessage ="$msg at $time"
                    Notification.Builder(this, "test")
                }
                else  Notification.Builder(this)

                builder.setSmallIcon(R.drawable.ic_launcher_foreground)
                builder.setContentTitle("EMERGENCY")
                builder.setContentText(notifyMessage)
                builder.setAutoCancel(true)


                val myNotify = builder.build()

                nManager.notify(id, myNotify)
    }

}