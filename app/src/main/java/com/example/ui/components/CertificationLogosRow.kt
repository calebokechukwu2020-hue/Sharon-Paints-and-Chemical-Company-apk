package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ui.theme.SharonBluePrimary
import com.example.ui.theme.SharonGold
import com.example.ui.theme.SharonGreen

@Composable
fun CertificationLogosRow(
  modifier: Modifier = Modifier
) {
  var showDetails by remember { mutableStateOf(false) }

  Card(
    modifier = modifier
      .fillMaxWidth()
      .testTag("certification_logos_card"),
    colors = CardDefaults.cardColors(
      containerColor = Color(0xFFF1F5F9)
    ),
    shape = RoundedCornerShape(16.dp),
    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
  ) {
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .padding(12.dp)
    ) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Icon(
            imageVector = Icons.Default.Verified,
            contentDescription = "Verified Seals",
            tint = SharonGreen,
            modifier = Modifier.size(20.dp)
          )
          Spacer(modifier = Modifier.width(6.dp))
          Text(
            text = "Official National Quality Certifications",
            style = MaterialTheme.typography.labelLarge.copy(
              fontWeight = FontWeight.Bold,
              color = SharonBluePrimary
            )
          )
        }

        IconButton(
          onClick = { showDetails = !showDetails },
          modifier = Modifier.size(32.dp).testTag("cert_info_toggle")
        ) {
          Icon(
            imageVector = Icons.Default.Info,
            contentDescription = "Toggle info",
            tint = SharonBluePrimary,
            modifier = Modifier.size(18.dp)
          )
        }
      }

      Spacer(modifier = Modifier.height(10.dp))

      // 4 Logos in equal spaced grid
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
      ) {
        // 1. Sharon Company Logo
        CertBadgeItem(
          drawableId = R.drawable.ic_sharon_logo,
          title = "Sharon Paints",
          sub = "Registered",
          testTag = "logo_sharon"
        )

        // 2. MANCAP Logo
        CertBadgeItem(
          drawableId = R.drawable.ic_mancap_logo,
          title = "MANCAP",
          sub = "CT-21237",
          testTag = "logo_mancap"
        )

        // 3. Standards Organisation of Nigeria (SON) Logo
        CertBadgeItem(
          drawableId = R.drawable.ic_son_logo,
          title = "SON",
          sub = "NIS 278/280",
          testTag = "logo_son"
        )

        // 4. Nigeria Mark of Quality (NIS) Logo
        CertBadgeItem(
          drawableId = R.drawable.ic_nis_logo,
          title = "NIS Mark",
          sub = "No. CT.-350",
          testTag = "logo_nis"
        )
      }

      AnimatedVisibility(visible = showDetails) {
        Column(
          modifier = Modifier
            .fillMaxWidth()
            .padding(top = 10.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(Color.White)
            .padding(10.dp)
        ) {
          Text(
            text = "Quality Verification Codes:",
            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold, color = SharonBluePrimary)
          )
          Spacer(modifier = Modifier.height(4.dp))
          Text(
            text = "• NIS Mark of Quality: Certificate No. CT.-350\n• MANCAP: Certificate No. CT-21237 (Mandatory Conformity Assessment Programme)\n• Standards Organisation of Nigeria: Approved under NIS 278 & NIS 280\n• Sharon Paints & Chemical Co. Nig. Ltd. (RC: 1984/Enugu)",
            style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp, color = Color(0xFF334155))
          )
        }
      }
    }
  }
}

@Composable
private fun CertBadgeItem(
  drawableId: Int,
  title: String,
  sub: String,
  testTag: String
) {
  Column(
    horizontalAlignment = Alignment.CenterHorizontally,
    modifier = Modifier
      .testTag(testTag)
      .clip(RoundedCornerShape(10.dp))
      .background(Color.White)
      .border(1.dp, Color(0xFFE2E8F0), RoundedCornerShape(10.dp))
      .padding(horizontal = 8.dp, vertical = 6.dp)
      .width(72.dp)
  ) {
    Image(
      painter = painterResource(id = drawableId),
      contentDescription = title,
      modifier = Modifier.size(36.dp)
    )
    Spacer(modifier = Modifier.height(3.dp))
    Text(
      text = title,
      style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold, fontSize = 10.sp),
      color = SharonBluePrimary,
      maxLines = 1
    )
    Text(
      text = sub,
      style = MaterialTheme.typography.bodySmall.copy(fontSize = 9.sp, color = Color.Gray),
      maxLines = 1
    )
  }
}
