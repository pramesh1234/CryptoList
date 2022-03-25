package com.example.cryptolist.viewmodel

import LoaderStatus
import RetrofitManager
import SharedPrefManager
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.example.cryptolist.CryptoApp
import com.example.cryptolist.R
import com.example.cryptolist.model.ErrorResponse
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import org.json.JSONObject
import retrofit2.Response
import kotlin.coroutines.CoroutineContext

open class MyBaseViewModel(application: Application) : AndroidViewModel(application),
    CoroutineScope {
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.IO + rootJob

    protected val TAG: String = this.javaClass.simpleName

    protected var errorLiveData = MutableLiveData<String?>()

    // ...because this is what we'll want to expose
    val errorMediatorLiveData = MediatorLiveData<String?>()

    var isLoading = MutableLiveData<LoaderStatus>()

    val rootJob = Job()

    fun showLoader() {
        isLoading.postValue(LoaderStatus.loading)
    }

    fun hideLoader() {
        isLoading.postValue(LoaderStatus.success)
    }

    protected val retrofitManager: RetrofitManager by lazy {
        RetrofitManager.getInstance(
            getApplication()
        )
    }

    protected val handler = CoroutineExceptionHandler { _, exception ->
        isLoading.postValue(LoaderStatus.failed)
        errorLiveData.postValue(exception.message)
        exception.printStackTrace()
    }

    init {
        errorMediatorLiveData.addSource(errorLiveData) { result: String? ->
            result?.let {
                errorMediatorLiveData.value = result
            }
        }
    }

    val sharedPrefManager: SharedPrefManager
        get() {
            return SharedPrefManager.getInstance(getApplication())
        }

    protected val exceptionHandler: CoroutineContext =
        CoroutineExceptionHandler { coroutineContext, throwable ->
            isLoading.postValue(LoaderStatus.failed)
            errorLiveData.postValue(throwable.message)
            throwable.printStackTrace()
        }


    protected fun getGsonObject(obj: JSONObject): JsonObject {
        val jsonParser = JsonParser()
        return jsonParser.parse(obj.toString()) as JsonObject
    }

    protected fun <T : Any> isResponseSuccess(response: Response<T>): Boolean {
        if (!response.isSuccessful) {
            isLoading.postValue(LoaderStatus.failed)
            if (response.errorBody() != null) {
                val jsonString = response.errorBody()!!.string()
                if (jsonString.contains("{")) {
                    val errorModel = ErrorResponse(jsonString)
                    if (errorModel.message != null) {
                        errorLiveData.postValue(errorModel.message)
                    } else {
                        errorLiveData.postValue(errorModel.message)

                    }
                } else {
                    errorLiveData.postValue(response.message())
                }
            } else if (!response.message().isEmpty())
                errorLiveData.postValue(response.message())
            else
                errorLiveData.postValue(getApplication<CryptoApp>().getString(R.string.something_wrong))
        }
        return response.isSuccessful
    }


    override fun onCleared() {
        super.onCleared()
        rootJob.cancel()
    }
}