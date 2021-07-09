package com.pecawolf.presentation.extensions

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
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