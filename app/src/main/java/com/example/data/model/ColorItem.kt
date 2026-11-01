package com.example.data.model

import androidx.compose.ui.graphics.Color

data class ColorItem(
  val code: String,
  val name: String,
  val category: String,
  val hexValue: String,
  val isChartColor: Boolean = true, // on official printed chart or custom formulation
  val description: String = ""
) {
  val composeColor: Color
    get() = try {
      Color(android.graphics.Color.parseColor(hexValue))
    } catch (_: Exception) {
      Color.LightGray
    }
}
