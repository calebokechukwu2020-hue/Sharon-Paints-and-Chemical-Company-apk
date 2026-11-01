package com.example.data.model

import androidx.annotation.DrawableRes
import com.example.R

enum class PaintSize(val label: String, val liters: Int) {
  SIZE_20L("20 Litres", 20),
  SIZE_10L("10 Litres", 10),
  SIZE_4L("4 Litres", 4)
}

enum class PaintCategory(val title: String) {
  DECORATIVE("Decorative & Architectural"),
  PREMIUM_MATT("Luxury Matts & Silks"),
  TEXTURED("Textured & Heavy Duty"),
  SPECIALIZED("Weather & Specialty"),
  AUTOMOBILE("Automobile & Industrial"),
  SURFACE_PREP("Putty, Bonds & Screeding")
}

data class PaintProduct(
  val id: String,
  val name: String,
  val uppercaseTitle: String = name.uppercase(),
  val category: PaintCategory,
  val shortDescription: String,
  val fullDescription: String,
  val prices: Map<PaintSize, Long>, // in Nigerian Naira (NGN)
  val availableSizes: List<PaintSize> = listOf(PaintSize.SIZE_20L),
  val coverageSqmPerLiter: Double, // Approx m² per liter per single coat
  val dryingTimeHours: Double,
  val recoatTimeHours: Double,
  val recommendedCoats: Int,
  val finishType: String,
  val suitableSurfaces: List<String>,
  val applicationTools: List<String>,
  @DrawableRes val imageResId: Int = R.drawable.ic_sharon_logo,
  val isPopular: Boolean = false,
  val nisCertified: Boolean = true,
  val mancapApproved: Boolean = true
) {
  fun getPrice(size: PaintSize): Long = prices[size] ?: (prices[availableSizes.firstOrNull() ?: PaintSize.SIZE_20L] ?: 0L)
}
