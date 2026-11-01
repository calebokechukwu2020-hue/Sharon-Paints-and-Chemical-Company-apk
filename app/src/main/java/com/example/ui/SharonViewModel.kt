package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.model.*
import com.example.data.repository.SharonRepository
import com.example.service.SharonVoiceSpeaker
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.Calendar

enum class AppNavDestination(val label: String) {
  CATALOG("Paints"),
  COLORS("Color Chart"),
  DEPOTS("Depots & GPS"),
  VOICE_AI("AI Voice Tutor"),
  CALCULATOR("AI Calculator"),
  INVOICES("Receipts & Docs"),
  ADS("AI Ads Showcase"),
  ADMIN("Admin Portal")
}

data class PaintCoverageResult(
  val surfaceAreaSqm: Double,
  val paintNeededLiters: Double,
  val recommendedBuckets20L: Int,
  val recommendedBuckets10L: Int,
  val recommendedCans4L: Int,
  val estimatedCostNaira: Long
)

class SharonViewModel(application: Application) : AndroidViewModel(application) {

  val repository = SharonRepository()
  val voiceSpeaker = SharonVoiceSpeaker(application)

  // Navigation Destination
  private val _currentDestination = MutableStateFlow(AppNavDestination.CATALOG)
  val currentDestination: StateFlow<AppNavDestination> = _currentDestination.asStateFlow()

  // Selected Product for Details
  private val _selectedProduct = MutableStateFlow(repository.products[0])
  val selectedProduct: StateFlow<PaintProduct> = _selectedProduct.asStateFlow()

  // Selected Paint Size
  private val _selectedSize = MutableStateFlow(PaintSize.SIZE_20L)
  val selectedSize: StateFlow<PaintSize> = _selectedSize.asStateFlow()

  // Selected Color
  private val _selectedColor = MutableStateFlow(repository.colorCharts[0])
  val selectedColor: StateFlow<ColorItem> = _selectedColor.asStateFlow()

  // Order Details / Document Viewer
  private val _viewingOrder = MutableStateFlow<Order?>(null)
  val viewingOrder: StateFlow<Order?> = _viewingOrder.asStateFlow()

  // Tracking Code Search
  private val _searchTrackingCode = MutableStateFlow("")
  val searchTrackingCode: StateFlow<String> = _searchTrackingCode.asStateFlow()

  private val _searchedOrder = MutableStateFlow<Order?>(null)
  val searchedOrder: StateFlow<Order?> = _searchedOrder.asStateFlow()

  // Calculator Inputs
  private val _calcWallLengthMeters = MutableStateFlow("15.0")
  val calcWallLengthMeters: StateFlow<String> = _calcWallLengthMeters.asStateFlow()

  private val _calcWallHeightMeters = MutableStateFlow("3.0")
  val calcWallHeightMeters: StateFlow<String> = _calcWallHeightMeters.asStateFlow()

  private val _calcCoats = MutableStateFlow(2)
  val calcCoats: StateFlow<Int> = _calcCoats.asStateFlow()

  private val _calcSurfaceType = MutableStateFlow("POP / Screeded Wall (Standard)")
  val calcSurfaceType: StateFlow<String> = _calcSurfaceType.asStateFlow()

  private val _calcSelectedProduct = MutableStateFlow(repository.products[0])
  val calcSelectedProduct: StateFlow<PaintProduct> = _calcSelectedProduct.asStateFlow()

  // Admin Code Input
  private val _adminCodeInput = MutableStateFlow("")
  val adminCodeInput: StateFlow<String> = _adminCodeInput.asStateFlow()

  private val _adminLoginError = MutableStateFlow<String?>(null)
  val adminLoginError: StateFlow<String?> = _adminLoginError.asStateFlow()

  // Customer OTP and Auth
  private val _otpInput = MutableStateFlow("")
  val otpInput: StateFlow<String> = _otpInput.asStateFlow()

  private val _otpSuccessMessage = MutableStateFlow<String?>(null)
  val otpSuccessMessage: StateFlow<String?> = _otpSuccessMessage.asStateFlow()

