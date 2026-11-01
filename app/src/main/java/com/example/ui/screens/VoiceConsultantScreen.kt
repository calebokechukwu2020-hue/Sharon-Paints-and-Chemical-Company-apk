package com.example.ui.screens

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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.PaintingIssue
import com.example.data.model.VoiceLesson
import com.example.ui.SharonViewModel
import com.example.ui.theme.SharonBluePrimary
import com.example.ui.theme.SharonCrimson
import com.example.ui.theme.SharonGold
import com.example.ui.theme.SharonGreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VoiceConsultantScreen(
  viewModel: SharonViewModel,
  modifier: Modifier = Modifier
) {
  var selectedTab by remember { mutableStateOf(0) } // 0 = Error Consultant, 1 = Voice Tutor
  val issues = viewModel.repository.paintingIssues
  val lessons = viewModel.repository.tutorLessons
  val isSpeaking by viewModel.isSpeaking.collectAsState()
  val activeVoiceId by viewModel.activeVoicePlayingId.collectAsState()

  var customQuestionText by remember { mutableStateOf("") }
  var customAnswerText by remember { mutableStateOf<String?>(null) }

  Scaffold(
    topBar = {
      TopAppBar(
        title = {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.RecordVoiceOver, contentDescription = null, tint = SharonBluePrimary)
            Spacer(modifier = Modifier.width(8.dp))
            Column {
              Text(
                text = "Sharon AI Voice Consultant & Tutor",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = SharonBluePrimary
              )
              Text(
                text = "Audible Q&A Advisor & Professional Paint Lessons",
                style = MaterialTheme.typography.bodySmall.copy(fontSize = 10.sp, color = Color.Gray)
              )
            }
          }
        },
        actions = {
          if (isSpeaking) {
            IconButton(
              onClick = { viewModel.stopVoice() },
              modifier = Modifier.testTag("stop_voice_btn")
            ) {
              Icon(Icons.Default.StopCircle, contentDescription = "Stop", tint = SharonCrimson)
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
      // Tab selector
      TabRow(
        selectedTabIndex = selectedTab,
        containerColor = Color.White,
        contentColor = SharonBluePrimary,
        modifier = Modifier.clip(RoundedCornerShape(12.dp))
      ) {
        Tab(
          selected = selectedTab == 0,
          onClick = { selectedTab = 0 },
          text = {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Icon(Icons.Default.HelpOutline, contentDescription = null, modifier = Modifier.size(16.dp))
              Spacer(modifier = Modifier.width(4.dp))
              Text("Error Consultant (Q&A)", fontWeight = FontWeight.Bold)
            }
          },
          modifier = Modifier.testTag("tab_consultant")
        )
        Tab(
          selected = selectedTab == 1,
          onClick = { selectedTab = 1 },
          text = {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Icon(Icons.Default.School, contentDescription = null, modifier = Modifier.size(16.dp))
              Spacer(modifier = Modifier.width(4.dp))
              Text("Voice Tutor (Lessons)", fontWeight = FontWeight.Bold)
            }
          },
          modifier = Modifier.testTag("tab_tutor")
        )
      }

      Spacer(modifier = Modifier.height(10.dp))

      // Active Voice Banner (Audio visualizer)
      if (isSpeaking) {
        Card(
          modifier = Modifier.fillMaxWidth().testTag("active_voice_banner"),
          colors = CardDefaults.cardColors(containerColor = SharonBluePrimary),
          shape = RoundedCornerShape(12.dp)
        ) {
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
          ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Icon(Icons.Default.GraphicEq, contentDescription = null, tint = SharonGold, modifier = Modifier.size(24.dp))
              Spacer(modifier = Modifier.width(8.dp))
              Column {
                Text(
                  text = "Sharon AI Voice Consultant is Speaking...",
                  style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold, color = Color.White)
                )
                Text(
                  text = "Listen carefully to avoid costly painting mistakes.",
                  style = MaterialTheme.typography.bodySmall.copy(fontSize = 10.sp, color = Color(0xFFCBD5E1))
                )
              }
            }

            IconButton(
              onClick = { viewModel.stopVoice() },
              modifier = Modifier.size(32.dp).background(Color.White, CircleShape)
            ) {
              Icon(Icons.Default.Stop, contentDescription = "Stop", tint = SharonCrimson, modifier = Modifier.size(18.dp))
            }
          }
        }
        Spacer(modifier = Modifier.height(10.dp))
      }

      if (selectedTab == 0) {
        // ERROR CONSULTANT & Q&A
        LazyColumn(
          modifier = Modifier.fillMaxSize(),
          verticalArrangement = Arrangement.spacedBy(10.dp),
          contentPadding = PaddingValues(bottom = 24.dp)
        ) {
          // Custom Painter Question Box
          item {
            Card(
              modifier = Modifier.fillMaxWidth(),
              colors = CardDefaults.cardColors(containerColor = Color(0xFFF8FAFC)),
              shape = RoundedCornerShape(12.dp),
              border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(Color(0xFFCBD5E1)))
            ) {
              Column(modifier = Modifier.padding(12.dp)) {
                Text(
                  text = "Ask Friendly AI Consultant Any Paint Question",
                  style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold, color = SharonBluePrimary)
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                  text = "Describe any defect (peeling, bubbling, efflorescence, thinning ratio, primer, moisture) and receive spoken voice advice.",
                  style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp, color = Color.Gray)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(modifier = Modifier.fillMaxWidth()) {
                  OutlinedTextField(
                    value = customQuestionText,
                    onValueChange = { customQuestionText = it },
                    placeholder = { Text("e.g. My paint is flaking after rain...") },
                    modifier = Modifier.weight(1f).testTag("custom_consultant_input"),
                    shape = RoundedCornerShape(10.dp),
                    singleLine = true
                  )
                  Spacer(modifier = Modifier.width(8.dp))
                  Button(
                    onClick = {
                      if (customQuestionText.isNotBlank()) {
                        val query = customQuestionText.lowercase()
                        val matched = issues.find { issue ->
                          query.contains("blister") || query.contains("bubble") && issue.id == "blistering" ||
                          query.contains("peel") || query.contains("flake") && issue.id == "peeling" ||
                          query.contains("chalk") || query.contains("powder") && issue.id == "chalking" ||
                          query.contains("salt") || query.contains("efflor") && issue.id == "efflorescence" ||
                          query.contains("mold") || query.contains("algae") || query.contains("green") && issue.id == "algae_mold" ||
                          query.contains("roller") || query.contains("line") || query.contains("lap") && issue.id == "roller_marks"
                        } ?: issues[0]

                        val reply = "Hello! Sharon AI Voice Consultant answering your question about: $customQuestionText. ${matched.audioExplanation}"
                        customAnswerText = reply
                        viewModel.voiceSpeaker.speak(reply)
                      }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = SharonBluePrimary),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.testTag("ask_ai_voice_btn")
                  ) {
                    Text("Speak")
                  }
                }

                customAnswerText?.let { ans ->
                  Spacer(modifier = Modifier.height(8.dp))
                  Box(
                    modifier = Modifier
                      .fillMaxWidth()
                      .clip(RoundedCornerShape(8.dp))
                      .background(Color(0xFFEFF6FF))
                      .padding(10.dp)
                  ) {
                    Column {
                      Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.VolumeUp, contentDescription = null, tint = SharonBluePrimary, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("AI Consultant Vocal Response:", fontWeight = FontWeight.Bold, fontSize = 11.sp, color = SharonBluePrimary)
                      }
                      Spacer(modifier = Modifier.height(4.dp))
                      Text(ans, fontSize = 11.sp, color = Color(0xFF1E293B))
                    }
                  }
                }
              }
            }
          }

          item {
            Text(
              text = "Common Painter Mistakes & Corrective Advice",
              style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
              color = SharonBluePrimary
            )
          }

          items(issues, key = { it.id }) { issue ->
            PainterIssueCard(
              issue = issue,
              isPlaying = isSpeaking && activeVoiceId == issue.id,
              onPlayVoice = { viewModel.playVoiceAdvice(issue) },
              onStopVoice = { viewModel.stopVoice() }
            )
          }
        }
      } else {
        // VOICE TUTOR LESSONS
        LazyColumn(
          modifier = Modifier.fillMaxSize(),
          verticalArrangement = Arrangement.spacedBy(10.dp),
          contentPadding = PaddingValues(bottom = 24.dp)
        ) {
          item {
            Card(
              modifier = Modifier.fillMaxWidth(),
              colors = CardDefaults.cardColors(containerColor = Color(0xFFFFFBEB)),
              shape = RoundedCornerShape(12.dp),
              border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(Color(0xFFFDE68A)))
            ) {
              Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.AutoStories, contentDescription = null, tint = SharonGold, modifier = Modifier.size(24.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                  Text(
                    text = "Sharon Paints Masterclass Audio Series",
                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold, color = SharonBluePrimary)
                  )
                  Text(
                    text = "Listen to step-by-step master lessons on screeding, paint selection, waterproofing, and auto spray finishing.",
                    style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp, color = Color(0xFF78350F))
                  )
                }
              }
            }
          }

          items(lessons, key = { it.id }) { lesson ->
            VoiceLessonCard(
              lesson = lesson,
              isPlaying = isSpeaking && activeVoiceId == lesson.id,
              onPlayLesson = { viewModel.playVoiceLesson(lesson) },
              onStopLesson = { viewModel.stopVoice() }
            )
          }
        }
      }
    }
  }
}

