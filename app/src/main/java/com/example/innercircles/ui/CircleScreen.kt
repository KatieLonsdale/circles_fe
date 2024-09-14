package com.example.innercircles.ui

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.innercircles.R
import com.example.innercircles.SampleData
import com.example.innercircles.SessionManager
import com.example.innercircles.api.RetrofitClient.apiService
import com.example.innercircles.api.data.Post
import com.example.innercircles.api.data.PostResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

var circlePosts by mutableStateOf(emptyList<Post>())

//WIP: add back button to navigate back to circles screen

@Composable
fun CircleScreen(
    circleId: String,
) {
    var isLoading by remember { mutableStateOf(true) }
    val userId = SessionManager.getUserId()

    LaunchedEffect(Unit) {
        getPostsForCircle(circleId, userId)
        isLoading = false
    }

    if (isLoading) {
        CircularProgressIndicator()
    } else {
        DisplayPosts(circlePosts)
    }
}

@Composable
fun DisplayPosts(posts: List<Post>) {
    LazyColumn {
        items(posts) { post ->
            PostCard(post)
        }
    }
}

//change post card to reusable component
@Composable
fun PostCard(post: Post) {
    val medias = post.attributes.contents.data
    var hasMedia = true;
    if (medias.isEmpty()) { hasMedia = false }
    Column {
        for (media in medias) {
            val imageUrl = media.attributes.imageUrl
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(imageUrl)
                    .crossfade(true)
                    .build(),
                error = painterResource(R.drawable.ic_image_error_24dp),
                contentDescription = stringResource(R.string.description),
                contentScale = ContentScale.Fit
            )
        }

        Spacer(modifier = Modifier.height(5.dp))
        val textSize: Int = if(!hasMedia) {
            25
        } else {
            15
        }

        Text(
            text = post.attributes.caption,
            color = Color.DarkGray,
            fontSize = textSize.sp,
        )

        Spacer(modifier = Modifier.height(20.dp))
    }
}

private fun getPostsForCircle(circleId: String, userId: String?) {
    apiService.getPostsForCircle(userId, circleId).enqueue(object : Callback<PostResponse> {
        override fun onResponse(call: Call<PostResponse>, response: Response<PostResponse>) {
            if (response.isSuccessful) {
                circlePosts = response.body()?.data ?: emptyList()
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
    MaterialTheme {
        Surface {
            val posts = SampleData.returnSamplePosts()
            DisplayPosts(posts)
        }
    }
}