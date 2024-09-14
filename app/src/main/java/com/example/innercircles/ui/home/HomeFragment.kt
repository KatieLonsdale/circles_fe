package com.example.innercircles.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.innercircles.SessionManager
import com.example.innercircles.api.data.Post
import com.example.innercircles.api.data.PostResponse
import com.example.innercircles.databinding.FragmentHomeBinding
import com.example.innercircles.utils.PostDiffCallback
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.example.innercircles.api.RetrofitClient.apiService

class HomeFragment : Fragment() {
}
