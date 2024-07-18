package com.example.innercircles

import android.app.Application

class CustomApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        SessionManager.init(this)
    }
}
