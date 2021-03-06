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
import java.util.regex.Pattern

class MainActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {
    private var jsonExchangeRates: BaseResponse? = null
    private var textExchangeRate: TextView? = null

    private val KOREA_EXCHANGE_RATE_INDEX = 0
    private val JPY_EXCHANGE_RATE_INDEX = 1
    private val PHP_EXCHANGE_RATE_INDEX = 2

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
        var exchangeRatePerNation = when (position) {
            KOREA_EXCHANGE_RATE_INDEX -> {
                jsonExchangeRates?.quotes?.USDKRW
            }
            JPY_EXCHANGE_RATE_INDEX -> {
                jsonExchangeRates?.quotes?.USDJPY
            }
            PHP_EXCHANGE_RATE_INDEX -> {
                jsonExchangeRates?.quotes?.USDPHP
            }
            else -> {
                0
            }
        }

        var exchangeRatesByCountriesText = resources.getStringArray(R.array.exchange_rates_by_country)
        var result: String? = dataExtractBracket(exchangeRatesByCountriesText, position)

        textExchangeRate?.text =String.format("%,.2f %s/USD", exchangeRatePerNation, result)
    }

    private fun dataExtractBracket(items: Array<String>, position: Int): String? {
        var p = Pattern.compile("\\((.*?)\\)")
        var m = p.matcher(items.get(position))

        var result: String? = null
        while (m.find()) {
            result = m.group(1)
        }
        return result
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {
    }
}