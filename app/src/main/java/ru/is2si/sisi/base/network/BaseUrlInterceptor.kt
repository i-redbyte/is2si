package ru.is2si.sisi.base.network

import okhttp3.HttpUrl
import okhttp3.Interceptor
import okhttp3.Response
import ru.is2si.sisi.data.auth.ServerUrlHolder

internal class BaseUrlInterceptor(private val serverUrlHolder: ServerUrlHolder) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()
        val oldUrl = request.url()
        val newBaseUrl = HttpUrl.parse(serverUrlHolder.serverUrl)!!
        val finalUrl = oldUrl.newBuilder()
                .scheme(newBaseUrl.scheme())
                .host(newBaseUrl.host())
                .port(newBaseUrl.port())
                .build()
        request = request.newBuilder()
                .url(finalUrl)
                .build()
        return chain.proceed(request)
    }

}