package com.example.ui.screens

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.model.AdvertisementItem
import com.example.ui.SharonViewModel
import com.example.ui.theme.SharonBluePrimary
import com.example.ui.theme.SharonCrimson
import com.example.ui.theme.SharonGold
import com.example.ui.theme.SharonGreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdsMediaScreen(
  viewModel: SharonViewModel,
  modifier: Modifier = Modifier
) {
  val context = LocalContext.current
  val ads = viewModel.repository.advertisements
  val isSpeaking by viewModel.isSpeaking.collectAsState()
  val activeAdId by viewModel.activeVoicePlayingId.collectAsState()

  var selectedAdForVideoModal by remember { mutableStateOf<AdvertisementItem?>(null) }
  var showAdGeneratorDialog by remember { mutableStateOf(false) }

  // Ad generator state
  var targetAudience by remember { mutableStateOf("Architects & Builders") }
  var chosenProduct by remember { mutableStateOf("Vineyard Silk") }
  var generatedAdScript by remember { mutableStateOf<String?>(null) }

  Scaffold(
    topBar = {
      TopAppBar(
        title = {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.Campaign, contentDescription = null, tint = SharonBluePrimary)
            Spacer(modifier = Modifier.width(8.dp))
            Column {
              Text(
                text = "Sharon AI Musics & Video Ads",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = SharonBluePrimary
              )
              Text(
                text = "Continuous Broadcast Jingles & Video Commercials",
                style = MaterialTheme.typography.bodySmall.copy(fontSize = 10.sp, color = Color.Gray)
              )
            }
          }
        },
        actions = {
          FilledTonalButton(
            onClick = { showAdGeneratorDialog = true },
            modifier = Modifier.testTag("open_ad_generator_btn"),
            contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp)
          ) {
            Icon(Icons.Default.AutoAwesome, contentDescription = null, modifier = Modifier.size(16.dp))
            Spacer(modifier = Modifier.width(4.dp))
            Text("AI Generator", fontSize = 11.sp)
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
      item {
        Card(
          modifier = Modifier.fillMaxWidth().testTag("ads_hero_card"),
          colors = CardDefaults.cardColors(containerColor = Color(0xFF0F172A)),
          shape = RoundedCornerShape(14.dp)
        ) {
          Column(modifier = Modifier.padding(14.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Icon(Icons.Default.MusicNote, contentDescription = null, tint = SharonGold)
              Spacer(modifier = Modifier.width(8.dp))
              Text(
                text = "Sharon Paints On-Air Advertising Suite",
                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold, color = Color.White)
              )
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(
              text = "Constant promotion of Sharon Emulsion, Vineyard Silk, Texcoat, and Auto Paints across Nigeria radio stations and TV screens.",
              style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp, color = Color(0xFFCBD5E1))
            )
          }
        }
      }

      // Ad items list
      items(ads, key = { it.id }) { ad ->
        AdItemCard(
          ad = ad,
          isPlaying = isSpeaking && activeAdId == ad.id,
          onPlayAudio = { viewModel.playAdJingle(ad) },
          onStopAudio = { viewModel.stopVoice() },
          onWatchVideo = { selectedAdForVideoModal = ad }
        )
      }

      item {
        Spacer(modifier = Modifier.height(24.dp))
      }
    }
  }

  // Video Reel Modal
  selectedAdForVideoModal?.let { ad ->
    AlertDialog(
      onDismissRequest = { selectedAdForVideoModal = null },
      confirmButton = {
        Button(onClick = { selectedAdForVideoModal = null }) {
          Text("Close Reel")
        }
      },
      title = {
        Text(ad.title, style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold))
      },
      text = {
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
          Box(
            modifier = Modifier
              .fillMaxWidth()
              .height(180.dp)
              .clip(RoundedCornerShape(10.dp))
              .background(Color.Black),
            contentAlignment = Alignment.Center
          ) {
            Image(
              painter = painterResource(id = R.drawable.prod_sharon_vineyard_matt),
              contentDescription = null,
              modifier = Modifier.fillMaxSize(),
              contentScale = ContentScale.Crop
            )
            Box(
              modifier = Modifier
                .size(48.dp)
                .background(Color(0xCC000000), CircleShape),
              contentAlignment = Alignment.Center
            ) {
              Icon(Icons.Default.PlayArrow, contentDescription = null, tint = SharonGold, modifier = Modifier.size(32.dp))
            }
          }

          Text("Tagline: \"${ad.tagLine}\"", fontWeight = FontWeight.Bold, color = SharonBluePrimary, fontSize = 12.sp)
          Text("Storyboard Scene: ${ad.visualDescription}", fontSize = 11.sp, color = Color(0xFF334155))
          Text("Script Voiceover: ${ad.scriptContent}", fontSize = 10.sp, color = Color(0xFF475569))
        }
      }
    )
  }

  // AI Ad Generator Dialog
  if (showAdGeneratorDialog) {
    AlertDialog(
      onDismissRequest = { showAdGeneratorDialog = false },
      confirmButton = {
        Button(
          onClick = {
            generatedAdScript = "Attention all $targetAudience in Nigeria! Elevate your projects with Sharon $chosenProduct. Certified with NIS No. CT-1620, Standards Organisation of Nigeria, and MANCAP. Visit our 9th Mile factory or any Sharon Depot nationwide today. Sharon Paints: The World Pace-Setters in Paint Making!"
            Toast.makeText(context, "New Ad Script Generated!", Toast.LENGTH_SHORT).show()
          },
          modifier = Modifier.testTag("generate_script_btn")
        ) {
          Text("Generate Script")
        }
      },
      dismissButton = {
        TextButton(onClick = { showAdGeneratorDialog = false }) {
          Text("Close")
        }
      },
      title = {
        Text("AI Musics & Video Ads Generator", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold))
      },
      text = {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
          Text("Select target audience & product to craft an instant commercial ad script:", fontSize = 11.sp, color = Color.Gray)
          OutlinedTextField(
            value = targetAudience,
            onValueChange = { targetAudience = it },
            label = { Text("Target Audience (e.g. Master Painters, Landlords)") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
          )
          OutlinedTextField(
            value = chosenProduct,
            onValueChange = { chosenProduct = it },
            label = { Text("Product (e.g. Weather Proof, Texcoat, Gold Matt)") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
          )

          generatedAdScript?.let { script ->
            Spacer(modifier = Modifier.height(6.dp))
            Box(
              modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(8.dp))
                .background(Color(0xFFEFF6FF))
                .padding(10.dp)
            ) {
              Column {
                Text("Generated Broadcast Script:", fontWeight = FontWeight.Bold, fontSize = 10.sp, color = SharonBluePrimary)
                Text(script, fontSize = 10.sp, color = Color(0xFF1E293B))
                Spacer(modifier = Modifier.height(6.dp))
                Button(
                  onClick = { viewModel.voiceSpeaker.speak(script) },
                  colors = ButtonDefaults.buttonColors(containerColor = SharonGreen),
                  modifier = Modifier.align(Alignment.End),
                  contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp)
                ) {
                  Icon(Icons.Default.VolumeUp, contentDescription = null, modifier = Modifier.size(14.dp))
                  Spacer(modifier = Modifier.width(4.dp))
                  Text("Play Audio", fontSize = 10.sp)
                }
              }
            }
          }
        }
      }
    )
  }
}

