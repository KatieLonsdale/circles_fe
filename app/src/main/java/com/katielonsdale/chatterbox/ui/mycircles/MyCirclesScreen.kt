package com.katielonsdale.chatterbox.ui.mycircles

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.katielonsdale.chatterbox.SessionManager
import com.katielonsdale.chatterbox.api.RetrofitClient
import com.katielonsdale.chatterbox.api.data.Circle
import com.katielonsdale.chatterbox.api.data.CirclesResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import androidx.compose.material3.*
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.ui.graphics.Color
import com.katielonsdale.chatterbox.api.data.CircleAttributes
import com.katielonsdale.chatterbox.theme.ChatterBoxTheme

@Composable
fun MyCirclesScreen(
    onCircleClick: (Circle) -> Unit = {},
) {
    var isLoading by remember { mutableStateOf(true) }
    val userId = SessionManager.getUserId()
    val userChatters = remember { mutableStateListOf<Circle>() }


    LaunchedEffect(Unit) {
        getCircles(
            userId = userId,
            userChatters = userChatters
        )  // Await the result of getPosts
        isLoading = false    // Set loading to false after fetching the posts
    }

    if (isLoading) {
        // Show loading indicator while posts are being fetched
        CircularProgressIndicator()
    } else {
        MyCirclesList(userChatters, onCircleClick)
        //todo: if no chatters, add button to create one
    }
}

@Composable
fun MyCirclesList(
    chatters: MutableList<Circle>,
    onCircleClick: (Circle) -> Unit
) {
    val sortedChatters = chatters.sortedBy { it.attributes.name }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        LazyColumn {
            items(sortedChatters) { chatter ->
                CircleCard(chatter, onCircleClick)
            }
        }
    }
}

@Composable
fun CircleCard(
    chatter: Circle,
    onCircleClick: (Circle) -> Unit = {}
) {
    val chatterAttributes = chatter.attributes
    Column(
        modifier = Modifier.clickable(
            //todo: when setting chatter, add its attributes as well
            onClick = {
                onCircleClick(chatter)
            })
            .fillMaxWidth()
    ) {
        Spacer(modifier = Modifier.height(15.dp))
        Text(
            text = chatterAttributes.name,
            color = MaterialTheme.colorScheme.primary,
            style = MaterialTheme.typography.titleSmall
        )
        Spacer(modifier = Modifier.height(5.dp))
        Text(
            text = chatterAttributes.description,
            color = MaterialTheme.colorScheme.primary,
            style = MaterialTheme.typography.bodySmall
        )
        Spacer(modifier = Modifier.height(15.dp))
    }
}

private fun getCircles(
    userId: String?,
    userChatters: MutableList<Circle>
) {
    RetrofitClient.apiService.getCircles(userId).enqueue(object : Callback<CirclesResponse> {
        override fun onResponse(call: Call<CirclesResponse>, response: Response<CirclesResponse>) {
            if (response.isSuccessful) {
                val chatters = response.body()?.data ?: emptyList()
                if (chatters.isNotEmpty()) { userChatters.addAll(chatters) }
            } else {
                Log.e("MyCirclesScreen", "Failed to fetch chatters: ${response.errorBody()?.string()}")
            }
        }

        override fun onFailure(call: Call<CirclesResponse>, t: Throwable) {
            Log.e("MyCirclesScreen", "Error fetching chatters ${t.message}", t)
        }
    })
}

@Preview(apiLevel = 34, showBackground = true)
@Composable
fun PreviewMyCirclesList() {
    val circle1 = Circle(
        id = "1",
        type = "circle",
        attributes = CircleAttributes(
            id = 1,
            userId = "1",
            name = "College",
            description = "College Friends."
        )
    )
    val circle2 = Circle(
        id = "2",
        type = "circle",
        attributes = CircleAttributes(
            id = 2,
            userId = "1",
            name = "High School",
            description = "High school friends. This description is super long. I need to make it look nicer."
        )
    )
    val circle3 = Circle(
        id = "3",
        type = "circle",
        attributes = CircleAttributes(
            id = 3,
            userId = "2",
            name = "House Bonanza",
            description = "Erie crew"
        )
    )
    val circle4 = Circle(
        id = "3",
        type = "circle",
        attributes = CircleAttributes(
            id = 3,
            userId = "2",
            name = "Basketball Team",
            description = "rec basketball team chat"
        )
    )
    ChatterBoxTheme {
        MyCirclesList(
            mutableStateListOf(circle1, circle2, circle3, circle4),
            onCircleClick = {}
        )
    }

}
