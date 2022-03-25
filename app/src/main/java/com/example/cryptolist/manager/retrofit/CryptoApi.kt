package com.example.cryptolist.manager.retrofit

import com.example.cryptolist.model.CryptoListApiResponse
import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.GET

interface CryptoApi {
    @GET("tickers/24hr")
    fun getCryptoList(): Deferred<Response<CryptoListApiResponse>>

}