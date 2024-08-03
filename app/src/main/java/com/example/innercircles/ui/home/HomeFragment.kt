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

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: PostsAdapter
    private val _posts = MutableLiveData<List<Post>>()
    private var postList = mutableListOf<Post>()
    private lateinit var recyclerView: RecyclerView
    private lateinit var tvNoPosts: TextView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        recyclerView = binding.recyclerView
        recyclerView.layoutManager = LinearLayoutManager(context)
        adapter = PostsAdapter(postList)
        recyclerView.adapter = adapter
        tvNoPosts = binding.tvNoPosts

        fetchPosts()

        return binding.root
    }

private fun fetchPosts() {
    val userId = SessionManager.getUserId()
    if (userId != null) {
        apiService.getNewsfeed(userId).enqueue(object : Callback<PostResponse> {
            override fun onResponse(call: Call<PostResponse>, response: Response<PostResponse>) {
                if (response.isSuccessful) {
                    response.body()?.data?.let { fetchedPosts: List<Post> ->
                        if (fetchedPosts.isEmpty()) {
                            tvNoPosts.visibility = View.VISIBLE
                        }
                        tvNoPosts.visibility = View.GONE
                        val sortedPosts = fetchedPosts.sortedBy { it.attributes.createdAt }
                        val diffCallback = PostDiffCallback(postList, sortedPosts)
                        val diffResult = DiffUtil.calculateDiff(diffCallback)
                        postList.clear()
                        postList.addAll(sortedPosts)
                        diffResult.dispatchUpdatesTo(adapter)
                    }
                } else {
                    // Handle error
                    Log.e("MyCirclesFragment", "Failed to fetch circles: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<PostResponse>, t: Throwable) {
                // Handle failure
                Log.e("HomeFragment", "Error fetching posts", t)
            }
        })
    } else {
        Log.e("HomeFragment", "User ID is null")
    }
}
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
