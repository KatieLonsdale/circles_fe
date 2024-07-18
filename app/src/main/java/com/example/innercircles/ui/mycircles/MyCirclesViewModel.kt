package com.example.innercircles.ui.mycircles

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MyCirclesViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "No circles yet."
    }
    val text: LiveData<String> = _text
}