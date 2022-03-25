package com.example.cryptolist.model

import com.google.gson.annotations.SerializedName

data class CryptoModel(
    @SerializedName("symbol") var symbol: String,
    @SerializedName("baseAsset") var baseAsset: String,
    @SerializedName("quoteAsset") var quoteAsset: String,
    @SerializedName("openPrice") var openPrice: String,
    @SerializedName("lowPrice") var lowPrice:String,
    @SerializedName("highPrice") var highPrice:String,
    @SerializedName("lastPrice") var lastPrice:String,
    @SerializedName("volume") var volume:String,
    @SerializedName("bidPrice") var bidPrice:String,
    @SerializedName("askPrice") var askPrice:String,
    @SerializedName("at") var at:Long,
    var full:Boolean = false
)
