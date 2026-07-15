package com.example.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import com.example.SystemMonitorViewModel

@Composable
fun PerformanceProfileTab(viewModel: SystemMonitorViewModel) {
    val currentProfile by viewModel.cpuProfileMode.collectAsState()
    
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Text(
                text = "CPU PERFORMANCE SCHEDULING",
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF00E676),
                letterSpacing = 1.5.sp,
                modifier = Modifier.padding(top = 8.dp, start = 4.dp)
            )
        }

        // Profile Option 1: Power Saver
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { viewModel.setCpuProfileMode("Power Saver") }
                    .testTag("profile_power_saver"),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (currentProfile == "Power Saver") Color(0xFF1B5E20) else Color(0xFF1E2A38)
                )
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(if (currentProfile == "Power Saver") Color.White.copy(alpha = 0.2f) else Color(0xFF4CAF50).copy(alpha = 0.1f))
                            .padding(10.dp)
                    ) {
                        Icon(
                            Icons.Default.BatterySaver,
                            contentDescription = "Saver icon",
                            tint = if (currentProfile == "Power Saver") Color.White else Color(0xFF4CAF50)
                        )
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            "Power Saver Mode",
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            style = MaterialTheme.typography.bodyLarge
                        )
                        Text(
                            "Limits background refresh rates and clamps CPU cores below 1.5GHz. Recommended for maximum uptime and screen-off standby stability.",
                            color = Color.White.copy(alpha = 0.7f),
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                    if (currentProfile == "Power Saver") {
                        Icon(Icons.Default.Check, contentDescription = "Active", tint = Color.White)
                    }
                }
            }
        }

        // Profile Option 2: Balanced
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { viewModel.setCpuProfileMode("Balanced") }
                    .testTag("profile_balanced"),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (currentProfile == "Balanced") Color(0xFF0D47A1) else Color(0xFF1E2A38)
                )
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(if (currentProfile == "Balanced") Color.White.copy(alpha = 0.2f) else Color(0xFF00B0FF).copy(alpha = 0.1f))
                            .padding(10.dp)
                    ) {
                        Icon(
                            Icons.Default.Tune,
                            contentDescription = "Balanced icon",
                            tint = if (currentProfile == "Balanced") Color.White else Color(0xFF00B0FF)
                        )
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            "Balanced Scheduler",
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            style = MaterialTheme.typography.bodyLarge
                        )
                        Text(
                            "Utilizes standard Android Schedutil governor. Dynamically scales clock frequencies between 0.8GHz and 2.2GHz matching load demand.",
                            color = Color.White.copy(alpha = 0.7f),
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                    if (currentProfile == "Balanced") {
                        Icon(Icons.Default.Check, contentDescription = "Active", tint = Color.White)
                    }
                }
            }
        }

        // Profile Option 3: Full Power
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { viewModel.setCpuProfileMode("Full Power") }
                    .testTag("profile_full_power"),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (currentProfile == "Full Power") Color(0xFFB71C1C) else Color(0xFF1E2A38)
                )
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(if (currentProfile == "Full Power") Color.White.copy(alpha = 0.2f) else Color(0xFFFF5252).copy(alpha = 0.1f))
                            .padding(10.dp)
                    ) {
                        Icon(
                            Icons.Default.Bolt,
                            contentDescription = "Full power icon",
                            tint = if (currentProfile == "Full Power") Color.White else Color(0xFFFF5252)
                        )
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            "Full Power Mode",
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            style = MaterialTheme.typography.bodyLarge
                        )
                        Text(
                            "Removes core clock limits, pins big cores to maximum 2.96GHz, and acquires custom Performance Wakelocks for heavy gaming (up to 120 FPS).",
                            color = Color.White.copy(alpha = 0.7f),
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                    if (currentProfile == "Full Power") {
                        Icon(Icons.Default.Check, contentDescription = "Active", tint = Color.White)
                    }
                }
            }
        }

        // Warning Alert for Full Power Mode
        if (currentProfile == "Full Power") {
            item {
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFFF5252).copy(alpha = 0.15f)),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.Warning, contentDescription = "Alert", tint = Color(0xFFFF5252))
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = "Warning: Full Power pins performance clocks, which can cause severe heating or power draw spillage under extensive usage periods.",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color(0xFFFF8A80),
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}
