package com.example.innercircles.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.example.innercircles.R
import com.example.innercircles.SessionManager
import com.example.innercircles.api.RetrofitClient.apiService
import com.example.innercircles.api.data.SignInResponse
import com.example.innercircles.api.data.SignUpRequest
import com.example.innercircles.api.data.SignUpResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


@Composable
fun SignUpScreen(
    onClickSignUp: () -> Unit,
    onClickBack: () -> Unit,
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var displayName by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 10.dp, start = 2.dp)
    ){
        // Box for back arrow
        IconButton(
            onClick = {
                onClickBack()
            },
            colors = IconButtonDefaults.iconButtonColors(
                containerColor = Color.White
            ),
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_back), // Use your back arrow drawable
                contentDescription = "Back",
                modifier = Modifier.align(Alignment.Start),
            )
        }
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TextField(
            value = displayName,
            onValueChange = { displayName = it },
            label = { Text("Display Name") }
        )

        TextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Email)
        )

        TextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            visualTransformation = PasswordVisualTransformation()
        )

        TextField(
            value = confirmPassword,
            onValueChange = { confirmPassword = it },
            label = { Text("Confirm Password") },
            visualTransformation = PasswordVisualTransformation()
        )

        Spacer(modifier = Modifier.height(16.dp))

        errorMessage?.let {
            Text(text = it, color = Color.Red)
        }

        Button(
            onClick = {
                if (displayName.isNotBlank() && email.isNotBlank() && password.isNotBlank() && confirmPassword.isNotBlank()) {
                    if (password == confirmPassword) {
                        isLoading = true
                        signUpUser(
                            email,
                            password,
                            confirmPassword,
                            displayName) { isSuccess, error ->
                            isLoading = false
                            if (isSuccess) {
                                onClickSignUp()
                            } else {
                                errorMessage = error ?: "Sign up failed"
                            }
                        }
                    } else {
                        errorMessage = "Passwords do not match"
                    }
                } else {
                    errorMessage = "Please fill out all fields"
                }
            },
            enabled = !isLoading
        ) {
            Text("Sign Up")
        }
    }
}

fun signUpUser(
    email: String,
    password: String,
    confirmPassword: String,
    displayName: String,
    onResult: (Boolean, String?) -> Unit
){
    val signUpRequest = SignUpRequest(
        email,
        displayName,
        password,
        confirmPassword,
    )
    apiService.signUpUser(signUpRequest).enqueue(object : Callback<SignUpResponse> {
        override fun onResponse(call: Call<SignUpResponse>, response: Response<SignUpResponse>) {
            if (response.isSuccessful) {
                // HTTP 200: Success
                val userId = response.body()?.data?.id
                if (userId != null) {
                    SessionManager.saveUserId(userId)
                    onResult(true, null)
                } else {
                    onResult(false, response.message())
                }
            } else if (response.code() == 404) {
                onResult(false, response.message())
            } else {
                onResult(false, response.message())
            }
        }

        override fun onFailure(call: Call<SignUpResponse>, t: Throwable) {
            // Network or other failure
            onResult(false, t.message)
        }
    })
}

@Preview(showBackground = true)
@Composable
fun SignUpScreenPreview(){
    SignUpScreen(
        onClickSignUp = {},
        onClickBack = {},
    )
}