package com.example.innercircles.ui.mycircles

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.innercircles.databinding.FragmentMyCirclesBinding
import com.example.innercircles.api.RetrofitClient.apiService
import com.example.innercircles.api.data.MyCirclesAdapter
import com.example.innercircles.api.data.Circle
import com.example.innercircles.api.data.CirclesResponse
import com.example.innercircles.utils.CircleDiffCallback
import com.example.innercircles.utils.PreferencesHelper
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MyCirclesFragment : Fragment() {
}