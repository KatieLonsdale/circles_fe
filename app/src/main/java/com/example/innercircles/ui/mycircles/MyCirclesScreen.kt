package com.example.innercircles.ui.mycircles

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.unit.sp
import com.example.innercircles.SessionManager
import com.example.innercircles.api.RetrofitClient
import com.example.innercircles.api.data.Circle
import com.example.innercircles.api.data.CirclesResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import androidx.compose.material3.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import com.example.innercircles.api.data.Attribute

var circles by mutableStateOf(emptyList<Circle>())

@Composable
fun MyCirclesScreen(
    onCircleClick: (String) -> Unit = {},
) {
    var isLoading by remember { mutableStateOf(true) }
    val userId = SessionManager.getUserId()

    LaunchedEffect(Unit) {
        getCircles(userId)  // Await the result of getPosts
        isLoading = false    // Set loading to false after fetching the posts
    }

    if (isLoading) {
        // Show loading indicator while posts are being fetched
        CircularProgressIndicator()
    } else {
        MyCirclesList(circles, onCircleClick)
    }
}

@Composable
fun MyCirclesList(circles: List<Circle>, onCircleClick: (String) -> Unit) {
    LazyColumn {
        items(circles) { circle ->
            CircleCard(circle, onCircleClick)
        }
    }
}

@Composable
fun CircleCard(circle: Circle, onCircleClick: (String) -> Unit = {}) {
    val circleAttributes = circle.attributes
    val circleId = circle.id
    Row(
        modifier = Modifier.clickable(
            onClick = {
                onCircleClick(circleId)
        })
            .fillMaxWidth()
    ) {
        Text (
            text = circleAttributes.name,
            color = Color.DarkGray,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.align(Alignment.Bottom)
        )
        Spacer(modifier = Modifier.width(5.dp))

        Text(
            text = circleAttributes.description,
            color = Color.DarkGray,
            fontSize = 15.sp,
            modifier = Modifier.align(Alignment.Bottom)
        )

        Spacer(modifier = Modifier.height(40.dp))
    }
}

private fun getCircles(userId: String?) {
    RetrofitClient.apiService.getCircles(userId).enqueue(object : Callback<CirclesResponse> {
        override fun onResponse(call: Call<CirclesResponse>, response: Response<CirclesResponse>) {
            if (response.isSuccessful) {
                circles = response.body()?.data ?: emptyList()
            } else {
                Log.e("MainActivity", "Failed to fetch newsfeed: ${response.errorBody()?.string()}")
            }
        }

        override fun onFailure(call: Call<CirclesResponse>, t: Throwable) {
            Log.e("NewsfeedScreen", "Error fetching posts", t)
        }
    })
}

@Preview(showBackground = true)
@Composable
fun PreviewMyCirclesList() {
    val circle1 = Circle(
        id = "1",
        type = "circle",
        attributes = Attribute(
            id = 1,
            userId = 1,
            name = "College",
            description = "College Friends."
        )
    )
    val circle2 = Circle(
        id = "2",
        type = "circle",
        attributes = Attribute(
            id = 2,
            userId = 1,
            name = "High School",
            description = "High school friends."
        )
    )
    MaterialTheme {
        MyCirclesList(
            listOf(circle1, circle2),
            onCircleClick = {}
        )
    }

}