@Composable
fun PainterIssueCard(
  issue: PaintingIssue,
  isPlaying: Boolean,
  onPlayVoice: () -> Unit,
  onStopVoice: () -> Unit
) {
  var isExpanded by remember { mutableStateOf(false) }

  Card(
    modifier = Modifier
      .fillMaxWidth()
      .testTag("issue_card_${issue.id}"),
    colors = CardDefaults.cardColors(containerColor = Color.White),
    shape = RoundedCornerShape(12.dp),
    border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(Color(0xFFE2E8F0))),
    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
  ) {
    Column(modifier = Modifier.padding(12.dp)) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Column(modifier = Modifier.weight(1f)) {
          Text(
            text = issue.problemTitle,
            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
            color = SharonBluePrimary
          )
          Text(
            text = "Category: ${issue.defectCategory}",
            style = MaterialTheme.typography.bodySmall.copy(fontSize = 10.sp, color = SharonCrimson, fontWeight = FontWeight.SemiBold)
          )
        }

        // Voice play / stop button
        Button(
          onClick = { if (isPlaying) onStopVoice() else onPlayVoice() },
          colors = ButtonDefaults.buttonColors(
            containerColor = if (isPlaying) SharonCrimson else SharonBluePrimary
          ),
          contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp),
          modifier = Modifier.testTag("voice_btn_${issue.id}")
        ) {
          Icon(
            if (isPlaying) Icons.Default.Stop else Icons.Default.VolumeUp,
            contentDescription = null,
            modifier = Modifier.size(16.dp)
          )
          Spacer(modifier = Modifier.width(4.dp))
          Text(if (isPlaying) "Stop" else "Listen", fontSize = 11.sp)
        }
      }

      Spacer(modifier = Modifier.height(6.dp))

      Text(
        text = issue.symptomDescription,
        style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp, color = Color(0xFF334155))
      )

      Spacer(modifier = Modifier.height(8.dp))

      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Text(
          text = "Recommended: ${issue.recommendedSharonProduct}",
          style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold, color = SharonGreen)
        )
        TextButton(
          onClick = { isExpanded = !isExpanded },
          contentPadding = PaddingValues(0.dp)
        ) {
          Text(if (isExpanded) "Hide Remedy" else "View Steps", fontSize = 11.sp, color = SharonBluePrimary)
          Icon(if (isExpanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore, contentDescription = null, modifier = Modifier.size(16.dp))
        }
      }

      AnimatedVisibility(visible = isExpanded) {
        Column(
          modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(Color(0xFFF8FAFC))
            .padding(10.dp)
        ) {
          Text("Root Causes:", fontWeight = FontWeight.Bold, fontSize = 11.sp, color = SharonBluePrimary)
          issue.rootCauses.forEach { cause ->
            Text("• $cause", fontSize = 10.sp, color = Color(0xFF475569))
          }
          Spacer(modifier = Modifier.height(6.dp))
          Text("Step-by-Step Fix:", fontWeight = FontWeight.Bold, fontSize = 11.sp, color = SharonBluePrimary)
          issue.stepByStepSolution.forEachIndexed { i, step ->
            Text("${i + 1}. $step", fontSize = 10.sp, color = Color(0xFF334155))
          }
          Spacer(modifier = Modifier.height(6.dp))
          Text("Prevention: ${issue.preventionTips}", fontSize = 10.sp, color = Color(0xFF047857), fontWeight = FontWeight.Medium)
        }
      }
    }
  }
}

