package com.pecawolf.presentation.extensions

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer

fun <T> LiveData<T>.reObserve(owner: LifecycleOwner, observer: Observer<T>) {
    removeObservers(owner)
    observe(owner, observer)
}

fun <T> MutableLiveData<MutableSet<T>>.add(item: T) {
    value = value?.apply {
        add(item)
    } ?: mutableSetOf()
}

fun <T> MutableLiveData<MutableSet<T>>.remove(item: T) {
    value = value?.apply {
        remove(item)
    } ?: mutableSetOf()
}

fun <T> MutableLiveData<T>.notifyChanged() {
    postValue(value)
}

fun MutableLiveData<Boolean>.toggle() {
    postValue(value?.not() ?: false)
}

fun MutableLiveData<Int>.inc() {
    postValue(value?.inc() ?: 0)
}

fun MutableLiveData<Int>.dec() {
    postValue(value?.dec() ?: 0)
}

inline fun <X, Y> LiveData<X>.mapNotNull(crossinline transform: (X) -> Y?): LiveData<Y> {
    val result = MediatorLiveData<Y>()

    result.addSource(this) { transform(it)?.let { result.value = it } }

    return result
}