package cz.pecawolf.charactersheet.presentation.extensions

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer

fun <T> LiveData<T>.reObserve(owner: LifecycleOwner, observer: Observer<T>) {
    removeObservers(owner)
    observe(owner, observer)
}

fun <T> MutableLiveData<T>.notifyChanged() {
    postValue(value)
}

fun MutableLiveData<Boolean>.toggle() {
    postValue(value?.not() ?: false)
}