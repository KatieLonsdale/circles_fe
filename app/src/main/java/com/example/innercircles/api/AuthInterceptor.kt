package com.example.innercircles.api

import com.example.innercircles.SessionManager
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor(private val sessionManager: SessionManager) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        // Check for the custom header
        val noAuthHeader = request.header("No-Authorization")
        if (noAuthHeader != null && noAuthHeader.toBoolean()) {
            // Skip adding the Authorization header for this request
            return chain.proceed(request.newBuilder().removeHeader("No-Authorization").build())
        }
        val token = sessionManager.getToken()
        val newRequest = chain.request().newBuilder()
            .addHeader("Authorization", "Bearer $token")
            .build()
        return chain.proceed(newRequest)
    }
}
