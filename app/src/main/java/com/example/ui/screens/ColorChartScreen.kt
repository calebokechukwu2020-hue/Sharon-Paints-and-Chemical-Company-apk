package com.example.ui.screens

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.model.ColorItem
import com.example.ui.SharonViewModel
import com.example.ui.theme.SharonBluePrimary
import com.example.ui.theme.SharonCrimson
import com.example.ui.theme.SharonGold
import com.example.ui.theme.SharonGreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ColorChartScreen(
  viewModel: SharonViewModel,
  modifier: Modifier = Modifier
) {
  val context = LocalContext.current
  val colors = viewModel.repository.colorCharts
  var selectedCategory by remember { mutableStateOf<String?>(null) }
  var searchQuery by remember { mutableStateOf("") }
  val activeSelectedColor by viewModel.selectedColor.collectAsState()

  var showCustomMixDialog by remember { mutableStateOf(false) }
  var showBrochureScanDialog by remember { mutableStateOf(false) }

  // Custom mix input state
  var customColorName by remember { mutableStateOf("") }
  var customColorHex by remember { mutableStateOf("#4A90E2") }

  val categories = remember {
    colors.map { it.category }.distinct()
  }

  val filteredColors = remember(selectedCategory, searchQuery) {
    colors.filter { c ->
      (selectedCategory == null || c.category == selectedCategory) &&
      (searchQuery.isBlank() || c.name.contains(searchQuery, ignoreCase = true) || c.code.contains(searchQuery, ignoreCase = true))
    }
  }

  Scaffold(
    topBar = {
      TopAppBar(
        title = {
          Column {
            Text(
              text = "Sharon Official Color Charts",
              style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
              color = SharonBluePrimary
            )
            Text(
              text = "Over 40+ Certified Shades & Custom Formulations",
              style = MaterialTheme.typography.bodySmall.copy(fontSize = 10.sp, color = Color.Gray)
            )
          }
        },
        actions = {
          IconButton(
            onClick = { showBrochureScanDialog = true },
            modifier = Modifier.testTag("open_scan_brochure_btn")
          ) {
            Icon(Icons.Default.PhotoLibrary, contentDescription = "Original Printed Chart", tint = SharonBluePrimary)
          }
          IconButton(
            onClick = { showCustomMixDialog = true },
            modifier = Modifier.testTag("open_custom_mix_btn")
          ) {
            Icon(Icons.Default.ColorLens, contentDescription = "Custom Mix", tint = SharonCrimson)
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
      // Search input
      OutlinedTextField(
        value = searchQuery,
        onValueChange = { searchQuery = it },
        placeholder = { Text("Search by code (e.g. 1-024, 9090, SP50) or name...") },
        leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
        trailingIcon = {
          if (searchQuery.isNotEmpty()) {
            IconButton(onClick = { searchQuery = "" }) {
              Icon(Icons.Default.Clear, contentDescription = null)
            }
          }
        },
        modifier = Modifier
          .fillMaxWidth()
          .testTag("color_search_input"),
        shape = RoundedCornerShape(12.dp),
        singleLine = true
      )

      Spacer(modifier = Modifier.height(10.dp))

      // Category chips
      LazyRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.fillMaxWidth()
      ) {
        item {
          FilterChip(
            selected = selectedCategory == null,
            onClick = { selectedCategory = null },
            label = { Text("All Shades (${colors.size})") },
            modifier = Modifier.testTag("color_filter_all")
          )
        }
        items(categories) { cat ->
          FilterChip(
            selected = selectedCategory == cat,
            onClick = { selectedCategory = cat },
            label = { Text(cat) },
            modifier = Modifier.testTag("color_filter_${cat.lowercase().replace(" ", "_")}")
          )
        }
      }

      Spacer(modifier = Modifier.height(10.dp))

      // Active Selection Banner
      Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF8FAFC)),
        shape = RoundedCornerShape(12.dp),
        border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(Color(0xFFE2E8F0)))
      ) {
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp),
          verticalAlignment = Alignment.CenterVertically
        ) {
          Box(
            modifier = Modifier
              .size(40.dp)
              .clip(CircleShape)
              .background(activeSelectedColor.composeColor)
              .border(2.dp, Color.White, CircleShape)
              .border(3.dp, SharonBluePrimary, CircleShape)
          )
          Spacer(modifier = Modifier.width(12.dp))
          Column(modifier = Modifier.weight(1f)) {
            Text(
              text = "Active Paint Color: ${activeSelectedColor.name}",
              style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
              color = SharonBluePrimary
            )
            Text(
              text = "Sharon Code: ${activeSelectedColor.code} | HEX: ${activeSelectedColor.hexValue}",
              style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp, color = Color(0xFF64748B))
            )
          }
          Icon(Icons.Default.CheckCircle, contentDescription = "Active", tint = SharonGreen)
        }
      }

      Spacer(modifier = Modifier.height(10.dp))

      // Grid of Swatches
      LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(10.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        contentPadding = PaddingValues(bottom = 24.dp)
      ) {
        items(filteredColors, key = { it.code }) { colorItem ->
          val isSelected = colorItem.code == activeSelectedColor.code
          Card(
            modifier = Modifier
              .fillMaxWidth()
              .clickable {
                viewModel.selectColor(colorItem)
                Toast.makeText(context, "Selected ${colorItem.name} (${colorItem.code})", Toast.LENGTH_SHORT).show()
              }
              .testTag("swatch_${colorItem.code}"),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            border = if (isSelected) {
              CardDefaults.outlinedCardBorder().copy(
                brush = androidx.compose.ui.graphics.SolidColor(SharonBluePrimary),
                width = 2.dp
              )
            } else {
              CardDefaults.outlinedCardBorder().copy(
                brush = androidx.compose.ui.graphics.SolidColor(Color(0xFFE2E8F0))
              )
            }
          ) {
            Column {
              // Color Swatch Bar
              Box(
                modifier = Modifier
                  .fillMaxWidth()
                  .height(60.dp)
                  .background(colorItem.composeColor)
              ) {
                if (isSelected) {
                  Box(
                    modifier = Modifier
                      .align(Alignment.TopEnd)
                      .padding(6.dp)
                      .size(20.dp)
                      .background(Color.White, CircleShape),
                    contentAlignment = Alignment.Center
                  ) {
                    Icon(
                      imageVector = Icons.Default.Check,
                      contentDescription = "Selected",
                      tint = SharonBluePrimary,
                      modifier = Modifier.size(14.dp)
                    )
                  }
                }
              }

              // Color details
              Column(modifier = Modifier.padding(8.dp)) {
                Text(
                  text = colorItem.name,
                  style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                  color = SharonBluePrimary,
                  maxLines = 1
                )
                Row(
                  modifier = Modifier.fillMaxWidth(),
                  horizontalArrangement = Arrangement.SpaceBetween
                ) {
                  Text(
                    text = "Code: ${colorItem.code}",
                    style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp, color = SharonCrimson, fontWeight = FontWeight.SemiBold)
                  )
                  Text(
                    text = colorItem.hexValue,
                    style = MaterialTheme.typography.bodySmall.copy(fontSize = 10.sp, color = Color.Gray)
                  )
                }
              }
            }
          }
        }
      }
    }
  }

  // Custom Mix Dialog
  if (showCustomMixDialog) {
    AlertDialog(
      onDismissRequest = { showCustomMixDialog = false },
      confirmButton = {
        Button(
          onClick = {
            if (customColorName.isNotBlank()) {
              val generatedCode = "CST-${(100..999).random()}"
              val customItem = ColorItem(
                code = generatedCode,
                name = customColorName,
                category = "Custom Tinting",
                hexValue = customColorHex,
                isChartColor = false,
                description = "Custom tint formulation by Sharon AI Color Lab"
              )
              viewModel.selectColor(customItem)
              showCustomMixDialog = false
              Toast.makeText(context, "Custom Color '$customColorName' ($generatedCode) generated and selected!", Toast.LENGTH_LONG).show()
            }
          },
          modifier = Modifier.testTag("save_custom_color_btn")
        ) {
          Text("Generate Color Code")
        }
      },
      dismissButton = {
        TextButton(onClick = { showCustomMixDialog = false }) {
          Text("Cancel")
        }
      },
      title = {
        Text("AI Custom Tinting & Color Lab", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold))
      },
      text = {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
          Text(
            "Need a custom color not in the standard chart? Input your requested shade name and color hex. Sharon computerized tinting stations will produce your custom mix code.",
            fontSize = 12.sp,
            color = Color.Gray
          )
          OutlinedTextField(
            value = customColorName,
            onValueChange = { customColorName = it },
            label = { Text("Color Name (e.g. Royal Sapphire, Sunset Gold)") },
            modifier = Modifier.fillMaxWidth().testTag("custom_color_name_input"),
            singleLine = true
          )
          OutlinedTextField(
            value = customColorHex,
            onValueChange = { customColorHex = it },
            label = { Text("Color HEX Code (e.g. #3B82F6, #D97706)") },
            modifier = Modifier.fillMaxWidth().testTag("custom_color_hex_input"),
            singleLine = true
          )
          Row(verticalAlignment = Alignment.CenterVertically) {
            Text("Preview Swatch: ", fontSize = 12.sp)
            Spacer(modifier = Modifier.width(8.dp))
            val previewColor = try {
              Color(android.graphics.Color.parseColor(customColorHex))
            } catch (_: Exception) {
              Color.Gray
            }
            Box(
              modifier = Modifier
                .size(36.dp)
                .clip(CircleShape)
                .background(previewColor)
                .border(1.dp, Color.Gray, CircleShape)
            )
          }
        }
      }
    )
  }

  // Brochure Scan Dialog
  if (showBrochureScanDialog) {
    AlertDialog(
      onDismissRequest = { showBrochureScanDialog = false },
      confirmButton = {
        Button(onClick = { showBrochureScanDialog = false }) {
          Text("Done")
        }
      },
      title = {
        Text("Original Sharon Printed Charts", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold))
      },
      text = {
        LazyColumn(
          modifier = Modifier
            .fillMaxWidth()
            .height(400.dp),
          verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
          item {
            Text("1. Warm Decorative Shades (Greys, Beiges, Browns, Yellows, Oranges, Reds)", fontSize = 12.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(4.dp))
            Image(
              painter = painterResource(id = R.drawable.chart_sharon_warm_colors),
              contentDescription = "Warm Colors Chart",
              modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .clip(RoundedCornerShape(8.dp)),
              contentScale = ContentScale.Crop
            )
          }
          item {
            Text("2. Cool Decorative & Auto Shades (Blues, Greens, Violets, Metallics)", fontSize = 12.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(4.dp))
            Image(
              painter = painterResource(id = R.drawable.chart_sharon_cool_colors),
              contentDescription = "Cool Colors Chart",
              modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .clip(RoundedCornerShape(8.dp)),
              contentScale = ContentScale.Crop
            )
          }
        }
      }
    )
  }
}
