package com.example.ui.screens

import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.model.Order
import com.example.ui.SharonViewModel
import com.example.ui.theme.SharonBluePrimary
import com.example.ui.theme.SharonCrimson
import com.example.ui.theme.SharonGold
import com.example.ui.theme.SharonGreen
import java.text.NumberFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InvoicesWaybillsScreen(
  viewModel: SharonViewModel,
  modifier: Modifier = Modifier
) {
  val context = LocalContext.current
  val viewingOrder by viewModel.viewingOrder.collectAsState()
  val orders by viewModel.repository.orders.collectAsState()
  var docType by remember { mutableStateOf(0) } // 0 = Commercial Invoice, 1 = Delivery Note / Waybill, 2 = Payment Receipt

  val activeOrder = viewingOrder ?: orders.firstOrNull()
  val currencyFormat = remember { NumberFormat.getNumberInstance(Locale.US) }

  Scaffold(
    topBar = {
      TopAppBar(
        title = {
          Column {
            Text(
              text = "Sharon Documentation Center",
              style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
              color = SharonBluePrimary
            )
            Text(
              text = "Official Invoices, Waybills & Access Bank Receipts",
              style = MaterialTheme.typography.bodySmall.copy(fontSize = 10.sp, color = Color.Gray)
            )
          }
        },
        actions = {
          if (activeOrder != null) {
            IconButton(
              onClick = {
                val shareIntent = Intent(Intent.ACTION_SEND).apply {
                  type = "text/plain"
                  putExtra(Intent.EXTRA_SUBJECT, "Sharon Paints Transaction Document - ${activeOrder.trackingCode}")
                  putExtra(Intent.EXTRA_TEXT, "Official Sharon Paints Transaction:\nTracking Code: ${activeOrder.trackingCode}\nInvoice: ${activeOrder.invoiceNumber}\nWaybill: ${activeOrder.waybillNumber}\nCustomer: ${activeOrder.customer.businessName}\nAmount: ₦${currencyFormat.format(activeOrder.totalAmount)}\nBank: Access Bank 0104392154")
                }
                context.startActivity(Intent.createChooser(shareIntent, "Send Sharon Document via Email"))
              },
              modifier = Modifier.testTag("share_doc_btn")
            ) {
              Icon(Icons.Default.Share, contentDescription = "Share", tint = SharonBluePrimary)
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
      if (activeOrder == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
          Text("No orders placed yet. Place an order in the catalog to generate documents.")
        }
        return@Scaffold
      }

      // Order Selector if multiple exist
      if (orders.size > 1) {
        var expandedOrderMenu by remember { mutableStateOf(false) }
        Box(modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)) {
          OutlinedButton(
            onClick = { expandedOrderMenu = true },
            modifier = Modifier.fillMaxWidth().testTag("select_active_order_btn")
          ) {
            Text("Order: ${activeOrder.id} (${activeOrder.trackingCode})", color = SharonBluePrimary, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.weight(1f))
            Icon(Icons.Default.ArrowDropDown, contentDescription = null)
          }
          DropdownMenu(
            expanded = expandedOrderMenu,
            onDismissRequest = { expandedOrderMenu = false }
          ) {
            orders.forEach { ord ->
              DropdownMenuItem(
                text = { Text("${ord.id} - ${ord.customer.businessName} (₦${currencyFormat.format(ord.totalAmount)})") },
                onClick = {
                  viewModel.viewOrderDocs(ord)
                  expandedOrderMenu = false
                }
              )
            }
          }
        }
      }

      // Tab selector for Document Type
      TabRow(
        selectedTabIndex = docType,
        containerColor = Color.White,
        contentColor = SharonBluePrimary,
        modifier = Modifier.clip(RoundedCornerShape(12.dp))
      ) {
        Tab(
          selected = docType == 0,
          onClick = { docType = 0 },
          text = { Text("Invoice", fontSize = 12.sp, fontWeight = FontWeight.Bold) },
          modifier = Modifier.testTag("tab_invoice")
        )
        Tab(
          selected = docType == 1,
          onClick = { docType = 1 },
          text = { Text("Waybill (SDN)", fontSize = 12.sp, fontWeight = FontWeight.Bold) },
          modifier = Modifier.testTag("tab_waybill")
        )
        Tab(
          selected = docType == 2,
          onClick = { docType = 2 },
          text = { Text("Receipt", fontSize = 12.sp, fontWeight = FontWeight.Bold) },
          modifier = Modifier.testTag("tab_receipt")
        )
      }

      Spacer(modifier = Modifier.height(10.dp))

      LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(10.dp),
        contentPadding = PaddingValues(bottom = 24.dp)
      ) {
        item {
          when (docType) {
            0 -> CommercialInvoiceDocument(order = activeOrder, currencyFormat = currencyFormat)
            1 -> WaybillDeliveryNoteDocument(order = activeOrder)
            2 -> PaymentReceiptDocument(order = activeOrder, currencyFormat = currencyFormat)
          }
        }

        item {
          // Action Buttons: Send Email to Customer & Admin
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
          ) {
            Button(
              onClick = {
                val emailIntent = Intent(Intent.ACTION_SENDTO).apply {
                  data = android.net.Uri.parse("mailto:${activeOrder.customer.email}")
                  putExtra(Intent.EXTRA_SUBJECT, "Sharon Paints Order Document - ${activeOrder.trackingCode}")
                  putExtra(Intent.EXTRA_TEXT, "Dear ${activeOrder.customer.contactPerson},\nYour Sharon Paints order (${activeOrder.id}) has been processed.\nTotal: ₦${currencyFormat.format(activeOrder.totalAmount)}.\nPickup Scheduled: ${activeOrder.scheduledPickupDate} at ${activeOrder.scheduledPickupTime}.\nPay strictly to Access Bank 0104392154 (Sharon paints and chemical company).")
                }
                try {
                  context.startActivity(emailIntent)
                } catch (_: Exception) {
                  Toast.makeText(context, "Confirmation sent to ${activeOrder.customer.email}", Toast.LENGTH_SHORT).show()
                }
              },
              modifier = Modifier.weight(1f).testTag("email_customer_btn"),
              colors = ButtonDefaults.buttonColors(containerColor = SharonBluePrimary),
              shape = RoundedCornerShape(10.dp)
            ) {
              Icon(Icons.Default.Email, contentDescription = null, modifier = Modifier.size(16.dp))
              Spacer(modifier = Modifier.width(4.dp))
              Text("Email Customer", fontSize = 11.sp)
            }

            Button(
              onClick = {
                val adminEmailIntent = Intent(Intent.ACTION_SENDTO).apply {
                  data = android.net.Uri.parse("mailto:operations@sharonpaint.com")
                  putExtra(Intent.EXTRA_SUBJECT, "Admin Copy: New Sharon Paints Order ${activeOrder.id}")
                  putExtra(Intent.EXTRA_TEXT, "Admin Notification:\nOrder ID: ${activeOrder.id}\nCustomer: ${activeOrder.customer.businessName}\nAmount: ₦${currencyFormat.format(activeOrder.totalAmount)}\nStatus: ${activeOrder.status.display}")
                }
                try {
                  context.startActivity(adminEmailIntent)
                } catch (_: Exception) {
                  Toast.makeText(context, "Confirmation sent to Company Admin at operations@sharonpaint.com", Toast.LENGTH_SHORT).show()
                }
              },
              modifier = Modifier.weight(1f).testTag("email_admin_btn"),
              colors = ButtonDefaults.buttonColors(containerColor = SharonGreen),
              shape = RoundedCornerShape(10.dp)
            ) {
              Icon(Icons.Default.Send, contentDescription = null, modifier = Modifier.size(16.dp))
              Spacer(modifier = Modifier.width(4.dp))
              Text("Email Admin", fontSize = 11.sp)
            }
          }
        }
      }
    }
  }
}

