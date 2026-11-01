package com.example.data.model

data class CompanyAdmin(
  val id: String,
  val name: String,
  val role: String,
  val confirmationCode: String,
  val email: String,
  val phone: String
)

data class StockItem(
  val productId: String,
  val productName: String,
  val size: PaintSize,
  val quantity: Int,
  val lowStockThreshold: Int = 20,
  val unitPrice: Long,
  val depotLocation: String = "9th Mile Central Factory"
) {
  val totalValuation: Long
    get() = quantity * unitPrice
  val isLowStock: Boolean
    get() = quantity <= lowStockThreshold
}

enum class AuditReportPeriod(val title: String) {
  MONTHLY("Monthly Stock & Sales Audit"),
  YEARLY("Annual Comprehensive Sharon Inventory & Financial Receipt")
}

data class StockAuditReceipt(
  val receiptId: String,
  val period: AuditReportPeriod,
  val periodName: String,
  val generatedDate: String,
  val generatedByAdmin: String,
  val totalUnitsManufactured: Int,
  val totalUnitsSold: Int,
  val currentUnitsInStock: Int,
  val inventoryValueNaira: Long,
  val totalSalesRevenueNaira: Long,
  val accessBankApprovedTransfers: Long,
  val complianceStamp: String = "NIS CT-1620 / SON / MANCAP CERTIFIED"
)
