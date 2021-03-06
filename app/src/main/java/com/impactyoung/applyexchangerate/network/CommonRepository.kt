package com.impactyoung.applyexchangerate.network

import com.impactyoung.applyexchangerate.BuildConfig


class CommonRepository private constructor() {
    companion object {
        @Volatile
        private var instance: CommonRepository? = null

        fun getInstance() = instance ?: synchronized(this) {
            instance ?: CommonRepository().also { instance = it }
        }

        fun deleteInstance() {
            instance = null
        }

        val TAG = CommonRepository::class.java.canonicalName
    }

    lateinit var organApi: OrganRestApi

    fun initApis() {
        organApi = ServiceFactory().getOrganApi(BuildConfig.URL_HOST)
    }

  fun getAPILive() = organApi.getApiLive("ee50cd7cc73c9b7a7bb3d9617cfb6b9c")

}
