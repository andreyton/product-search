package com.meli.test.data.remote.auth

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.MutablePreferences
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.mutablePreferencesOf
import androidx.datastore.preferences.core.stringPreferencesKey
import com.meli.test.data.remote.api.MeliAuthService
import com.meli.test.data.remote.model.TokenResponse
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import java.util.concurrent.TimeUnit

@Suppress("UNCHECKED_CAST")
@ExperimentalCoroutinesApi
class TokenManagerTest {
    private lateinit var tokenManager: TokenManager
    private lateinit var authService: MeliAuthService
    private lateinit var dataStore: DataStore<Preferences>
    private val accessTokenKey = stringPreferencesKey("access_token")
    private val expirationTimeKey = longPreferencesKey("expiration_time")
    private lateinit var dataFlow: MutableStateFlow<Preferences>
    private val tokenResponse = TokenResponse(
        accessToken = "new_token",
        tokenType = "Bearer",
        expiresIn = 21600,
        scope = "read write",
        userId = 12345
    )

    @Before
    fun setup() {
        authService = mockk()
        dataStore = mockk()
        dataFlow = MutableStateFlow(mutablePreferencesOf())
        every { dataStore.data } returns dataFlow

        tokenManager = TokenManager(dataStore, authService)
    }

    @Test
    fun `isTokenExpired returns true when token is expired`() = runBlocking {
        // Given
        val currentTime = System.currentTimeMillis()
        val expiredTime = currentTime - TimeUnit.MINUTES.toMillis(10) // 10 minutes ago
        val preferences = mutablePreferencesOf(
            expirationTimeKey to expiredTime
        )
        dataFlow.value = preferences

        // When
        val result = tokenManager.isTokenExpired()

        // Then
        assertTrue(result)
    }

    @Test
    fun `isTokenExpired returns true when token will expire soon`() = runBlocking {
        // Given
        val currentTime = System.currentTimeMillis()
        val expiringTime = currentTime + TimeUnit.MINUTES.toMillis(3) // 3 minutes from now
        val preferences = mutablePreferencesOf(
            expirationTimeKey to expiringTime
        )
        dataFlow.value = preferences

        // When
        val result = tokenManager.isTokenExpired()

        // Then
        assertTrue(result)
    }

    @Test
    fun `isTokenExpired returns false when token is valid and not expiring soon`() = runBlocking {
        // Given
        val currentTime = System.currentTimeMillis()
        val validTime = currentTime + TimeUnit.MINUTES.toMillis(10) // 10 minutes from now
        val preferences = mutablePreferencesOf(
            expirationTimeKey to validTime
        )
        dataFlow.value = preferences

        // When
        val result = tokenManager.isTokenExpired()

        // Then
        assertFalse(result)
    }

    @Test
    fun `isTokenExpired returns true when expiration time is null`() = runBlocking {
        // Given
        dataFlow.value = mutablePreferencesOf() // without expiration time

        // When
        val result = tokenManager.isTokenExpired()

        // Then
        assertTrue(result)
    }

    @Test
    fun `refreshToken fetches new token and stores it`() = runBlocking {
        // Given
        coEvery { authService.getAccessToken() } returns tokenResponse
        coEvery { dataStore.updateData(any()) } coAnswers {
            val transform = it.invocation.args[0] as suspend (Preferences) -> Preferences
            val newPreferences = transform(mutablePreferencesOf())
            dataFlow.value = newPreferences
            newPreferences
        }

        // When
        val result = tokenManager.refreshToken()

        // Then
        assertEquals("new_token", result)
        coVerify { authService.getAccessToken() }
        coVerify { dataStore.updateData(any()) }

        val storedToken = dataFlow.value[accessTokenKey]
        assertEquals("new_token", storedToken)

        val storedExpiration = dataFlow.value[expirationTimeKey]
        assertTrue(storedExpiration != null)
    }

    @Test
    fun `getValidToken returns stored token when not expired`() = runBlocking {
        // Given
        val storedToken = "valid_token"
        val currentTime = System.currentTimeMillis()
        val validExpirationTime = currentTime + TimeUnit.HOURS.toMillis(5) // 5 hours from now
        val preferences = mutablePreferencesOf(
            accessTokenKey to storedToken,
            expirationTimeKey to validExpirationTime
        )
        dataFlow.value = preferences

        // When
        val result = tokenManager.getValidToken()

        // Then
        assertEquals(storedToken, result)
        coVerify(exactly = 0) { authService.getAccessToken() }
    }

    @Test
    fun `getValidToken refreshes token when expired`() = runBlocking {
        // Given
        val currentTime = System.currentTimeMillis()
        val expiredTime = currentTime - TimeUnit.MINUTES.toMillis(10) // 10 minutes ago
        val preferences = mutablePreferencesOf(
            expirationTimeKey to expiredTime
        )
        dataFlow.value = preferences

        coEvery { authService.getAccessToken() } returns tokenResponse
        coEvery { dataStore.updateData(any()) } coAnswers {
            val transform = it.invocation.args[0] as suspend (MutablePreferences) -> Unit
            val newPreferences = mutablePreferencesOf()
            transform(newPreferences)
            dataFlow.value = newPreferences
            newPreferences
        }

        // When
        val result = tokenManager.getValidToken()

        // Then
        assertEquals("new_token", result)
        coVerify { authService.getAccessToken() }
    }

    @Test
    fun `getValidToken refreshes token when stored token is null`() = runBlocking {
        // Given
        val currentTime = System.currentTimeMillis()
        val validExpirationTime = currentTime + TimeUnit.HOURS.toMillis(5) // 5 hours from now

        //  valid expiration time but no token
        val preferences = mutablePreferencesOf(
            expirationTimeKey to validExpirationTime
        )
        dataFlow.value = preferences

        coEvery { authService.getAccessToken() } returns tokenResponse
        coEvery { dataStore.updateData(any()) } coAnswers {
            val transform = it.invocation.args[0] as suspend (MutablePreferences) -> Unit
            val newPreferences = mutablePreferencesOf()
            transform(newPreferences)
            dataFlow.value = newPreferences
            newPreferences
        }

        // When
        val result = tokenManager.getValidToken()

        // Then
        assertEquals("new_token", result)
        coVerify { authService.getAccessToken() }
    }
}