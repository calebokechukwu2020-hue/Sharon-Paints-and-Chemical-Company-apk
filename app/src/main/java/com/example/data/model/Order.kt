package com.example.data.model

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class CustomerProfile(
  val businessName: String,
  val contactPerson: String,
  val email: String,
  val phone: String,
  val shopAddress: String,
  val isOtpVerified: Boolean = false,
  val isBiometricEnabled: Boolean = false,
  val securityRegistrationCode: String = ""
)

data class CartItem(
  val product: PaintProduct,
  val size: PaintSize,
  val color: ColorItem,
  val quantity: Int
) {
  val subtotal: Long
    get() = product.getPrice(size) * quantity
}

enum class OrderStatus(val display: String) {
  AWAITING_PAYMENT("Awaiting Access Bank Payment"),
  PAYMENT_SUBMITTED("Payment Submitted - Verifying"),
  APPROVED_FOR_PICKUP("Approved - Ready For Pickup"),
  DISPATCHED_WAYBILL("Waybill Issued - In Transit"),
  COMPLETED("Completed & Received")
}

data class Order(
  val id: String,
  val trackingCode: String, // e.g. SHP-TRK-88219
  val customer: CustomerProfile,
  val items: List<CartItem>,
  val targetDepotId: String,
  val scheduledPickupDate: String, // "Tuesday, Nov 03, 2026"
  val scheduledPickupTime: String, // "10:30 AM"
  val isWithinWorkingHours: Boolean = true,
  val totalAmount: Long,
  val paymentReference: String = "",
  val bankName: String = "Access Bank PLC",
  val accountNumber: String = "0104392154",
  val accountName: String = "Sharon paints and chemical company",
  val status: OrderStatus = OrderStatus.AWAITING_PAYMENT,
  val approvedByAdmin: String = "",
  val createdAt: Long = System.currentTimeMillis(),
  val waybillNumber: String = "SDN-${(20000..99999).random()}",
  val invoiceNumber: String = "SHP-INV-${(1000..9999).random()}",
  val antiFraudVerificationHash: String = ""
) {
  val formattedDate: String
    get() {
      val sdf = SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault())
      return sdf.format(Date(createdAt))
    }
}
