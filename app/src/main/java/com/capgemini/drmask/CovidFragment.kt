package com.capgemini.drmask

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.capgemini.drmask.retrofitdatabase.CovidDbInterface
import com.capgemini.drmask.retrofitdatabase.CovidDetails
import com.capgemini.drmask.retrofitdatabase.NewsDbInterface
import kotlinx.android.synthetic.main.fragment_covid.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response



class CovidFragment : Fragment() {


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        covidBedB.setOnClickListener {
            val intent =Intent(activity,CovidBedsActivity::class.java)
            intent.putExtra("url","https://external.sprinklr.com/insights/explorer/dashboard/601b9e214c7a6b689d76f493/tab/9?id=DASHBOARD_601b9e214c7a6b689d76f493&tabId=9%249_Hospital%20Beds")
            startActivity(intent)
        }
        covidBedB2.setOnClickListener {
            val intent =Intent(activity,CovidBedsActivity::class.java)
            intent.putExtra("url","https://covid.army/chennai/ventilator")
            startActivity(intent)
        }
        covidBedB3.setOnClickListener {
            val intent =Intent(activity,CovidBedsActivity::class.java)
            startActivity(intent)
        }


        val request = CovidDbInterface.getInstance().getCasesDetails("https://api.rootnet.in/covid19-in/stats/latest")
        request.enqueue(object : Callback<CovidDetails> {
            override fun onResponse(call: Call<CovidDetails>, response: Response<CovidDetails>) {
                if (response.isSuccessful) {
                    val cases = response.body()!!.data
                    Log.d("Covid", "Covid :${cases.toString()}")

                    val indiaCases = cases.summary
                    covidSummaryT.text ="INDIA:\nConfirmed : ${indiaCases.total}\nDeaths : ${indiaCases.deaths}\nDischarged : ${indiaCases.discharged}"

                    val stateCases = cases.regional.filter {
                        it.loc == "Tamil Nadu"
                    }
                    if(stateCases.size>=0) {
                        covidStateT.text =stateCases[0].loc
                        covidTotalT.text = "${stateCases[0].totalConfirmed}\nTotal"
                        covidDischargedT.text = "${stateCases[0].discharged}\nDischarged"
                        covidDeathsT.text = "${stateCases[0].deaths}\nDeaths"
                    }
                }
            }

            override fun onFailure(call: Call<CovidDetails>, t: Throwable) {
                Log.d("Covid", "Covid :${t.message}")
            }

        })
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_covid, container, false)
    }

}