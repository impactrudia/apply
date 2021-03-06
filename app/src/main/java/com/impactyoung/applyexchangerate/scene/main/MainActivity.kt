package com.impactyoung.applyexchangerate.scene.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import com.impactyoung.applyexchangerate.R
import com.impactyoung.applyexchangerate.common.CommonApplication
import com.impactyoung.applyexchangerate.databinding.ActivityMainBinding
import com.impactyoung.applyexchangerate.model.BaseResponse
import java.util.regex.Pattern

class MainActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {
    private lateinit var binding: ActivityMainBinding
    private var jsonExchangeRates: BaseResponse? = null

    private val KOREA_EXCHANGE_RATE_INDEX = 0
    private val JPY_EXCHANGE_RATE_INDEX = 1
    private val PHP_EXCHANGE_RATE_INDEX = 2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.lifecycleOwner = this

        CommonApplication.instance?.let {
            jsonExchangeRates = it.exchangeRateApply
        }

        ArrayAdapter.createFromResource(
            this, R.array.exchange_rate_by_countries, android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.spinnerRecipientCountry.adapter = adapter
        }
        binding.spinnerRecipientCountry.onItemSelectedListener = this
        binding.spinnerRecipientCountry.setSelection(0)

        binding.btuSubmit.setOnClickListener {
            var text = binding.editRemittanceAmountContent.text.toString()
            if(text.isNullOrEmpty() || (text.toDoubleOrNull() != null && text.toDoubleOrNull()!! >= 10_000)){
                Toast.makeText(this@MainActivity, getString(R.string.msg_uncorrect_match), Toast.LENGTH_SHORT).show()
            }
        }
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

        var exchangeRatesByCountriesText = resources.getStringArray(R.array.exchange_rate_by_countries)
        var result: String? = dataExtractBracket(exchangeRatesByCountriesText, position)

        binding.textExchangeRate?.text =String.format("%,.2f %s/USD", exchangeRatePerNation, result)
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