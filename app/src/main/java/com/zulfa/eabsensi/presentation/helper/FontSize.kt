package com.zulfa.eabsensi.presentation.helper

import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun responsiveFontSize(screenWidth: Dp, baseSize: TextUnit): TextUnit {
    val scaleFactor = when {
        screenWidth < 360.dp -> 0.9f   // layar kecil
        screenWidth < 400.dp -> 1.0f   // normal
        screenWidth < 480.dp -> 1.1f   // agak besar
        else -> 1.2f                   // layar lebar
    }
    return (baseSize.value * scaleFactor).sp
}