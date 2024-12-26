package com.example.innercircles.ui

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.innercircles.R
import com.example.innercircles.SessionManager
import com.example.innercircles.api.RetrofitClient
import com.example.innercircles.api.data.UserRequest
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.OffsetDateTime

@Composable
fun TermsOfUseScreen(
    onClickBack: () -> Unit,
    onReadFullTermsOfUse: () -> Unit,
    onClickAccept: () -> Unit
) {
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

    var isChecked by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Terms of Use",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        Text(
            text = "By using this app, you agree to our Terms of Use and Privacy Policy. Please review them carefully before continuing.",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        TextButton(
            onClick = { onReadFullTermsOfUse() }
        ) {
            Text(text = "Read Full Terms of Use")
        }
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(vertical = 16.dp)
        ) {
            Checkbox(
                checked = isChecked,
                onCheckedChange = { isChecked = it }
            )
            Text(text = "I agree to the Terms of Use and Privacy Policy")
        }
        Button(
            onClick = { if (isChecked) {
                updateUserTou()
                onClickAccept()
            }},
            enabled = isChecked
        ) {
            Text(text = "Accept and Continue")
        }
    }
}

private fun updateUserTou() {
    val lastTouAcceptance = OffsetDateTime.now()
    val userRequest = UserRequest(
        lastTouAcceptance = lastTouAcceptance.toString()
    )
    val userId = SessionManager.getUserId()
    RetrofitClient.apiService.updateUser(userId, userRequest).enqueue(
        object : Callback<Void> {
        override fun onResponse(call: Call<Void>, response: Response<Void>) {
            if (response.isSuccessful) {
                // HTTP 200: Success
                Log.d("updateUserTou", "User updated successfully")
            } else if (response.code() == 404) {
                Log.d("updateUserTou", "User not found")
            } else {
                // Handle other error codes as needed
                val errorMessage = response.message()
                Log.e("updateUserTou", errorMessage)
            }
        }

        override fun onFailure(call: Call<Void>, t: Throwable) {
            // Network or other failure
            Log.e("updateUserTou", "Network or other failure", t)
        }
    })
}

@Preview(showBackground = true)
@Composable
fun TermsOfUseScreenPreview() {
    MaterialTheme {
        TermsOfUseScreen(
            onClickBack = {},
            onReadFullTermsOfUse = {},
            onClickAccept = {},
        )
    }
}