package com.capgemini.drmask

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_bmi.*
import kotlin.math.round


private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class BmiFragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null


    private lateinit var auth: FirebaseAuth
    lateinit var ref: DatabaseReference


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = FirebaseAuth.getInstance()
        ref = FirebaseDatabase.getInstance().getReference("users").child(auth.currentUser.uid)
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists())
                {
                    val weight = snapshot.child("weight").value
                    val height = snapshot.child("height").value
                    bmiHeightE.setText("$height")
                    bmiWeightE.setText("$weight")
                }
            }

            override fun onCancelled(error: DatabaseError) {
            }

        })

        bmiGoB.setOnClickListener {
            if(bmiHeightE.text==null ||bmiWeightE.text==null)
                Toast.makeText(activity,"Enter valid Details",Toast.LENGTH_LONG).show()
            else{
                val height = bmiHeightE.text.toString().toDouble()
                val weight = bmiWeightE.text.toString().toDouble()
                var bmi = weight*10000/(height*height)
                bmi=String.format("%.2f", bmi).toDouble()
                bmiAnsT.text="BMI = $bmi"
                when{
                    bmi>35.0-> {
                        bmiImage.setImageResource(R.drawable.extremely_obese)
                        bmiInfoT.text ="You are Extremely Obese"+
                                "\n" +
                                "Obesity is considered the leading preventable disease worldwide. This can be achieved mainly by lifestyle modification that includes diet plans for weight loss and physical activity. Behavioral therapy, drug therapy, and surgery also aid in reducing the illness.\n" +
                                "\n" +
                                "The treatment can include\n" +
                                "\n" +
                                "1. Weight loss diet control and management\n" +
                                "2. Behavioral therapy (through stress management, counseling people for psychological eating disorders, anti-depression therapies)\n" +
                                "3. Drug management (consisting of medication for specific hormonal imbalances and other co-morbidities)\n" +
                                "4. Surgery (Bariatric surgery and procedures like gastric sleeve and Roux--En-Y gastric bypass surgery)"
                    }
                    bmi>30.0-> {bmiImage.setImageResource(R.drawable.obese)
                        bmiInfoT.text ="You are Obese"+
                                "\n" +"Obesity is considered the leading preventable disease worldwide. This can be achieved mainly by lifestyle modification that includes diet plans for weight loss and physical activity. Behavioral therapy, drug therapy, and surgery also aid in reducing the illness.\n" +
                                "\n" +
                                "The treatment can include\n" +
                                "\n" +
                                "1. Weight loss diet control and management\n" +
                                "2. Behavioral therapy (through stress management, counseling people for psychological eating disorders, anti-depression therapies)\n" +
                                "3. Drug management (consisting of medication for specific hormonal imbalances and other co-morbidities)\n" +
                                "4. Surgery (Bariatric surgery and procedures like gastric sleeve and Roux--En-Y gastric bypass surgery)"
                    }
                    bmi>25.0-> {bmiImage.setImageResource(R.drawable.overweight)
                        bmiInfoT.text ="You are Overweight"+
                                "\n" +"Obesity is considered the leading preventable disease worldwide. This can be achieved mainly by lifestyle modification that includes diet plans for weight loss and physical activity. Behavioral therapy, drug therapy, and surgery also aid in reducing the illness.\n" +
                                "\n" +
                                "The treatment can include\n" +
                                "\n" +
                                "1. Weight loss diet control and management\n" +
                                "2. Behavioral therapy (through stress management, counseling people for psychological eating disorders, anti-depression therapies)\n" +
                                "3. Drug management (consisting of medication for specific hormonal imbalances and other co-morbidities)\n" +
                                "4. Surgery (Bariatric surgery and procedures like gastric sleeve and Roux--En-Y gastric bypass surgery)"
                    }
                    bmi>18.5-> { bmiImage.setImageResource(R.drawable.normal)
                        bmiInfoT.text =" You are healthy.. continue your diet and exercises to stay healthy"
                    }
                    else-> {bmiImage.setImageResource(R.drawable.underweight)
                        bmiInfoT.text =" You are underweight.. we recommend a diet-change with protein and carbohydrates"
                    }
                }
            }
        }

    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_bmi, container, false)
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            BmiFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}