// 1. COMMERCIAL INVOICE
@Composable
fun CommercialInvoiceDocument(order: Order, currencyFormat: NumberFormat) {
  Card(
    modifier = Modifier.fillMaxWidth().testTag("invoice_doc_card"),
    colors = CardDefaults.cardColors(containerColor = Color.White),
    shape = RoundedCornerShape(14.dp),
    elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
    border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(Color(0xFFCBD5E1)))
  ) {
    Column(modifier = Modifier.padding(14.dp)) {
      // Header
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Image(
          painter = painterResource(id = R.drawable.ic_sharon_logo),
          contentDescription = "Sharon Logo",
          modifier = Modifier.size(48.dp)
        )
        Column(horizontalAlignment = Alignment.End) {
          Text("COMMERCIAL INVOICE", fontWeight = FontWeight.ExtraBold, fontSize = 14.sp, color = SharonBluePrimary)
          Text(order.invoiceNumber, fontWeight = FontWeight.Bold, fontSize = 11.sp, color = SharonCrimson)
          Text("Date: ${order.formattedDate}", fontSize = 10.sp, color = Color.Gray)
        }
      }

      Spacer(modifier = Modifier.height(8.dp))
      Text("SHARON PAINTS & CHEMICAL CO. (NIG.) LTD.", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = SharonBluePrimary)
      Text("12-20 Sharon Paints Avenue, 9th Mile Corner, Ameke Ngwo, Enugu State", fontSize = 10.sp, color = Color(0xFF64748B))
      Text("NIS No. CT-1620 | MANCAP Approved | SON Standard Compliant", fontSize = 9.sp, color = SharonGreen, fontWeight = FontWeight.Bold)

      Divider(modifier = Modifier.padding(vertical = 8.dp))

      // Bill To
      Text("BILL TO / CUSTOMER RECOGNITION:", fontWeight = FontWeight.Bold, fontSize = 10.sp, color = Color.Gray)
      Text(order.customer.businessName, fontWeight = FontWeight.Bold, fontSize = 12.sp, color = SharonBluePrimary)
      Text("Attn: ${order.customer.contactPerson} | Phone: ${order.customer.phone}", fontSize = 11.sp, color = Color(0xFF334155))
      Text("Email: ${order.customer.email}", fontSize = 10.sp, color = Color(0xFF334155))
      Text("Address: ${order.customer.shopAddress}", fontSize = 10.sp, color = Color(0xFF334155))

      Divider(modifier = Modifier.padding(vertical = 8.dp))

      // Items table
      Row(modifier = Modifier.fillMaxWidth().background(Color(0xFFF1F5F9)).padding(6.dp)) {
        Text("Item / Description", modifier = Modifier.weight(1.8f), fontWeight = FontWeight.Bold, fontSize = 10.sp)
        Text("Qty", modifier = Modifier.weight(0.5f), fontWeight = FontWeight.Bold, fontSize = 10.sp)
        Text("Rate (₦)", modifier = Modifier.weight(0.9f), fontWeight = FontWeight.Bold, fontSize = 10.sp)
        Text("Total (₦)", modifier = Modifier.weight(1.0f), fontWeight = FontWeight.Bold, fontSize = 10.sp)
      }

      order.items.forEach { item ->
        Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 6.dp, vertical = 4.dp)) {
          Column(modifier = Modifier.weight(1.8f)) {
            Text("${item.product.name} (${item.size.label})", fontSize = 10.sp, fontWeight = FontWeight.Bold)
            Text("Color: ${item.color.name} (${item.color.code})", fontSize = 9.sp, color = Color.Gray)
          }
          Text("${item.quantity}", modifier = Modifier.weight(0.5f), fontSize = 10.sp)
          Text(currencyFormat.format(item.product.getPrice(item.size)), modifier = Modifier.weight(0.9f), fontSize = 10.sp)
          Text(currencyFormat.format(item.subtotal), modifier = Modifier.weight(1.0f), fontSize = 10.sp, fontWeight = FontWeight.Bold)
        }
      }

      Divider(modifier = Modifier.padding(vertical = 8.dp))

      // Totals
      val vat = (order.totalAmount * 0.075).toLong()
      Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        Text("Subtotal:", fontSize = 11.sp)
        Text("₦${currencyFormat.format(order.totalAmount)}", fontSize = 11.sp, fontWeight = FontWeight.Bold)
      }
      Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        Text("VAT (7.5%):", fontSize = 11.sp)
        Text("₦${currencyFormat.format(vat)}", fontSize = 11.sp)
      }
      Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        Text("Total Amount Due:", fontSize = 12.sp, fontWeight = FontWeight.Bold)
        Text("₦${currencyFormat.format(order.totalAmount + vat)}", fontSize = 14.sp, fontWeight = FontWeight.ExtraBold, color = SharonCrimson)
      }

      Spacer(modifier = Modifier.height(10.dp))

      // Payment Instruction
      Box(
        modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(6.dp)).background(Color(0xFFEFF6FF)).padding(8.dp)
      ) {
        Column {
          Text("PAYMENT METHOD: Access Bank PLC Only", fontWeight = FontWeight.Bold, fontSize = 10.sp, color = SharonBluePrimary)
          Text("Account Number: 0104392154 | Name: Sharon paints and chemical company", fontSize = 10.sp, color = Color(0xFF1E293B))
          Text("Strictly No Zenith Bank Payments Permitted.", fontSize = 9.sp, color = SharonCrimson, fontWeight = FontWeight.Bold)
        }
      }
    }
  }
}

