package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.SharonBluePrimary
import com.example.ui.theme.SharonGreen

@Composable
fun WorkingHoursCard(
  modifier: Modifier = Modifier
) {
  Row(
    modifier = modifier
      .fillMaxWidth()
      .clip(RoundedCornerShape(12.dp))
      .background(Color(0xFFF0FDF4))
      .padding(horizontal = 12.dp, vertical = 8.dp)
      .testTag("working_hours_card"),
    verticalAlignment = Alignment.CenterVertically
  ) {
    Icon(
      imageVector = Icons.Default.AccessTime,
      contentDescription = "Working Hours",
      tint = SharonGreen,
      modifier = Modifier.size(20.dp)
    )
    Spacer(modifier = Modifier.width(8.dp))
    Column {
      Text(
        text = "Factory & Depot Schedule: Tuesday – Saturday",
        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold, color = SharonGreen)
      )
      Text(
        text = "Operating Hours: 9:00 AM – 5:00 PM (Closed Sundays & Mondays)",
        style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp, color = Color(0xFF166534))
      )
    }
  }
}
