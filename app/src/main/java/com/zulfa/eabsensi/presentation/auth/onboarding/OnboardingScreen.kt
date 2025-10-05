package com.zulfa.eabsensi.presentation.auth.onboarding


import android.content.res.Configuration
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign

import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zulfa.eabsensi.R
import com.zulfa.eabsensi.presentation.theme.EAbsensiTheme
import com.zulfa.eabsensi.presentation.theme.Poppins

@Composable
fun OnboardingScreen(
    onFinish: () -> Unit = {}
) {

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color.White
    ){
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(modifier = Modifier.height(24.dp))

            Image(
                painter = painterResource(id = R.drawable.authpic),
                contentDescription = "Onboarding Illustration",
                modifier = Modifier
                    .fillMaxWidth(),
                contentScale = ContentScale.Fit
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Title
            Text(
                text = "Selamat Datang Di E-Absensi Peserta Magang",
                fontFamily = Poppins,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(10.dp))

            // Subtitle
            Text(
                text = "Dinas Ketahanan Pangan, Pertanian \ndan Perikanan Kota Banjarmasin",
                fontFamily = Poppins,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(70.dp))

            // Button
            OutlinedButton(
                onClick = { onFinish() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    ,
                border = BorderStroke(width = 2.dp, color = Color(0xFF000000)),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE0F2FE)), // blue color
                shape = RoundedCornerShape(50),
            ) {
                Text(
                    text = "MULAI",
                    textAlign = TextAlign.Center,
                    style = TextStyle(
                        fontFamily = Poppins,
                        fontWeight = FontWeight.Bold,
                        fontSize = 40.sp,
                        color = Color(0xFF0284C7),
                        textAlign = TextAlign.Center
                        )
                )
            }
        }
    }
}

@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_NO,
)
@Composable
fun OnboardingScreenPreview() {
    EAbsensiTheme {
        OnboardingScreen()
    }
}