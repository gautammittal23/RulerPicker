package com.a.ruler

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        ruler_picker.selectValue(20)
        ruler_picker.setValuePickerListener(object:RulerValuePickerListener
        {
            override fun onValueChange(selectedValue: Int) {

                txtviewcurrentvalue.text="$selectedValue"

            }

            override fun onIntermediateValueChange(selectedValue: Int) {
                txtviewcurrentvalue.text="$selectedValue"
            }
        })
    }
}