  // Scheduling State
  private val _selectedPickupDay = MutableStateFlow(Calendar.TUESDAY) // Tuesday by default
  val selectedPickupDay: StateFlow<Int> = _selectedPickupDay.asStateFlow()

  private val _selectedPickupHour = MutableStateFlow(10) // 10:00 AM by default
  val selectedPickupHour: StateFlow<Int> = _selectedPickupHour.asStateFlow()

  private val _pickupScheduleWarning = MutableStateFlow<String?>(null)
  val pickupScheduleWarning: StateFlow<String?> = _pickupScheduleWarning.asStateFlow()

  // Active AI Voice Playing item
  private val _activeVoicePlayingId = MutableStateFlow<String?>(null)
  val activeVoicePlayingId: StateFlow<String?> = _activeVoicePlayingId.asStateFlow()

  val isSpeaking = voiceSpeaker.isSpeaking

  init {
    validatePickupSchedule(_selectedPickupDay.value, _selectedPickupHour.value)
    // Initialize viewing first sample order for document screen
    _viewingOrder.value = repository.orders.value.firstOrNull()
  }

  fun navigateTo(dest: AppNavDestination) {
    _currentDestination.value = dest
  }

  fun selectProduct(product: PaintProduct) {
    _selectedProduct.value = product
  }

  fun selectSize(size: PaintSize) {
    _selectedSize.value = size
  }

  fun selectColor(color: ColorItem) {
    _selectedColor.value = color
  }

  fun viewOrderDocs(order: Order) {
    _viewingOrder.value = order
    _currentDestination.value = AppNavDestination.INVOICES
  }

  fun addToCart(product: PaintProduct, size: PaintSize, color: ColorItem, qty: Int = 1) {
    repository.addToCart(product, size, color, qty)
  }

  // AI Voice controls
  fun playVoiceAdvice(issue: PaintingIssue) {
    _activeVoicePlayingId.value = issue.id
    voiceSpeaker.speak(issue.audioExplanation)
  }

  fun playVoiceLesson(lesson: VoiceLesson) {
    _activeVoicePlayingId.value = lesson.id
    voiceSpeaker.speak(lesson.spokenScript)
  }

  fun playAdJingle(ad: AdvertisementItem) {
    _activeVoicePlayingId.value = ad.id
    voiceSpeaker.speak(ad.scriptContent)
  }

  fun stopVoice() {
    _activeVoicePlayingId.value = null
    voiceSpeaker.stop()
  }

  // Calculator logic
  fun updateCalcWallLength(v: String) { _calcWallLengthMeters.value = v }
  fun updateCalcWallHeight(v: String) { _calcWallHeightMeters.value = v }
  fun updateCalcCoats(c: Int) { _calcCoats.value = c }
  fun updateCalcSurfaceType(t: String) { _calcSurfaceType.value = t }
  fun updateCalcProduct(p: PaintProduct) { _calcSelectedProduct.value = p }

  fun calculateCoverage(): PaintCoverageResult {
    val length = _calcWallLengthMeters.value.toDoubleOrNull() ?: 10.0
    val height = _calcWallHeightMeters.value.toDoubleOrNull() ?: 3.0
    val area = length * height
    val coats = _calcCoats.value

    // Absorption factor based on surface
    val surfaceFactor = when (_calcSurfaceType.value) {
      "Raw Concrete / Porous Brick" -> 1.45
      "Rough Texcoat / Heavy Texture" -> 1.80
      "Plastered Wall (Unprimed)" -> 1.25
      else -> 1.0 // Screeded or previously painted
    }

    val prod = _calcSelectedProduct.value
    val litersPerCoat = area / prod.coverageSqmPerLiter
    val totalLiters = litersPerCoat * coats * surfaceFactor

    // Determine bucket distribution (20L buckets, then 10L, then 4L)
    var remaining = totalLiters
    val b20 = (remaining / 20.0).toInt()
    remaining -= b20 * 20.0
    val b10 = (remaining / 10.0).toInt()
    remaining -= b10 * 10.0
    val c4 = if (remaining > 0) Math.ceil(remaining / 4.0).toInt() else 0

    val cost = (b20 * prod.getPrice(PaintSize.SIZE_20L)) +
               (b10 * prod.getPrice(PaintSize.SIZE_10L)) +
               (c4 * prod.getPrice(PaintSize.SIZE_4L))

    return PaintCoverageResult(
      surfaceAreaSqm = area,
      paintNeededLiters = totalLiters,
      recommendedBuckets20L = b20,
      recommendedBuckets10L = b10,
      recommendedCans4L = c4,
      estimatedCostNaira = cost
    )
  }

