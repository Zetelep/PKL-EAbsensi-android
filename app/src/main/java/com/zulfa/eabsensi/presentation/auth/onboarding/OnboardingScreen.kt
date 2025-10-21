package com.zulfa.eabsensi.presentation.auth.onboarding


import android.content.res.Configuration
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign

import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zulfa.eabsensi.R
import com.zulfa.eabsensi.core.data.pref.UserModel
import com.zulfa.eabsensi.core.data.pref.UserPreference
import com.zulfa.eabsensi.core.data.pref.dataStore
import com.zulfa.eabsensi.presentation.theme.EAbsensiTheme
import com.zulfa.eabsensi.presentation.theme.Poppins

@Composable
fun OnboardingScreen(
    onFinish: () -> Unit = {}
) {
    val scrollState = rememberScrollState()

    val context = LocalContext.current
    val userPreference = remember { UserPreference.getInstance(context.dataStore) }
    val user by userPreference.getSession().collectAsState(initial = UserModel(
        "",
        "",
        "",
        "",
        false
    )
    )
    Scaffold(
        contentWindowInsets = WindowInsets.systemBars,
        bottomBar = {
            if (!user.isLogin) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .navigationBarsPadding()
                        .imePadding()
                        .padding(horizontal = 24.dp, vertical = 12.dp)
                ) {
                    OutlinedButton(
                        onClick = { onFinish() },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        border = BorderStroke(width = 2.dp, color = Color(0xFF000000)),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE0F2FE)),
                        shape = RoundedCornerShape(50)
                    ) {
                        Text(
                            text = "MULAI",
                            textAlign = TextAlign.Center,
                            style = TextStyle(
                                fontFamily = Poppins,
                                fontWeight = FontWeight.Bold,
                                fontSize = 30.sp,
                                color = Color(0xFF0284C7),
                                textAlign = TextAlign.Center
                            ),
                            maxLines = 1,
                            softWrap = false
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            color = Color.White
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 24.dp)
                    .verticalScroll(scrollState),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(24.dp))

                // Batasi tinggi gambar supaya tombol tidak terdorong ke luar layar
                Image( painter = painterResource(id = R.drawable.authpic),
                    contentDescription = "Onboarding Illustration",
                    modifier = Modifier .fillMaxWidth(),
                    contentScale = ContentScale.Fit )


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

                // Spacer kecil saja; tombol sudah sticky di bawah
                Spacer(modifier = Modifier.height(24.dp))
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

@Composable
private fun OnboardingScreenPreviewHost() {
    EAbsensiTheme {
        OnboardingScreen(onFinish = {})
    }
}

/* =======================
   PREVIEW BERDASARKAN SIZE
   Semua portrait only
   ======================= */

// Very Small Phone (mis. 4.7" kelas lama)
@Preview(
    name = "Very Small 320×568dp",
    showBackground = true,
    showSystemUi = true,
    device = "spec:width=320dp,height=568dp,orientation=portrait"
)
@Composable
fun Preview_VerySmall_Portrait() {
    OnboardingScreenPreviewHost()
}

// Small Phone (compact width umum, 360×640)
@Preview(
    name = "Small 360×640dp",
    showBackground = true,
    showSystemUi = true,
    device = "spec:width=360dp,height=640dp,orientation=portrait"
)
@Composable
fun Preview_Small_Portrait() {
    OnboardingScreenPreviewHost()
}

// Medium Phone (Pixel 7-ish 393×808)
@Preview(
    name = "Medium 393×808dp",
    showBackground = true,
    showSystemUi = true,
    device = "spec:width=393dp,height=808dp,orientation=portrait"
)
@Composable
fun Preview_Medium_Portrait() {
    OnboardingScreenPreviewHost()
}

// Large Phone (411×891)
@Preview(
    name = "Large 411×891dp",
    showBackground = true,
    showSystemUi = true,
    device = "spec:width=411dp,height=891dp,orientation=portrait"
)
@Composable
fun Preview_Large_Portrait() {
    OnboardingScreenPreviewHost()
}

// XL Phone (480×960) — phablet besar
@Preview(
    name = "XL 480×960dp",
    showBackground = true,
    showSystemUi = true,
    device = "spec:width=480dp,height=960dp,orientation=portrait"
)
@Composable
fun Preview_XL_Portrait() {
    OnboardingScreenPreviewHost()
}

/* =======================
   PREVIEW VARIASI FONT SCALE
   (aksesibilitas)
   ======================= */

// Small phone + font lebih besar (1.15x)
@Preview(
    name = "Small 360×640dp | Font x1.15",
    showBackground = true,
    showSystemUi = true,
    fontScale = 1.15f,
    device = "spec:width=360dp,height=640dp,orientation=portrait"
)
@Composable
fun Preview_Small_Font115() {
    OnboardingScreenPreviewHost()
}

// Medium phone + font besar (1.30x)
@Preview(
    name = "Medium 393×808dp | Font x1.30",
    showBackground = true,
    showSystemUi = true,
    fontScale = 1.30f,
    device = "spec:width=393dp,height=808dp,orientation=portrait"
)
@Composable
fun Preview_Medium_Font130() {
    OnboardingScreenPreviewHost()
}
