package com.capgemini.drmask

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.telephony.SmsManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_fire_base_plot.*
import kotlinx.android.synthetic.main.activity_main.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.ArrayList


class FireBasePlotActivity : AppCompatActivity() {

    lateinit var humidityDataValues : MutableList<String>
    lateinit var bpmDataValues : MutableList<String>
    lateinit var spo2DataValues : MutableList<String>
    var SAMPLE_SIZE = 15
    lateinit var bpmChart : LineChart
    lateinit var spo2Chart : LineChart
    lateinit var humChart : LineChart
    var maxHum = 0.0
    var minHum = 100.0
    var normalHum =90.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fire_base_plot)
        val ref1 = FirebaseDatabase.getInstance().getReference("BPM").child("Value")
        val ref2 = FirebaseDatabase.getInstance().getReference("Humidity").child("Value")
        val ref3 = FirebaseDatabase.getInstance().getReference("SPO2").child("Value")

        humidityDataValues = mutableListOf()
        bpmDataValues = mutableListOf()
        spo2DataValues = mutableListOf()

        val bpmGraphValues = ArrayList<Entry>()
        val spo2GraphValues = ArrayList<Entry>()
        val humidityGraphValues = ArrayList<Entry>()


        //GRAPHS
        refreshB.setOnClickListener {
            //bpm
            bpmGraphValues.clear()
            for(x in 1..bpmDataValues.size)
                bpmGraphValues.add(Entry((x).toFloat(),(bpmDataValues[x-1]).toFloat()))
            bpmChart = findViewById(R.id.bpmGraph)
            val lineDataSetBpm = LineDataSet(bpmGraphValues,"BPM CHART")
            val dataSetsBpm = ArrayList<ILineDataSet>()
            dataSetsBpm.add(lineDataSetBpm)
            val dataBpm = LineData(dataSetsBpm)
            bpmChart.data=dataBpm
            bpmChart.invalidate()

            //spo2
            spo2GraphValues.clear()
            for(x in 1..spo2DataValues.size)
                spo2GraphValues.add(Entry((x).toFloat(),(spo2DataValues[x-1]).toFloat()))
            spo2Chart = findViewById(R.id.spo2Graph)
            val lineDataSetSpo2 = LineDataSet(spo2GraphValues,"SPO2 CHART")
            val dataSetsSpo2 = ArrayList<ILineDataSet>()
            dataSetsSpo2.add(lineDataSetSpo2)
            val dataSpo2 = LineData(dataSetsSpo2)
            spo2Chart.data=dataSpo2
            spo2Chart.invalidate()

            //spo2
            humidityGraphValues.clear()
            for(x in 1..humidityDataValues.size)
                humidityGraphValues.add(Entry((x).toFloat(),(humidityDataValues[x-1]).toFloat()))
            humChart = findViewById(R.id.humidityGraph)
            val lineDataSetHum = LineDataSet(humidityGraphValues,"HUMIDITY CHART")
            val dataSetsHum = ArrayList<ILineDataSet>()
            dataSetsHum.add(lineDataSetHum)
            val dataHum = LineData(dataSetsHum)
            humChart.data=dataHum
            humChart.invalidate()
        }

        ref1.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists())
                {
                    val data  = snapshot.value.toString()
                    //CONSTRAINT
                    if(data.toDouble()<40 ||data.toDouble()>150) {
                        sendNotification("$data BPM Abnormality",1)
                    }
                    bpmDataValues.add(data)
                    bpmT.text ="BPM : $data"
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
                    humidityT.text ="HUMIDITY : $data"
                    if(humidityDataValues.size>=SAMPLE_SIZE) {
                        humidityDataValues.removeFirst()
                        var sumHum=0.0
                        for (x in humidityDataValues)
                            sumHum+=x.toDouble()
                        sumHum /= SAMPLE_SIZE
                        Toast.makeText(this@FireBasePlotActivity,"$sumHum , $normalHum",Toast.LENGTH_SHORT).show()
                        if(sumHum<normalHum){
                            sendNotification("$sumHum Breathing Abnormality",2)
                        }
                    }
                    if(humidityDataValues.size==10) {
                        for (x in humidityDataValues) {
                            if (x.toDouble() > maxHum) maxHum = x.toDouble()
                            if (x.toDouble() < minHum) minHum = x.toDouble()
                        }
                        normalHum = minHum + (maxHum-minHum)/4
                        Toast.makeText(this@FireBasePlotActivity,"($maxHum,$minHum)\n$normalHum is the normal humidity calibrated",Toast.LENGTH_SHORT).show()
                    }
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
                    if(data.toDouble()<=92){
                        sendNotification("$data SPO2 Abnormality",3)
                    }
                    spo2DataValues.add(data)
                    spo2T.text ="SPO2 : $data"
                    if(spo2DataValues.size>=SAMPLE_SIZE)
                        spo2DataValues.removeFirst()
                }
            }
            override fun onCancelled(error: DatabaseError) {
            }
        })
    }

    private fun sendNotification(msg: String, id: Int) {
        //Getting intent and PendingIntent instance
        //Getting intent and PendingIntent instance
        val intent = Intent(applicationContext, MainActivity::class.java)
        val pi = PendingIntent.getActivity(applicationContext, 0, intent, 0)

        var notifyMessage =msg
        val nManager =getSystemService(NOTIFICATION_SERVICE) as NotificationManager
                val builder: Notification.Builder = if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {//checks version
                    val channel = NotificationChannel("test", "Reminder Done", NotificationManager.IMPORTANCE_HIGH)
                    nManager.createNotificationChannel(channel)

                    val date = LocalDateTime.now()
                    val time = DateTimeFormatter
                        .ofPattern("dd-MM HH:mm:ss").format(date)
                    notifyMessage ="$msg at $time"
                    Notification.Builder(this, "test")
                }
                else  Notification.Builder(this)


        //Get the SmsManager instance and call the sendTextMessage method to send message
        //Get the SmsManager instance and call the sendTextMessage method to send message


                builder.setSmallIcon(R.drawable.ic_launcher_foreground)
                builder.setContentTitle("EMERGENCY")
                builder.setContentText(notifyMessage)
                builder.setAutoCancel(true)


                val myNotify = builder.build()

                nManager.notify(id, myNotify)

        Toast.makeText(this@FireBasePlotActivity,"$notifyMessage is sent as sms",Toast.LENGTH_SHORT).show()
        val sms: SmsManager = SmsManager.getDefault()
        sms.sendTextMessage("7010153098", null, notifyMessage, pi, null)
    }

}