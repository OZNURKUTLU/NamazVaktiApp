package com.oznurkutlu.mynamazvakti.data.remote

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private const val BASE_URL = "https:/vakit.vercel.app/"

    val api: NamazVaktiApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(NamazVaktiApi::class.java)
    }
}
