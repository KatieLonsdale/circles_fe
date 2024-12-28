package com.katielonsdale.chatterbox

import android.app.Application

class CustomApplication : Application() {

    override fun onCreate() {
        super.onCreate()
//        todo: delete?
//        SessionManager.init(this)
////        clear image cache
//        val imageLoader = ImageLoader(this)
//        imageLoader.memoryCache?.clear()
    }
}
