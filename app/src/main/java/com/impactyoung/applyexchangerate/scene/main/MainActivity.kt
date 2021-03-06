package com.impactyoung.applyexchangerate.scene.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import com.impactyoung.applyexchangerate.R
import com.impactyoung.applyexchangerate.common.CommonApplication
import com.impactyoung.applyexchangerate.model.BaseResponse

class MainActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {
    private var jsonExchangeRates: BaseResponse? = null
    private var textExchangeRate: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        CommonApplication.instance?.let {
            jsonExchangeRates = it.exchangeRateApply
        }
        textExchangeRate = findViewById(R.id.textExchangeRate)

        val spinner: Spinner = findViewById(R.id.spinnerRecipientCountry)
        ArrayAdapter.createFromResource(
            this, R.array.exchange_rates_by_country, android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = adapter
        }
        spinner.onItemSelectedListener = this
        spinner.setSelection(0)
    }

    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, p3: Long) {
        var result = when (position) {
            0 -> {
                jsonExchangeRates?.quotes?.USDKRW.toString()
            }
            1 -> {
                jsonExchangeRates?.quotes?.USDJPY.toString()
            }
            else -> {
                jsonExchangeRates?.quotes?.USDPHP.toString()
            }
        }
        textExchangeRate?.text = result
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {
    }
}