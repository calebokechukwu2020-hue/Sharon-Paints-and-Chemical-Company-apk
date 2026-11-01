package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
  primary = Color(0xFF60A5FA),
  onPrimary = Color(0xFF0F172A),
  primaryContainer = SharonBlueSecondary,
  onPrimaryContainer = Color(0xFFE2E8F0),
  secondary = SharonGold,
  onSecondary = Color(0xFF0F172A),
  tertiary = SharonCrimson,
  background = SharonDarkSurface,
  surface = SharonDarkCard,
  onBackground = SharonDarkText,
  onSurface = SharonDarkText
)

private val LightColorScheme = lightColorScheme(
  primary = SharonBluePrimary,
  onPrimary = Color.White,
  primaryContainer = Color(0xFFE0E7FF),
  onPrimaryContainer = SharonBluePrimary,
  secondary = SharonCrimson,
  onSecondary = Color.White,
  tertiary = SharonGold,
  background = SharonSurfaceLight,
  surface = SharonCardLight,
  onBackground = SharonTextPrimary,
  onSurface = SharonTextPrimary
)

@Composable
fun SharonPaintsTheme(
  darkTheme: Boolean = isSystemInDarkTheme(),
  dynamicColor: Boolean = false, // Keep brand colors distinct for Sharon Paints
  content: @Composable () -> Unit,
) {
  val colorScheme = when {
    dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
      val context = LocalContext.current
      if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
    }
    darkTheme -> DarkColorScheme
    else -> LightColorScheme
  }

  MaterialTheme(
    colorScheme = colorScheme,
    typography = Typography,
    content = content
  )
}
