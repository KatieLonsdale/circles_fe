package com.example.innercircles.ui.home

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.innercircles.SampleData
import com.example.innercircles.SessionManager
import com.example.innercircles.api.RetrofitClient.apiService
import com.example.innercircles.api.data.Comment
import com.example.innercircles.api.data.Post
import com.example.innercircles.api.data.PostResponse
import com.example.innercircles.ui.components.PostCard
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

var posts by mutableStateOf(emptyList<Post>())
@Composable
fun NewsfeedScreen(
    onClickDisplayPost: (Post) -> Unit = {},
) {
    var isLoading by remember { mutableStateOf(true) }
    val userId = SessionManager.getUserId()

    LaunchedEffect(Unit) {
        getPosts(userId)  // Await the result of getPosts
        isLoading = false    // Set loading to false after fetching the posts
    }

    if (isLoading) {
        // Show loading indicator while posts are being fetched
        CircularProgressIndicator()
    } else {
        Newsfeed(posts, onClickDisplayPost)
    }
}

@Composable
fun Newsfeed(
    posts: List<Post>,
    onClickDisplayPost: (Post) -> Unit,
) {
//    todo: sort posts by updatedAt
    LazyColumn {
        items(posts) { post ->
            PostCard(
                post,
                onClickDisplayPost,
                true
            )
        }
    }
}

@Composable
fun CommentCard(comment: Comment) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
    ){
        Text(
            text = comment.attributes.authorDisplayName,
            color = Color.DarkGray,
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.width(10.dp))

        Text(
            text = comment.attributes.commentText,
            color = Color.DarkGray,
            fontSize = 15.sp,
        )
    }
}

private fun getPosts(userId: String?) {
    apiService.getNewsfeed(userId).enqueue(object : Callback<PostResponse> {
        override fun onResponse(call: Call<PostResponse>, response: Response<PostResponse>) {
            if (response.isSuccessful) {
                posts = response.body()?.data ?: emptyList()
            } else {
                Log.e("NewsfeedScreen", "Failed to fetch newsfeed: ${response.errorBody()?.string()}")
            }
        }

        override fun onFailure(call: Call<PostResponse>, t: Throwable) {
            Log.e("NewsfeedScreen", "Error fetching posts", t)
        }
    })
}

@Preview(showBackground = true)
@Composable
fun PreviewNewsfeed() {
    MaterialTheme {
        Surface {
            val posts = SampleData.returnSamplePosts()
            Newsfeed(
                posts,
                onClickDisplayPost = {}
            )
        }
    }
}