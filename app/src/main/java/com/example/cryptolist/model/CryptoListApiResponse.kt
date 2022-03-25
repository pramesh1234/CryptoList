package com.example.cryptolist.model

import com.google.gson.*
import java.lang.reflect.Type

class CryptoListApiResponse {
    var cryptoList: ArrayList<CryptoModel> = ArrayList()

    class CryptoDeserializer : JsonDeserializer<CryptoListApiResponse> {
        override fun deserialize(
            json: JsonElement?,
            typeOfT: Type?,
            context: JsonDeserializationContext?
        ): CryptoListApiResponse {
            val cryptoListApiResponse = CryptoListApiResponse()
            val jsonArray = json!!.asJsonArray
            if(jsonArray is JsonArray){
                for (i in jsonArray) {
                    val crypto: CryptoModel? = Gson().fromJson(i, CryptoModel::class.java)
                    if (crypto != null) {
                        cryptoListApiResponse.cryptoList.add(crypto)
                    }
                }
            }
            return cryptoListApiResponse
        }

    }
}