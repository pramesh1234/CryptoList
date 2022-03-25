package com.example.cryptolist.model

import org.json.JSONObject

class ErrorResponse (jsonString: String) {
    var errorCode: String? = null
    var message: String? = null

    init {
        val jsonObject = JSONObject(jsonString)

        if(jsonObject.has("message"))
            message = jsonObject.getString("message")
        if(jsonObject.has("errorCode"))
            errorCode = jsonObject.getString("errorCode")

    }
}