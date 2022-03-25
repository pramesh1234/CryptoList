package com.reliantparking.resident.managers.utils

import android.content.Context
import android.net.ConnectivityManager

object ConnectionCheckUtils {
    fun checkOnline(ctx : Context) : Boolean{
        try {
            val cm = ctx.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val netInfo = cm.activeNetworkInfo
            return netInfo != null && netInfo.isConnected
        } catch (e: Exception) {
            e.printStackTrace()
            return false
        }
    }
}