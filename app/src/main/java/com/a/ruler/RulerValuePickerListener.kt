package com.a.ruler


interface RulerValuePickerListener {
        fun onValueChange(selectedValue: Int)
        fun onIntermediateValueChange(selectedValue: Int)
    }