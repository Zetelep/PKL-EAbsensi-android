package com.zulfa.eabsensi.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zulfa.eabsensi.presentation.helper.responsiveFontSize
import com.zulfa.eabsensi.presentation.theme.Poppins

@Composable
fun HistoryCard(date: String, time: String, status: String, Width: Dp) {
    Card(
        shape = RoundedCornerShape(24.dp),
        elevation = CardDefaults.cardElevation(6.dp),
        modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp, top = 8.dp),
        colors = CardDefaults.cardColors(Color.White)
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 20.dp, vertical = 16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Kolom kiri: Date
            Column(
                modifier = Modifier.weight(1f), // fleksibel ambil sisa ruang
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = date,
                    fontFamily = Poppins,
                    fontWeight = FontWeight.Bold,
                    fontSize = responsiveFontSize(screenWidth = Width, 12.sp)
                )
            }

            // Kolom kanan: Waktu Masuk + Status
            Row(
                modifier = Modifier.weight(1f), // sama-sama bagi rata
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Kolom Waktu Masuk
                Column(
                    modifier = Modifier.width(100.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        "Waktu Masuk",
                        fontFamily = Poppins,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        fontSize = responsiveFontSize(screenWidth = Width, 12.sp),
                        modifier = Modifier.fillMaxWidth()
                    )
                    Text(
                        text = time.ifBlank { "-" },
                        color = Color(0xFF1565C0),
                        fontWeight = FontWeight.Bold,
                        fontFamily = Poppins,
                        textAlign = TextAlign.Center,
                        fontSize = responsiveFontSize(screenWidth = Width, 12.sp),
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                // Kolom Status
                Column(
                    modifier = Modifier.width(100.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        "Status",
                        fontFamily = Poppins,
                        fontWeight = FontWeight.Bold,
                        fontSize = responsiveFontSize(screenWidth = Width, 12.sp),
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Text(
                        text = status,
                        fontFamily = Poppins,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth(),
                        fontSize = responsiveFontSize(screenWidth = Width, 12.sp),
                        color = when (status) {
                            "Hadir" -> Color(0xFF4CAF50)
                            "Di Luar Area" -> Color(0xFF03A9F4)
                            "Izin Disetujui" -> Color(0xFF4CAF50)
                            else -> Color(0xFF9E9E9E)
                        }
                    )
                }
            }
        }
    }
}