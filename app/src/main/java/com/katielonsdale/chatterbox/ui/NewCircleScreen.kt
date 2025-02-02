package com.katielonsdale.chatterbox.ui

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.katielonsdale.chatterbox.R
import com.katielonsdale.chatterbox.SessionManager
import com.katielonsdale.chatterbox.api.RetrofitClient
import com.katielonsdale.chatterbox.api.data.Circle
import com.katielonsdale.chatterbox.api.data.NewCircleRequest
import com.katielonsdale.chatterbox.api.data.NewPostResponse
import com.katielonsdale.chatterbox.ui.components.BackButton
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@Composable
fun NewCircleScreen(
    onClickBack: () -> Unit,
    onClickCreate: () -> Unit
){
    var circleName by remember { mutableStateOf("Circle Name") }
    var circleDescription by remember { mutableStateOf("Circle Description") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ){
        BackButton(
            onClickBack = onClickBack
        )
        Text(
            text = stringResource(id = R.string.create_circle_title),
            fontSize = MaterialTheme.typography.titleLarge.fontSize,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        TextField(
            value = circleName,
            onValueChange = { circleName = it },
            label = { Text(
                stringResource(id = R.string.new_circle_name_label)
            ) }
        )

        Spacer(modifier = Modifier.height(16.dp))

        TextField(
            value = circleDescription,
            onValueChange = { circleDescription = it },
            label = { Text(
                stringResource(id = R.string.new_circle_description_label)
            ) }
        )
    }
    Box(
        modifier = Modifier
            .fillMaxSize() // Make the Box fill the entire screen
    ) {
        ElevatedButton(
            onClick = {
                createCircle(
                    circleName = circleName,
                    circleDescription = circleDescription
                )
                onClickCreate()
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.LightGray, // Background color
                contentColor = Color.DarkGray,  // Text color
            ),
            modifier = Modifier
                .align(Alignment.BottomEnd) // Align to the bottom-right
                .padding(16.dp) // Padding to prevent the button from touching the screen edge
        ) {
            Text("Create")
        }
    }
}

private fun createCircle(
    circleName: String = "Circle Name",
    circleDescription: String = "Circle Description",
) {
    val userId = SessionManager.getUserId()
    val newCircleRequest = NewCircleRequest(
        name = circleName,
        description = circleDescription,
    )
    RetrofitClient.apiService.createCircle(userId, newCircleRequest).enqueue(object :
        Callback<Circle> {
        override fun onResponse(
            call: Call<Circle>,
            response: Response<Circle>
        ) {
            if (response.isSuccessful) {
                Log.e(
                    "NewCircleScreen",
                    "Circle created successfully: ${response.body()}"
                )
            } else {
                Log.e(
                    "NewCircleScreen",
                    "Failed to createCircle: ${response.body()} status: ${response.code()}"
                )
            }
        }

        override fun onFailure(call: Call<Circle>, t: Throwable) {
            Log.e("NewCircleScreen", "Error creating circle", t)
        }
    })
}

@Preview(showBackground = true)
@Composable
fun NewCircleScreenPreview() {
    NewCircleScreen(
        onClickBack = {},
        onClickCreate = {}
    )
}