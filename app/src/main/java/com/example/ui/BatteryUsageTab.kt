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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.SystemMonitorViewModel

@Composable
fun BatteryUsageTab(viewModel: SystemMonitorViewModel) {
    var selectedDayIndex by remember { mutableStateOf(3) } // Monday default
    
    val weekDays = listOf("Fri", "Sat", "Sun", "Mon", "Tue", "Wed", "Thu")
    val screenTimeData = listOf("2h 15m", "4h 10m", "5h 25m", "3h 50m", "1h 45m", "2h 30m", "3h 10m")
    val percentageHeights = listOf(0.4f, 0.75f, 0.95f, 0.70f, 0.35f, 0.45f, 0.60f)

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Since Last Charge header
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
                    .testTag("usage_stats_card"),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF1E2A38))
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(
                        text = "SINCE LAST CHARGE",
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF00E676),
                        letterSpacing = 1.sp
                    )
                    Text(
                        text = "Last charged from 35% to 92% • 8h 40m ago",
                        color = Color.White.copy(alpha = 0.6f),
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(top = 4.dp, bottom = 16.dp)
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text("Screen On", style = MaterialTheme.typography.bodySmall, color = Color.White.copy(alpha = 0.5f))
                            Text("1h 24m", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = Color.White)
                        }
                        Column {
                            Text("Screen Off", style = MaterialTheme.typography.bodySmall, color = Color.White.copy(alpha = 0.5f))
                            Text("7h 16m", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = Color.White)
                        }
                        Column {
                            Text("Battery Used", style = MaterialTheme.typography.bodySmall, color = Color.White.copy(alpha = 0.5f))
                            Text("18% used", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = Color(0xFFFF5252))
                        }
                    }
                }
            }
        }

        // Screen Time Weekly Bar Chart
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF1E2A38))
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(
                        text = "WEEKLY SCREEN TIME",
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF00E676),
                        letterSpacing = 1.sp
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Chart Row
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(130.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.Bottom
                    ) {
                        weekDays.forEachIndexed { index, day ->
                            val isSelected = selectedDayIndex == index
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier
                                    .weight(1f)
                                    .clickable { selectedDayIndex = index }
                            ) {
                                Box(
                                    modifier = Modifier
                                        .width(14.dp)
                                        .weight(1f),
                                    contentAlignment = Alignment.BottomCenter
                                ) {
                                    // Background track
                                    Box(
                                        modifier = Modifier
                                            .fillMaxHeight()
                                            .width(8.dp)
                                            .clip(RoundedCornerShape(4.dp))
                                            .background(Color.White.copy(alpha = 0.05f))
                                    )
                                    // Filled active cylinder
                                    Box(
                                        modifier = Modifier
                                            .fillMaxHeight(percentageHeights[index])
                                            .width(8.dp)
                                            .clip(RoundedCornerShape(4.dp))
                                            .background(if (isSelected) Color(0xFF00E676) else Color(0xFF00B0FF))
                                    )
                                }
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = day,
                                    style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                    color = if (isSelected) Color(0xFF00E676) else Color.White.copy(alpha = 0.6f)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))
                    Divider(color = Color.White.copy(alpha = 0.1f))
                    Spacer(modifier = Modifier.height(8.dp))

                    // Selected day's detailed readout label
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "${weekDays[selectedDayIndex]}, May ${22 + selectedDayIndex} • Screen Time: ${screenTimeData[selectedDayIndex]}",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }
            }
        }

        // Drain Rate Grid Card
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF1E2A38))
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(
                        text = "DRAIN RATES",
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF00E676),
                        letterSpacing = 1.sp
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(12.dp))
                                .background(Color.White.copy(alpha = 0.05f))
                                .padding(12.dp)
                        ) {
                            Text("3.6%/hr", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Black, color = Color.White)
                            Text("Average Drain", style = MaterialTheme.typography.labelSmall, color = Color.White.copy(alpha = 0.5f))
                        }
                        Column(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(12.dp))
                                .background(Color.White.copy(alpha = 0.05f))
                                .padding(12.dp)
                        ) {
                            Text("1.5%/hr", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Black, color = Color(0xFF00B0FF))
                            Text("Overnight Drain", style = MaterialTheme.typography.labelSmall, color = Color.White.copy(alpha = 0.5f))
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(12.dp))
                                .background(Color.White.copy(alpha = 0.05f))
                                .padding(12.dp)
                        ) {
                            Text("6.0%/hr", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Black, color = Color(0xFFFF5252))
                            Text("Screen On Rate", style = MaterialTheme.typography.labelSmall, color = Color.White.copy(alpha = 0.5f))
                        }
                        Column(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(12.dp))
                                .background(Color.White.copy(alpha = 0.05f))
                                .padding(12.dp)
                        ) {
                            Text("3.0%/hr", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Black, color = Color.White)
                            Text("Screen Off Rate", style = MaterialTheme.typography.labelSmall, color = Color.White.copy(alpha = 0.5f))
                        }
                    }
                }
            }
        }

        // Charge Sessions History
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF1E2A38))
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(
                        text = "CHARGE SESSIONS (24H)",
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF00E676),
                        letterSpacing = 1.sp
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Session Item 1
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .clip(CircleShape)
                                .background(Color(0xFF00E676).copy(alpha = 0.1f))
                                .padding(8.dp)
                        ) {
                            Icon(Icons.Default.FlashOn, contentDescription = "Charge AC", tint = Color(0xFF00E676), modifier = Modifier.size(18.dp))
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text("AC Rapid Charger", style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold, color = Color.White)
                            Text("Charged from 35% to 92% • Today 08:30", style = MaterialTheme.typography.bodySmall, color = Color.White.copy(alpha = 0.6f))
                        }
                        Text("+57%", style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Black, color = Color(0xFF00E676))
                    }

                    Divider(color = Color.White.copy(alpha = 0.05f))

                    // Session Item 2
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .clip(CircleShape)
                                .background(Color(0xFF00B0FF).copy(alpha = 0.1f))
                                .padding(8.dp)
                        ) {
                            Icon(Icons.Default.Usb, contentDescription = "Charge USB", tint = Color(0xFF00B0FF), modifier = Modifier.size(18.dp))
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text("USB Port Standard", style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold, color = Color.White)
                            Text("Charged from 20% to 45% • Yesterday 21:15", style = MaterialTheme.typography.bodySmall, color = Color.White.copy(alpha = 0.6f))
                        }
                        Text("+25%", style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Black, color = Color(0xFF00B0FF))
                    }
                }
            }
        }

        item { Spacer(modifier = Modifier.height(16.dp)) }
    }
}
