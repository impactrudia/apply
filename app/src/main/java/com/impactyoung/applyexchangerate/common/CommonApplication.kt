package com.impactyoung.applyexchangerate.common

import androidx.multidex.MultiDexApplication
import com.google.gson.Gson
import com.impactyoung.applyexchangerate.model.BaseResponse

class CommonApplication: MultiDexApplication() {
    companion object{
        var instance : CommonApplication?= null
    }

    val exchangeRateApply by lazy {
        loadJson()
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }

    private fun readMenuJson(): String? {
        return applicationContext.assets.open("exchange_rate.json").bufferedReader().use {
            it.readText()
        }
    }

    private fun loadJson(): BaseResponse? {
        var loadReadJson = readMenuJson()
        return Gson().fromJson(loadReadJson, BaseResponse::class.java)
    }
}