package com.treflor.data.remote.intercepters

import com.treflor.internal.UnauthorizedException
import okhttp3.Interceptor
import okhttp3.Response

class UnauthorizedInterceptorImpl : UnauthorizedInterceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val response = chain.proceed(chain.request())
        if (response.code() == 401) {
            throw UnauthorizedException()
        }
        return response
    }
}