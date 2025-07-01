package com.meli.test.data.remote.auth

import com.meli.test.BuildConfig
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

class AuthInterceptor(private val tokenManager: TokenManager) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        
        // Skip token for auth requests
        if (originalRequest.url.toString().contains(BuildConfig.AUTH_URL)) {
            return chain.proceed(originalRequest)
        }
        
        val token = runBlocking { tokenManager.getValidToken() }
        val newRequest = addAuthHeader(originalRequest, token)
        val response = chain.proceed(newRequest)

        // Try to refresh the token once and retry the request
        if (response.code == 401) {
            response.close()
            val newToken = runBlocking { tokenManager.refreshToken() }
            val newRequestWithFreshToken = addAuthHeader(originalRequest, newToken)
            return chain.proceed(newRequestWithFreshToken)
        }
        
        return response
    }
    
    private fun addAuthHeader(request: Request, token: String): Request {
        return request.newBuilder()
            .header("Authorization", "Bearer $token")
            .build()
    }
}