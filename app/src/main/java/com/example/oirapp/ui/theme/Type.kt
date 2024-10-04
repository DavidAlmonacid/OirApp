package com.example.oirapp.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.oirapp.R

val bodyFontFamily = FontFamily(
    Font(R.font.mukta_regular, FontWeight.Normal),
    Font(R.font.mukta_medium, FontWeight.Medium),
    Font(R.font.mukta_semibold, FontWeight.SemiBold),
    Font(R.font.mukta_bold, FontWeight.Bold),
)

val bodyFontFamilyMono = FontFamily(
    Font(R.font.ubuntu_sans_mono_medium, FontWeight.Medium),
)

val baseline = Typography()

val Typography = Typography(
    displayMedium = baseline.displayMedium.copy(
        fontWeight = FontWeight.Bold,
        fontFamily = bodyFontFamily,
    ),

    titleLarge = baseline.titleLarge.copy(
        fontWeight = FontWeight.Medium,
        fontSize = 20.sp,
        fontFamily = bodyFontFamily,
    ),
    titleMedium = baseline.titleMedium.copy(
        fontSize = 18.sp,
        fontFamily = bodyFontFamily,
    ),

    bodyLarge = baseline.bodyLarge.copy(fontFamily = bodyFontFamily),
    bodyMedium = baseline.bodyMedium.copy(
        fontWeight = FontWeight.Medium,
        fontFamily = bodyFontFamily,
    ),

    labelLarge = baseline.labelLarge.copy(fontFamily = bodyFontFamily),
    labelMedium = baseline.labelMedium.copy(fontFamily = bodyFontFamily),
    labelSmall = baseline.labelSmall.copy(fontFamily = bodyFontFamily),
)
