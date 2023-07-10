package com.moviemagic.data.api.interceptor

import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HeaderInterceptor @Inject constructor(): Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val requestBuilder = chain.request().newBuilder()
        try {
            requestBuilder.addHeader("X-RapidAPI-Key", "0b7c91b0a9msh77621cef849563ep1c4dd9jsn4c58232bd538")
            requestBuilder.addHeader("X-RapidAPI-Host", "moviesdatabase.p.rapidapi.com")
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return chain.proceed(requestBuilder.build())
    }
}