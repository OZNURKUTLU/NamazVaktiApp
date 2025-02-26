package com.oznurkutlu.mynamazvakti.data.local
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.oznurkutlu.mynamazvakti.model.NamazVakitleri

@Dao
interface NamazVakitleriDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertVakitler(vakitler: List<NamazVakitleri>) // 3 gün veri eklemek için liste

    @Query("SELECT * FROM namaz_vakitleri WHERE sehir = :sehir AND tarih = :tarih LIMIT 1")
    fun getVakitler(sehir: String, tarih: String): NamazVakitleri?
}
