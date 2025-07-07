package com.katielonsdale.chatterbox.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.katielonsdale.chatterbox.R
import com.katielonsdale.chatterbox.api.RetrofitClient.apiService
import com.katielonsdale.chatterbox.api.data.SignUpFields
import com.katielonsdale.chatterbox.api.data.SignUpRequest
import com.katielonsdale.chatterbox.api.data.SignUpResponse
import com.katielonsdale.chatterbox.theme.ChatterBoxTheme
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.katielonsdale.chatterbox.ui.components.TextFieldOnSurface

@Composable
fun SignUpScreen(
    onClickSignUp: () -> Unit,
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var displayName by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier
            .background(
                MaterialTheme.colorScheme.background
            )
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
    ) {
        Surface(
            shape = MaterialTheme.shapes.small,
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        MaterialTheme.colorScheme.secondary.copy(
                            alpha = (0.5F)
                        )
                    ),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
            ) {
                Image(
                    painterResource(
                        id = R.drawable.cb_logo_dark
                    ),
                    contentDescription = "ChatterBox Logo",
                    modifier = Modifier
                        .height(125.dp),
                )

                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    text = "Sign up for ChatterBox",
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.bodyMedium,
                )

                Spacer(modifier = Modifier.height(20.dp))

                errorMessage?.let {
                    Text(
                        text = it,
                        color = MaterialTheme.colorScheme.onErrorContainer,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(
                            start = 20.dp,
                            end = 20.dp
                        )
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                }

                TextFieldOnSurface(
                    value = displayName,
                    onValueChange = { displayName = it },
                    label = "Display Name",
                    modifier =  Modifier.width(280.dp),
                )

                Spacer(modifier = Modifier.height(16.dp))

                TextFieldOnSurface(
                    value = email,
                    onValueChange = { email = it },
                    label = "Email",
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Email),
                    modifier =  Modifier.width(280.dp),
                )

                Spacer(modifier = Modifier.height(16.dp))

                TextFieldOnSurface(
                    value = password,
                    onValueChange = { password = it },
                    label = "Password",
                    hidden = true,
                    modifier =  Modifier.width(280.dp),
                )

                Spacer(modifier = Modifier.height(16.dp))

                TextFieldOnSurface(
                    value = confirmPassword,
                    onValueChange = { confirmPassword = it },
                    label = "Confirm Password",
                    hidden = true,
                    modifier =  Modifier.width(280.dp),
                )

                Spacer(modifier = Modifier.height(30.dp))

                Button(
                    onClick = {
                        if (displayName.isNotBlank() && email.isNotBlank() && password.isNotBlank() && confirmPassword.isNotBlank()) {
                            if (password.trim() == confirmPassword.trim()) {
                                isLoading = true
                                signUpUser(
                                    email.trim(),
                                    password.trim(),
                                    confirmPassword.trim(),
                                    displayName.trim()
                                ) { isSuccess, error ->
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
                    Text(
                        text = "Sign Up",
                        style = MaterialTheme.typography.labelSmall
                    )
                }
            }
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
    val signUpRequest = SignUpFields(
        email,
        displayName,
        password,
        confirmPassword,
    )
    apiService.signUpUser(SignUpRequest(signUpRequest)).enqueue(object : Callback<SignUpResponse> {
        override fun onResponse(call: Call<SignUpResponse>, response: Response<SignUpResponse>) {
            if (response.isSuccessful) {
                onResult(true, null)
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

@Preview(apiLevel = 34, showBackground = true)
@Composable
fun SignUpScreenPreview(){
    ChatterBoxTheme {
        SignUpScreen(
            onClickSignUp = {},
        )
    }
}