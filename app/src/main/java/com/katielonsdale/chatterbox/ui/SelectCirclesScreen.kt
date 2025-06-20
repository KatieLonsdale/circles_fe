package com.katielonsdale.chatterbox.ui

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.tooling.preview.Preview
import com.katielonsdale.chatterbox.SessionManager
import com.katielonsdale.chatterbox.api.RetrofitClient
import com.katielonsdale.chatterbox.api.data.Circle
import com.katielonsdale.chatterbox.api.data.CirclesResponse
import com.katielonsdale.chatterbox.api.data.NewPostUiState
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.material3.*
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import com.katielonsdale.chatterbox.api.data.PostRequest
import com.katielonsdale.chatterbox.api.data.PostRequestContent
import com.katielonsdale.chatterbox.api.data.NewPostResponse

var circles by mutableStateOf(emptyList<Circle>())

@Composable
fun SelectCirclesScreen(
    newPostUiState: NewPostUiState = NewPostUiState(),
    onClickPost: () -> Unit = {},
){
    var isLoading by remember { mutableStateOf(true) }
    val userId = SessionManager.getUserId()

    LaunchedEffect(Unit) {
        getCircles(userId)  // Await the result of getPosts
        isLoading = false    // Set loading to false after fetching the posts
    }

    if (isLoading) {
        CircularProgressIndicator()
    } else {
        SelectCircles(newPostUiState, onClickPost)
    }
}

@Composable
fun SelectCircles(
    newPostUiState: NewPostUiState = NewPostUiState(),
    onClickPost: () -> Unit = {},
){
    var showError by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        val selectedCircleIds = remember { mutableStateListOf<String>() }

        Text(
            text = "Select Chatters",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(
                start = 20.dp,
                bottom = 16.dp
            )
        )
        circles.forEach { circle ->
            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(
                    checked = selectedCircleIds.contains(circle.id),
                    onCheckedChange = { isChecked ->
                        if (isChecked) {
                            // Add the circle.id to the list if checked
                            selectedCircleIds.add(circle.id)
                        } else {
                            // Remove the circle.id if unchecked
                            selectedCircleIds.remove(circle.id)
                        }
                    }
                )
                Text(circle.attributes.name) // Show the circle's name as label for the checkbox
            }
        }

        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            ElevatedButton(
                onClick = {
                    Log.e("SelectCirclesScreen", "Button clicked")
                    // Call the ViewModel's method with the selectedCircleIds
                    createPost(selectedCircleIds.toList(),newPostUiState)
                    onClickPost()
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.LightGray, // Background color
                    contentColor = Color.DarkGray,  // Text color
                ),
                modifier = Modifier.padding(top = 16.dp)
                    .align(Alignment.BottomEnd)
            ) {
                Text("Post")
            }
        }
        if (showError) {
            Text(
                text = "An error occurred. Please try again.",
                color = Color.Red
            )
        }
    }
}

private fun getCircles(userId: String?) {
    RetrofitClient.apiService.getCircles(userId).enqueue(object : Callback<CirclesResponse> {
        override fun onResponse(call: Call<CirclesResponse>, response: Response<CirclesResponse>) {
            if (response.isSuccessful) {
                circles = response.body()?.data ?: emptyList()
            } else {
                Log.e("SelectCirclesScreen", "Failed to fetch newsfeed: ${response.errorBody()?.string()}")
            }
        }

        override fun onFailure(call: Call<CirclesResponse>, t: Throwable) {
            Log.e("SelectCirclesScreen", "Error fetching posts", t)
        }
    })
}

private fun createPost(
    circleIds: List<String>,
    newPostUiState: NewPostUiState
) : Boolean {
    val userId = SessionManager.getUserId()
    val postRequest = createPostRequest(newPostUiState)
    var success = false

    for (circleId in circleIds) {
        RetrofitClient.apiService.createPost(userId, circleId, postRequest).enqueue(object : Callback<NewPostResponse> {
            override fun onResponse(
                call: Call<NewPostResponse>,
                response: Response<NewPostResponse>
            ) {
                if (response.isSuccessful) {
                    success = true
                    Log.e(
                        "SelectCirclesScreen",
                        "Post created successfully: ${response.body()}"
                    )
                } else {
                    Log.e(
                        "SelectCirclesScreen",
                        "Failed to createPost: ${response.body()} status: ${response.code()}"
                    )
                }
            }

            override fun onFailure(call: Call<NewPostResponse>, t: Throwable) {
                Log.e("SelectCirclesScreen", "Error creating posts", t)
            }
        })
    }
    return success
}

private fun createPostRequest(newPostUiState: NewPostUiState): PostRequest {
    val caption = newPostUiState.caption
    val image = newPostUiState.contents?.getImage()
//    TODO: make compatible with videos
    if (image != null) {
        val postRequestContent = PostRequestContent(image, null)
        val postRequest = PostRequest(caption,postRequestContent)
        return postRequest
    } else {
       return PostRequest(caption,null)
    }
}

//@Preview(apiLevel = 34, showBackground = true)
//@Composable
//fun SelectCirclesScreenPreview(){
//    MaterialTheme {
//        SelectCirclesScreen()
//    }
//}

@Preview(apiLevel = 34, showBackground = true)
@Composable
fun SelectCirclesPreview() {
    MaterialTheme {
        SelectCircles()
    }
}