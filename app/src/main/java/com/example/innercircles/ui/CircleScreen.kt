package com.example.innercircles.ui

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.innercircles.R
import com.example.innercircles.SampleData
import com.example.innercircles.SessionManager
import com.example.innercircles.api.RetrofitClient.apiService
import com.example.innercircles.api.data.Post
import com.example.innercircles.api.data.PostResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.example.innercircles.ui.components.PostCard
import androidx.compose.material3.Text
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.innercircles.api.data.CircleUiState
import androidx.compose.foundation.layout.Row

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
            .fillMaxHeight()
            .padding(16.dp)
    ) {
        Box(
            modifier = Modifier.fillMaxWidth()
        ) {
            IconButton(
                onClick = {
                    onClickBack()
                },
                colors = IconButtonDefaults.iconButtonColors(
                    containerColor = Color.White
                ),
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_back), // Use your back arrow drawable
                    contentDescription = "Back",
                    modifier = Modifier.align(Alignment.TopStart),

                    )
            }
        }

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
            items(posts) { post ->
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
    apiService.getPostsForCircle(userId, circleId).enqueue(object : Callback<PostResponse> {
        override fun onResponse(call: Call<PostResponse>, response: Response<PostResponse>) {
            if (response.isSuccessful) {
                circlePosts = response.body()?.data ?: emptyList()
                for (post in circlePosts) {

                }
            } else {
                Log.e("CircleScreen", "Failed to fetch circle ${circleId} posts: ${response.errorBody()?.string()}")
            }
        }

        override fun onFailure(call: Call<PostResponse>, t: Throwable) {
            Log.e("CircleScreen", "Error fetching posts", t)
        }
    })
}

@Preview(showBackground = true)
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