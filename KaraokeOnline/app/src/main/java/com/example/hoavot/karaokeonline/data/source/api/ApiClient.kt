package com.example.hoavot.karaokeonline.data.source.api

import com.google.gson.FieldNamingPolicy
import com.google.gson.GsonBuilder
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Protocol
import okhttp3.ResponseBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.reflect.Type
import java.util.*
import java.util.concurrent.TimeUnit

/**
 *
 *
 *
 * */

open class ApiClient private constructor(url: String? = null) {

    internal var token: String? = null
    //    private var baseUrl: String = if (url == null || url.isEmpty()) BuildConfig.BASE_API_URL else url
    private var baseYoutbeUrl: String = "https://www.googleapis.com/"
    private var baseKaraUrl: String = "http://192.168.1.10:3000"

    companion object : SingletonHolder<ApiClient, String>(::ApiClient) {
        private const val API_TIMEOUT = 10L // 10 minutes
    }

    val youtTubeService: ApiService
        get() {
            return createService()
        }

    val karaService: ApiService
        get() {
            return createKaraService()
        }

    private fun createService(): ApiService {
        val httpClientBuilder = OkHttpClient.Builder()
        httpClientBuilder.interceptors().add(Interceptor { chain ->
            val original = chain.request()
            // Request customization: add request headers
            val requestBuilder = original.newBuilder()
                    .method(original.method(), original.body())
            val request = requestBuilder.build()
            chain.proceed(request)
        })
        val client = httpClientBuilder
                .connectTimeout(API_TIMEOUT, TimeUnit.MINUTES)
                .writeTimeout(API_TIMEOUT, TimeUnit.MINUTES)
                .readTimeout(API_TIMEOUT, TimeUnit.MINUTES)
                .protocols(Collections.singletonList(Protocol.HTTP_1_1))
                .build()
        val gson = GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .serializeNulls()
                .create()

        val nullOnEmptyConverterFactory = object : Converter.Factory() {
            fun converterFactory() = this
            override fun responseBodyConverter(type: Type, annotations: Array<out Annotation>, retrofit: Retrofit) = object : Converter<ResponseBody, Any?> {
                val nextResponseBodyConverter = retrofit.nextResponseBodyConverter<Any?>(converterFactory(), type, annotations)
                override fun convert(value: ResponseBody) = if (value.contentLength() != 0L) nextResponseBodyConverter.convert(value) else null
            }
        }
        val retrofit = Retrofit.Builder()
                .baseUrl(baseYoutbeUrl)
                .addConverterFactory(nullOnEmptyConverterFactory)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(CustomCallAdapterFactory.create())
                .client(client)
                .build()
        return retrofit.create(ApiService::class.java)
    }

    private fun createKaraService(): ApiService {
        val log = HttpLoggingInterceptor()
        log.level = HttpLoggingInterceptor.Level.BODY
        val httpClientBuilder = OkHttpClient.Builder()
        httpClientBuilder.interceptors().add(Interceptor { chain ->
            val original = chain.request()
            // Request customization: add request headers
            val requestBuilder = original.newBuilder()
                    .method(original.method(), original.body())
            token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VybmFtZSI6Im1pa2FzYSIsInBhc3N3b3JkIjoiaG9hIGRlIHRodW9uZyIsImlhdCI6MTUyNTE1MDAxOCwiZXhwIjoxNTI3NzQyMDE4fQ.ic1FnKgOxQsqVBlEUTtWiI14kOfvfq6Dl_kBanwkxco"
            if (token != null) {
                requestBuilder.addHeader("x-access-token", "$token")
            }
            val request = requestBuilder.build()
            chain.proceed(request)
        })
        val client = httpClientBuilder
                .addInterceptor(log)
                .connectTimeout(API_TIMEOUT, TimeUnit.MINUTES)
                .writeTimeout(API_TIMEOUT, TimeUnit.MINUTES)
                .readTimeout(API_TIMEOUT, TimeUnit.MINUTES)
                .protocols(Collections.singletonList(Protocol.HTTP_1_1))
                .build()
        val gson = GsonBuilder()
                .setLenient()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .serializeNulls()
                .create()

        val nullOnEmptyConverterFactory = object : Converter.Factory() {
            fun converterFactory() = this
            override fun responseBodyConverter(type: Type, annotations: Array<out Annotation>, retrofit: Retrofit) = object : Converter<ResponseBody, Any?> {
                val nextResponseBodyConverter = retrofit.nextResponseBodyConverter<Any?>(converterFactory(), type, annotations)
                override fun convert(value: ResponseBody) = if (value.contentLength() != 0L) nextResponseBodyConverter.convert(value) else null
            }
        }
        val retrofit = Retrofit.Builder()
                .baseUrl(baseKaraUrl)
                .addConverterFactory(nullOnEmptyConverterFactory)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(CustomCallAdapterFactory.create())
                .client(client)
                .build()
        return retrofit.create(ApiService::class.java)
    }

}

/**
 * Use this class to create singleton object with argument
 */
open class SingletonHolder<out T, in A>(private var creator: (A?) -> T) {
    @kotlin.jvm.Volatile
    private var instance: T? = null

    /**
     * Generate instance for T class with argument A
     */
    fun getInstance(arg: A?): T {
        val i = instance
        if (i != null) {
            return i
        }

        return synchronized(this) {
            val i2 = instance
            if (i2 != null) {
                i2
            } else {
                val created = creator(arg)
                instance = created
                created
            }
        }
    }

    /**
     * Clear current instance
     */
    fun clearInstance() {
        instance = null
    }
}
