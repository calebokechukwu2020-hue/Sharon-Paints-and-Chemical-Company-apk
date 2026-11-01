package com.example.ui.screens

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
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
import com.example.data.model.*
import com.example.ui.AppNavDestination
import com.example.ui.SharonViewModel
import com.example.ui.components.AccessBankNoticeCard
import com.example.ui.components.CertificationLogosRow
import com.example.ui.components.WorkingHoursCard
import com.example.ui.theme.SharonBluePrimary
import com.example.ui.theme.SharonCrimson
import com.example.ui.theme.SharonGold
import com.example.ui.theme.SharonGreen
import java.text.NumberFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CatalogScreen(
  viewModel: SharonViewModel,
  modifier: Modifier = Modifier
) {
  val context = LocalContext.current
  val products by viewModel.repository.productsFlow.collectAsState()
  val priceRevisionNotes by viewModel.repository.priceListRevisionNotes.collectAsState()
  var selectedCategory by remember { mutableStateOf<PaintCategory?>(null) }
  var searchQuery by remember { mutableStateOf("") }
  var showPriceListDialog by remember { mutableStateOf(false) }

  val filteredProducts = remember(products, selectedCategory, searchQuery) {
    products.filter { product ->
      (selectedCategory == null || product.category == selectedCategory) &&
      (searchQuery.isBlank() || product.name.contains(searchQuery, ignoreCase = true) || product.uppercaseTitle.contains(searchQuery, ignoreCase = true) || product.shortDescription.contains(searchQuery, ignoreCase = true))
    }
  }

  val currencyFormat = remember {
    NumberFormat.getNumberInstance(Locale.US)
  }

  Scaffold(
    topBar = {
      TopAppBar(
        title = {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Image(
              painter = painterResource(id = R.drawable.ic_sharon_logo),
              contentDescription = "Sharon Paints",
              modifier = Modifier.size(36.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Column {
              Text(
                text = "Sharon Paints & Chemicals",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = SharonBluePrimary
              )
              Text(
                text = "The World Pace-Setters In Paint Making",
                style = MaterialTheme.typography.bodySmall.copy(fontSize = 10.sp, color = Color.Gray)
              )
            }
          }
        },
        actions = {
          // View complete price list button
          FilledTonalButton(
            onClick = { showPriceListDialog = true },
            modifier = Modifier.testTag("open_price_list_button"),
            contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp)
          ) {
            Icon(Icons.Default.PriceCheck, contentDescription = "Price List", modifier = Modifier.size(16.dp))
            Spacer(modifier = Modifier.width(4.dp))
            Text("Price List", fontSize = 12.sp)
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
      // Certification logos banner (Sharon, MANCAP, SON, NIS)
      item {
        CertificationLogosRow(modifier = Modifier.padding(top = 8.dp))
      }

      // Working hours schedule (Tue - Sat, 9am - 5pm)
      item {
        WorkingHoursCard()
      }

      // Access bank payment notice
      item {
        AccessBankNoticeCard()
      }

      // Interactive Paint Coverage Tool Quick Action Card
      item {
        Card(
          modifier = Modifier
            .fillMaxWidth()
            .clickable { viewModel.navigateTo(AppNavDestination.CALCULATOR) }
            .testTag("paint_coverage_tool_card"),
          colors = CardDefaults.cardColors(containerColor = Color(0xFFEFF6FF)),
          shape = RoundedCornerShape(14.dp),
          border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(Color(0xFF93C5FD)))
        ) {
          Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Box(
              modifier = Modifier
                .size(44.dp)
                .clip(CircleShape)
                .background(SharonBluePrimary),
              contentAlignment = Alignment.Center
            ) {
              Icon(Icons.Default.SquareFoot, contentDescription = null, tint = Color.White, modifier = Modifier.size(24.dp))
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
              Text("AI Paint Coverage Tool", fontWeight = FontWeight.Bold, color = SharonBluePrimary, fontSize = 13.sp)
              Text("Calculate wall m², required 20L / 10L / 4L units & cost estimation", fontSize = 11.sp, color = Color(0xFF475569))
            }
            Icon(Icons.Default.ArrowForwardIos, contentDescription = null, tint = SharonBluePrimary, modifier = Modifier.size(16.dp))
          }
        }
      }

      // Search bar
      item {
        OutlinedTextField(
          value = searchQuery,
          onValueChange = { searchQuery = it },
          placeholder = { Text("Search paints (e.g. Silk, Texcoat, Putty, Auto)...") },
          leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search") },
          trailingIcon = {
            if (searchQuery.isNotEmpty()) {
              IconButton(onClick = { searchQuery = "" }) {
                Icon(Icons.Default.Clear, contentDescription = "Clear")
              }
            }
          },
          modifier = Modifier
            .fillMaxWidth()
            .testTag("paint_search_input"),
          shape = RoundedCornerShape(12.dp),
          singleLine = true
        )
      }

      // Category filter chips
      item {
        LazyRow(
          horizontalArrangement = Arrangement.spacedBy(8.dp),
          modifier = Modifier.fillMaxWidth()
        ) {
          item {
            FilterChip(
              selected = selectedCategory == null,
              onClick = { selectedCategory = null },
              label = { Text("All Products (${products.size})") },
              modifier = Modifier.testTag("filter_all")
            )
          }
          items(PaintCategory.values()) { cat ->
            FilterChip(
              selected = selectedCategory == cat,
              onClick = { selectedCategory = cat },
              label = { Text(cat.title) },
              modifier = Modifier.testTag("filter_${cat.name.lowercase()}")
            )
          }
        }
      }

      // Products listing
      items(filteredProducts, key = { it.id }) { product ->
        PaintProductItemCard(
          product = product,
          viewModel = viewModel,
          currencyFormat = currencyFormat,
          onAddToCart = { size, color, qty ->
            viewModel.addToCart(product, size, color, qty)
            Toast.makeText(context, "Added $qty x ${product.name} (${size.label}) to cart", Toast.LENGTH_SHORT).show()
          }
        )
      }

      item {
        Spacer(modifier = Modifier.height(30.dp))
      }
    }
  }

  // Price List Dialog
  if (showPriceListDialog) {
    AlertDialog(
      onDismissRequest = { showPriceListDialog = false },
      confirmButton = {
        Button(
          onClick = { showPriceListDialog = false },
          modifier = Modifier.testTag("close_price_list_btn")
        ) {
          Text("Close Price List")
        }
      },
      title = {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Icon(Icons.Default.ReceiptLong, contentDescription = null, tint = SharonBluePrimary)
          Spacer(modifier = Modifier.width(8.dp))
          Text("Official Sharon Paints Price List", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold))
        }
      },
      text = {
        LazyColumn(
          modifier = Modifier
            .fillMaxWidth()
            .height(430.dp),
          verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          item {
            Card(
              colors = CardDefaults.cardColors(containerColor = Color(0xFFEFF6FF)),
              shape = RoundedCornerShape(8.dp),
              modifier = Modifier.fillMaxWidth()
            ) {
              Column(modifier = Modifier.padding(8.dp)) {
                Text(
                  text = priceRevisionNotes,
                  style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold, color = SharonBluePrimary)
                )
                Text(
                  text = "Official Sharon Paints & Chemical Co. Rates in Nigerian Naira (₦). Certified NIS No. CT-1620 / MANCAP Approved.",
                  style = MaterialTheme.typography.bodySmall.copy(fontSize = 10.sp, color = Color(0xFF475569))
                )
              }
            }
            Divider(modifier = Modifier.padding(vertical = 6.dp))
          }
          items(products) { prod ->
            Card(
              modifier = Modifier.fillMaxWidth(),
              colors = CardDefaults.cardColors(containerColor = Color(0xFFF8FAFC)),
              shape = RoundedCornerShape(8.dp)
            ) {
              Column(modifier = Modifier.padding(10.dp)) {
                Row(
                  modifier = Modifier.fillMaxWidth(),
                  horizontalArrangement = Arrangement.SpaceBetween,
                  verticalAlignment = Alignment.CenterVertically
                ) {
                  Text(
                    text = prod.uppercaseTitle,
                    style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.ExtraBold, color = SharonBluePrimary)
                  )
                  Text(
                    text = prod.category.title,
                    style = MaterialTheme.typography.bodySmall.copy(fontSize = 10.sp, color = Color(0xFF64748B))
                  )
                }
                Spacer(modifier = Modifier.height(4.dp))
                prod.availableSizes.forEach { size ->
                  Row(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 2.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                  ) {
                    Text("${size.label}:", style = MaterialTheme.typography.bodySmall)
                    Text(
                      "₦${currencyFormat.format(prod.getPrice(size))}",
                      style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold, color = SharonCrimson)
                    )
                  }
                }
              }
            }
          }
        }
      }
    )
  }
}

