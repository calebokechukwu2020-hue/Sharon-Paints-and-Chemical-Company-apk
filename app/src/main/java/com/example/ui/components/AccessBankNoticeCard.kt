package com.example.ui.components

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.SharonBluePrimary
import com.example.ui.theme.SharonCrimson
import com.example.ui.theme.SharonGold

@Composable
fun AccessBankNoticeCard(
  modifier: Modifier = Modifier
) {
  val clipboardManager = LocalClipboardManager.current
  val context = LocalContext.current
  val accNumber = "0104392154"

  Card(
    modifier = modifier
      .fillMaxWidth()
      .testTag("access_bank_card"),
    colors = CardDefaults.cardColors(
      containerColor = Color(0xFFF8FAFC)
    ),
    shape = RoundedCornerShape(16.dp),
    border = CardDefaults.outlinedCardBorder().copy(
      brush = androidx.compose.ui.graphics.SolidColor(Color(0xFFCBD5E1))
    )
  ) {
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .padding(14.dp)
    ) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Icon(
            imageVector = Icons.Default.AccountBalance,
            contentDescription = "Bank",
            tint = SharonBluePrimary,
            modifier = Modifier.size(24.dp)
          )
          Spacer(modifier = Modifier.width(8.dp))
          Column {
            Text(
              text = "Official Company Bank Account",
              style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
              color = SharonBluePrimary
            )
            Text(
              text = "Access Bank PLC",
              style = MaterialTheme.typography.bodySmall.copy(color = Color(0xFF0284C7), fontWeight = FontWeight.SemiBold)
            )
          }
        }

        OutlinedButton(
          onClick = {
            clipboardManager.setText(AnnotatedString(accNumber))
            Toast.makeText(context, "Account number 0104392154 copied to clipboard!", Toast.LENGTH_SHORT).show()
          },
          modifier = Modifier.testTag("copy_acc_button"),
          contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp)
        ) {
          Icon(Icons.Default.ContentCopy, contentDescription = "Copy", modifier = Modifier.size(16.dp))
          Spacer(modifier = Modifier.width(4.dp))
          Text("Copy", fontSize = 12.sp)
        }
      }

      Spacer(modifier = Modifier.height(10.dp))

      // Account box
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .clip(RoundedCornerShape(10.dp))
          .background(Color.White)
          .border(1.dp, Color(0xFFE2E8F0), RoundedCornerShape(10.dp))
          .padding(12.dp)
      ) {
        Column {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
          ) {
            Text(
              text = "Account Number:",
              style = MaterialTheme.typography.bodyMedium.copy(color = Color(0xFF64748B))
            )
            Text(
              text = accNumber,
              style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.ExtraBold,
                color = SharonBluePrimary,
                letterSpacing = 1.sp
              )
            )
          }

          Spacer(modifier = Modifier.height(4.dp))

          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
          ) {
            Text(
              text = "Account Name:",
              style = MaterialTheme.typography.bodyMedium.copy(color = Color(0xFF64748B))
            )
            Text(
              text = "Sharon paints and chemical company",
              style = MaterialTheme.typography.bodyMedium.copy(
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1E293B)
              )
            )
          }
        }
      }

      Spacer(modifier = Modifier.height(8.dp))

      // Anti-fraud security warning
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .clip(RoundedCornerShape(8.dp))
          .background(Color(0xFFFEF2F2))
          .border(1.dp, Color(0xFFFCA5A5), RoundedCornerShape(8.dp))
          .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
      ) {
        Icon(
          imageVector = Icons.Default.Warning,
          contentDescription = "Warning",
          tint = SharonCrimson,
          modifier = Modifier.size(18.dp)
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text(
          text = "SECURITY NOTICE: Do NOT use Zenith Bank for app transactions. All payments must be made strictly to Access Bank 0104392154.",
          style = MaterialTheme.typography.bodySmall.copy(
            color = SharonCrimson,
            fontWeight = FontWeight.Bold,
            fontSize = 10.sp
          )
        )
      }
    }
  }
}