  // Schedule pickup validation: Tuesday to Saturday, 9am to 5pm
  fun updatePickupDay(dayOfWeek: Int) {
    _selectedPickupDay.value = dayOfWeek
    validatePickupSchedule(dayOfWeek, _selectedPickupHour.value)
  }

  fun updatePickupHour(hour: Int) {
    _selectedPickupHour.value = hour
    validatePickupSchedule(_selectedPickupDay.value, hour)
  }

  private fun validatePickupSchedule(dayOfWeek: Int, hour: Int) {
    val isOperating = repository.isWithinOperatingHours(dayOfWeek, hour)
    if (!isOperating) {
      val dayName = when (dayOfWeek) {
        Calendar.SUNDAY -> "Sunday (Closed)"
        Calendar.MONDAY -> "Monday (Closed)"
        Calendar.TUESDAY -> "Tuesday"
        Calendar.WEDNESDAY -> "Wednesday"
        Calendar.THURSDAY -> "Thursday"
        Calendar.FRIDAY -> "Friday"
        Calendar.SATURDAY -> "Saturday"
        else -> "Selected Day"
      }
      _pickupScheduleWarning.value = "Warning: Sharon Depots are strictly open Tuesday – Saturday, 9:00 AM – 5:00 PM. $dayName at ${formatHour(hour)} is outside factory working hours."
    } else {
      _pickupScheduleWarning.value = null
    }
  }

  private fun formatHour(hour: Int): String {
    return when {
      hour == 12 -> "12:00 PM"
      hour > 12 -> "${hour - 12}:00 PM"
      else -> "$hour:00 AM"
    }
  }

  // Search by tracking code
  fun updateSearchCode(code: String) {
    _searchTrackingCode.value = code
    val clean = code.trim()
    _searchedOrder.value = repository.orders.value.find { it.trackingCode.contains(clean, ignoreCase = true) || it.id.contains(clean, ignoreCase = true) }
  }

  // Admin login
  fun updateAdminCode(code: String) {
    _adminCodeInput.value = code
    _adminLoginError.value = null
  }

  fun submitAdminLogin() {
    val admin = repository.loginAdmin(_adminCodeInput.value)
    if (admin == null) {
      _adminLoginError.value = "Invalid Confirmation Code. Valid codes: SHP-ADA-0816 or SHP-ANI-0810"
    } else {
      _adminLoginError.value = null
      _adminCodeInput.value = ""
    }
  }

  fun logoutAdmin() {
    repository.logoutAdmin()
  }

  // Dynamic Price List Updating by Admin
  fun updateProductPrice(productId: String, size: PaintSize, newPrice: Long) {
    repository.updateProductPrice(productId, size, newPrice)
  }

  fun uploadAndSyncPriceList(notes: String) {
    repository.uploadAndSyncPriceList(notes)
  }

  // Customer OTP
  fun updateOtpInput(code: String) {
    _otpInput.value = code
  }

  fun verifyOtp() {
    val ok = repository.verifyOtp(_otpInput.value)
    if (ok) {
      _otpSuccessMessage.value = "Authentication Successful! Account verified via OTP."
      _otpInput.value = ""
    } else {
      _otpSuccessMessage.value = "Please enter a valid 6-digit verification code."
    }
  }

  override fun onCleared() {
    super.onCleared()
    voiceSpeaker.shutdown()
  }
}
