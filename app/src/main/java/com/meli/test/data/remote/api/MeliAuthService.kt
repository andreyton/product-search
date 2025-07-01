package com.meli.test.data.remote.api

import com.meli.test.data.remote.model.TokenResponse
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST
import com.meli.test.BuildConfig

interface MeliAuthService {
    @FormUrlEncoded
    @POST(BuildConfig.AUTH_URL)
    suspend fun getAccessToken(
        @Field("grant_type") grantType: String = BuildConfig.GRANT_TYPE,
        @Field("client_id") clientId: String = BuildConfig.CLIENT_ID,
        @Field("client_secret") clientSecret: String = BuildConfig.CLIENT_SECRET
    ): TokenResponse
}