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

    private var _binding: FragmentMyCirclesBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: MyCirclesAdapter
    private var circleList = mutableListOf<Circle>()
    private lateinit var tvNoCircles: TextView
    private lateinit var preferencesHelper: PreferencesHelper

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentMyCirclesBinding.inflate(inflater, container, false)

        recyclerView = binding.recyclerView
        recyclerView.layoutManager = LinearLayoutManager(context)
        adapter = MyCirclesAdapter(circleList)
        recyclerView.adapter = adapter
        tvNoCircles = binding.tvNoCircles
        preferencesHelper = PreferencesHelper(requireContext())

        val userId = preferencesHelper.getUserId()

        fetchCircles(userId)

        return binding.root
    }

    private fun fetchCircles(userId: String?) {
        apiService.getCircles(userId).enqueue(object : Callback<CirclesResponse> {
            override fun onResponse(call: Call<CirclesResponse>, response: Response<CirclesResponse>) {
                if (response.isSuccessful) {
                    response.body()?.data?.let {fetchedCircles: List<Circle> ->
                        if (fetchedCircles.isEmpty()) {
                            tvNoCircles.visibility = View.VISIBLE

                        }
                    tvNoCircles.visibility = View.GONE
                    val sortedCircles = fetchedCircles.sortedBy { it.attributes.name }
                    val diffCallback = CircleDiffCallback(circleList, sortedCircles)
                    val diffResult = DiffUtil.calculateDiff(diffCallback)
                    circleList.clear()
                    circleList.addAll(sortedCircles)
                    diffResult.dispatchUpdatesTo(adapter)
                    }
                } else {
                    // Handle error
                    Log.e("MyCirclesFragment", "Failed to fetch circles: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<CirclesResponse>, t: Throwable) {
                // Handle failure
                Log.e("MyCirclesFragment", "Error fetching circles", t)
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}