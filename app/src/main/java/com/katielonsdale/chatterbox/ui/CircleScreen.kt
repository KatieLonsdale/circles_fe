package com.katielonsdale.chatterbox.ui

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.katielonsdale.chatterbox.SampleData
import com.katielonsdale.chatterbox.SessionManager
import com.katielonsdale.chatterbox.api.RetrofitClient.apiService
import com.katielonsdale.chatterbox.api.data.Post
import com.katielonsdale.chatterbox.api.data.PostsResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.katielonsdale.chatterbox.ui.components.PostCard
import androidx.compose.material3.Text
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.katielonsdale.chatterbox.api.data.CircleUiState
import androidx.compose.foundation.layout.Row
import com.katielonsdale.chatterbox.ui.components.BackButton

var circlePosts by mutableStateOf(emptyList<Post>())

@Composable
fun CircleScreen(
    circle: CircleUiState,
    onClickBack: () -> Unit = {},
    onClickDisplayPost: (Post) -> Unit = {}
) {
    var isLoading by remember { mutableStateOf(true) }
    val userId = SessionManager.getUserId()

    LaunchedEffect(Unit) {
        getPostsForCircle(circle.id, userId)
        isLoading = false
    }

    if (isLoading) {
        CircularProgressIndicator()
    } else {
        DisplayPosts(
            circle,
            circlePosts,
            onClickBack,
            onClickDisplayPost
        )
    }
}

@Composable
fun DisplayPosts(
    circle: CircleUiState,
    posts: List<Post>,
    onClickBack: () -> Unit,
    onClickDisplayPost: (Post) -> Unit
    ) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
            BackButton(onClickBack = onClickBack)

        Row(
            modifier = Modifier.fillMaxWidth()
                .padding(bottom = 16.dp),
            horizontalArrangement = Arrangement.Center
        ){
            Text(
                text = circle.name,
                color = Color.DarkGray,
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold,
            )
        }

        LazyColumn {
            val sortedPosts = posts.sortedByDescending { it.attributes.updatedAt }
            items(sortedPosts) { post ->
                PostCard(
                    post,
                    onClickDisplayPost,
                    false
                )
            }
        }
    }
}

private fun getPostsForCircle(circleId: String, userId: String?) {
    apiService.getPostsForCircle(userId, circleId).enqueue(object : Callback<PostsResponse> {
        override fun onResponse(call: Call<PostsResponse>, response: Response<PostsResponse>) {
            if (response.isSuccessful) {
                circlePosts = response.body()?.data ?: emptyList()
                for (post in circlePosts) {

                }
            } else {
                Log.e("CircleScreen", "Failed to fetch circle ${circleId} posts: ${response.errorBody()?.string()}")
            }
        }

        override fun onFailure(call: Call<PostsResponse>, t: Throwable) {
            Log.e("CircleScreen", "Error fetching posts", t)
        }
    })
}

@Preview(apiLevel = 34, showBackground = true)
@Composable
fun PreviewCircleScreen() {
    val circle = CircleUiState("1", "Sample Circle")
    val posts = SampleData.returnSamplePosts()
    MaterialTheme {
        Surface{
            DisplayPosts(
                circle,
                posts,
                onClickBack = {},
                onClickDisplayPost = {}
            )
        }
    }
}