/**
 * Beautiful capital-lettered typography badge sampling paint products without photos,
 * as explicitly instructed by the user.
 */
@Composable
fun PaintLetteredTypographyBadge(
  product: PaintProduct,
  modifier: Modifier = Modifier
) {
  val (gradientColors, borderColor, accentTagColor) = when (product.category) {
    PaintCategory.PREMIUM_MATT -> Triple(
      listOf(Color(0xFF2E0854), Color(0xFF4C1D95)),
      SharonGold,
      SharonGold
    )
    PaintCategory.TEXTURED -> Triple(
      listOf(Color(0xFF451A03), Color(0xFF78350F)),
      Color(0xFFFBBF24),
      Color(0xFFFDE68A)
    )
    PaintCategory.SPECIALIZED -> Triple(
      listOf(Color(0xFF064E3B), Color(0xFF047857)),
      Color(0xFF34D399),
      Color(0xFFA7F3D0)
    )
    PaintCategory.AUTOMOBILE -> Triple(
      listOf(Color(0xFF0F172A), Color(0xFF1E293B)),
      SharonCrimson,
      Color(0xFFFDA4AF)
    )
    PaintCategory.SURFACE_PREP -> Triple(
      listOf(Color(0xFF1E293B), Color(0xFF334155)),
      Color(0xFF94A3B8),
      Color(0xFFE2E8F0)
    )
    PaintCategory.DECORATIVE -> Triple(
      listOf(Color(0xFF0C4A6E), Color(0xFF0369A1)),
      SharonGold,
      Color(0xFFBAE6FD)
    )
  }

  Box(
    modifier = modifier
      .size(88.dp)
      .clip(RoundedCornerShape(12.dp))
      .background(androidx.compose.ui.graphics.Brush.verticalGradient(gradientColors))
      .border(1.5.dp, borderColor, RoundedCornerShape(12.dp))
      .padding(5.dp),
    contentAlignment = Alignment.Center
  ) {
    Column(
      modifier = Modifier.fillMaxSize(),
      horizontalAlignment = Alignment.CenterHorizontally,
      verticalArrangement = Arrangement.SpaceBetween
    ) {
      Text(
        text = "SHARON",
        style = MaterialTheme.typography.labelSmall.copy(
          fontSize = 8.sp,
          fontWeight = FontWeight.Black,
          letterSpacing = 1.sp,
          color = accentTagColor
        )
      )

      Text(
        text = product.uppercaseTitle,
        style = MaterialTheme.typography.labelMedium.copy(
          fontWeight = FontWeight.ExtraBold,
          fontSize = if (product.uppercaseTitle.length > 14) 9.sp else 10.sp,
          letterSpacing = 0.5.sp,
          lineHeight = 11.sp,
          color = Color.White
        ),
        textAlign = androidx.compose.ui.text.style.TextAlign.Center,
        maxLines = 3
      )

      val sizeTag = when {
        product.availableSizes == listOf(PaintSize.SIZE_4L) -> "4L ONLY"
        product.availableSizes.size > 1 -> "${product.availableSizes.first().liters}L / ${product.availableSizes.last().liters}L"
        else -> "20L DRUM"
      }
      Box(
        modifier = Modifier
          .clip(RoundedCornerShape(4.dp))
          .background(Color.White.copy(alpha = 0.2f))
          .padding(horizontal = 4.dp, vertical = 1.dp)
      ) {
        Text(
          text = sizeTag,
          fontSize = 7.sp,
          fontWeight = FontWeight.Bold,
          color = Color.White
        )
      }
    }
  }
}

