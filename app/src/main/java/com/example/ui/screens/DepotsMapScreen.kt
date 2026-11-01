package com.example.ui.screens

import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
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
import com.example.data.model.Depot
import com.example.data.model.Order
import com.example.ui.SharonViewModel
import com.example.ui.theme.SharonBluePrimary
import com.example.ui.theme.SharonCrimson
import com.example.ui.theme.SharonGold
import com.example.ui.theme.SharonGreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DepotsMapScreen(
  viewModel: SharonViewModel,
  modifier: Modifier = Modifier
) {
  val context = LocalContext.current
  val depots = viewModel.repository.depots
  val searchTrackingCode by viewModel.searchTrackingCode.collectAsState()
  val searchedOrder by viewModel.searchedOrder.collectAsState()

  var selectedDepotForNav by remember { mutableStateOf<Depot?>(null) }

  Scaffold(
    topBar = {
      TopAppBar(
        title = {
          Column {
            Text(
              text = "Sharon Depots & GPS Tracking",
              style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
              color = SharonBluePrimary
            )
            Text(
              text = "9 Regional Distribution Hubs & Live Dispatch Radar",
              style = MaterialTheme.typography.bodySmall.copy(fontSize = 10.sp, color = Color.Gray)
            )
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
      // Live Tracking Code Search Box
      item {
        Card(
          modifier = Modifier
            .fillMaxWidth()
            .testTag("tracking_card"),
          colors = CardDefaults.cardColors(containerColor = Color(0xFFEFF6FF)),
          shape = RoundedCornerShape(16.dp),
          border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(Color(0xFFBFDBFE)))
        ) {
          Column(modifier = Modifier.padding(14.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Icon(Icons.Default.GpsFixed, contentDescription = null, tint = Color(0xFF2563EB))
              Spacer(modifier = Modifier.width(8.dp))
              Text(
                text = "Google AI Live Order & Transit Tracking",
                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                color = SharonBluePrimary
              )
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(
              text = "Input your Sharon dispatch tracking code (e.g. SHP-TRK-78421 or SHP-TRK-88319) to track preparation, depot clearance, and vehicle movement.",
              style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp, color = Color(0xFF475569))
            )
            Spacer(modifier = Modifier.height(10.dp))

            Row(
              modifier = Modifier.fillMaxWidth(),
              verticalAlignment = Alignment.CenterVertically
            ) {
              OutlinedTextField(
                value = searchTrackingCode,
                onValueChange = { viewModel.updateSearchCode(it) },
                placeholder = { Text("Enter Tracking Code (SHP-TRK-...)") },
                modifier = Modifier
                  .weight(1f)
                  .testTag("tracking_code_input"),
                shape = RoundedCornerShape(10.dp),
                singleLine = true
              )
              Spacer(modifier = Modifier.width(8.dp))
              Button(
                onClick = {
                  if (searchedOrder == null && searchTrackingCode.isNotBlank()) {
                    Toast.makeText(context, "No active order found for code '$searchTrackingCode'", Toast.LENGTH_SHORT).show()
                  }
                },
                colors = ButtonDefaults.buttonColors(containerColor = SharonBluePrimary),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier.testTag("track_btn")
              ) {
                Text("Track")
              }
            }

            // Tracking result details
            searchedOrder?.let { order ->
              Spacer(modifier = Modifier.height(10.dp))
              Box(
                modifier = Modifier
                  .fillMaxWidth()
                  .clip(RoundedCornerShape(10.dp))
                  .background(Color.White)
                  .border(1.dp, Color(0xFFCBD5E1), RoundedCornerShape(10.dp))
                  .padding(12.dp)
              ) {
                Column {
                  Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                  ) {
                    Text(
                      text = order.trackingCode,
                      style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.ExtraBold, color = SharonBluePrimary)
                    )
                    Badge(containerColor = if (order.status.name.contains("APPROVED")) SharonGreen else SharonGold) {
                      Text(order.status.display, fontSize = 10.sp, color = Color.White)
                    }
                  }
                  Spacer(modifier = Modifier.height(6.dp))
                  Text("Customer: ${order.customer.businessName} (${order.customer.contactPerson})", fontSize = 11.sp, color = Color(0xFF334155))
                  Text("Target Depot: ${depots.find { it.id == order.targetDepotId }?.name ?: "9th Mile Central Factory"}", fontSize = 11.sp, color = Color(0xFF334155))
                  Text("Scheduled Pickup: ${order.scheduledPickupDate} at ${order.scheduledPickupTime}", fontSize = 11.sp, color = Color(0xFF334155), fontWeight = FontWeight.SemiBold)
                  Text("Waybill SDN: ${order.waybillNumber} | Access Bank Ref: ${order.paymentReference.ifBlank { "Pending Verification" }}", fontSize = 11.sp, color = SharonCrimson)

                  Spacer(modifier = Modifier.height(8.dp))

                  // Interactive Map Simulation View
                  Box(
                    modifier = Modifier
                      .fillMaxWidth()
                      .height(90.dp)
                      .clip(RoundedCornerShape(8.dp))
                      .background(Color(0xFFE2E8F0)),
                    contentAlignment = Alignment.Center
                  ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                      Icon(Icons.Default.Navigation, contentDescription = null, tint = SharonBluePrimary, modifier = Modifier.size(24.dp))
                      Text("Radar: In Factory Prep & Staging Bay", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = SharonBluePrimary)
                      Text("GPS Coordinates: 6.4361° N, 7.3983° E (9th Mile)", fontSize = 10.sp, color = Color.Gray)
                    }
                  }
                }
              }
            }
          }
        }
      }

      // Depots Header
      item {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text(
            text = "Sharon Factory & Depots (${depots.size})",
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
            color = SharonBluePrimary
          )
          Text(
            text = "Tue - Sat, 9am - 5pm",
            style = MaterialTheme.typography.bodySmall.copy(color = SharonGreen, fontWeight = FontWeight.Bold)
          )
        }
      }

      // Depots Cards
      items(depots, key = { it.id }) { depot ->
        DepotItemCard(
          depot = depot,
          onCall = {
            val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:${depot.cleanPhone}"))
            context.startActivity(intent)
          },
          onWhatsapp = {
            val url = "https://wa.me/${depot.cleanWhatsapp}?text=Hello%20${depot.managerName},%20I%20am%20inquirying%20about%20Sharon%20Paints%20from%20the%20app"
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            context.startActivity(intent)
          },
          onNavigate = {
            val uri = Uri.parse("geo:${depot.latitude},${depot.longitude}?q=${depot.latitude},${depot.longitude}(${Uri.encode(depot.name)})")
            val mapIntent = Intent(Intent.ACTION_VIEW, uri)
            context.startActivity(mapIntent)
          }
        )
      }

      item {
        Spacer(modifier = Modifier.height(24.dp))
      }
    }
  }
}

