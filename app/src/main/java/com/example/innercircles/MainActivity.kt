package com.example.innercircles

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import com.example.innercircles.ui.MainScreen
import com.example.innercircles.ui.SignInMainScreen


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        todo: is this being initialized twice
        SessionManager.init(this)
        if (SessionManager.isUserLoggedIn()) {
            setContent {
                MaterialTheme {
                    MainScreen()
                }
            }
        } else {
            setContent{
                MaterialTheme {
                    SignInMainScreen()
                }
            }
        }
    }
}