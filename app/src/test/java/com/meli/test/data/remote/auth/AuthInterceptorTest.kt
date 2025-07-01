package com.meli.test.data.remote.auth

import com.meli.test.BuildConfig
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class AuthInterceptorTest {

    private lateinit var authInterceptor: AuthInterceptor
    private lateinit var tokenManager: TokenManager
    private lateinit var chain: Interceptor.Chain
    private lateinit var request: Request
    private lateinit var response: Response

    @Before
    fun setup() {
        tokenManager = mockk()
        authInterceptor = AuthInterceptor(tokenManager)
        
        request = Request.Builder()
            .url("https://api.mercadolibre.com/sites/MCO/search")
            .build()
        
        response = mockk {
            every { code } returns 200
        }
        
        chain = mockk {
            every { request() } returns request
            every { proceed(any()) } returns response
        }
    }

    @Test
    fun `intercept adds Authorization header with token`() = runBlocking {
        // Given
        val token = "test_token"
        coEvery { tokenManager.getValidToken() } returns token
        
        // Capture the request with the token
        every { chain.proceed(match { req -> 
            req.header("Authorization") == "Bearer $token"
        }) } returns response

        // When
        val result = authInterceptor.intercept(chain)

        // Then
        assertEquals(response, result)
    }

    @Test
    fun `intercept skips auth for token endpoint`() = runBlocking {
        // Given
        val tokenRequest = Request.Builder()
            .url(BuildConfig.BASE_URL+ BuildConfig.AUTH_URL)
            .build()
        
        val tokenChain = mockk<Interceptor.Chain> {
            every { request() } returns tokenRequest
            every { proceed(tokenRequest) } returns response
        }

        // When
        val result = authInterceptor.intercept(tokenChain)

        // Then
        assertEquals(response, result)
    }

    @Test
    fun `intercept refreshes token on 401 response`() = runBlocking {
        // Given
        val initialToken = "initial_token"
        val refreshedToken = "refreshed_token"
        
        coEvery { tokenManager.getValidToken() } returns initialToken
        
        // First response is 401
        val unauthorizedResponse = mockk<Response> {
            every { code } returns 401
            every { close() } returns Unit
        }
        
        // Second response after token refresh is 200
        val authorizedResponse = mockk<Response> {
            every { code } returns 200
        }
        
        // First request with initial token returns 401
        every { chain.proceed(match { req -> 
            req.header("Authorization") == "Bearer $initialToken"
        }) } returns unauthorizedResponse
        
        // After refresh, request with new token returns 200
        coEvery { tokenManager.refreshToken() } returns refreshedToken
        every { chain.proceed(match { req -> 
            req.header("Authorization") == "Bearer $refreshedToken"
        }) } returns authorizedResponse

        // When
        val result = authInterceptor.intercept(chain)

        // Then
        assertEquals(authorizedResponse, result)
    }
}