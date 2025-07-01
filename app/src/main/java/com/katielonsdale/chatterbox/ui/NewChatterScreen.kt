package com.katielonsdale.chatterbox.ui

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.katielonsdale.chatterbox.R
import com.katielonsdale.chatterbox.SessionManager
import com.katielonsdale.chatterbox.api.RetrofitClient
import com.katielonsdale.chatterbox.api.data.NewCircleAttributes
import com.katielonsdale.chatterbox.api.data.NewCircleRequest
import com.katielonsdale.chatterbox.api.data.NewCircleResponse
import com.katielonsdale.chatterbox.theme.ChatterBoxTheme
import com.katielonsdale.chatterbox.ui.components.NewOptionIcon
import com.katielonsdale.chatterbox.ui.components.TextFieldOnSurface
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.katielonsdale.chatterbox.ui.components.SelectFriends

val TAG = "NewChatterScreen"
@Composable
fun NewChatterScreen(
    onClickCreate: () -> Unit
){
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current
    var chatterName by remember { mutableStateOf("") }
    var chatterDescription by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .background(MaterialTheme.colorScheme.background)
            .pointerInput(Unit) {
                detectTapGestures(onTap = {
                    // Remove focus and dismiss keyboard when tapping outside the TextField
                    focusManager.clearFocus()
                    keyboardController?.hide()
                })
            },
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ){
        Surface(
            shape = MaterialTheme.shapes.small,
            modifier = Modifier.padding(
                10.dp
            )
                .fillMaxSize()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        MaterialTheme.colorScheme.secondary.copy(
                            alpha = (0.5F)
                        )
                    )
                    .padding(
                        top = 20.dp,
                        bottom = 20.dp,
                        start = 10.dp,
                        end = 10.dp,
                    ),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
            ) {
                Row(
                    modifier = Modifier
                        .padding(10.dp)
                ) {
                    NewOptionIcon(
                        icon = R.drawable.new_chatter,
                        label = "Chatter"
                    )
                }
                Row(
                    modifier = Modifier
                        .padding(10.dp)
                        .fillMaxWidth()
                ) {
                    TextFieldOnSurface(
                        value = chatterName,
                        onValueChange = { value ->
                            chatterName = value
                        },
                        label = "Chatter Name",
                        maxLines = 2,
                    )
                }
                Row(
                    modifier = Modifier
                        .padding(10.dp)
                        .fillMaxWidth()
                ) {
                    TextFieldOnSurface(
                        value = chatterDescription,
                        onValueChange = { value ->
                            chatterDescription = value
                        },
                        label = "Description of your chatter",
                        maxLines = 5,
                    )
                }

                Row(
                    modifier = Modifier
                        .padding(
                            start = 10.dp,
                            end = 10.dp,
                        )
                ){
//                    if (userFriends.isNotEmpty()) {
//                        SelectFriends(
//                            friends = userFriends,
//                            selectedChatterIds = selectedFriendIds,
//                        )
//                    } else {
                        Text(
                            text = "Please add friends before creating a Chatter.",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onErrorContainer,
                        )
//                    }
                }

                Spacer(Modifier.height(20.dp))
                Row(
                    verticalAlignment = Alignment.Bottom,
                    horizontalArrangement = Arrangement.End,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Button(
                        onClick = {
                            createChatter(
                                chatterName = chatterName,
                                chatterDescription = chatterDescription
                            )
                            onClickCreate()
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(
                                alpha = 0.5F
                            ),
                            contentColor = MaterialTheme.colorScheme.primary
                        ),
                    ) {
                        Text(
                            text = "Create",
                            color = MaterialTheme.colorScheme.primary,
                            style = MaterialTheme.typography.bodySmall,
                        )
                    }
                }
            }
        }
    }
}

private fun createChatter(
    chatterName: String,
    chatterDescription: String,
) {
    val userId = SessionManager.getUserId()
    val newChatterRequest = NewCircleRequest(
        circle = NewCircleAttributes(
            userId = userId,
            name = chatterName,
            description = chatterDescription,
        )
    )
    RetrofitClient.apiService.createCircle(userId, newChatterRequest).enqueue(object :
        Callback<NewCircleResponse> {
        override fun onResponse(
            call: Call<NewCircleResponse>,
            response: Response<NewCircleResponse>
        ) {
            if (response.isSuccessful) {
                Log.e(TAG, "Chatter created successfully: ${response.body()}"
                )
            } else {
                Log.e(TAG, "Failed to create chatter: ${response.body()} status: ${response.code()}"
                )
            }
        }

        override fun onFailure(call: Call<NewCircleResponse>, t: Throwable) {
            Log.e(TAG, "Error creating chatter", t)
        }
    })
}

@Preview(apiLevel = 34, showBackground = true)
@Composable
fun NewChatterScreenPreview() {
    ChatterBoxTheme {
        NewChatterScreen(
            onClickCreate = {}
        )
    }
}