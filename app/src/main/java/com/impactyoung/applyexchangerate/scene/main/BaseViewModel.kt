package com.impactyoung.applyexchangerate.scene.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.impactyoung.applyexchangerate.model.ExchangeRate
import com.impactyoung.applyexchangerate.network.CommonRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.addTo

open class BaseViewModel : ViewModel() {
    private val repository = CommonRepository.getInstance()
    private lateinit var onPauseDisposable: CompositeDisposable
    var onDestroyDisposable = CompositeDisposable()
    var exchangeRate = MutableLiveData<ExchangeRate>()

    fun getAppVersion(success: TypeCallback<ExchangeRate>, error: EmptyCallback) {
        repository.getAPILive()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                if(it.success == true){
                    it?.quotes?.let(success::invoke)
                }else{
                    error?.invoke()
                }
            }, {
                error?.invoke()
            })
            .addToOnDestroyDisposable()
    }

    /**
     * ViewModel 에서만 호출
     */
    private fun Disposable.addToOnDestroyDisposable() {
        addTo(onDestroyDisposable)
    }

    /**
     * onPauseDisposable 객체를 dispose 한다.
     */
    fun clearOnPauseDisposable() {
        if (::onPauseDisposable.isInitialized) {
            onPauseDisposable.clear()
        }
    }

    /**
     * 가지고 있는 모든 CompositeDisposable 객체를 dispose 한다.
     */
    fun disposeAllDisposable() {
        if (!onDestroyDisposable.isDisposed) {
            onDestroyDisposable.dispose()
        }
        if (::onPauseDisposable.isInitialized && !onPauseDisposable.isDisposed) {
            onPauseDisposable.dispose()
        }
    }
}

typealias TypeCallback<T> = (T) -> Unit

/**
 * 실패에 대한 익명함수를 타입화
 */
typealias EmptyCallback = (() -> Unit)?