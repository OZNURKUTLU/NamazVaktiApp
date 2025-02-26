package com.oznurkutlu.mynamazvakti.model
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "namaz_vakitleri")
data class NamazVakitleri(
    @PrimaryKey(autoGenerate = true) val id: Int = 0, // Otomatik artan ID kullan
    val sehir: String,
    val tarih: String,
    val imsak: String,
    val gunes: String,
    val ogle: String,
    val ikindi: String,
    val aksam: String,
    val yatsi: String
)

