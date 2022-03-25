import android.content.Context
import android.content.ContextWrapper
import com.example.cryptolist.R
import com.example.cryptolist.manager.ConnectivityInterceptor
import com.example.cryptolist.manager.retrofit.CryptoApi
import com.example.cryptolist.model.CryptoListApiResponse
import com.google.gson.GsonBuilder
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitManager(context: Context) : ContextWrapper(context) {
    private var userRetrofit: Retrofit? = null

    companion object {
        private var INSTANCE: RetrofitManager? = null
        private lateinit var retrofit: Retrofit

        fun getInstance(context: Context): RetrofitManager {
            if (INSTANCE == null) {
                initManager(context)
            }
            return INSTANCE!!;
        }

        private fun initManager(context: Context) {
            INSTANCE = RetrofitManager(context)

            val interceptor = Interceptor { chain ->
                val request = chain.request().newBuilder()
                    .addHeader("content-type", "application/json")
                    .build()

                chain.proceed(request)
            }

            val httpClient: OkHttpClient.Builder =
                OkHttpClient.Builder().addInterceptor(interceptor)
                    .addInterceptor(ConnectivityInterceptor(context))


            val logInterceptor = HttpLoggingInterceptor()
            logInterceptor.level = HttpLoggingInterceptor.Level.BODY
            httpClient.addInterceptor(logInterceptor)


            val gson1 = GsonBuilder()
                .registerTypeAdapter(
                    CryptoListApiResponse::class.java,
                    CryptoListApiResponse.CryptoDeserializer()
                )
                .create()

            retrofit = Retrofit.Builder()
                .baseUrl(context.getString(R.string.base_url))// + context.getString(R.string.url_version))
                .addConverterFactory(GsonConverterFactory.create(gson1))
                .addCallAdapterFactory(CoroutineCallAdapterFactory())
                .client(httpClient.build())
                .build()

        }
    }


    fun getAuthApi(): CryptoApi {
        return retrofit.create(CryptoApi::class.java)
    }

}