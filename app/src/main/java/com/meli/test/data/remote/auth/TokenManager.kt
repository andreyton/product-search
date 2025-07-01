package com.meli.test.data.remote.auth

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import com.meli.test.data.remote.api.MeliAuthService
import com.meli.test.data.remote.model.TokenResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import java.util.concurrent.TimeUnit

class TokenManager(
    private val encryptedDataStore: DataStore<Preferences>,
    private val authService: MeliAuthService
) {
    private val accessTokenKey = stringPreferencesKey("access_token")
    private val expirationTimeKey = longPreferencesKey("expiration_time")

    /**
     * Get the stored access token
     */
    val accessToken: Flow<String?> = encryptedDataStore.data.map { preferences ->
        preferences[accessTokenKey]
    }

    /**
     * Get the stored expiration time
     */
    private val expirationTime: Flow<Long?> = encryptedDataStore.data.map { preferences ->
        preferences[expirationTimeKey]
    }

    /**
     * Check if the token is expired
     */
    suspend fun isTokenExpired(): Boolean {
        val expTime = expirationTime.first() ?: 0L
        val currentTime = System.currentTimeMillis()
        // Evaluate if it'll expire within 5 minutes
        return expTime - currentTime < TimeUnit.MINUTES.toMillis(5)
    }

    /**
     * Fetch a new token and store
     */
    suspend fun refreshToken(): String {
        val tokenResponse = authService.getAccessToken()
        storeToken(tokenResponse)
        return tokenResponse.accessToken
    }

    /**
     * Store token and expirationTime
     */
    private suspend fun storeToken(tokenResponse: TokenResponse) {
        val expirationTime = System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(tokenResponse.expiresIn.toLong())
        encryptedDataStore.edit { preferences ->
            preferences[accessTokenKey] = tokenResponse.accessToken
            preferences[expirationTimeKey] = expirationTime
        }
    }

    /**
     * Get a valid token
     */
    suspend fun getValidToken(): String {
        return if (isTokenExpired()) {
            refreshToken()
        } else {
            accessToken.first() ?: refreshToken()
        }
    }
}