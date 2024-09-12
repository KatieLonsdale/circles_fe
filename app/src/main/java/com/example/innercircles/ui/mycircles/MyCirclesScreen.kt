package com.example.innercircles.ui.mycircles

import android.util.Log
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.innercircles.SessionManager
import com.example.innercircles.api.RetrofitClient
import com.example.innercircles.api.data.Circle
import com.example.innercircles.api.data.CirclesResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

var circles by mutableStateOf(emptyList<Circle>())

@Composable
fun MyCirclesScreen() {
    var isLoading by remember { mutableStateOf(true) }
    val userId = SessionManager.getUserId()

    LaunchedEffect(Unit) {
        getCircles(userId)  // Await the result of getPosts
        isLoading = false    // Set loading to false after fetching the posts
    }

    if (isLoading) {
        // Show loading indicator while posts are being fetched
        CircularProgressIndicator()
    } else {
        MyCirclesList(circles)
    }
}

@Composable
fun MyCirclesList(circles: List<Circle>) {
    LazyRow {
        items(circles) { circle ->
            CircleCard(circle)
        }
    }
}

@Composable
fun CircleCard(circle: Circle) {
    val circleAttributes = circle.attributes
    Row(modifier = Modifier.padding(all = 8.dp)) {
        Text (
            text = circleAttributes.name,
            color = MaterialTheme.colorScheme.primary,
            fontSize = 15.sp,
        )

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = circleAttributes.description,
            color = MaterialTheme.colorScheme.primary,
            fontSize = 15.sp,
        )

        Spacer(modifier = Modifier.height(20.dp))
    }
}

private fun getCircles(userId: String?) {
    RetrofitClient.apiService.getCircles(userId).enqueue(object : Callback<CirclesResponse> {
        override fun onResponse(call: Call<CirclesResponse>, response: Response<CirclesResponse>) {
            if (response.isSuccessful) {
                circles = response.body()?.data ?: emptyList()
            } else {
                Log.e("MainActivity", "Failed to fetch newsfeed: ${response.errorBody()?.string()}")
            }
        }

        override fun onFailure(call: Call<CirclesResponse>, t: Throwable) {
            Log.e("NewsfeedScreen", "Error fetching posts", t)
        }
    })
}