@Composable
fun AdItemCard(
  ad: AdvertisementItem,
  isPlaying: Boolean,
  onPlayAudio: () -> Unit,
  onStopAudio: () -> Unit,
  onWatchVideo: () -> Unit
) {
  Card(
    modifier = Modifier
      .fillMaxWidth()
      .testTag("ad_card_${ad.id}"),
    colors = CardDefaults.cardColors(containerColor = Color.White),
    shape = RoundedCornerShape(12.dp),
    border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(Color(0xFFE2E8F0))),
    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
  ) {
    Column(modifier = Modifier.padding(14.dp)) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Column(modifier = Modifier.weight(1f)) {
          Text(
            text = ad.title,
            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
            color = SharonBluePrimary
          )
          Text(
            text = "${ad.format} • ${ad.duration}",
            style = MaterialTheme.typography.bodySmall.copy(fontSize = 10.sp, color = SharonCrimson, fontWeight = FontWeight.SemiBold)
          )
        }

        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
          // Play audio jingle
          FilledTonalButton(
            onClick = { if (isPlaying) onStopAudio() else onPlayAudio() },
            colors = ButtonDefaults.filledTonalButtonColors(containerColor = if (isPlaying) SharonCrimson else Color(0xFFE0E7FF)),
            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp)
          ) {
            Icon(if (isPlaying) Icons.Default.Stop else Icons.Default.PlayArrow, contentDescription = null, modifier = Modifier.size(16.dp))
            Spacer(modifier = Modifier.width(4.dp))
            Text(if (isPlaying) "Stop" else "Audio Jingle", fontSize = 10.sp)
          }

          // Watch video reel
          Button(
            onClick = onWatchVideo,
            colors = ButtonDefaults.buttonColors(containerColor = SharonBluePrimary),
            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp)
          ) {
            Icon(Icons.Default.Videocam, contentDescription = null, modifier = Modifier.size(16.dp))
            Spacer(modifier = Modifier.width(4.dp))
            Text("Video Ad", fontSize = 10.sp)
          }
        }
      }

      Spacer(modifier = Modifier.height(6.dp))

      Text(
        text = "\"${ad.tagLine}\"",
        style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp, fontStyle = androidx.compose.ui.text.font.FontStyle.Italic, color = Color(0xFF0F172A), fontWeight = FontWeight.Bold)
      )

      Spacer(modifier = Modifier.height(4.dp))

      Text(
        text = ad.description,
        style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp, color = Color(0xFF475569))
      )
    }
  }
}
