package com.example.ui.screens

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
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
import com.example.data.model.CustomerProfile
import com.example.ui.AppNavDestination
import com.example.ui.SharonViewModel
import com.example.ui.components.AccessBankNoticeCard
import com.example.ui.components.WorkingHoursCard
import com.example.ui.theme.SharonBluePrimary
import com.example.ui.theme.SharonCrimson
import com.example.ui.theme.SharonGold
import com.example.ui.theme.SharonGreen
import java.text.NumberFormat
import java.util.Calendar
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookingCheckoutScreen(
  viewModel: SharonViewModel,
  modifier: Modifier = Modifier
) {
  val context = LocalContext.current
  val cart by viewModel.repository.cart.collectAsState()
  val customer by viewModel.repository.currentCustomer.collectAsState()
  val depots = viewModel.repository.depots

  val selectedDay by viewModel.selectedPickupDay.collectAsState()
  val selectedHour by viewModel.selectedPickupHour.collectAsState()
  val scheduleWarning by viewModel.pickupScheduleWarning.collectAsState()

  val otpInput by viewModel.otpInput.collectAsState()
  val otpMessage by viewModel.otpSuccessMessage.collectAsState()

  // Form Fields
  var businessName by remember { mutableStateOf(customer.businessName) }
  var contactPerson by remember { mutableStateOf(customer.contactPerson) }
  var email by remember { mutableStateOf(customer.email) }
  var phone by remember { mutableStateOf(customer.phone) }
  var shopAddress by remember { mutableStateOf(customer.shopAddress) }
  var selectedDepotId by remember { mutableStateOf(depots.first().id) }
  var paymentReference by remember { mutableStateOf("") }
  var isBiometricVerified by remember { mutableStateOf(customer.isBiometricEnabled) }

  val currencyFormat = remember { NumberFormat.getNumberInstance(Locale.US) }
  val cartSubtotal = cart.sumOf { it.subtotal }

  Scaffold(
    topBar = {
      TopAppBar(
        title = {
          Column {
            Text(
              text = "Customer Booking & Checkout",
              style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
              color = SharonBluePrimary
            )
            Text(
              text = "Security Verification & Pickup Scheduling",
              style = MaterialTheme.typography.bodySmall.copy(fontSize = 10.sp, color = Color.Gray)
            )
          }
        },
        actions = {
          if (customer.isOtpVerified) {
            TextButton(
              onClick = {
                viewModel.repository.logoutCustomer()
                Toast.makeText(context, "Logged out successfully", Toast.LENGTH_SHORT).show()
              },
              modifier = Modifier.testTag("logout_customer_btn")
            ) {
              Text("Log Out", color = SharonCrimson, fontSize = 12.sp)
            }
          }
        },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
      )
    }
  ) { innerPadding ->
    LazyColumn(
      modifier = modifier
        .fillMaxSize()
        .padding(innerPadding)
        .padding(horizontal = 14.dp),
      verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
      // Working Hours Card
      item {
        WorkingHoursCard()
      }

      // Customer Recognition & Profile Card
      item {
        Card(
          modifier = Modifier.fillMaxWidth().testTag("customer_recognition_card"),
          colors = CardDefaults.cardColors(containerColor = Color.White),
          shape = RoundedCornerShape(14.dp),
          border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(Color(0xFFE2E8F0)))
        ) {
          Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Icon(Icons.Default.Badge, contentDescription = null, tint = SharonBluePrimary)
              Spacer(modifier = Modifier.width(8.dp))
              Text(
                text = "Customer Recognition & Business Details",
                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold, color = SharonBluePrimary)
              )
            }

            OutlinedTextField(
              value = businessName,
              onValueChange = { businessName = it },
              label = { Text("Business / Enterprise Name") },
              modifier = Modifier.fillMaxWidth().testTag("cust_business_input"),
              singleLine = true
            )

            OutlinedTextField(
              value = contactPerson,
              onValueChange = { contactPerson = it },
              label = { Text("Contact Person Name") },
              modifier = Modifier.fillMaxWidth().testTag("cust_contact_input"),
              singleLine = true
            )

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
              OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email Address") },
                modifier = Modifier.weight(1f).testTag("cust_email_input"),
                singleLine = true
              )
              OutlinedTextField(
                value = phone,
                onValueChange = { phone = it },
                label = { Text("Phone Number") },
                modifier = Modifier.weight(1f).testTag("cust_phone_input"),
                singleLine = true
              )
            }

            OutlinedTextField(
              value = shopAddress,
              onValueChange = { shopAddress = it },
              label = { Text("Shop / Business Delivery Address") },
              modifier = Modifier.fillMaxWidth().testTag("cust_address_input")
            )
          }
        }
      }

      // Security Verification: OTP & Fingerprint / Code
      item {
        Card(
          modifier = Modifier.fillMaxWidth().testTag("security_verification_card"),
          colors = CardDefaults.cardColors(containerColor = Color(0xFFF8FAFC)),
          shape = RoundedCornerShape(14.dp),
          border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(Color(0xFFCBD5E1)))
        ) {
          Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Icon(Icons.Default.Shield, contentDescription = null, tint = SharonGreen)
              Spacer(modifier = Modifier.width(8.dp))
              Text(
                text = "Anti-Fraud Security & OTP Verification",
                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold, color = SharonBluePrimary)
              )
            }
            Text(
              text = "Protects your paint orders against unauthorized manipulations. All orders carry anti-tamper security tokens.",
              style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp, color = Color.Gray)
            )

            // OTP Box
            Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
              OutlinedTextField(
                value = otpInput,
                onValueChange = { viewModel.updateOtpInput(it) },
                label = { Text("Enter 6-Digit Email OTP") },
                placeholder = { Text("e.g. 550182") },
                modifier = Modifier.weight(1f).testTag("otp_code_input"),
                singleLine = true
              )
              Spacer(modifier = Modifier.width(8.dp))
              Button(
                onClick = { viewModel.verifyOtp() },
                colors = ButtonDefaults.buttonColors(containerColor = SharonGreen),
                modifier = Modifier.testTag("verify_otp_btn")
              ) {
                Text("Verify OTP")
              }
            }

            otpMessage?.let {
              Text(it, fontSize = 11.sp, color = if (it.contains("Successful")) SharonGreen else SharonCrimson, fontWeight = FontWeight.SemiBold)
            }

            // Fingerprint & Registration Code Switch
            Row(
              modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(8.dp))
                .background(Color.White)
                .padding(horizontal = 10.dp, vertical = 6.dp),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Fingerprint, contentDescription = null, tint = SharonBluePrimary, modifier = Modifier.size(24.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                  Text("Biometric Fingerprint & Secure Token", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = SharonBluePrimary)
                  Text("Registration Code: SEC-7749 (Active)", fontSize = 10.sp, color = Color.Gray)
                }
              }
              Switch(
                checked = isBiometricVerified,
                onCheckedChange = { isBiometricVerified = it },
                colors = SwitchDefaults.colors(checkedThumbColor = SharonGreen),
                modifier = Modifier.testTag("biometric_switch")
              )
            }
          }
        }
      }

      // Order Cart Items Review
      item {
        Card(
          modifier = Modifier.fillMaxWidth().testTag("cart_items_card"),
          colors = CardDefaults.cardColors(containerColor = Color.White),
          shape = RoundedCornerShape(14.dp),
          border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(Color(0xFFE2E8F0)))
        ) {
          Column(modifier = Modifier.padding(14.dp)) {
            Text(
              text = "Ordered Paint Products (${cart.size} items)",
              style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold, color = SharonBluePrimary)
            )

            if (cart.isEmpty()) {
              Spacer(modifier = Modifier.height(10.dp))
              Text("Your cart is empty. Please select paints from the Paints Catalog.", color = Color.Gray, fontSize = 12.sp)
              Spacer(modifier = Modifier.height(8.dp))
              Button(
                onClick = { viewModel.navigateTo(AppNavDestination.CATALOG) },
                colors = ButtonDefaults.buttonColors(containerColor = SharonBluePrimary),
                modifier = Modifier.testTag("browse_paints_btn")
              ) {
                Text("Browse Paints")
              }
            } else {
              Spacer(modifier = Modifier.height(8.dp))
              cart.forEachIndexed { idx, item ->
                Row(
                  modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                  horizontalArrangement = Arrangement.SpaceBetween,
                  verticalAlignment = Alignment.CenterVertically
                ) {
                  Column(modifier = Modifier.weight(1f)) {
                    Text("${item.quantity}x ${item.product.name} (${item.size.label})", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    Text("Color: ${item.color.name} (${item.color.code})", fontSize = 10.sp, color = Color.Gray)
                  }
                  Text("₦${currencyFormat.format(item.subtotal)}", fontWeight = FontWeight.ExtraBold, fontSize = 12.sp, color = SharonCrimson)
                  IconButton(
                    onClick = { viewModel.repository.updateCartQuantity(idx, 0) },
                    modifier = Modifier.size(24.dp)
                  ) {
                    Icon(Icons.Default.Delete, contentDescription = "Remove", tint = Color.Gray, modifier = Modifier.size(16.dp))
                  }
                }
                Divider(modifier = Modifier.padding(vertical = 4.dp))
              }

              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
              ) {
                Text("Total Payable:", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold))
                Text(
                  "₦${currencyFormat.format(cartSubtotal)}",
                  style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.ExtraBold, color = SharonCrimson)
                )
              }
            }
          }
        }
      }

      // Scheduled Pickup Day & Time Picker (Tuesday - Saturday, 9am - 5pm)
      item {
        Card(
          modifier = Modifier.fillMaxWidth().testTag("pickup_schedule_card"),
          colors = CardDefaults.cardColors(containerColor = Color.White),
          shape = RoundedCornerShape(14.dp),
          border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(Color(0xFFE2E8F0)))
        ) {
          Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Icon(Icons.Default.Event, contentDescription = null, tint = SharonBluePrimary)
              Spacer(modifier = Modifier.width(8.dp))
              Text(
                text = "Depot Pickup Date & Time Scheduling",
                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold, color = SharonBluePrimary)
              )
            }
            Text(
              text = "Working days: Tuesday to Saturday | Hours: 9:00 AM – 5:00 PM (Requires Admin Approval)",
              style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp, color = SharonGreen, fontWeight = FontWeight.SemiBold)
            )

            // Select Target Depot
            Text("Select Pickup Depot:", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = SharonBluePrimary)
            var expandedDepotMenu by remember { mutableStateOf(false) }
            Box {
              OutlinedButton(
                onClick = { expandedDepotMenu = true },
                modifier = Modifier.fillMaxWidth().testTag("depot_select_btn")
              ) {
                Text(depots.find { it.id == selectedDepotId }?.name ?: "Select Depot", color = SharonBluePrimary)
                Spacer(modifier = Modifier.weight(1f))
                Icon(Icons.Default.ArrowDropDown, contentDescription = null)
              }
              DropdownMenu(
                expanded = expandedDepotMenu,
                onDismissRequest = { expandedDepotMenu = false }
              ) {
                depots.forEach { d ->
                  DropdownMenuItem(
                    text = { Text("${d.name} (${d.townState})") },
                    onClick = {
                      selectedDepotId = d.id
                      expandedDepotMenu = false
                    }
                  )
                }
              }
            }

            // Pickup Day Selector
            Text("Select Pickup Day (Tue – Sat):", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = SharonBluePrimary)
            val daysList = listOf(
              Calendar.TUESDAY to "Tuesday",
              Calendar.WEDNESDAY to "Wednesday",
              Calendar.THURSDAY to "Thursday",
              Calendar.FRIDAY to "Friday",
              Calendar.SATURDAY to "Saturday",
              Calendar.SUNDAY to "Sunday (Closed)",
              Calendar.MONDAY to "Monday (Closed)"
            )
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
              daysList.take(5).forEach { (dayConst, label) ->
                FilterChip(
                  selected = selectedDay == dayConst,
                  onClick = { viewModel.updatePickupDay(dayConst) },
                  label = { Text(label.substring(0, 3), fontSize = 11.sp) },
                  modifier = Modifier.testTag("day_chip_$dayConst")
                )
              }
            }

            // Pickup Hour Selector (9am - 5pm)
            Text("Select Pickup Hour (9am – 5pm):", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = SharonBluePrimary)
            val hoursList = listOf(9, 10, 11, 12, 13, 14, 15, 16, 17)
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
              hoursList.take(5).forEach { hr ->
                val hrLabel = if (hr < 12) "${hr}am" else if (hr == 12) "12pm" else "${hr - 12}pm"
                FilterChip(
                  selected = selectedHour == hr,
                  onClick = { viewModel.updatePickupHour(hr) },
                  label = { Text(hrLabel, fontSize = 11.sp) },
                  modifier = Modifier.testTag("hour_chip_$hr")
                )
              }
            }

            // Warning if outside hours
            scheduleWarning?.let { warn ->
              Box(
                modifier = Modifier
                  .fillMaxWidth()
                  .clip(RoundedCornerShape(8.dp))
                  .background(Color(0xFFFEF2F2))
                  .border(1.dp, Color(0xFFF87171), RoundedCornerShape(8.dp))
                  .padding(8.dp)
              ) {
                Text(warn, color = SharonCrimson, fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
              }
            }
          }
        }
      }

      // Access Bank Payment Info & Reference Input
      item {
        AccessBankNoticeCard()
      }

      item {
        Card(
          modifier = Modifier.fillMaxWidth().testTag("payment_ref_card"),
          colors = CardDefaults.cardColors(containerColor = Color.White),
          shape = RoundedCornerShape(14.dp),
          border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(Color(0xFFE2E8F0)))
        ) {
          Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(
              text = "Input Access Bank Transfer Reference Code",
              style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold, color = SharonBluePrimary)
            )
            Text(
              text = "After transferring funds to Sharon Paints & Chemical Co Access Bank account 0104392154, paste your bank session ID or transaction reference here for admin confirmation.",
              style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp, color = Color.Gray)
            )
            OutlinedTextField(
              value = paymentReference,
              onValueChange = { paymentReference = it },
              placeholder = { Text("e.g. ACC-REF-9941829 or NIP Session ID") },
              modifier = Modifier.fillMaxWidth().testTag("payment_reference_input"),
              singleLine = true
            )
          }
        }
      }

      // Place Order Button
      item {
        Button(
          onClick = {
            if (cart.isEmpty()) {
              Toast.makeText(context, "Please add paint products to cart first!", Toast.LENGTH_SHORT).show()
              return@Button
            }
            if (businessName.isBlank() || email.isBlank()) {
              Toast.makeText(context, "Please enter Business Name and Email Address!", Toast.LENGTH_SHORT).show()
              return@Button
            }

            // Update customer profile
            viewModel.repository.updateCustomerProfile(
              CustomerProfile(
                businessName = businessName,
                contactPerson = contactPerson,
                email = email,
                phone = phone,
                shopAddress = shopAddress,
                isOtpVerified = true,
                isBiometricEnabled = isBiometricVerified,
                securityRegistrationCode = "SEC-${(1000..9999).random()}"
              )
            )

            val dayLabel = when (selectedDay) {
              Calendar.TUESDAY -> "Tuesday"
              Calendar.WEDNESDAY -> "Wednesday"
              Calendar.THURSDAY -> "Thursday"
              Calendar.FRIDAY -> "Friday"
              Calendar.SATURDAY -> "Saturday"
              else -> "Selected Day"
            }
            val hourLabel = if (selectedHour < 12) "${selectedHour}:00 AM" else if (selectedHour == 12) "12:00 PM" else "${selectedHour - 12}:00 PM"

            val order = viewModel.repository.placeOrder(
              depotId = selectedDepotId,
              pickupDate = "$dayLabel, 2026",
              pickupTime = hourLabel,
              paymentRef = paymentReference,
              isWithinHours = scheduleWarning == null
            )

            Toast.makeText(context, "Order Placed Successfully! Tracking Code: ${order.trackingCode}", Toast.LENGTH_LONG).show()
            viewModel.viewOrderDocs(order)
          },
          modifier = Modifier
            .fillMaxWidth()
            .height(52.dp)
            .testTag("submit_order_btn"),
          colors = ButtonDefaults.buttonColors(containerColor = SharonBluePrimary),
          shape = RoundedCornerShape(12.dp)
        ) {
          Icon(Icons.Default.Lock, contentDescription = null, modifier = Modifier.size(18.dp))
          Spacer(modifier = Modifier.width(8.dp))
          Text("Confirm Booking & Generate Invoices", fontSize = 14.sp, fontWeight = FontWeight.Bold)
        }
      }

      item {
        Spacer(modifier = Modifier.height(24.dp))
      }
    }
  }
}
