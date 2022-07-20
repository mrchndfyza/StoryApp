package com.greentea.storyapp2.services.retrofit

import com.greentea.storyapp2.services.api.StoryAPI
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitInstance {
    companion object {
        private val retrofit by lazy{
            val logging = HttpLoggingInterceptor()
            val level = HttpLoggingInterceptor.Level.BODY
            logging.setLevel(level)

            val client = OkHttpClient
                .Builder()
                .addInterceptor(logging)
                .build()
            Retrofit.Builder()
                .baseUrl("https://story-api.dicoding.dev/v1/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()
        }

        val API_OBJECT: StoryAPI by lazy{
            retrofit.create(StoryAPI::class.java)
        }
    }
}