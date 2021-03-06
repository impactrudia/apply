package com.impactyoung.applyexchangerate.scene.main

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource

class MainActivityTest{
    @ParameterizedTest
    @CsvSource( "10001", "-1")
    fun 수취_금액을_입력하지_않거나_0보다_작은_금액이거나_10_000보다큰금액(dollar : String){
        //given
        //when
        var isValid = MainActivity.isValidExchangeMoney(dollar)

        //then
        assertThat(isValid).isFalse
    }
}