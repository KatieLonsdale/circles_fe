package com.katielonsdale.chatterbox.api

import com.katielonsdale.chatterbox.SessionManager
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor(private val sessionManager: SessionManager) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        // Check for the custom header
        val noAuthHeader = request.header("No-Authorization")
        if (noAuthHeader != null && noAuthHeader.toBoolean()) {
            // Skip adding the Authorization header for this request
            return chain.proceed(
                request.newBuilder()
                    .removeHeader("No-Authorization")
                    .addHeader("Content-Type", "application/json")
                    .build())
        }
        val token = sessionManager.getJwtToken()
        val newRequest = chain.request().newBuilder()
            .addHeader("Content-Type", "application/json")
            .addHeader("Authorization", "Bearer $token")
            .build()
        return chain.proceed(newRequest)
    }
}
