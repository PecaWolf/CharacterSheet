package cz.pecawolf.charactersheet.presentation.extensions

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer

fun <T> LiveData<T>.reObserve(owner: LifecycleOwner, observer: (T) -> Unit) {
    removeObservers(owner)
    observe(owner, Observer { observer.invoke(it) })
}

fun <T> MutableLiveData<T>.notifyChanged() {
    postValue(value)
}