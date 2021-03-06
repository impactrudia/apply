package com.impactyoung.applyexchangerate.network

import com.impactyoung.applyexchangerate.model.BaseResponse
import io.reactivex.Single
import retrofit2.http.*
import retrofit2.http.Headers

interface OrganRestApi {
    @Headers("Content-Type: application/json")

    /**
     * http://www.apilayer.net/api/live?access_key=ee50cd7cc73c9b7a7bb3d9617cfb6b9c
     */
    @GET("/api/live")
    fun getApiLive(@Query("access_key") accessKey: String): Single<BaseResponse>
}