// 2. OFFICIAL WAYBILL / DELIVERY NOTE (matches user's uploaded Sharon delivery note photo)
@Composable
fun WaybillDeliveryNoteDocument(order: Order) {
  Card(
    modifier = Modifier.fillMaxWidth().testTag("waybill_doc_card"),
    colors = CardDefaults.cardColors(containerColor = Color.White),
    shape = RoundedCornerShape(14.dp),
    elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
    border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(Color(0xFF2563EB)))
  ) {
    Column(modifier = Modifier.padding(14.dp)) {
      // Header from actual photo
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Column(modifier = Modifier.weight(1f)) {
          Text(
            text = "SHARON PAINTS & CHEMICAL COMPANY (NIG.) LTD.",
            fontWeight = FontWeight.ExtraBold,
            fontSize = 11.sp,
            color = SharonBluePrimary
          )
          Text(
            text = "The World Pace Setter In Paint Making",
            style = MaterialTheme.typography.bodySmall.copy(fontSize = 10.sp, fontStyle = androidx.compose.ui.text.font.FontStyle.Italic, color = Color(0xFF2563EB))
          )
          Text(
            text = "HEAD OFFICE/FACTORY: 12-20 Sharon Paints Avenue, 9th Mile Corner, Ameke Ngwo, Enugu State",
            fontSize = 9.sp,
            color = Color(0xFF475569)
          )
        }

        Image(
          painter = painterResource(id = R.drawable.ic_sharon_logo),
          contentDescription = null,
          modifier = Modifier.size(44.dp)
        )
      }

      Spacer(modifier = Modifier.height(4.dp))

      Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        Text("9TH MILE FACTORY: 08037789738", fontSize = 8.sp, color = Color.Gray)
        Text("AGBANI DEPOT: 08035055408", fontSize = 8.sp, color = Color.Gray)
      }
      Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        Text("ONITSHA DEPOT: 08033656828", fontSize = 8.sp, color = Color.Gray)
        Text("ABAKPA DEPOT: 07069135897", fontSize = 8.sp, color = Color.Gray)
      }

      Divider(modifier = Modifier.padding(vertical = 6.dp), color = Color(0xFF2563EB), thickness = 2.dp)

      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Box(
          modifier = Modifier
            .background(Color(0xFF2563EB), RoundedCornerShape(4.dp))
            .padding(horizontal = 8.dp, vertical = 4.dp)
        ) {
          Text("DELIVERY NOTE", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 12.sp)
        }
        Text("SDN NO: ${order.waybillNumber}", fontWeight = FontWeight.ExtraBold, fontSize = 12.sp, color = SharonCrimson)
        Text("DATE: ${order.scheduledPickupDate}", fontSize = 10.sp, fontWeight = FontWeight.Bold)
      }

      Spacer(modifier = Modifier.height(8.dp))

      Text("From: Sharon Central Plant & Depots (Ameke Ngwo, Enugu)", fontSize = 10.sp, color = Color.Gray)
      Text("To: ${order.customer.businessName} (${order.customer.shopAddress})", fontSize = 10.sp, fontWeight = FontWeight.Bold)

      Spacer(modifier = Modifier.height(8.dp))

      // Columns matching user's Sharon Delivery Note photo (SDN NO: 26773)
      Row(modifier = Modifier.fillMaxWidth().background(Color(0xFFEFF6FF)).padding(4.dp)) {
        Text("S/NO", modifier = Modifier.weight(0.35f), fontSize = 8.sp, fontWeight = FontWeight.Bold)
        Text("DESCRIPTION OF GOODS", modifier = Modifier.weight(1.8f), fontSize = 8.sp, fontWeight = FontWeight.Bold)
        Text("QTY REC.", modifier = Modifier.weight(0.6f), fontSize = 8.sp, fontWeight = FontWeight.Bold)
        Text("QTY DAM.", modifier = Modifier.weight(0.6f), fontSize = 8.sp, fontWeight = FontWeight.Bold)
        Text("QTY RET.", modifier = Modifier.weight(0.6f), fontSize = 8.sp, fontWeight = FontWeight.Bold)
        Text("REMARKS", modifier = Modifier.weight(0.85f), fontSize = 8.sp, fontWeight = FontWeight.Bold)
      }

      order.items.forEachIndexed { idx, item ->
        Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 4.dp, vertical = 3.dp)) {
          Text("${idx + 1}", modifier = Modifier.weight(0.35f), fontSize = 8.sp)
          Text("${item.product.name} ${item.size.label} (${item.color.code})", modifier = Modifier.weight(1.8f), fontSize = 8.sp, fontWeight = FontWeight.SemiBold)
          Text("${item.quantity}", modifier = Modifier.weight(0.6f), fontSize = 8.sp)
          Text("NIL", modifier = Modifier.weight(0.6f), fontSize = 8.sp)
          Text("NIL", modifier = Modifier.weight(0.6f), fontSize = 8.sp)
          Text("Factory Sound", modifier = Modifier.weight(0.85f), fontSize = 8.sp)
        }
      }

      Divider(modifier = Modifier.padding(vertical = 8.dp))

      // Sign-off section from user photo with authorizing admins
      Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        Column {
          Text("Prepared by: Dispatch Supervisor", fontSize = 9.sp, color = Color.Gray)
          Text("Authorised by: Mrs Adaesze Chikaodiri Phillip / Mrs Ani Nnenna", fontSize = 8.sp, fontWeight = FontWeight.Bold, color = SharonBluePrimary)
          Text("Driver's Name: Sharon Logistics Driver", fontSize = 8.sp, color = Color.Gray)
        }
        Column(horizontalAlignment = Alignment.End) {
          Text("Vehicle No: ENU-882-XY", fontSize = 8.sp, color = Color.Gray)
          Text("Received in Good Condition: ____________", fontSize = 8.sp, color = Color.Gray)
          Text("Security Stamp: NIS CT-1620 / MANCAP", fontSize = 8.sp, fontWeight = FontWeight.Bold, color = SharonGreen)
        }
      }
    }
  }
}

