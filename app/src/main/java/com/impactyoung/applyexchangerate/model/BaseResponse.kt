package com.impactyoung.applyexchangerate.model

data class BaseResponse(
    val success: Boolean? = false,
    val terms: String? = "",
    val privacy: String? = "",
    val timestamp: Int? = 0,
    val source: String? = "",
    val quotes: ExchangeRate? = ExchangeRate()
)