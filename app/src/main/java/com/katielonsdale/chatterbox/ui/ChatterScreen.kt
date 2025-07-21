package com.katielonsdale.chatterbox.ui

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
import com.katielonsdale.chatterbox.api.data.Post
import com.katielonsdale.chatterbox.ui.components.PostCard
import androidx.compose.material3.Text
import com.katielonsdale.chatterbox.api.data.CircleUiState
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.PullToRefreshDefaults.Indicator
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.style.TextAlign
import com.katielonsdale.chatterbox.theme.ChatterBoxTheme
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatterScreen(
    chatter: CircleUiState,
    onClickDisplayPost: (Post) -> Unit = {},
    onChatterEvent: (CircleViewModel.MyEvent) -> Unit,
) {
    var isLoading by remember { mutableStateOf(true) }
    val userId = SessionManager.getUserId()

    LaunchedEffect(Unit) {
        onChatterEvent(CircleViewModel.MyEvent.GetChatterPosts(chatter.id, userId))
        isLoading = false
    }

    var isRefreshing = remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    val state = rememberPullToRefreshState()

    PullToRefreshBox(
        modifier = Modifier.fillMaxSize(),
        state = state,
        isRefreshing = isRefreshing.value,
        onRefresh = {
            scope.launch {
                isRefreshing.value = true
                onChatterEvent(CircleViewModel.MyEvent.UpdateChatterPosts(
                    chatterId = chatter.id,
                    userId = userId,
                    isRefreshing = isRefreshing
                ))
            }
        },
        indicator = {
            Indicator(
                isRefreshing = isRefreshing.value,
                state = state,
                modifier = Modifier.align(
                    Alignment.TopCenter
                ),
                containerColor = MaterialTheme.colorScheme.background,
                color = MaterialTheme.colorScheme.primary,
            )
        }
    ) {
        if (isLoading) {
            CircularProgressIndicator()
        } else {
            DisplayPosts(
                chatter,
                chatter.posts,
                onClickDisplayPost
            )
        }
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