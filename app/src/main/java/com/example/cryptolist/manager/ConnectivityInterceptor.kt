package com.example.cryptolist.manager

import NoConnectivityException
import android.content.Context
import com.reliantparking.resident.managers.utils.ConnectionCheckUtils
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

class ConnectivityInterceptor(val ctx: Context) : Interceptor {
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        if (!ConnectionCheckUtils.checkOnline(ctx)) {
            throw NoConnectivityException(ctx)
        }
        return chain.proceed(request)
    }
}