@Composable
fun DepotItemCard(
  depot: Depot,
  onCall: () -> Unit,
  onWhatsapp: () -> Unit,
  onNavigate: () -> Unit
) {
  Card(
    modifier = Modifier
      .fillMaxWidth()
      .testTag("depot_card_${depot.id}"),
    colors = CardDefaults.cardColors(containerColor = Color.White),
    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
    shape = RoundedCornerShape(14.dp),
    border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(Color(0xFFE2E8F0)))
  ) {
    Column(modifier = Modifier.padding(14.dp)) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Top
      ) {
        Column(modifier = Modifier.weight(1f)) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
              text = depot.name,
              style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
              color = SharonBluePrimary
            )
            if (depot.isHeadquarters) {
              Spacer(modifier = Modifier.width(6.dp))
              Box(
                modifier = Modifier
                  .background(SharonCrimson, RoundedCornerShape(4.dp))
                  .padding(horizontal = 6.dp, vertical = 2.dp)
              ) {
                Text("HQ / Factory", color = Color.White, fontSize = 9.sp, fontWeight = FontWeight.Bold)
              }
            }
          }
          Text(
            text = depot.townState,
            style = MaterialTheme.typography.bodySmall.copy(color = Color(0xFF0284C7), fontWeight = FontWeight.SemiBold)
          )
        }

        IconButton(
          onClick = onNavigate,
          modifier = Modifier.testTag("map_nav_btn_${depot.id}").background(Color(0xFFEFF6FF), CircleShape)
        ) {
          Icon(Icons.Default.Navigation, contentDescription = "Navigate", tint = Color(0xFF2563EB), modifier = Modifier.size(20.dp))
        }
      }

      Spacer(modifier = Modifier.height(6.dp))

      Text(
        text = "Address: ${depot.address}",
        style = MaterialTheme.typography.bodySmall.copy(color = Color(0xFF334155), fontSize = 11.sp)
      )

      Spacer(modifier = Modifier.height(8.dp))

      // Manager and contact box
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .clip(RoundedCornerShape(8.dp))
          .background(Color(0xFFF8FAFC))
          .padding(8.dp)
      ) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Column {
            Text(
              text = "Manager: ${depot.managerName}",
              style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold, color = SharonBluePrimary)
            )
            Text(
              text = depot.managerRole,
              style = MaterialTheme.typography.bodySmall.copy(fontSize = 10.sp, color = Color.Gray)
            )
            Text(
              text = "Phone: ${depot.phone}",
              style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp, color = Color(0xFF0F172A), fontWeight = FontWeight.SemiBold)
            )
          }

          Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            // Direct call button
            FilledTonalButton(
              onClick = onCall,
              modifier = Modifier.testTag("call_btn_${depot.id}"),
              contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
              colors = ButtonDefaults.filledTonalButtonColors(containerColor = Color(0xFFE0E7FF))
            ) {
              Icon(Icons.Default.Phone, contentDescription = "Call", modifier = Modifier.size(16.dp), tint = SharonBluePrimary)
              Spacer(modifier = Modifier.width(4.dp))
              Text("Call", fontSize = 11.sp, color = SharonBluePrimary)
            }

            // Direct WhatsApp button
            Button(
              onClick = onWhatsapp,
              modifier = Modifier.testTag("whatsapp_btn_${depot.id}"),
              contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
              colors = ButtonDefaults.buttonColors(containerColor = SharonGreen)
            ) {
              Icon(Icons.Default.Chat, contentDescription = "WhatsApp", modifier = Modifier.size(16.dp), tint = Color.White)
              Spacer(modifier = Modifier.width(4.dp))
              Text("WhatsApp", fontSize = 11.sp, color = Color.White)
            }
          }
        }
      }

      Spacer(modifier = Modifier.height(6.dp))

      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
      ) {
        Text(
          text = "Hours: ${depot.workingDays}, ${depot.workingHours}",
          style = MaterialTheme.typography.bodySmall.copy(fontSize = 10.sp, color = Color.Gray)
        )
        Text(
          text = "GPS: ${depot.latitude}, ${depot.longitude}",
          style = MaterialTheme.typography.bodySmall.copy(fontSize = 10.sp, color = Color.Gray)
        )
      }
    }
  }
}
