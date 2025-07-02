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
import com.katielonsdale.chatterbox.api.data.Circle
import com.katielonsdale.chatterbox.api.data.NewPostUiState
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
import com.katielonsdale.chatterbox.theme.ChatterBoxTheme
import com.katielonsdale.chatterbox.api.data.source.ChatterDataSource.getChatters
import com.katielonsdale.chatterbox.api.data.source.PostDataSource.createPost


var circles by mutableStateOf(emptyList<Circle>())

@Composable
fun SelectCirclesScreen(
    newPostUiState: NewPostUiState = NewPostUiState(),
    onClickPost: () -> Unit = {},
){
    var isLoading by remember { mutableStateOf(true) }
    val userId = SessionManager.getUserId()

    LaunchedEffect(Unit) {
        getChatters(userId)
        isLoading = false
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
                            selectedCircleIds.add(circle.id)
                        } else {
                            selectedCircleIds.remove(circle.id)
                        }
                    }
                )
                Text(circle.attributes.name)
            }
        }

        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            ElevatedButton(
                onClick = {
                    Log.e("SelectCirclesScreen", "Button clicked")
                    // Call the ViewModel's method with the selectedCircleIds
                    createPost(newPostUiState)
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

@Preview(apiLevel = 34, showBackground = true)
@Composable
fun SelectCirclesPreview() {
    ChatterBoxTheme {
        SelectCircles()
    }
}
