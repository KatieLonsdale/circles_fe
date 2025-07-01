package com.katielonsdale.chatterbox.ui

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.katielonsdale.chatterbox.R
import com.katielonsdale.chatterbox.SessionManager
import com.katielonsdale.chatterbox.api.RetrofitClient.apiService
import com.katielonsdale.chatterbox.api.data.SignInRequest
import com.katielonsdale.chatterbox.api.data.SignInResponse
import com.katielonsdale.chatterbox.api.data.UserAttributes
import com.katielonsdale.chatterbox.theme.ChatterBoxTheme
import com.katielonsdale.chatterbox.ui.components.TextFieldOnSurface
import com.katielonsdale.chatterbox.utils.TouAcceptanceValidator
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


@Composable
fun SignInScreen(
    updateUser: (UserAttributes) -> Unit,
    onClickSignIn: () -> Unit,
    onTouOutdated: () -> Unit,
    onClickSignUp: () -> Unit,
    signedUp: Boolean = false,
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }

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
            modifier = Modifier
                .padding(10.dp)
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
                        .height(175.dp)
                )

                Spacer(modifier = Modifier.height(20.dp))

                //successful sign up message
                if (signedUp) {
                    Box(
                        modifier = Modifier
                            .background(
                                color = MaterialTheme.colorScheme.surfaceVariant,
                                shape = RoundedCornerShape(50)
                            )
                    ) {
                        Text(
                            text = "Successful sign up. Please log in.",
                            color = MaterialTheme.colorScheme.onSecondary,
                            style = MaterialTheme.typography.bodySmall,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(
                                top = 5.dp,
                                bottom = 5.dp,
                                start = 10.dp,
                                end = 10.dp
                            )
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                TextFieldOnSurface(
                    value = email,
                    onValueChange = { email = it },
                    label = "Email address",
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Email),
                    modifier = Modifier.width(280.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))

                TextFieldOnSurface(
                    value = password,
                    onValueChange = { password = it },
                    label = "Password",
                    hidden = true,
                    modifier = Modifier.width(280.dp),
                )

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        errorMessage = ""
                        loginUser(
                            email,
                            password.trimEnd(),
                            updateUser,
                        ) { isSuccess, error ->
                            if (isSuccess) {
                                onClickSignIn()
                            } else if (error === "TOU NOT UP TO DATE") {
                                onTouOutdated()
                            } else {
                                errorMessage = error ?: "An unknown error occurred"
                            }
                        }
                    }
                ) {
                    Text(
                        text = "Sign In",
                        style = MaterialTheme.typography.labelSmall
                    )
                }

                if (errorMessage.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = errorMessage,
                        color = MaterialTheme.colorScheme.onErrorContainer,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(
                            start = 20.dp,
                            end = 20.dp
                        )
                    )
                }

                Spacer(modifier = Modifier.height(50.dp))

                Text(
                    text = "Don't have a ChatterBox account?",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.primary
                )

                Spacer(modifier = Modifier.height(20.dp))


                Button(
                    onClick = {
                        onClickSignUp()
                    }
                ) {
                    Text(
                        text = "Sign Up",
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        }
    }
}


private fun loginUser(
    username: String,
    password: String,
    updateUser: (UserAttributes) -> Unit,
    onResult: (Boolean, String?) -> Unit,
) {
    val signInRequest = SignInRequest(username, password)

    apiService.authenticateUser(signInRequest).enqueue(object : Callback<SignInResponse> {
        override fun onResponse(call: Call<SignInResponse>, response: Response<SignInResponse>) {
            if (response.isSuccessful) {
                // HTTP 200: Success
                val authToken = response.body()?.token
                val attributes = extractUserAttributes(response.body()?.user?.data?.attributes)
                if (attributes.isEmpty()) {
                    Log.e(TAG, "Authenticate User response is successful but empty")
                }
                val userId = attributes["id"]
                if (authToken != null && userId != null) {
                    SessionManager.clearSession()
                    SessionManager.saveSession(
                        userId = userId,
                        jwtToken = authToken,
                    )
                    TouAcceptanceValidator.validate(attributes["lastTouAcceptance"].toString())
                    if (!SessionManager.isTouUpToDate()) {
                        val error = "TOU NOT UP TO DATE"
                        onResult(false, error)
                        return
                    }
                    // if all is good, the user is signed in and we can set the current user
                    updateUser(response.body()!!.user.data.attributes)
                } else {
                    val errorMessage = "Token or userId is null. Response: ${response.body()}"
                    onResult(false, errorMessage)
                    return
                }
                onResult(true, null)
                return
            } else if (response.code() == 404) {
                val errorMessage = response.message()
                onResult(false, errorMessage)
                return
            } else {
                // Handle other error codes as needed
                val errorMessage = response.message()
                onResult(false, errorMessage)
                return
            }
        }

        override fun onFailure(call: Call<SignInResponse>, t: Throwable) {
            // Network or other failure
            onResult(false, t.message)
        }
    })
}

private fun extractUserAttributes(userData: UserAttributes?): Map<String, String> {
    val attributes = mutableMapOf<String, String>()
    attributes["id"] = userData?.id.toString()
    attributes["email"] = userData?.email.toString()
    attributes["displayName"] = userData?.displayName.toString()
    attributes["notificationFrequency"] = userData?.notificationFrequency.toString()
    attributes["lastTouAcceptance"] = userData?.lastTouAcceptance ?: ""
    attributes["notificationsToken"] = userData?.notificationsToken ?: ""

    return attributes
}

@Preview(apiLevel = 34, showBackground = true)
@Composable
fun PreviewLoginScreen() {
    ChatterBoxTheme {
        SignInScreen(
            updateUser = {},
            onClickSignIn = {},
            onTouOutdated = {},
            onClickSignUp = {},
            signedUp = true,
        )
    }
}

//todo: error handling for sign in