@Composable
fun VoiceLessonCard(
  lesson: VoiceLesson,
  isPlaying: Boolean,
  onPlayLesson: () -> Unit,
  onStopLesson: () -> Unit
) {
  Card(
    modifier = Modifier
      .fillMaxWidth()
      .testTag("lesson_card_${lesson.id}"),
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
            text = "Lesson ${lesson.lessonNumber}: ${lesson.title}",
            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
            color = SharonBluePrimary
          )
          Text(
            text = "Duration: ${lesson.duration} | Spoken Masterclass",
            style = MaterialTheme.typography.bodySmall.copy(fontSize = 10.sp, color = Color(0xFF64748B))
          )
        }

        Button(
          onClick = { if (isPlaying) onStopLesson() else onPlayLesson() },
          colors = ButtonDefaults.buttonColors(
            containerColor = if (isPlaying) SharonCrimson else SharonBluePrimary
          ),
          contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp),
          modifier = Modifier.testTag("play_lesson_${lesson.id}")
        ) {
          Icon(if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow, contentDescription = null, modifier = Modifier.size(16.dp))
          Spacer(modifier = Modifier.width(4.dp))
          Text(if (isPlaying) "Pause" else "Play", fontSize = 11.sp)
        }
      }

      Spacer(modifier = Modifier.height(6.dp))

      Text(
        text = lesson.summary,
        style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp, color = Color(0xFF334155))
      )

      Spacer(modifier = Modifier.height(8.dp))

      Column(
        modifier = Modifier
          .fillMaxWidth()
          .clip(RoundedCornerShape(8.dp))
          .background(Color(0xFFF8FAFC))
          .padding(8.dp)
      ) {
        Text("Key Takeaways:", fontWeight = FontWeight.Bold, fontSize = 10.sp, color = SharonBluePrimary)
        lesson.keyTakeaways.forEach { takeaway ->
          Text("✓ $takeaway", fontSize = 10.sp, color = Color(0xFF1E293B))
        }
      }
    }
  }
}