@Composable
fun PaintProductItemCard(
  product: PaintProduct,
  viewModel: SharonViewModel,
  currencyFormat: NumberFormat,
  onAddToCart: (PaintSize, ColorItem, Int) -> Unit
) {
  var selectedSize by remember(product.id) { mutableStateOf(product.availableSizes.first()) }
  var quantity by remember { mutableStateOf(1) }
  var isExpanded by remember { mutableStateOf(false) }
  val activeColor by viewModel.selectedColor.collectAsState()

  Card(
    modifier = Modifier
      .fillMaxWidth()
      .testTag("product_card_${product.id}"),
    colors = CardDefaults.cardColors(containerColor = Color.White),
    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
    shape = RoundedCornerShape(16.dp),
    border = CardDefaults.outlinedCardBorder().copy(
      brush = androidx.compose.ui.graphics.SolidColor(Color(0xFFE2E8F0))
    )
  ) {
    Column(modifier = Modifier.padding(14.dp)) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
      ) {
        // Beautiful Capital-Lettered Typography Badge (No pictures)
        PaintLetteredTypographyBadge(product = product)

        Spacer(modifier = Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
          Text(
            text = product.name,
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
            color = SharonBluePrimary
          )
          Text(
            text = product.shortDescription,
            style = MaterialTheme.typography.bodySmall.copy(color = Color(0xFF64748B), fontSize = 11.sp),
            maxLines = 2
          )
          Spacer(modifier = Modifier.height(4.dp))
          Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
              text = "₦${currencyFormat.format(product.getPrice(selectedSize))}",
              style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.ExtraBold,
                color = SharonCrimson
              )
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
              text = "per ${selectedSize.label}",
              style = MaterialTheme.typography.bodySmall.copy(color = Color.Gray, fontSize = 11.sp)
            )
          }
        }
      }

      Spacer(modifier = Modifier.height(10.dp))

      // Size Selector Segment - Constrained strictly to available sizes for this product
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        product.availableSizes.forEach { size ->
          val isSelected = selectedSize == size
          Box(
            modifier = Modifier
              .weight(1f)
              .clip(RoundedCornerShape(8.dp))
              .background(if (isSelected) SharonBluePrimary else Color(0xFFF1F5F9))
              .clickable { selectedSize = size }
              .padding(vertical = 6.dp),
            contentAlignment = Alignment.Center
          ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
              Text(
                text = size.label,
                style = MaterialTheme.typography.labelSmall.copy(
                  fontWeight = FontWeight.Bold,
                  color = if (isSelected) Color.White else Color(0xFF334155)
                )
              )
              Text(
                text = "₦${currencyFormat.format(product.getPrice(size))}",
                style = MaterialTheme.typography.bodySmall.copy(
                  fontSize = 10.sp,
                  color = if (isSelected) SharonGold else SharonCrimson
                )
              )
            }
          }
        }
      }

      Spacer(modifier = Modifier.height(8.dp))

      // Selected Color Preview & Qty Counter
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        // Selected Color Tag
        Row(
          verticalAlignment = Alignment.CenterVertically,
          modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(Color(0xFFF8FAFC))
            .border(1.dp, Color(0xFFE2E8F0), RoundedCornerShape(8.dp))
            .clickable { viewModel.navigateTo(AppNavDestination.COLORS) }
            .padding(horizontal = 8.dp, vertical = 4.dp)
        ) {
          Box(
            modifier = Modifier
              .size(16.dp)
              .clip(CircleShape)
              .background(activeColor.composeColor)
              .border(1.dp, Color.LightGray, CircleShape)
          )
          Spacer(modifier = Modifier.width(6.dp))
          Text(
            text = "${activeColor.name} (${activeColor.code})",
            style = MaterialTheme.typography.labelSmall.copy(fontSize = 11.sp, color = SharonBluePrimary)
          )
          Icon(Icons.Default.ArrowDropDown, contentDescription = "Change Color", modifier = Modifier.size(16.dp))
        }

        // Quantity selector
        Row(verticalAlignment = Alignment.CenterVertically) {
          IconButton(
            onClick = { if (quantity > 1) quantity-- },
            modifier = Modifier.size(28.dp).background(Color(0xFFF1F5F9), CircleShape)
          ) {
            Text("-", fontWeight = FontWeight.Bold, color = SharonBluePrimary)
          }
          Text(
            text = "$quantity",
            modifier = Modifier.padding(horizontal = 10.dp),
            fontWeight = FontWeight.Bold,
            color = SharonBluePrimary
          )
          IconButton(
            onClick = { quantity++ },
            modifier = Modifier.size(28.dp).background(Color(0xFFF1F5F9), CircleShape)
          ) {
            Text("+", fontWeight = FontWeight.Bold, color = SharonBluePrimary)
          }
        }
      }

      Spacer(modifier = Modifier.height(10.dp))

      // Action row: Add to Order + Expand specs
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
      ) {
        OutlinedButton(
          onClick = { isExpanded = !isExpanded },
          modifier = Modifier.weight(0.9f).testTag("specs_btn_${product.id}"),
          contentPadding = PaddingValues(horizontal = 6.dp, vertical = 6.dp)
        ) {
          Icon(
            if (isExpanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
            contentDescription = null,
            modifier = Modifier.size(16.dp)
          )
          Spacer(modifier = Modifier.width(4.dp))
          Text(if (isExpanded) "Hide Specs" else "Paint Specs", fontSize = 11.sp)
        }

        Button(
          onClick = { onAddToCart(selectedSize, activeColor, quantity) },
          modifier = Modifier.weight(1.3f).testTag("add_cart_btn_${product.id}"),
          colors = ButtonDefaults.buttonColors(containerColor = SharonBluePrimary),
          contentPadding = PaddingValues(horizontal = 8.dp, vertical = 6.dp)
        ) {
          Icon(Icons.Default.ShoppingCart, contentDescription = null, modifier = Modifier.size(16.dp))
          Spacer(modifier = Modifier.width(6.dp))
          Text("Book / Order", fontSize = 12.sp, fontWeight = FontWeight.Bold)
        }
      }

      // Technical Specs Dropdown
      AnimatedVisibility(visible = isExpanded) {
        Column(
          modifier = Modifier
            .fillMaxWidth()
            .padding(top = 10.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(Color(0xFFF8FAFC))
            .padding(10.dp)
        ) {
          Text(
            text = product.fullDescription,
            style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp, color = Color(0xFF334155))
          )
          Spacer(modifier = Modifier.height(8.dp))
          Text("• Coverage: ~${product.coverageSqmPerLiter} m² per liter per coat", fontSize = 11.sp, color = Color(0xFF475569))
          Text("• Drying Time: ${product.dryingTimeHours} hrs (Recoat: ${product.recoatTimeHours} hrs)", fontSize = 11.sp, color = Color(0xFF475569))
          Text("• Recommended Coats: ${product.recommendedCoats} coat(s)", fontSize = 11.sp, color = Color(0xFF475569))
          Text("• Ideal Surfaces: ${product.suitableSurfaces.joinToString(", ")}", fontSize = 11.sp, color = Color(0xFF475569))
          Text("• Application Tools: ${product.applicationTools.joinToString(", ")}", fontSize = 11.sp, color = Color(0xFF475569))
        }
      }
    }
  }
}
