package com.example.cryptolist.viewmodel

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.example.cryptolist.model.CryptoModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : MyBaseViewModel(application) {
    val cryptoListLiveData = MutableLiveData<ArrayList<CryptoModel>>()
    fun getCryptoList() {
        CoroutineScope(exceptionHandler).launch {
            val request = retrofitManager.getAuthApi().getCryptoList()
            val response = request.await()
            if (isResponseSuccess(response)) {
                val apiResponse = response.body()!!
                cryptoListLiveData.postValue(apiResponse.cryptoList)
            }
        }
    }
}