package com.oznurkutlu.mynamazvakti.data.local
import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.oznurkutlu.mynamazvakti.model.NamazVakitleri

@Database(entities = [NamazVakitleri::class], version = 1, exportSchema = false)
abstract class NamazVaktiDatabase : RoomDatabase() {

    abstract fun namazVakitleriDao(): NamazVakitleriDao

    companion object {
        @Volatile
        private var INSTANCE: NamazVaktiDatabase? = null

        fun getDatabase(context: Context): NamazVaktiDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    NamazVaktiDatabase::class.java,
                    "namaz_vakti_database"
                )
                    .fallbackToDestructiveMigration() // Veritabanı şeması değişirse eski veriyi sil ve yeniden oluştur
                    .build()
                    .also { INSTANCE = it }
            }
        }
    }
}
