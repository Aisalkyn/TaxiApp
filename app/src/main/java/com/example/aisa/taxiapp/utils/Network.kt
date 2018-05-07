package com.example.nestana.myweather.utils

import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


class Network {

    companion object {
        val REQUEST_TIME = 1L

        fun initService(baseURL: String): ForumService {
            return Retrofit.Builder()
                    .baseUrl(baseURL)
                    .addConverterFactory(initGson())
                    .client(getClient()).build().create(ForumService::class.java)
        }

        private fun getClient(): OkHttpClient {
            val interceptor = HttpLoggingInterceptor()
            interceptor.level = HttpLoggingInterceptor.Level.BODY

            return OkHttpClient.Builder()
                    .addInterceptor(interceptor)
                    .writeTimeout(REQUEST_TIME, TimeUnit.MINUTES)
                    .readTimeout(REQUEST_TIME, TimeUnit.MINUTES)
                    .connectTimeout(REQUEST_TIME, TimeUnit.MINUTES)
                    .build()
        }

        private fun initGson(): GsonConverterFactory {
            return GsonConverterFactory.create(GsonBuilder().create())
        }

    }
}