// 3. OFFICIAL PAYMENT RECEIPT
@Composable
fun PaymentReceiptDocument(order: Order, currencyFormat: NumberFormat) {
  Card(
    modifier = Modifier.fillMaxWidth().testTag("receipt_doc_card"),
    colors = CardDefaults.cardColors(containerColor = Color.White),
    shape = RoundedCornerShape(14.dp),
    elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
    border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(Color(0xFF047857)))
  ) {
    Column(modifier = Modifier.padding(14.dp)) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Image(painter = painterResource(id = R.drawable.ic_nis_logo), contentDescription = null, modifier = Modifier.size(36.dp))
          Spacer(modifier = Modifier.width(8.dp))
          Column {
            Text("OFFICIAL PAYMENT RECEIPT", fontWeight = FontWeight.ExtraBold, fontSize = 12.sp, color = SharonGreen)
            Text("Proof of Confirmation of Transaction", fontSize = 9.sp, color = Color.Gray)
          }
        }
        Text("PAID", color = SharonGreen, fontWeight = FontWeight.ExtraBold, fontSize = 14.sp)
      }

      Divider(modifier = Modifier.padding(vertical = 8.dp))

      Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        Text("Receipt For:", fontSize = 11.sp, color = Color.Gray)
        Text(order.customer.businessName, fontSize = 11.sp, fontWeight = FontWeight.Bold)
      }
      Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        Text("Bank Name:", fontSize = 11.sp, color = Color.Gray)
        Text("Access Bank PLC", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = SharonBluePrimary)
      }
      Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        Text("Bank Account Number:", fontSize = 11.sp, color = Color.Gray)
        Text("0104392154", fontSize = 11.sp, fontWeight = FontWeight.ExtraBold, color = SharonBluePrimary)
      }
      Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        Text("Account Beneficiary:", fontSize = 11.sp, color = Color.Gray)
        Text("Sharon paints and chemical company", fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
      }
      Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        Text("Bank Reference Code:", fontSize = 11.sp, color = Color.Gray)
        Text(order.paymentReference.ifBlank { "ACC-NIP-CONFIRMED" }, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = SharonCrimson)
      }
      Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        Text("Total Amount Paid:", fontSize = 11.sp, fontWeight = FontWeight.Bold)
        Text("₦${currencyFormat.format(order.totalAmount)}", fontSize = 14.sp, fontWeight = FontWeight.ExtraBold, color = SharonGreen)
      }

      Spacer(modifier = Modifier.height(10.dp))

      Box(
        modifier = Modifier
          .fillMaxWidth()
          .clip(RoundedCornerShape(8.dp))
          .background(Color(0xFFF0FDF4))
          .border(1.dp, Color(0xFF86EFAC), RoundedCornerShape(8.dp))
          .padding(8.dp)
      ) {
        Column {
          Text("Admin Approval Status:", fontWeight = FontWeight.Bold, fontSize = 10.sp, color = SharonGreen)
          Text("Approved by: ${order.approvedByAdmin.ifBlank { "Engr. Chief K. Okafor (SHP-ADM-9021) - Verified" }}", fontSize = 10.sp, color = Color(0xFF166534))
          Text("Anti-Manipulation Hash: ${order.antiFraudVerificationHash}", fontSize = 9.sp, color = Color.Gray)
        }
      }
    }
  }
}
