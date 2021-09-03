package com.pecawolf.presentation.viewmodel

import android.os.Handler
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.pecawolf.common.extensions.postDelayed
import com.pecawolf.presentation.extensions.add
import com.pecawolf.presentation.extensions.remove
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Maybe
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import timber.log.Timber

open class BaseViewModel : ViewModel() {

    private val _loading = MutableLiveData<MutableSet<String>>(mutableSetOf())

    val isLoading = Transformations.map(_loading) {
        Timber.w("isLoading(): $it")
        it.isNotEmpty()
    }

    val disposable = CompositeDisposable()

    override fun onCleared() {
        super.onCleared()

        if (!disposable.isDisposed) disposable.dispose()
    }

    open fun onRefresh() {}

    fun <T> Observable<T>.observe(
        loading: String,
        onError: (Throwable) -> Unit = {},
        onNext: (T) -> Unit = {},
        onComplete: () -> Unit = {}
    ) = subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .doOnSubscribe { _loading.add(loading) }
        .doOnSubscribe { disposable.add(it) }
        .doOnNext { stopLoadingFor(loading) }
        .doOnError { stopLoadingFor(loading) }
        .subscribe(onNext, onError, onComplete)

    fun <T> Single<T>.observe(
        loading: String,
        onError: (Throwable) -> Unit = {},
        onSuccess: (T) -> Unit = {}
    ) = subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .doOnSubscribe { _loading.add(loading) }
        .doOnSubscribe { disposable.add(it) }
        .doOnSuccess { stopLoadingFor(loading) }
        .doOnError { stopLoadingFor(loading) }
        .subscribe(onSuccess, onError)

    fun <T> Maybe<T>.observe(
        loading: String,
        onError: (Throwable) -> Unit = {},
        onSuccess: (T) -> Unit = {},
        onComplete: () -> Unit = {}
    ) = subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .doOnSubscribe { _loading.add(loading) }
        .doOnSubscribe { disposable.add(it) }
        .doOnSuccess { stopLoadingFor(loading) }
        .doOnError { stopLoadingFor(loading) }
        .doOnComplete { stopLoadingFor(loading) }
        .subscribe(onSuccess, onError, onComplete)

    fun Completable.observe(
        loading: String,
        onError: (Throwable) -> Unit = {},
        onSuccess: () -> Unit = {}
    ) = subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .doOnSubscribe { _loading.add(loading) }
        .doOnSubscribe { disposable.add(it) }
        .doOnComplete { stopLoadingFor(loading) }
        .doOnError { stopLoadingFor(loading) }
        .subscribe(onSuccess, onError)

    private fun stopLoadingFor(loading: String) {
        Handler().postDelayed(500) { _loading.remove(loading) }
    }
}
