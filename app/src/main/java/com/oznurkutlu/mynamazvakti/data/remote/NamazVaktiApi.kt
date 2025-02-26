package com.oznurkutlu.mynamazvakti.data.remote

import com.oznurkutlu.mynamazvakti.model.NamazVaktiResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface NamazVaktiApi {
    @GET("api/regions")
    suspend fun getSehirListesi(
        @Query("country") country: String = "Turkey"
    ): retrofit2.Response<List<String>>

    @GET("api/timesFromPlace")
    suspend fun getNamazVakitleri(
        @Query("country") country: String = "Turkey",
        @Query("region") region: String,
        @Query("city") city: String,
        @Query("date") date: String,
        @Query("days") days: Int,
        @Query("timezoneOffset") timezoneOffset: Int,
        @Query("calculationMethod") calculationMethod: String = "Turkey"
    ): retrofit2.Response<NamazVaktiResponse>
}
