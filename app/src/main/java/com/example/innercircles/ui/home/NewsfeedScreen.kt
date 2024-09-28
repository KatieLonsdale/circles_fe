package com.example.innercircles.ui.home

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.innercircles.R
import com.example.innercircles.SampleData
import com.example.innercircles.SessionManager
import com.example.innercircles.api.RetrofitClient.apiService
import com.example.innercircles.api.data.Attribute
import com.example.innercircles.api.data.Circle
import com.example.innercircles.api.data.Post
import com.example.innercircles.api.data.PostAttributes
import com.example.innercircles.api.data.PostResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

var posts by mutableStateOf(emptyList<Post>())
@Composable
fun NewsfeedScreen() {
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
        Newsfeed(posts)
    }
}

@Composable
fun Newsfeed(posts: List<Post>) {
    LazyColumn {
        items(posts) { post ->
            PostCard(post)
        }
    }
}

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

//        TODO: change newsfeed return to include author display name
        Row{
            Text(
                text = "Author Name",
                color = Color.DarkGray,
                fontSize = textSize.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.width(10.dp))

            Text(
                text = post.attributes.caption,
                color = Color.DarkGray,
                fontSize = textSize.sp,
            )
        }

        Spacer(modifier = Modifier.height(20.dp))
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
            Newsfeed(posts)
        }
    }
}