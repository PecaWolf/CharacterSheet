package com.pecawolf.presentation.viewmodel.main

import androidx.lifecycle.LiveData
import com.pecawolf.presentation.extensions.SingleLiveEvent
import com.pecawolf.presentation.viewmodel.BaseViewModel

class OtherViewModel(val mainViewModel: MainViewModel) : BaseViewModel() {

    private val _navigateTo = SingleLiveEvent<Destination>()

    val navigateTo: LiveData<Destination> = _navigateTo

    fun onCreateCharacterClicked() {
        _navigateTo.postValue(Destination.CreateCharacter)
    }

    sealed class Destination {
        object CreateCharacter : Destination()
    }
}