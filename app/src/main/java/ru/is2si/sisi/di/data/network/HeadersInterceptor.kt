package ru.is2si.sisi.di.data.network

import okhttp3.Interceptor
import okhttp3.Response


class HeadersInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val request = originalRequest.newBuilder()
            .method(originalRequest.method(), originalRequest.body())
            .build()
        return chain.proceed(request)
    }
}