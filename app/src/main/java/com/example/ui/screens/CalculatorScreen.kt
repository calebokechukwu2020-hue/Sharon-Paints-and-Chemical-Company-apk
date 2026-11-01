package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.OrderStatus
import com.example.data.model.PaintProduct
import com.example.ui.SharonViewModel
import com.example.ui.theme.SharonBluePrimary
import com.example.ui.theme.SharonCrimson
import com.example.ui.theme.SharonGold
import com.example.ui.theme.SharonGreen
import java.text.NumberFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalculatorScreen(
  viewModel: SharonViewModel,
  modifier: Modifier = Modifier
) {
  var selectedTab by remember { mutableStateOf(0) } // 0 = Paint Estimator, 1 = Financial Transactions Ledger
  val wallLength by viewModel.calcWallLengthMeters.collectAsState()
  val wallHeight by viewModel.calcWallHeightMeters.collectAsState()
  val coats by viewModel.calcCoats.collectAsState()
  val surfaceType by viewModel.calcSurfaceType.collectAsState()
  val selectedCalcProd by viewModel.calcSelectedProduct.collectAsState()

  val products = viewModel.repository.products
  val orders by viewModel.repository.orders.collectAsState()

  val currencyFormat = remember { NumberFormat.getNumberInstance(Locale.US) }
  val coverageResult = viewModel.calculateCoverage()

  // Transactions calculations
  val totalOrdersCount = orders.size
  val totalTransactionsValue = orders.sumOf { it.totalAmount }
  val approvedPaymentsValue = orders.filter { it.status == OrderStatus.APPROVED_FOR_PICKUP || it.status == OrderStatus.COMPLETED }.sumOf { it.totalAmount }
  val pendingPaymentsValue = orders.filter { it.status == OrderStatus.PAYMENT_SUBMITTED || it.status == OrderStatus.AWAITING_PAYMENT }.sumOf { it.totalAmount }
  val vatAmount = (totalTransactionsValue * 0.075).toLong()

  Scaffold(
    topBar = {
      TopAppBar(
        title = {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.Calculate, contentDescription = null, tint = SharonBluePrimary)
            Spacer(modifier = Modifier.width(8.dp))
            Column {
              Text(
                text = "Sharon AI Realistic Calculator",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = SharonBluePrimary
              )
              Text(
                text = "Surface Paint Estimator & Financial Transactions Ledger",
                style = MaterialTheme.typography.bodySmall.copy(fontSize = 10.sp, color = Color.Gray)
              )
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
      TabRow(
        selectedTabIndex = selectedTab,
        containerColor = Color.White,
        contentColor = SharonBluePrimary,
        modifier = Modifier.clip(RoundedCornerShape(12.dp))
      ) {
        Tab(
          selected = selectedTab == 0,
          onClick = { selectedTab = 0 },
          text = { Text("Paint Coverage Estimator", fontWeight = FontWeight.Bold) },
          modifier = Modifier.testTag("tab_paint_calc")
        )
        Tab(
          selected = selectedTab == 1,
          onClick = { selectedTab = 1 },
          text = { Text("App Transactions Ledger", fontWeight = FontWeight.Bold) },
          modifier = Modifier.testTag("tab_financial_calc")
        )
      }

      Spacer(modifier = Modifier.height(12.dp))

      if (selectedTab == 0) {
        // PAINT COVERAGE CALCULATOR
        LazyColumn(
          modifier = Modifier.fillMaxSize(),
          verticalArrangement = Arrangement.spacedBy(10.dp),
          contentPadding = PaddingValues(bottom = 24.dp)
        ) {
          item {
            Card(
              modifier = Modifier.fillMaxWidth().testTag("calc_input_card"),
              colors = CardDefaults.cardColors(containerColor = Color.White),
              shape = RoundedCornerShape(14.dp),
              border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(Color(0xFFE2E8F0)))
            ) {
              Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                  text = "Enter Wall Dimensions (Meters)",
                  style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold, color = SharonBluePrimary)
                )

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                  OutlinedTextField(
                    value = wallLength,
                    onValueChange = { viewModel.updateCalcWallLength(it) },
                    label = { Text("Length (m)") },
                    modifier = Modifier.weight(1f).testTag("calc_length_input"),
                    singleLine = true
                  )
                  OutlinedTextField(
                    value = wallHeight,
                    onValueChange = { viewModel.updateCalcWallHeight(it) },
                    label = { Text("Height (m)") },
                    modifier = Modifier.weight(1f).testTag("calc_height_input"),
                    singleLine = true
                  )
                }

                Text("Number of Coats:", fontWeight = FontWeight.SemiBold, fontSize = 12.sp, color = SharonBluePrimary)
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                  listOf(1, 2, 3).forEach { c ->
                    FilterChip(
                      selected = coats == c,
                      onClick = { viewModel.updateCalcCoats(c) },
                      label = { Text("$c Coat${if (c > 1) "s" else ""}") },
                      modifier = Modifier.testTag("coat_chip_$c")
                    )
                  }
                }

                Text("Surface Condition:", fontWeight = FontWeight.SemiBold, fontSize = 12.sp, color = SharonBluePrimary)
                val surfaces = listOf(
                  "POP / Screeded Wall (Standard)",
                  "Plastered Wall (Unprimed)",
                  "Rough Texcoat / Heavy Texture",
                  "Raw Concrete / Porous Brick"
                )
                surfaces.forEach { s ->
                  Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth().clickable { viewModel.updateCalcSurfaceType(s) }
                  ) {
                    RadioButton(
                      selected = surfaceType == s,
                      onClick = { viewModel.updateCalcSurfaceType(s) },
                      colors = RadioButtonDefaults.colors(selectedColor = SharonBluePrimary)
                    )
                    Text(s, fontSize = 12.sp, color = Color(0xFF1E293B))
                  }
                }

                Text("Choose Sharon Paint Product:", fontWeight = FontWeight.SemiBold, fontSize = 12.sp, color = SharonBluePrimary)
                var expandedProdMenu by remember { mutableStateOf(false) }
                Box {
                  OutlinedButton(
                    onClick = { expandedProdMenu = true },
                    modifier = Modifier.fillMaxWidth().testTag("select_calc_prod_btn")
                  ) {
                    Text(selectedCalcProd.name, color = SharonBluePrimary, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.weight(1f))
                    Icon(Icons.Default.ArrowDropDown, contentDescription = null)
                  }
                  DropdownMenu(
                    expanded = expandedProdMenu,
                    onDismissRequest = { expandedProdMenu = false }
                  ) {
                    products.forEach { p ->
                      DropdownMenuItem(
                        text = { Text(p.name) },
                        onClick = {
                          viewModel.updateCalcProduct(p)
                          expandedProdMenu = false
                        }
                      )
                    }
                  }
                }
              }
            }
          }

          // Calculation Result Summary Card
          item {
            Card(
              modifier = Modifier.fillMaxWidth().testTag("calc_result_card"),
              colors = CardDefaults.cardColors(containerColor = Color(0xFFF0FDF4)),
              shape = RoundedCornerShape(14.dp),
              border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(Color(0xFF86EFAC)))
            ) {
              Column(modifier = Modifier.padding(14.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                  Icon(Icons.Default.CheckCircle, contentDescription = null, tint = SharonGreen)
                  Spacer(modifier = Modifier.width(8.dp))
                  Text(
                    text = "AI Paint Requirement & Cost Summary",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, color = SharonGreen)
                  )
                }

                Spacer(modifier = Modifier.height(10.dp))

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                  Text("Total Surface Area:", style = MaterialTheme.typography.bodyMedium)
                  Text("${String.format(Locale.US, "%.1f", coverageResult.surfaceAreaSqm)} m²", fontWeight = FontWeight.Bold)
                }

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                  Text("Estimated Paint Volume:", style = MaterialTheme.typography.bodyMedium)
                  Text("${String.format(Locale.US, "%.1f", coverageResult.paintNeededLiters)} Litres", fontWeight = FontWeight.Bold)
                }

                Divider(modifier = Modifier.padding(vertical = 8.dp))

                Text("Optimal Bucket Combination:", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = SharonBluePrimary)
                Spacer(modifier = Modifier.height(4.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                  Text("• 20 Litres Buckets:", fontSize = 12.sp)
                  Text("${coverageResult.recommendedBuckets20L} bucket(s)", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                }
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                  Text("• 10 Litres Buckets:", fontSize = 12.sp)
                  Text("${coverageResult.recommendedBuckets10L} bucket(s)", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                }
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                  Text("• 4 Litres Cans:", fontSize = 12.sp)
                  Text("${coverageResult.recommendedCans4L} can(s)", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                }

                Divider(modifier = Modifier.padding(vertical = 8.dp))

                Row(
                  modifier = Modifier.fillMaxWidth(),
                  horizontalArrangement = Arrangement.SpaceBetween,
                  verticalAlignment = Alignment.CenterVertically
                ) {
                  Text("Total Estimated Cost:", style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold))
                  Text(
                    "₦${currencyFormat.format(coverageResult.estimatedCostNaira)}",
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.ExtraBold, color = SharonCrimson)
                  )
                }
              }
            }
          }
        }
      } else {
        // FINANCIAL TRANSACTIONS LEDGER (Customer & Admin View)
        LazyColumn(
          modifier = Modifier.fillMaxSize(),
          verticalArrangement = Arrangement.spacedBy(10.dp),
          contentPadding = PaddingValues(bottom = 24.dp)
        ) {
          item {
            Card(
              modifier = Modifier.fillMaxWidth().testTag("financial_ledger_card"),
              colors = CardDefaults.cardColors(containerColor = Color(0xFF0F172A)),
              shape = RoundedCornerShape(14.dp)
            ) {
              Column(modifier = Modifier.padding(14.dp)) {
                Text(
                  text = "Sharon App Realistic Financial Ledger",
                  style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold, color = Color.White)
                )
                Text(
                  text = "Official Access Bank Account 0104392154 Reconciliation",
                  style = MaterialTheme.typography.bodySmall.copy(fontSize = 10.sp, color = SharonGold)
                )
                Spacer(modifier = Modifier.height(12.dp))

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                  Column {
                    Text("Total App Orders", fontSize = 10.sp, color = Color.Gray)
                    Text("$totalOrdersCount Bookings", fontWeight = FontWeight.Bold, color = Color.White, fontSize = 14.sp)
                  }
                  Column {
                    Text("Total Transactions", fontSize = 10.sp, color = Color.Gray)
                    Text("₦${currencyFormat.format(totalTransactionsValue)}", fontWeight = FontWeight.ExtraBold, color = Color.White, fontSize = 14.sp)
                  }
                }

                Spacer(modifier = Modifier.height(8.dp))

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                  Column {
                    Text("Access Bank Approved", fontSize = 10.sp, color = Color.Gray)
                    Text("₦${currencyFormat.format(approvedPaymentsValue)}", fontWeight = FontWeight.Bold, color = Color(0xFF4ADE80), fontSize = 13.sp)
                  }
                  Column {
                    Text("Pending Verification", fontSize = 10.sp, color = Color.Gray)
                    Text("₦${currencyFormat.format(pendingPaymentsValue)}", fontWeight = FontWeight.Bold, color = SharonGold, fontSize = 13.sp)
                  }
                }
              }
            }
          }

          item {
            Text(
              text = "Itemized App Orders & Reconciliation",
              style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
              color = SharonBluePrimary
            )
          }

          items(orders, key = { it.id }) { ord ->
            Card(
              modifier = Modifier.fillMaxWidth(),
              colors = CardDefaults.cardColors(containerColor = Color.White),
              shape = RoundedCornerShape(10.dp),
              border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(Color(0xFFE2E8F0)))
            ) {
              Column(modifier = Modifier.padding(10.dp)) {
                Row(
                  modifier = Modifier.fillMaxWidth(),
                  horizontalArrangement = Arrangement.SpaceBetween,
                  verticalAlignment = Alignment.CenterVertically
                ) {
                  Text(ord.id, fontWeight = FontWeight.Bold, color = SharonBluePrimary)
                  Text("₦${currencyFormat.format(ord.totalAmount)}", fontWeight = FontWeight.ExtraBold, color = SharonCrimson)
                }
                Text("Customer: ${ord.customer.businessName}", fontSize = 11.sp, color = Color(0xFF334155))
                Text("Tracking Code: ${ord.trackingCode} | Pickup: ${ord.scheduledPickupDate}", fontSize = 10.sp, color = Color.Gray)
                Text("Status: ${ord.status.display}", fontSize = 10.sp, fontWeight = FontWeight.SemiBold, color = if (ord.status == OrderStatus.APPROVED_FOR_PICKUP) SharonGreen else SharonGold)
              }
            }
          }
        }
      }
    }
  }
}
