package com.impactyoung.applyexchangerate.scene.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.viewModels
import com.impactyoung.applyexchangerate.R
import com.impactyoung.applyexchangerate.databinding.ActivityMainBinding
import com.impactyoung.applyexchangerate.network.CommonRepository
import java.util.regex.Pattern

class MainActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {
    private lateinit var binding: ActivityMainBinding
    private val mViewModel: BaseViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        binding.lifecycleOwner = this
        binding.viewModel = mViewModel
        setContentView(binding.root)

        CommonRepository.getInstance().initApis()

        ArrayAdapter.createFromResource(
            this, R.array.exchange_rate_by_countries, android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.spinnerRecipientCountry.adapter = adapter
        }
        binding.spinnerRecipientCountry.onItemSelectedListener = this
        mViewModel.exchangeRate.observe(this, {
            if (it != null) {
                binding.spinnerRecipientCountry.setSelection(0)
            }
        })

        binding.btuSubmit.setOnClickListener {
            var exchangeMoney = binding.editRemittanceAmountContent.text.toString()
            if (isValidExchangeMoney(exchangeMoney)) {
                val position = binding.spinnerRecipientCountry.selectedItemPosition
                val dollar = binding.editRemittanceAmountContent.text.toString().toIntOrNull()
                val exchangeRatePerNation = getExchangeRatePerNation(position)
                val result = exchangeRatePerNation?.let { it1 -> dollar?.times(it1) }
                binding.textReceivableAmountResult.text = String.format(
                    getString(R.string.msg_exchange_rate_calculation_result),
                    result,
                    dataExtractBracket(position)
                )
            } else {
                Toast.makeText(
                    this@MainActivity,
                    getString(R.string.msg_uncorrect_match),
                    Toast.LENGTH_SHORT
                ).show()
                binding.textReceivableAmountResult.text = ""
            }
        }

        mViewModel.getAppVersion({
            mViewModel.exchangeRate.postValue(it)
        }, {
            Toast.makeText(
                this@MainActivity,
                getString(R.string.msg_network_error),
                Toast.LENGTH_SHORT
            ).show()
        })
    }


    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, p3: Long) {
        binding.textExchangeRate?.text = String.format(
            getString(R.string.exchange_rate_result),
            getExchangeRatePerNation(position),
            dataExtractBracket(position)
        )
    }

    private fun getExchangeRatePerNation(position: Int): Double? {
        var exchangeRateValue = mViewModel.exchangeRate.value
        return when (position) {
            KRW_EXCHANGE_RATE_INDEX -> {
                exchangeRateValue?.USDKRW ?: 0.0
            }
            JPY_EXCHANGE_RATE_INDEX -> {
                exchangeRateValue?.USDJPY ?: 0.0
            }
            PHP_EXCHANGE_RATE_INDEX -> {
                exchangeRateValue?.USDPHP ?: 0.0
            }
            else -> {
                0.0
            }
        }
    }

    private fun dataExtractBracket(position: Int): String? {
        var items = resources.getStringArray(R.array.exchange_rate_by_countries)
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

    override fun onPause() {
        super.onPause()
        mViewModel.clearOnPauseDisposable()
    }

    override fun onDestroy() {
        super.onDestroy()
        mViewModel.disposeAllDisposable()
    }

    companion object {
        const val KRW_EXCHANGE_RATE_INDEX = 0
        const val JPY_EXCHANGE_RATE_INDEX = 1
        const val PHP_EXCHANGE_RATE_INDEX = 2

        fun isValidExchangeMoney(exchangeMoney: String): Boolean{
            return !exchangeMoney.isNullOrEmpty() && exchangeMoney.toDoubleOrNull()!!>0 && exchangeMoney.toDoubleOrNull()!! <=10_000
//            return exchangeMoney.isNullOrEmpty() || (!exchangeMoney.isNullOrEmpty() && (exchangeMoney.toDoubleOrNull()!!<0 || exchangeMoney.toDoubleOrNull()!! > 10000))
        }

    }
}