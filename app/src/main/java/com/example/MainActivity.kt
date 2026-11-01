package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.AppNavDestination
import com.example.ui.SharonViewModel
import com.example.ui.screens.*
import com.example.ui.theme.SharonBluePrimary
import com.example.ui.theme.SharonCrimson
import com.example.ui.theme.SharonGold
import com.example.ui.theme.SharonPaintsTheme

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    setContent {
      SharonPaintsTheme {
        val viewModel: SharonViewModel = viewModel()
        SharonAppMain(viewModel = viewModel)
      }
    }
  }
}

@Composable
fun SharonAppMain(viewModel: SharonViewModel) {
  val currentDest by viewModel.currentDestination.collectAsState()
  val cart by viewModel.repository.cart.collectAsState()
  val cartCount = cart.sumOf { it.quantity }

  // Handle system back button: return to Catalog if currently on other screens
  BackHandler(enabled = currentDest != AppNavDestination.CATALOG) {
    viewModel.navigateTo(AppNavDestination.CATALOG)
  }

  Scaffold(
    modifier = Modifier.fillMaxSize(),
    bottomBar = {
      NavigationBar(
        containerColor = Color.White,
        contentColor = SharonBluePrimary,
        tonalElevation = 8.dp,
        modifier = Modifier.testTag("main_bottom_nav")
      ) {
        // 1. Paints Catalog
        NavigationBarItem(
          selected = currentDest == AppNavDestination.CATALOG,
          onClick = { viewModel.navigateTo(AppNavDestination.CATALOG) },
          icon = {
            Icon(
              if (currentDest == AppNavDestination.CATALOG) Icons.Filled.FormatPaint else Icons.Outlined.FormatPaint,
              contentDescription = "Paints"
            )
          },
          label = { Text("Paints", fontSize = 10.sp) },
          colors = NavigationBarItemDefaults.colors(
            selectedIconColor = SharonBluePrimary,
            selectedTextColor = SharonBluePrimary,
            indicatorColor = Color(0xFFE0E7FF)
          ),
          modifier = Modifier.testTag("nav_item_catalog")
        )

        // 2. Color Chart
        NavigationBarItem(
          selected = currentDest == AppNavDestination.COLORS,
          onClick = { viewModel.navigateTo(AppNavDestination.COLORS) },
          icon = {
            Icon(
              if (currentDest == AppNavDestination.COLORS) Icons.Filled.Palette else Icons.Outlined.Palette,
              contentDescription = "Colors"
            )
          },
          label = { Text("Colors", fontSize = 10.sp) },
          colors = NavigationBarItemDefaults.colors(
            selectedIconColor = SharonBluePrimary,
            selectedTextColor = SharonBluePrimary,
            indicatorColor = Color(0xFFE0E7FF)
          ),
          modifier = Modifier.testTag("nav_item_colors")
        )

        // 3. Depots & GPS
        NavigationBarItem(
          selected = currentDest == AppNavDestination.DEPOTS,
          onClick = { viewModel.navigateTo(AppNavDestination.DEPOTS) },
          icon = {
            Icon(
              if (currentDest == AppNavDestination.DEPOTS) Icons.Filled.LocationOn else Icons.Outlined.LocationOn,
              contentDescription = "Depots"
            )
          },
          label = { Text("Depots", fontSize = 10.sp) },
          colors = NavigationBarItemDefaults.colors(
            selectedIconColor = SharonBluePrimary,
            selectedTextColor = SharonBluePrimary,
            indicatorColor = Color(0xFFE0E7FF)
          ),
          modifier = Modifier.testTag("nav_item_depots")
        )

        // 4. AI Voice Tutor & Consultant
        NavigationBarItem(
          selected = currentDest == AppNavDestination.VOICE_AI,
          onClick = { viewModel.navigateTo(AppNavDestination.VOICE_AI) },
          icon = {
            Icon(
              if (currentDest == AppNavDestination.VOICE_AI) Icons.Filled.RecordVoiceOver else Icons.Outlined.RecordVoiceOver,
              contentDescription = "AI Voice"
            )
          },
          label = { Text("AI Tutor", fontSize = 10.sp) },
          colors = NavigationBarItemDefaults.colors(
            selectedIconColor = SharonBluePrimary,
            selectedTextColor = SharonBluePrimary,
            indicatorColor = Color(0xFFE0E7FF)
          ),
          modifier = Modifier.testTag("nav_item_voice")
        )

        // 5. AI Calculator
        NavigationBarItem(
          selected = currentDest == AppNavDestination.CALCULATOR,
          onClick = { viewModel.navigateTo(AppNavDestination.CALCULATOR) },
          icon = {
            Icon(
              if (currentDest == AppNavDestination.CALCULATOR) Icons.Filled.Calculate else Icons.Outlined.Calculate,
              contentDescription = "Calculator"
            )
          },
          label = { Text("Calculator", fontSize = 10.sp) },
          colors = NavigationBarItemDefaults.colors(
            selectedIconColor = SharonBluePrimary,
            selectedTextColor = SharonBluePrimary,
            indicatorColor = Color(0xFFE0E7FF)
          ),
          modifier = Modifier.testTag("nav_item_calculator")
        )

        // 6. Booking / Cart
        NavigationBarItem(
          selected = false,
          onClick = { viewModel.navigateTo(AppNavDestination.INVOICES) },
          icon = {
            BadgedBox(
              badge = {
                if (cartCount > 0) {
                  Badge(containerColor = SharonCrimson) {
                    Text("$cartCount", color = Color.White)
                  }
                }
              }
            ) {
              Icon(
                if (currentDest == AppNavDestination.INVOICES) Icons.Filled.ReceiptLong else Icons.Outlined.ReceiptLong,
                contentDescription = "Docs"
              )
            }
          },
          label = { Text("Docs", fontSize = 10.sp) },
          colors = NavigationBarItemDefaults.colors(
            selectedIconColor = SharonBluePrimary,
            selectedTextColor = SharonBluePrimary,
            indicatorColor = Color(0xFFE0E7FF)
          ),
          modifier = Modifier.testTag("nav_item_docs")
        )

        // 7. AI Ads
        NavigationBarItem(
          selected = currentDest == AppNavDestination.ADS,
          onClick = { viewModel.navigateTo(AppNavDestination.ADS) },
          icon = {
            Icon(
              if (currentDest == AppNavDestination.ADS) Icons.Filled.Campaign else Icons.Outlined.Campaign,
              contentDescription = "Ads"
            )
          },
          label = { Text("AI Ads", fontSize = 10.sp) },
          colors = NavigationBarItemDefaults.colors(
            selectedIconColor = SharonBluePrimary,
            selectedTextColor = SharonBluePrimary,
            indicatorColor = Color(0xFFE0E7FF)
          ),
          modifier = Modifier.testTag("nav_item_ads")
        )

        // 8. Admin Portal
        NavigationBarItem(
          selected = currentDest == AppNavDestination.ADMIN,
          onClick = { viewModel.navigateTo(AppNavDestination.ADMIN) },
          icon = {
            Icon(
              if (currentDest == AppNavDestination.ADMIN) Icons.Filled.AdminPanelSettings else Icons.Outlined.AdminPanelSettings,
              contentDescription = "Admin"
            )
          },
          label = { Text("Admin", fontSize = 10.sp) },
          colors = NavigationBarItemDefaults.colors(
            selectedIconColor = SharonBluePrimary,
            selectedTextColor = SharonBluePrimary,
            indicatorColor = Color(0xFFE0E7FF)
          ),
          modifier = Modifier.testTag("nav_item_admin")
        )
      }
    }
  ) { innerPadding ->
    Box(
      modifier = Modifier
        .fillMaxSize()
        .padding(innerPadding)
    ) {
      when (currentDest) {
        AppNavDestination.CATALOG -> CatalogScreen(viewModel = viewModel)
        AppNavDestination.COLORS -> ColorChartScreen(viewModel = viewModel)
        AppNavDestination.DEPOTS -> DepotsMapScreen(viewModel = viewModel)
        AppNavDestination.VOICE_AI -> VoiceConsultantScreen(viewModel = viewModel)
        AppNavDestination.CALCULATOR -> CalculatorScreen(viewModel = viewModel)
        AppNavDestination.INVOICES -> InvoicesWaybillsScreen(viewModel = viewModel)
        AppNavDestination.ADS -> AdsMediaScreen(viewModel = viewModel)
        AppNavDestination.ADMIN -> AdminPortalScreen(viewModel = viewModel)
      }
    }
  }
}
