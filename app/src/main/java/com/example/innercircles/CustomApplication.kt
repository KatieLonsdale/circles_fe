package com.example.innercircles

import android.app.Application
import coil.ImageLoader
import coil.annotation.ExperimentalCoilApi

class CustomApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        SessionManager.init(this)
//        clear image cache
        val imageLoader = ImageLoader(this)
        imageLoader.memoryCache?.clear()
    }
}
