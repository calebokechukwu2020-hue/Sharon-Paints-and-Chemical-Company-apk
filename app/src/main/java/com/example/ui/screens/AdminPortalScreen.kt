package com.example.ui.screens

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.AuditReportPeriod
import com.example.data.model.OrderStatus
import com.example.data.model.PaintSize
import com.example.ui.SharonViewModel
import com.example.ui.theme.SharonBluePrimary
import com.example.ui.theme.SharonCrimson
import com.example.ui.theme.SharonGold
import com.example.ui.theme.SharonGreen
import java.text.NumberFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminPortalScreen(
  viewModel: SharonViewModel,
  modifier: Modifier = Modifier
) {
  val context = LocalContext.current
  val activeAdmin by viewModel.repository.activeAdmin.collectAsState()
  val adminCodeInput by viewModel.adminCodeInput.collectAsState()
  val loginError by viewModel.adminLoginError.collectAsState()

  val orders by viewModel.repository.orders.collectAsState()
  val stockItems by viewModel.repository.stockItems.collectAsState()
  val auditReceipts by viewModel.repository.auditReceipts.collectAsState()
  val products by viewModel.repository.productsFlow.collectAsState()
  val priceRevisionNotes by viewModel.repository.priceListRevisionNotes.collectAsState()

  var adminTab by remember { mutableStateOf(0) } // 0 = Payment Approval, 1 = Price List & Upload, 2 = Stock Manager, 3 = Audit Receipts
  val currencyFormat = remember { NumberFormat.getNumberInstance(Locale.US) }

  var editingProduct by remember { mutableStateOf<com.example.data.model.PaintProduct?>(null) }
  var editingSize by remember { mutableStateOf<PaintSize?>(null) }
  var editPriceInput by remember { mutableStateOf("") }

  var showUploadPriceListDialog by remember { mutableStateOf(false) }
  var uploadScheduleInput by remember { mutableStateOf("") }

  Scaffold(
    topBar = {
      TopAppBar(
        title = {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.AdminPanelSettings, contentDescription = null, tint = SharonBluePrimary)
            Spacer(modifier = Modifier.width(8.dp))
            Column {
              Text(
                text = "Sharon Executive Admin Portal",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = SharonBluePrimary
              )
              Text(
                text = "Secure Two-Admin Access & Live Inventory AI Manager",
                style = MaterialTheme.typography.bodySmall.copy(fontSize = 10.sp, color = Color.Gray)
              )
            }
          }
        },
        actions = {
          if (activeAdmin != null) {
            IconButton(
              onClick = {
                viewModel.logoutAdmin()
                Toast.makeText(context, "Logged out from Admin portal", Toast.LENGTH_SHORT).show()
              },
              modifier = Modifier.testTag("admin_logout_btn")
            ) {
              Icon(Icons.Default.Logout, contentDescription = "Logout", tint = SharonCrimson)
            }
          }
        },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
      )
    }
  ) { innerPadding ->
    Column(
      modifier = modifier
        .fillMaxSize()
        .padding(innerPadding)
        .padding(horizontal = 14.dp)
    ) {
      if (activeAdmin == null) {
        // ADMIN LOGIN FORM WITH TWO DESIGNATED ADMINS & CONFIRMATION CODES
        LazyColumn(
          modifier = Modifier.fillMaxSize(),
          verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
          item {
            Card(
              modifier = Modifier.fillMaxWidth().testTag("admin_login_card"),
              colors = CardDefaults.cardColors(containerColor = Color.White),
              shape = RoundedCornerShape(14.dp),
              border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(Color(0xFFCBD5E1)))
            ) {
              Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text(
                  text = "Company Admin Security Verification",
                  style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, color = SharonBluePrimary)
                )
                Text(
                  text = "Restricted to the two authorized executive directors from Sharon Paints & Chemical Co. Nig. Ltd. Input your designated confirmation code.",
                  style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp, color = Color.Gray)
                )

                OutlinedTextField(
                  value = adminCodeInput,
                  onValueChange = { viewModel.updateAdminCode(it) },
                  label = { Text("Enter Admin Confirmation Code") },
                  placeholder = { Text("e.g. SHP-ADA-0816 or SHP-ANI-0810") },
                  modifier = Modifier.fillMaxWidth().testTag("admin_code_input"),
                  singleLine = true
                )

                loginError?.let { err ->
                  Text(err, color = SharonCrimson, fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
                }

                Button(
                  onClick = { viewModel.submitAdminLogin() },
                  modifier = Modifier.fillMaxWidth().testTag("admin_login_submit_btn"),
                  colors = ButtonDefaults.buttonColors(containerColor = SharonBluePrimary),
                  shape = RoundedCornerShape(10.dp)
                ) {
                  Icon(Icons.Default.LockOpen, contentDescription = null, modifier = Modifier.size(18.dp))
                  Spacer(modifier = Modifier.width(6.dp))
                  Text("Verify & Access Admin Portal", fontWeight = FontWeight.Bold)
                }
              }
            }
          }

          // Pre-configured Admins Credentials Reference Card
          item {
            Card(
              modifier = Modifier.fillMaxWidth(),
              colors = CardDefaults.cardColors(containerColor = Color(0xFFF8FAFC)),
              shape = RoundedCornerShape(12.dp),
              border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(Color(0xFFE2E8F0)))
            ) {
              Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Text(
                  text = "Two Authorized Company Admins:",
                  style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold, color = SharonBluePrimary)
                )

                viewModel.repository.companyAdmins.forEach { adm ->
                  Box(
                    modifier = Modifier
                      .fillMaxWidth()
                      .clip(RoundedCornerShape(8.dp))
                      .background(Color.White)
                      .border(1.dp, Color(0xFFE2E8F0), RoundedCornerShape(8.dp))
                      .padding(8.dp)
                  ) {
                    Row(
                      modifier = Modifier.fillMaxWidth(),
                      horizontalArrangement = Arrangement.SpaceBetween,
                      verticalAlignment = Alignment.CenterVertically
                    ) {
                      Column {
                        Text(adm.name, fontWeight = FontWeight.Bold, fontSize = 11.sp, color = SharonBluePrimary)
                        Text(adm.role, fontSize = 10.sp, color = Color.Gray)
                        Text("Phone: ${adm.phone}", fontSize = 10.sp, color = Color(0xFF475569))
                      }
                      Column(horizontalAlignment = Alignment.End) {
                        Text("Confirmation Code:", fontSize = 9.sp, color = Color.Gray)
                        Text(adm.confirmationCode, fontWeight = FontWeight.ExtraBold, fontSize = 12.sp, color = SharonCrimson)
                        OutlinedButton(
                          onClick = {
                            viewModel.updateAdminCode(adm.confirmationCode)
                            viewModel.submitAdminLogin()
                          },
                          contentPadding = PaddingValues(horizontal = 6.dp, vertical = 2.dp),
                          modifier = Modifier.padding(top = 2.dp)
                        ) {
                          Text("Quick Login", fontSize = 9.sp)
                        }
                      }
                    }
                  }
                }
              }
            }
          }
        }
      } else {
        // LOGGED-IN ADMIN DASHBOARD
        val currentAdmin = activeAdmin!!

        // Logged in Header
        Card(
          modifier = Modifier.fillMaxWidth().testTag("active_admin_header"),
          colors = CardDefaults.cardColors(containerColor = SharonBluePrimary),
          shape = RoundedCornerShape(12.dp)
        ) {
          Row(
            modifier = Modifier.fillMaxWidth().padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Column {
              Text("Logged In: ${currentAdmin.name}", fontWeight = FontWeight.Bold, color = Color.White, fontSize = 12.sp)
              Text("${currentAdmin.role} (${currentAdmin.confirmationCode})", color = SharonGold, fontSize = 10.sp)
            }
            Badge(containerColor = SharonGreen) {
              Text("Authenticated", color = Color.White, fontSize = 10.sp)
            }
          }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Admin Tabs: 0 = Approvals, 1 = Price List & Upload, 2 = Stock Manager, 3 = Audit Receipts
        TabRow(
          selectedTabIndex = adminTab,
          containerColor = Color.White,
          contentColor = SharonBluePrimary,
          modifier = Modifier.clip(RoundedCornerShape(12.dp))
        ) {
          Tab(
            selected = adminTab == 0,
            onClick = { adminTab = 0 },
            text = { Text("Approvals", fontSize = 10.sp, fontWeight = FontWeight.Bold) },
            modifier = Modifier.testTag("admin_tab_approvals")
          )
          Tab(
            selected = adminTab == 1,
            onClick = { adminTab = 1 },
            text = { Text("Price List", fontSize = 10.sp, fontWeight = FontWeight.Bold) },
            modifier = Modifier.testTag("admin_tab_pricelist")
          )
          Tab(
            selected = adminTab == 2,
            onClick = { adminTab = 2 },
            text = { Text("Stock Manager", fontSize = 10.sp, fontWeight = FontWeight.Bold) },
            modifier = Modifier.testTag("admin_tab_stock")
          )
          Tab(
            selected = adminTab == 3,
            onClick = { adminTab = 3 },
            text = { Text("Audit Receipts", fontSize = 10.sp, fontWeight = FontWeight.Bold) },
            modifier = Modifier.testTag("admin_tab_audit")
          )
        }

        Spacer(modifier = Modifier.height(10.dp))

        if (adminTab == 0) {
          // PAYMENT APPROVAL QUEUE
          LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            contentPadding = PaddingValues(bottom = 24.dp)
          ) {
            item {
              Box(
                modifier = Modifier
                  .fillMaxWidth()
                  .clip(RoundedCornerShape(8.dp))
                  .background(Color(0xFFEFF6FF))
                  .padding(10.dp)
              ) {
                Text(
                  text = "Payment Approvals: Verify that transfers reflect in Sharon Paints Access Bank Account 0104392154 before clicking Approve.",
                  fontSize = 11.sp,
                  color = SharonBluePrimary,
                  fontWeight = FontWeight.SemiBold
                )
              }
            }

            items(orders, key = { it.id }) { ord ->
              Card(
                modifier = Modifier.fillMaxWidth().testTag("admin_order_card_${ord.id}"),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(12.dp),
                border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(Color(0xFFE2E8F0)))
              ) {
                Column(modifier = Modifier.padding(12.dp)) {
                  Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                  ) {
                    Text(ord.id, fontWeight = FontWeight.Bold, color = SharonBluePrimary)
                    Text("₦${currencyFormat.format(ord.totalAmount)}", fontWeight = FontWeight.ExtraBold, color = SharonCrimson)
                  }
                  Text("Customer: ${ord.customer.businessName} (${ord.customer.contactPerson})", fontSize = 11.sp, color = Color(0xFF334155))
                  Text("Access Bank Ref: ${ord.paymentReference.ifBlank { "Awaiting Ref" }}", fontSize = 11.sp, color = Color(0xFF0284C7), fontWeight = FontWeight.Bold)
                  Text("Scheduled Pickup: ${ord.scheduledPickupDate} at ${ord.scheduledPickupTime}", fontSize = 10.sp, color = Color.Gray)
                  Text("Current Status: ${ord.status.display}", fontSize = 10.sp, color = if (ord.status == OrderStatus.APPROVED_FOR_PICKUP) SharonGreen else SharonGold, fontWeight = FontWeight.Bold)

                  Spacer(modifier = Modifier.height(8.dp))

                  if (ord.status != OrderStatus.APPROVED_FOR_PICKUP) {
                    Button(
                      onClick = {
                        val ok = viewModel.repository.approveOrderPayment(ord.id, "${currentAdmin.name} (${currentAdmin.confirmationCode})")
                        if (ok) {
                          Toast.makeText(context, "Payment for ${ord.id} Approved in Access Bank!", Toast.LENGTH_SHORT).show()
                        }
                      },
                      modifier = Modifier.fillMaxWidth().testTag("approve_payment_btn_${ord.id}"),
                      colors = ButtonDefaults.buttonColors(containerColor = SharonGreen),
                      shape = RoundedCornerShape(8.dp)
                    ) {
                      Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(16.dp))
                      Spacer(modifier = Modifier.width(6.dp))
                      Text("Approve Access Bank Payment", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                  } else {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                      Icon(Icons.Default.Verified, contentDescription = null, tint = SharonGreen, modifier = Modifier.size(16.dp))
                      Spacer(modifier = Modifier.width(4.dp))
                      Text("Approved by ${ord.approvedByAdmin}", fontSize = 10.sp, color = SharonGreen, fontWeight = FontWeight.Bold)
                    }
                  }
                }
              }
            }
          }
        } else if (adminTab == 1) {
          // LIVE FACTORY PRICE LIST & PERIODIC DOCUMENT UPLOAD BY ADMIN
          LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            contentPadding = PaddingValues(bottom = 24.dp)
          ) {
            item {
              Card(
                colors = CardDefaults.cardColors(containerColor = Color(0xFFEFF6FF)),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
              ) {
                Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                  Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                  ) {
                    Text("Factory Price Schedule Control", fontWeight = FontWeight.Bold, color = SharonBluePrimary, fontSize = 13.sp)
                    Badge(containerColor = SharonGreen) {
                      Text("Live Sync", color = Color.White, fontSize = 9.sp)
                    }
                  }
                  Text(
                    text = priceRevisionNotes,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF334155)
                  )
                  Text(
                    text = "As authorized Sharon company admin (${currentAdmin.name}), you can edit individual rates or upload and synchronize updated factory price lists periodically across all customer apps.",
                    fontSize = 10.sp,
                    color = Color.Gray
                  )
                  Button(
                    onClick = {
                      uploadScheduleInput = "Factory Schedule - Effective ${java.text.SimpleDateFormat("dd MMM yyyy", Locale.US).format(java.util.Date())}"
                      showUploadPriceListDialog = true
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = SharonBluePrimary),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.fillMaxWidth().testTag("upload_pricelist_doc_btn")
                  ) {
                    Icon(Icons.Default.CloudUpload, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Upload & Sync New Price List Document", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                  }
                }
              }
            }

            items(products, key = { it.id }) { prod ->
              Card(
                modifier = Modifier.fillMaxWidth().testTag("admin_price_card_${prod.id}"),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(10.dp),
                border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(Color(0xFFE2E8F0)))
              ) {
                Column(modifier = Modifier.padding(12.dp)) {
                  Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                  ) {
                    Text(
                      text = prod.uppercaseTitle,
                      fontWeight = FontWeight.ExtraBold,
                      fontSize = 12.sp,
                      color = SharonBluePrimary
                    )
                    Text(
                      text = prod.category.title,
                      fontSize = 10.sp,
                      color = Color.Gray
                    )
                  }
                  Spacer(modifier = Modifier.height(6.dp))

                  prod.availableSizes.forEach { size ->
                    Row(
                      modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 3.dp),
                      horizontalArrangement = Arrangement.SpaceBetween,
                      verticalAlignment = Alignment.CenterVertically
                    ) {
                      Column {
                        Text(size.label, fontWeight = FontWeight.Bold, fontSize = 11.sp, color = Color(0xFF334155))
                        Text("₦${currencyFormat.format(prod.getPrice(size))}", fontWeight = FontWeight.ExtraBold, color = SharonCrimson, fontSize = 11.sp)
                      }
                      OutlinedButton(
                        onClick = {
                          editingProduct = prod
                          editingSize = size
                          editPriceInput = prod.getPrice(size).toString()
                        },
                        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp),
                        modifier = Modifier.testTag("edit_price_${prod.id}_${size.name}")
                      ) {
                        Icon(Icons.Default.Edit, contentDescription = null, modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Edit Price", fontSize = 10.sp)
                      }
                    }
                  }
                }
              }
            }
          }
        } else if (adminTab == 2) {
          // LIVE STOCK RECORDS AI MANAGER
          LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            contentPadding = PaddingValues(bottom = 24.dp)
          ) {
            item {
              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
              ) {
                Column {
                  Text("Live Product Inventory", fontWeight = FontWeight.Bold, color = SharonBluePrimary)
                  Text("Total Products: ${stockItems.size} SKUs", fontSize = 10.sp, color = Color.Gray)
                }
                Text("Factory: 9th Mile", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = SharonGreen)
              }
            }

            items(stockItems) { item ->
              Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(10.dp),
                border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(Color(0xFFE2E8F0)))
              ) {
                Row(
                  modifier = Modifier.fillMaxWidth().padding(10.dp),
                  horizontalArrangement = Arrangement.SpaceBetween,
                  verticalAlignment = Alignment.CenterVertically
                ) {
                  Column(modifier = Modifier.weight(1f)) {
                    Text("${item.productName} (${item.size.label})", fontWeight = FontWeight.Bold, fontSize = 11.sp, color = SharonBluePrimary)
                    Text("Unit Price: ₦${currencyFormat.format(item.unitPrice)} | Value: ₦${currencyFormat.format(item.totalValuation)}", fontSize = 10.sp, color = Color.Gray)
                    if (item.isLowStock) {
                      Text("LOW STOCK ALERT (< ${item.lowStockThreshold} units)", fontSize = 9.sp, color = SharonCrimson, fontWeight = FontWeight.Bold)
                    }
                  }

                  // Quantity editor
                  Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(
                      onClick = {
                        if (item.quantity > 0) {
                          viewModel.repository.updateStockQuantity(item.productId, item.size, item.quantity - 10)
                        }
                      },
                      modifier = Modifier.size(28.dp).background(Color(0xFFF1F5F9), RoundedCornerShape(4.dp))
                    ) {
                      Text("-10", fontSize = 9.sp, fontWeight = FontWeight.Bold)
                    }
                    Text(
                      text = "${item.quantity}",
                      modifier = Modifier.padding(horizontal = 8.dp),
                      fontWeight = FontWeight.ExtraBold,
                      fontSize = 12.sp,
                      color = SharonBluePrimary
                    )
                    IconButton(
                      onClick = {
                        viewModel.repository.updateStockQuantity(item.productId, item.size, item.quantity + 20)
                      },
                      modifier = Modifier.size(28.dp).background(Color(0xFFF1F5F9), RoundedCornerShape(4.dp))
                    ) {
                      Text("+20", fontSize = 9.sp, fontWeight = FontWeight.Bold)
                    }
                  }
                }
              }
            }
          }
        } else {
          // AUDIT RECEIPTS (Monthly or Yearly)
          LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            contentPadding = PaddingValues(bottom = 24.dp)
          ) {
            item {
              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
              ) {
                Button(
                  onClick = {
                    val r = viewModel.repository.generateAuditReceipt(AuditReportPeriod.MONTHLY, "${currentAdmin.name} (${currentAdmin.confirmationCode})")
                    Toast.makeText(context, "Monthly Audit Receipt Generated (${r.receiptId})", Toast.LENGTH_SHORT).show()
                  },
                  modifier = Modifier.weight(1f).testTag("gen_monthly_audit_btn"),
                  colors = ButtonDefaults.buttonColors(containerColor = SharonBluePrimary),
                  shape = RoundedCornerShape(8.dp)
                ) {
                  Icon(Icons.Default.DateRange, contentDescription = null, modifier = Modifier.size(16.dp))
                  Spacer(modifier = Modifier.width(4.dp))
                  Text("Generate Monthly Receipt", fontSize = 10.sp)
                }

                Button(
                  onClick = {
                    val r = viewModel.repository.generateAuditReceipt(AuditReportPeriod.YEARLY, "${currentAdmin.name} (${currentAdmin.confirmationCode})")
                    Toast.makeText(context, "Yearly Audit Receipt Generated (${r.receiptId})", Toast.LENGTH_SHORT).show()
                  },
                  modifier = Modifier.weight(1f).testTag("gen_yearly_audit_btn"),
                  colors = ButtonDefaults.buttonColors(containerColor = SharonCrimson),
                  shape = RoundedCornerShape(8.dp)
                ) {
                  Icon(Icons.Default.Assessment, contentDescription = null, modifier = Modifier.size(16.dp))
                  Spacer(modifier = Modifier.width(4.dp))
                  Text("Generate Yearly Receipt", fontSize = 10.sp)
                }
              }
            }

            item {
              Text(
                text = "Official Sharon Stock & Financial Audit Receipts",
                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                color = SharonBluePrimary
              )
            }

            items(auditReceipts, key = { it.receiptId }) { receipt ->
              Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(12.dp),
                border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(Color(0xFFCBD5E1)))
              ) {
                Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                  Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                  ) {
                    Text(receipt.periodName, fontWeight = FontWeight.Bold, fontSize = 12.sp, color = SharonBluePrimary)
                    Badge(containerColor = if (receipt.period == AuditReportPeriod.MONTHLY) SharonBluePrimary else SharonCrimson) {
                      Text(receipt.period.name, color = Color.White, fontSize = 9.sp)
                    }
                  }
                  Text("Receipt ID: ${receipt.receiptId} | Date: ${receipt.generatedDate}", fontSize = 10.sp, color = Color.Gray)
                  Text("Generated By: ${receipt.generatedByAdmin}", fontSize = 10.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF334155))

                  Divider(modifier = Modifier.padding(vertical = 4.dp))

                  Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("Total Units Manufactured:", fontSize = 10.sp)
                    Text("${receipt.totalUnitsManufactured} units", fontWeight = FontWeight.Bold, fontSize = 10.sp)
                  }
                  Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("Current Units In Stock:", fontSize = 10.sp)
                    Text("${receipt.currentUnitsInStock} units", fontWeight = FontWeight.Bold, fontSize = 10.sp)
                  }
                  Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("Current Stock Valuation:", fontSize = 10.sp)
                    Text("₦${currencyFormat.format(receipt.inventoryValueNaira)}", fontWeight = FontWeight.Bold, fontSize = 10.sp, color = SharonBluePrimary)
                  }
                  Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("Access Bank Approved Transfers:", fontSize = 10.sp)
                    Text("₦${currencyFormat.format(receipt.accessBankApprovedTransfers)}", fontWeight = FontWeight.ExtraBold, fontSize = 11.sp, color = SharonGreen)
                  }

                  Text("Compliance: ${receipt.complianceStamp}", fontSize = 9.sp, color = SharonGreen, fontWeight = FontWeight.Bold)
                }
              }
            }
          }
        }
      }
    }
  }

  // Dialog to edit individual product price by Admin
  if (editingProduct != null && editingSize != null) {
    val prod = editingProduct!!
    val size = editingSize!!
    AlertDialog(
      onDismissRequest = {
        editingProduct = null
        editingSize = null
      },
      title = {
        Text("Edit Price: ${prod.uppercaseTitle} (${size.label})", fontWeight = FontWeight.Bold, color = SharonBluePrimary)
      },
      text = {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
          Text("Enter new official factory price in Nigerian Naira (₦):", fontSize = 11.sp, color = Color.Gray)
          OutlinedTextField(
            value = editPriceInput,
            onValueChange = { editPriceInput = it },
            label = { Text("Price in Naira (₦)") },
            prefix = { Text("₦ ") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth().testTag("edit_price_input")
          )
        }
      },
      confirmButton = {
        Button(
          onClick = {
            val newP = editPriceInput.toLongOrNull()
            if (newP != null && newP > 0) {
              viewModel.updateProductPrice(prod.id, size, newP)
              Toast.makeText(context, "${prod.name} (${size.label}) updated to ₦${currencyFormat.format(newP)}", Toast.LENGTH_SHORT).show()
              editingProduct = null
              editingSize = null
            } else {
              Toast.makeText(context, "Please enter a valid price amount", Toast.LENGTH_SHORT).show()
            }
          },
          colors = ButtonDefaults.buttonColors(containerColor = SharonBluePrimary),
          modifier = Modifier.testTag("save_price_btn")
        ) {
          Text("Update & Publish")
        }
      },
      dismissButton = {
        TextButton(onClick = {
          editingProduct = null
          editingSize = null
        }) {
          Text("Cancel")
        }
      }
    )
  }

  // Dialog to upload and sync new Price List Document periodically
  if (showUploadPriceListDialog) {
    AlertDialog(
      onDismissRequest = { showUploadPriceListDialog = false },
      title = {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Icon(Icons.Default.UploadFile, contentDescription = null, tint = SharonBluePrimary)
          Spacer(modifier = Modifier.width(8.dp))
          Text("Upload & Synchronize Price List", fontWeight = FontWeight.Bold, color = SharonBluePrimary)
        }
      },
      text = {
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
          Text(
            text = "Upload the latest official Sharon factory price schedule document to synchronize across all branch depots and customer apps.",
            fontSize = 11.sp,
            color = Color(0xFF334155)
          )
          OutlinedTextField(
            value = uploadScheduleInput,
            onValueChange = { uploadScheduleInput = it },
            label = { Text("Price Schedule Revision Title") },
            placeholder = { Text("e.g. Factory Schedule - Effective Nov 2026") },
            modifier = Modifier.fillMaxWidth().testTag("upload_schedule_input"),
            singleLine = true
          )
          Box(
            modifier = Modifier
              .fillMaxWidth()
              .clip(RoundedCornerShape(8.dp))
              .background(Color(0xFFF1F5F9))
              .border(1.dp, Color(0xFFCBD5E1), RoundedCornerShape(8.dp))
              .padding(10.dp)
          ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Icon(Icons.Default.AttachFile, contentDescription = null, tint = SharonBluePrimary, modifier = Modifier.size(18.dp))
              Spacer(modifier = Modifier.width(6.dp))
              Text("Attached: sharon_factory_price_list_2026.pdf", fontSize = 10.sp, color = Color(0xFF334155), fontWeight = FontWeight.Bold)
            }
          }
        }
      },
      confirmButton = {
        Button(
          onClick = {
            if (uploadScheduleInput.isNotBlank()) {
              viewModel.uploadAndSyncPriceList(uploadScheduleInput.trim())
              Toast.makeText(context, "Price List Document Uploaded and Synchronized!", Toast.LENGTH_LONG).show()
              showUploadPriceListDialog = false
            }
          },
          colors = ButtonDefaults.buttonColors(containerColor = SharonGreen),
          modifier = Modifier.testTag("confirm_upload_doc_btn")
        ) {
          Text("Upload & Sync")
        }
      },
      dismissButton = {
        TextButton(onClick = { showUploadPriceListDialog = false }) {
          Text("Cancel")
        }
      }
    )
  }
}
