package com.katielonsdale.chatterbox.ui

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
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
import com.katielonsdale.chatterbox.api.data.CircleUiState
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.ui.text.style.TextAlign
import com.katielonsdale.chatterbox.theme.ChatterBoxTheme

var chatterPosts by mutableStateOf(emptyList<Post>())

@Composable
fun ChatterScreen(
    chatter: CircleUiState,
    onClickDisplayPost: (Post) -> Unit = {}
) {
    var isLoading by remember { mutableStateOf(true) }
    val userId = SessionManager.getUserId()

    LaunchedEffect(Unit) {
        getPostsForChatter(chatter.id, userId)
        isLoading = false
    }

    if (isLoading) {
        CircularProgressIndicator()
    } else {
        DisplayPosts(
            chatter,
            chatterPosts,
            onClickDisplayPost
        )
    }
}

@Composable
fun DisplayPosts(
    chatter: CircleUiState,
    posts: List<Post>,
    onClickDisplayPost: (Post) -> Unit
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background),
        ) {
            item{ ChatterScreenHeader(chatter) }
            val sortedPosts = posts.sortedByDescending { it.attributes.updatedAt }
            items(sortedPosts) { post ->
                PostCard(
                    post,
                    onClickDisplayPost,
                    false
                )
                Spacer(modifier = Modifier.height(20.dp))
            }
        }
}

@Composable
fun ChatterScreenHeader(
    chatter: CircleUiState,
){
    Text(
        text = chatter.name,
        color = MaterialTheme.colorScheme.primary,
        style = MaterialTheme.typography.titleSmall,
        softWrap = true,
        textAlign = TextAlign.Center,
        maxLines = 2,
        modifier = Modifier
            .fillMaxWidth()
    )

    Spacer(Modifier.height(10.dp))

//        Row(
//            modifier = Modifier
//                .fillMaxWidth(),
//            verticalAlignment = Alignment.CenterVertically,
//            horizontalArrangement = Arrangement.Center,
//        ){
//            Icon(
//                painter = painterResource(R.drawable.chat),
//                contentDescription = "chat",
//                modifier = Modifier.height(80.dp)
//            )
//            Spacer(Modifier.width(70.dp))
//
//            Icon(
//                painter = painterResource(R.drawable.album),
//                contentDescription = "chat",
//                modifier = Modifier.height(80.dp)
//            )
//            Spacer(Modifier.width(70.dp))
//
//            Icon(
//                painter = painterResource(R.drawable.calendar),
//                contentDescription = "chat",
//                modifier = Modifier.height(80.dp)
//            )
//        }
//
//        Spacer(Modifier.height(10.dp))
}

private fun getPostsForChatter(
    chatterId: String,
    userId: String?,
) {
    apiService.getPostsForChatter(userId, chatterId).enqueue(object : Callback<PostsResponse> {
        override fun onResponse(call: Call<PostsResponse>, response: Response<PostsResponse>) {
            if (response.isSuccessful) {
                chatterPosts = response.body()?.data ?: emptyList()
                for (post in chatterPosts) {

                }
            } else {
                Log.e("CircleScreen", "Failed to fetch circle ${chatterId} posts: ${response.errorBody()?.string()}")
            }
        }

        override fun onFailure(call: Call<PostsResponse>, t: Throwable) {
            Log.e("CircleScreen", "Error fetching posts", t)
        }
    })
}

@Preview(apiLevel = 34, showBackground = true)
@Composable
fun PreviewChatterScreen() {
    val chatter = CircleUiState("1", "Tuesday Night Run Club")
    val posts = SampleData.returnSamplePosts()
    ChatterBoxTheme {
        Surface{
            DisplayPosts(
                chatter,
                posts,
                onClickDisplayPost = {}
            )
        }
    }
}