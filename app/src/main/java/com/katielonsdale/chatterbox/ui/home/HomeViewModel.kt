package com.katielonsdale.chatterbox.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class HomeViewModel : ViewModel() {
    private val _text = MutableLiveData<String>().apply {
        value = "No news to show"
    }
    val text: LiveData<String> = _text
}
