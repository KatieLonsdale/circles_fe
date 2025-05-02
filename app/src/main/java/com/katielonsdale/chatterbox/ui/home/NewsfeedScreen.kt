package com.katielonsdale.chatterbox.ui.home

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.katielonsdale.chatterbox.SampleData
import com.katielonsdale.chatterbox.SessionManager
import com.katielonsdale.chatterbox.api.RetrofitClient.apiService
import com.katielonsdale.chatterbox.api.data.Comment
import com.katielonsdale.chatterbox.api.data.Post
import com.katielonsdale.chatterbox.api.data.PostResponse
import com.katielonsdale.chatterbox.ui.components.PostCard
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import sh.calvin.autolinktext.rememberAutoLinkText

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
    if (posts.isEmpty()) {
        Text(
            text = "No posts available.",
            modifier = Modifier.padding(16.dp)
        )
    } else {
        val sortedPosts = posts.sortedByDescending { it.attributes.updatedAt }
        LazyColumn {
            items(sortedPosts) { post ->
                PostCard(
                    post,
                    onClickDisplayPost,
                    true
                )
            }
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
            text = AnnotatedString.rememberAutoLinkText(comment.attributes.authorDisplayName),
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