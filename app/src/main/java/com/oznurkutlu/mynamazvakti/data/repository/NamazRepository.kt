package com.oznurkutlu.mynamazvakti.data.repository

import android.util.Log
import com.oznurkutlu.mynamazvakti.data.local.NamazVakitleriDao
import com.oznurkutlu.mynamazvakti.data.remote.NamazVaktiApi
import com.oznurkutlu.mynamazvakti.model.NamazVakitleri
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class NamazRepository(private val api: NamazVaktiApi, private val dao: NamazVakitleriDao) {

    suspend fun kaydetNamazVakitleri(sehir: String, apiVerisi: List<NamazVakitleri>) {
        dao.insertVakitler(apiVerisi)
    }

    suspend fun getNamazVakitleri(sehir: String, tarih: String): NamazVakitleri? {
        return dao.getVakitler(sehir, tarih)
    }

    suspend fun fetchAndSaveNamazVakitleri(sehir: String) {
        if (sehir.isBlank()) {
            Log.e("NamazRepository", "Şehir bilgisi boş olamaz!")
            return
        }

        val currentDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())

            val response = api.getNamazVakitleri(
                region = sehir,
                city = sehir,
                date = currentDate,
                days = 3,
                timezoneOffset = 180
            )

        if (response.isSuccessful) {
            response.body()?.let { namazVaktiResponse ->
                // API'den gelen veriyi List<NamazVakitleri> formatına çevir
                val namazVakitleriList = namazVaktiResponse.times.map { (date, times) ->
                    NamazVakitleri(
                        sehir = sehir,
                        tarih = date,
                        imsak = times.getOrNull(0) ?: "",
                        gunes = times.getOrNull(1) ?: "",
                        ogle = times.getOrNull(2) ?: "",
                        ikindi = times.getOrNull(3) ?: "",
                        aksam = times.getOrNull(4) ?: "",
                        yatsi = times.getOrNull(5) ?: ""
                    )
                }
                if (namazVakitleriList.isNotEmpty()) {
                    kaydetNamazVakitleri(sehir, namazVakitleriList) // Dönüştürülen listeyi kaydet

                }
            }
        }
    }


}
