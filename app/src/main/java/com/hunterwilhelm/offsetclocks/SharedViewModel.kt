package com.hunterwilhelm.offsetclocks

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

// Allows communication between dialogs and activities
class SharedViewModel : ViewModel() {
    val name = MutableLiveData<String>()

    fun sendName(text: String) {
        name.value = text
    }
}