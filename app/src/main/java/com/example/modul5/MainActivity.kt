package com.example.modul5

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MaterialTheme {
                HalamanTiket()
            }
        }
    }
}

enum class StatusPesanan {
    AWAL,
    KOSONG,
    MEMPROSES,
    BERHASIL
}

@Composable
fun HalamanTiket() {
    var nama by rememberSaveable { mutableStateOf("") }
    var jumlahTiket by rememberSaveable { mutableIntStateOf(1) }
    var status by rememberSaveable { mutableStateOf(StatusPesanan.AWAL) }
    val hargaTiket = 50000

    LaunchedEffect(status) {
        if (status == StatusPesanan.MEMPROSES) {
            delay(2000)
            status = StatusPesanan.BERHASIL
        }
    }

    HalamanPemesanan(
        nama = nama,
        jumlahTiket = jumlahTiket,
        hargaTiket = hargaTiket,
        status = status,
        onNamaChange = { nama = it },
        onTambah = { jumlahTiket++ },
        onKurang = { if (jumlahTiket > 1) jumlahTiket-- },
        onPesan = {
            if (nama.isBlank()) {
                status = StatusPesanan.KOSONG
            } else {
                status = StatusPesanan.MEMPROSES
            }
        }
    )
}

@Composable
fun HalamanPemesanan(
    nama: String,
    jumlahTiket: Int,
    hargaTiket: Int,
    status: StatusPesanan,
    onNamaChange: (String) -> Unit,
    onTambah: () -> Unit,
    onKurang: () -> Unit,
    onPesan: () -> Unit
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly
    ) {

        Text(
            text = "Pemesanan Tiket",
            style = MaterialTheme.typography.headlineSmall
        )

        OutlinedTextField(
            value = nama,
            onValueChange = onNamaChange,
            label = { Text("Nama") },
            placeholder = { Text("Masukkan nama Anda") },
            modifier = Modifier.fillMaxWidth()
        )

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text("Jumlah Tiket")
            Spacer(modifier = Modifier.height(12.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Button(
                    onClick = onKurang,
                    modifier = Modifier.size(width = 60.dp, height = 45.dp)
                ) {
                    Text("-")
                }
                Spacer(modifier = Modifier.width(32.dp))
                Text(
                    text = jumlahTiket.toString(),
                    style = MaterialTheme.typography.headlineMedium
                )
                Spacer(modifier = Modifier.width(32.dp))
                Button(
                    onClick = onTambah,
                    modifier = Modifier.size(width = 60.dp, height = 45.dp)
                ) {
                    Text("+")
                }
            }
        }

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text("Harga Tiket : Rp $hargaTiket")
            Spacer(modifier = Modifier.height(4.dp))
            Text("Total Harga : Rp ${jumlahTiket * hargaTiket}")
        }

        Button(
            onClick = onPesan,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Pesan Tiket")
        }

        Card(
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = when (status) {
                    StatusPesanan.AWAL -> "Status: Silakan pesan tiket"
                    StatusPesanan.KOSONG -> "Status: Nama harus diisi"
                    StatusPesanan.MEMPROSES -> "Status: Memproses pesanan..."
                    StatusPesanan.BERHASIL -> "Status: Tiket berhasil dipesan!"
                },
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}