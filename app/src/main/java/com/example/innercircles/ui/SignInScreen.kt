package com.example.innercircles.ui

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.Button
import androidx.compose.ui.graphics.Color
import com.example.innercircles.api.RetrofitClient.apiService
import com.example.innercircles.api.data.SignInRequest
import com.example.innercircles.api.data.SignInResponse
import androidx.compose.ui.tooling.preview.Preview
import com.example.innercircles.SessionManager

@Composable
fun SignInScreen(
    onClickSignIn: () -> Unit,
    onClickSignUp: () -> Unit = {},
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        TextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth(),
        )
        Spacer(modifier = Modifier.height(16.dp))
        TextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = {
                errorMessage = ""
                loginUser(email, password) { isSuccess, error ->
                    if (isSuccess) {
                        onClickSignIn()
                    } else {
                        errorMessage = error ?: "An unknown error occurred"
                    }
                }
            }
        ) {
            Text("Sign In")
        }

        if (errorMessage.isNotEmpty()) {
            Spacer(modifier = Modifier.height(16.dp))
            Text(errorMessage, color = Color.Red)
        }

        Spacer(modifier = Modifier.height(50.dp))

        Text("Don't have an account?", modifier = Modifier)

        Spacer(modifier = Modifier.height(10.dp))


        Button(
            onClick = {
                onClickSignUp()
            }
        ) {
            Text("Sign Up")
        }
    }
}


private fun loginUser(username: String, password: String, onResult: (Boolean, String?) -> Unit) {
    val signInRequest = SignInRequest(username, password)

    apiService.authenticateUser(signInRequest).enqueue(object : Callback<SignInResponse> {
        override fun onResponse(call: Call<SignInResponse>, response: Response<SignInResponse>) {
            if (response.isSuccessful) {
                // HTTP 200: Success
                val userId = response.body()?.user?.data?.id
                val authToken = response.body()?.token
                if (userId != null && authToken != null) {
                    SessionManager.saveUserId(userId)
                    SessionManager.saveToken(authToken)
                    onResult(true, null)
                } else {
                    val errorMessage = response.message()
                    onResult(false, errorMessage)
                }
            } else if (response.code() == 404) {
                val errorMessage = response.message()
                onResult(false, errorMessage)
            } else {
                // Handle other error codes as needed
                val errorMessage = response.message()
                onResult(false, errorMessage)
            }
        }

        override fun onFailure(call: Call<SignInResponse>, t: Throwable) {
            // Network or other failure
            onResult(false, t.message)
        }
    })
}

@Preview(showBackground = true)
@Composable
fun PreviewLoginScreen() {
    SignInScreen(
        onClickSignIn = {},
        onClickSignUp = {},
    )
}

//todo: error handling for sign in
