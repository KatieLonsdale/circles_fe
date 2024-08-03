package com.example.innercircles.ui.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.DiffUtil
import com.example.innercircles.api.RetrofitClient.apiService
import com.example.innercircles.api.data.Post
import com.example.innercircles.SessionManager
import com.example.innercircles.api.data.PostResponse
import com.example.innercircles.utils.PostDiffCallback
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeViewModel : ViewModel() {
    private val _text = MutableLiveData<String>().apply {
        value = "No news to show"
    }
    val text: LiveData<String> = _text
}
