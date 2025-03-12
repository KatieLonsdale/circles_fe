package com.katielonsdale.chatterbox.api

import com.katielonsdale.chatterbox.BuildConfig
import java.util.concurrent.TimeUnit

/**
 * Constants used across the API layer.
 * This class centralizes all API-related configuration to make it easier to maintain and test.
 */
object ApiConstants {
    /**
     * Base URL for API requests.
     * This is configured in build.gradle.kts for different build types:
     * - Debug: Uses 10.0.2.2 (emulator's localhost)
     * - Release: Uses the production API URL
     */
    val BASE_URL = BuildConfig.API_BASE_URL
    
    /**
     * API version used in endpoints
     */
    const val API_VERSION = "v0"
    
    /**
     * Network timeouts
     */
    object Timeouts {
        val CONNECT = TimeUnit.SECONDS.toMillis(30)
        val READ = TimeUnit.SECONDS.toMillis(30)
        val WRITE = TimeUnit.SECONDS.toMillis(30)
    }
    
    /**
     * Common headers
     */
    object Headers {
        const val CONTENT_TYPE = "Content-Type"
        const val CONTENT_TYPE_JSON = "application/json"
        const val AUTHORIZATION = "Authorization"
        const val ACCEPT = "Accept"
    }
    
    /**
     * Pagination defaults
     */
    object Pagination {
        const val DEFAULT_PAGE_SIZE = 20
        const val DEFAULT_PAGE = 1
    }
    
    /**
     * Constructs a full endpoint URL
     * @param endpoint The endpoint path without leading slash
     * @return The complete URL
     */
    fun getEndpointUrl(endpoint: String): String {
        return "${BASE_URL.removeSuffix("/")}/$endpoint"
    }
} 