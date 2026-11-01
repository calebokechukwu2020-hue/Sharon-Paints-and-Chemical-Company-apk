package com.example.data.model

data class PaintingIssue(
  val id: String,
  val problemTitle: String,
  val defectCategory: String, // e.g., "Moisture & Adhesion", "Application Technique", "Surface Prep"
  val symptomDescription: String,
  val rootCauses: List<String>,
  val audioExplanation: String,
  val stepByStepSolution: List<String>,
  val recommendedSharonProduct: String,
  val preventionTips: String
)

data class VoiceLesson(
  val id: String,
  val lessonNumber: Int,
  val title: String,
  val duration: String,
  val summary: String,
  val spokenScript: String,
  val keyTakeaways: List<String>
)

data class AdvertisementItem(
  val id: String,
  val title: String,
  val format: String, // "Audio Jingle", "Video Commercial", "Social Ad"
  val duration: String,
  val tagLine: String,
  val description: String,
  val scriptContent: String,
  val audioTone: String,
  val visualDescription: String
)
