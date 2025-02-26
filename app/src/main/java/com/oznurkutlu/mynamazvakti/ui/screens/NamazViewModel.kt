package com.oznurkutlu.mynamazvakti.ui.screens

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import androidx.lifecycle.viewModelScope
import com.oznurkutlu.mynamazvakti.data.local.NamazVaktiDatabase
import com.oznurkutlu.mynamazvakti.data.remote.RetrofitClient
import com.oznurkutlu.mynamazvakti.data.repository.NamazRepository
import com.oznurkutlu.mynamazvakti.model.NamazVakitleri
import kotlinx.coroutines.launch

class NamazViewModel(application: Application) : AndroidViewModel(application) {

    private val namazRepository: NamazRepository = NamazRepository(RetrofitClient.api, NamazVaktiDatabase.getDatabase(application).namazVakitleriDao())

    // UI'yi güncellemek için LiveData kullanabiliriz
    private val _namazVakitleri = MutableLiveData<NamazVakitleri>()
    val namazVakitleri: LiveData<NamazVakitleri> get() = _namazVakitleri

    // Şehir bilgisi girildiğinde API'den veriyi alıp veritabanına kaydedelim
    fun fetchNamazVakitleri(sehir: String, tarih: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val mevcutVeri = namazRepository.getNamazVakitleri(sehir, tarih)
                if (mevcutVeri != null) {
                    withContext(Dispatchers.Main) {
                        _namazVakitleri.postValue(mevcutVeri) // Veriyi UI'ye aktar
                    }
                } else {
                    // Veritabanında veri yoksa API'den al
                    namazRepository.fetchAndSaveNamazVakitleri(sehir)

                    // Yeni veriyi al ve UI'ye aktar
                    val yeniVeri = namazRepository.getNamazVakitleri(sehir, tarih)
                    if (yeniVeri != null) {
                        withContext(Dispatchers.Main) {
                            _namazVakitleri.postValue(yeniVeri)
                        }
                    } else {
                        // Verinin hala bulunamaması durumunda kullanıcıya bildirim
                        Log.e("NamazVaktiApp", "Veri hala bulunamıyor!")
                    }
                }
            } catch (e: Exception) {
                Log.e("NamazVaktiApp", "Hata oluştu: ${e.message}", e)
                // Hata mesajını göstermek için UI'ye bildirim
            }
        }
    }

}
