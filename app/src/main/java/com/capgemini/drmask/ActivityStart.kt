package com.capgemini.drmask

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.PopupMenu
import kotlinx.android.synthetic.main.activity_start.*

class ActivityStart : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)

        doNews()

        floatingActionButton.setOnClickListener {
            val pMenu = PopupMenu(this,it)
            val menu = pMenu.menu
            menu.add("BMI")
            menu.add("News")
            menu.add("Data")
            pMenu.show()
            pMenu.setOnMenuItemClickListener {
                when(it.title){
                    "BMI"->{
                        doBMI()
                        true
                    }
                    "News"->{
                        doNews()
                        true
                    }
                    "Data"->{
                        true
                    }
                    else->{false}
                }
            }
        }
    }

    private fun doBMI() {
        val frag = BmiFragment.newInstance("","")
        supportFragmentManager.beginTransaction()
            .replace(R.id.startParentL,frag)
            .commit()
    }

    private fun doNews() {
        val frag = NewsFragment()
        supportFragmentManager.beginTransaction()
            .replace(R.id.startParentL,frag)
            .commit()
    }

}