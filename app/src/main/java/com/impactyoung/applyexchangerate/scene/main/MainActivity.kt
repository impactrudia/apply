package com.impactyoung.applyexchangerate.scene.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.impactyoung.applyexchangerate.R
import com.impactyoung.applyexchangerate.common.CommonApplication

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        CommonApplication.instance?.let {
            val list = it.exchangeRateApply
        }
    }
}