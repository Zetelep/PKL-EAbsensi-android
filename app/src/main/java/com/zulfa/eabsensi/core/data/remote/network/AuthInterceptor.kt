package com.zulfa.eabsensi.core.data.remote.network

import com.zulfa.eabsensi.core.data.pref.UserPreference
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor(
    private val userPreference: UserPreference
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        // Get session synchronously (blocking only inside Interceptor)
        val user = runBlocking { userPreference.getSession().first() }
        val token = user.token

        val request = if (token.isNotEmpty()) {
            chain.request().newBuilder()
                .addHeader("Authorization", "Bearer $token")
                .build()
        } else {
            chain.request()
        }

        return chain.proceed(request)
    }
}