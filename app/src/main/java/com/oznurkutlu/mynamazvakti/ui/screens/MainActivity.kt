package com.oznurkutlu.mynamazvakti.ui.screens

import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.oznurkutlu.mynamazvakti.R
import com.oznurkutlu.mynamazvakti.data.remote.RetrofitClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import androidx.lifecycle.Observer
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var namazViewModel: NamazViewModel

    private lateinit var imsakText: TextView
    private lateinit var gunesText: TextView
    private lateinit var ogleText: TextView
    private lateinit var ikindiText: TextView
    private lateinit var aksamText: TextView
    private lateinit var yatsiText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // ViewModel'ı başlatıyoruz
        namazViewModel = ViewModelProvider(this).get(NamazViewModel::class.java)

        // UI bileşenlerini bağlıyoruz
        imsakText = findViewById(R.id.imsakText)
        gunesText = findViewById(R.id.gunesText)
        ogleText = findViewById(R.id.ogleText)
        ikindiText = findViewById(R.id.ikindiText)
        aksamText = findViewById(R.id.aksamText)
        yatsiText = findViewById(R.id.yatsiText)

        val sehirSpinner: Spinner = findViewById(R.id.sehirSpinner)
        val getirButton: Button = findViewById(R.id.getirButton)

        // Şehir listesi Spinner'a yüklensin
        fetchSehirListesi(sehirSpinner)

        // Butona tıklandığında işlem başlasın
        getirButton.setOnClickListener {
            val secilenSehir = sehirSpinner.selectedItem.toString()
            val currentDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
            namazViewModel.fetchNamazVakitleri(secilenSehir, currentDate)
        }

        // ViewModel'deki veriyi gözlemleyip UI'yi güncelleyelim
        namazViewModel.namazVakitleri.observe(this, Observer { namazVakitleri ->
            if (namazVakitleri != null) {
                imsakText.text = "İmsak: ${namazVakitleri.imsak}"
                gunesText.text = "Güneş: ${namazVakitleri.gunes}"
                ogleText.text = "Öğle: ${namazVakitleri.ogle}"
                ikindiText.text = "İkindi: ${namazVakitleri.ikindi}"
                aksamText.text = "Akşam: ${namazVakitleri.aksam}"
                yatsiText.text = "Yatsı: ${namazVakitleri.yatsi}"
            }
        })
    }

    // Şehir listesini API'den çekip Spinner'a ekleyelim
    private fun fetchSehirListesi(sehirSpinner: Spinner) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = RetrofitClient.api.getSehirListesi()
                if (response.isSuccessful && response.body() != null) {
                    val sehirListesi = response.body()!!
                    val adapter = ArrayAdapter(this@MainActivity, android.R.layout.simple_spinner_dropdown_item, sehirListesi)
                    withContext(Dispatchers.Main) {
                        sehirSpinner.adapter = adapter
                    }
                } else {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(this@MainActivity, "Şehir listesi alınamadı!", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
                Log.e("NamazVaktiApp", "Şehir listesi hatası: ${e.message}", e)
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@MainActivity, "Şehir listesi alınamadı!", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
