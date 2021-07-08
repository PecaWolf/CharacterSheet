package com.pecawolf.presentation.extensions

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.Observer

class MergedLiveData2<S1, S2, O>(
    source1: LiveData<S1>,
    source2: LiveData<S2>,
    private val combine: (data1: S1, data2: S2) -> O
) : MediatorLiveData<O>() {

    private var data1: S1? = null
    private var data2: S2? = null

    init {
        super.addSource(source1) {
            data1 = it
            checkAndCombine()
        }
        super.addSource(source2) {
            data2 = it
            checkAndCombine()
        }
    }

    private fun checkAndCombine() {
        if (data1 != null && data2 != null) {
            value = combine(data1!!, data2!!)
        }
    }

    override fun <T : Any?> addSource(source: LiveData<T>, onChanged: Observer<in T>) {
        throw UnsupportedOperationException()
    }


    override fun <T : Any?> removeSource(toRemote: LiveData<T>) {
        throw UnsupportedOperationException()
    }
}

class MergedLiveData3<S1, S2, S3, O>(
    source1: LiveData<S1>,
    source2: LiveData<S2>,
    source3: LiveData<S3>,
    private val combine: (data1: S1, data2: S2, data3: S3) -> O
) : MediatorLiveData<O>() {

    private var data1: S1? = null
    private var data2: S2? = null
    private var data3: S3? = null

    init {
        super.addSource(source1) {
            data1 = it
            checkAndCombine()
        }
        super.addSource(source2) {
            data2 = it
            checkAndCombine()
        }
        super.addSource(source3) {
            data3 = it
            checkAndCombine()
        }
    }

    override fun <T : Any?> addSource(source: LiveData<T>, onChanged: Observer<in T>) {
        throw UnsupportedOperationException()
    }


    override fun <T : Any?> removeSource(toRemote: LiveData<T>) {
        throw UnsupportedOperationException()
    }

    private fun checkAndCombine() {
        if (data1 != null && data2 != null && data3 != null) {
            value = combine(data1!!, data2!!, data3!!)
        }
    }
}

class MergedLiveData4<S1, S2, S3, S4, O>(
    source1: LiveData<S1>,
    source2: LiveData<S2>,
    source3: LiveData<S3>,
    source4: LiveData<S4>,
    private val combine: (data1: S1, data2: S2, data3: S3, data4: S4) -> O
) : MediatorLiveData<O>() {

    private var data1: S1? = null
    private var data2: S2? = null
    private var data3: S3? = null
    private var data4: S4? = null

    init {
        super.addSource(source1) {
            data1 = it
            checkAndCombine()
        }
        super.addSource(source2) {
            data2 = it
            checkAndCombine()
        }
        super.addSource(source3) {
            data3 = it
            checkAndCombine()
        }
        super.addSource(source4) {
            data4 = it
            checkAndCombine()
        }
    }

    override fun <T : Any?> addSource(source: LiveData<T>, onChanged: Observer<in T>) {
        throw UnsupportedOperationException()
    }


    override fun <T : Any?> removeSource(toRemote: LiveData<T>) {
        throw UnsupportedOperationException()
    }

    private fun checkAndCombine() {
        if (data1 != null && data2 != null && data3 != null && data4 != null) {
            value = combine(data1!!, data2!!, data3!!, data4!!)
        }
    }
}

class MergedLiveData5<S1, S2, S3, S4, S5, O>(
    source1: LiveData<S1>,
    source2: LiveData<S2>,
    source3: LiveData<S3>,
    source4: LiveData<S4>,
    source5: LiveData<S5>,
    private val combine: (data1: S1, data2: S2, data3: S3, data4: S4, data5: S5) -> O
) : MediatorLiveData<O>() {

    private var data1: S1? = null
    private var data2: S2? = null
    private var data3: S3? = null
    private var data4: S4? = null
    private var data5: S5? = null

    init {
        super.addSource(source1) {
            data1 = it
            checkAndCombine()
        }
        super.addSource(source2) {
            data2 = it
            checkAndCombine()
        }
        super.addSource(source3) {
            data3 = it
            checkAndCombine()
        }
        super.addSource(source4) {
            data4 = it
            checkAndCombine()
        }
        super.addSource(source5) {
            data5 = it
            checkAndCombine()
        }
    }

    override fun <T : Any?> addSource(source: LiveData<T>, onChanged: Observer<in T>) {
        throw UnsupportedOperationException()
    }


    override fun <T : Any?> removeSource(toRemote: LiveData<T>) {
        throw UnsupportedOperationException()
    }

    private fun checkAndCombine() {
        if (data1 != null && data2 != null && data3 != null && data4 != null && data5 != null) {
            value = combine(data1!!, data2!!, data3!!, data4!!, data5!!)
        }
    }
}

class MergedLiveData7<S1, S2, S3, S4, S5, S6, S7, O>(
    source1: LiveData<S1>,
    source2: LiveData<S2>,
    source3: LiveData<S3>,
    source4: LiveData<S4>,
    source5: LiveData<S5>,
    source6: LiveData<S6>,
    source7: LiveData<S7>,
    private val combine: (data1: S1, data2: S2, data3: S3, data4: S4, data5: S5, data6: S6, data7: S7) -> O
) : MediatorLiveData<O>() {

    private var data1: S1? = null
    private var data2: S2? = null
    private var data3: S3? = null
    private var data4: S4? = null
    private var data5: S5? = null
    private var data6: S6? = null
    private var data7: S7? = null

    init {
        super.addSource(source1) {
            data1 = it
            checkAndCombine()
        }
        super.addSource(source2) {
            data2 = it
            checkAndCombine()
        }
        super.addSource(source3) {
            data3 = it
            checkAndCombine()
        }
        super.addSource(source4) {
            data4 = it
            checkAndCombine()
        }
        super.addSource(source5) {
            data5 = it
            checkAndCombine()
        }
        super.addSource(source6) {
            data6 = it
            checkAndCombine()
        }
        super.addSource(source7) {
            data7 = it
            checkAndCombine()
        }
    }

    override fun <T : Any?> addSource(source: LiveData<T>, onChanged: Observer<in T>) {
        throw UnsupportedOperationException()
    }


    override fun <T : Any?> removeSource(toRemote: LiveData<T>) {
        throw UnsupportedOperationException()
    }

    private fun checkAndCombine() {
        if (data1 != null && data2 != null && data3 != null && data4 != null && data5 != null && data6 != null && data7 != null) {
            value = combine(data1!!, data2!!, data3!!, data4!!, data5!!, data6!!, data7!!)
        }
    }
}