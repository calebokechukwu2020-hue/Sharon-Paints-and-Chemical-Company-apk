package com.example.data.model

data class Depot(
  val id: String,
  val name: String,
  val townState: String,
  val address: String,
  val managerName: String,
  val managerRole: String,
  val phone: String,
  val whatsappNumber: String,
  val latitude: Double,
  val longitude: Double,
  val workingDays: String = "Tuesday – Saturday",
  val workingHours: String = "9:00 AM – 5:00 PM",
  val isHeadquarters: Boolean = false
) {
  val cleanPhone: String
    get() = phone.replace(" ", "").replace("-", "")
  val cleanWhatsapp: String
    get() = whatsappNumber.replace(" ", "").replace("-", "").replace("+", "")
}
