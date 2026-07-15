package com.example.ui

import androidx.compose.foundation.background
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
import com.example.SystemMonitorViewModel
import com.example.data.OptimizationLog
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun LogsTab(viewModel: SystemMonitorViewModel) {
    val logs by viewModel.optimizationLogs.collectAsState()
    val totalSavings by viewModel.totalSavingsMah.collectAsState()
    
    val timeFormat = remember { SimpleDateFormat("HH:mm:ss", Locale.getDefault()) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Banner Header showing current savings
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
                    .testTag("logs_summary_card"),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF1E2A38))
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "TOTAL ACCUMULATED SAVINGS",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF00E676),
                                letterSpacing = 1.sp
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = String.format(Locale.US, "%.1f mAh", totalSavings),
                                style = MaterialTheme.typography.displaySmall.copy(fontSize = 32.sp),
                                fontWeight = FontWeight.Black,
                                color = Color.White
                            )
                        }
                        
                        Button(
                            onClick = { viewModel.clearLogs() },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF5252).copy(alpha = 0.15f), contentColor = Color(0xFFFF5252)),
                            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.testTag("clear_logs_btn")
                        ) {
                            Icon(Icons.Default.Delete, contentDescription = "Clear logs", modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Clear", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                        }
                    }
                    Text(
                        text = "Calculated from terminated loops, timers, and active clock adjustments",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.White.copy(alpha = 0.6f),
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }
            }
        }

        item {
            Text(
                text = "SYSTEM REGISTRY EVENTS",
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF00E676),
                letterSpacing = 1.sp,
                modifier = Modifier.padding(start = 4.dp, top = 4.dp)
            )
        }

        if (logs.isEmpty()) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            imageVector = Icons.Default.Info,
                            contentDescription = "No logs",
                            tint = Color.White.copy(alpha = 0.2f),
                            modifier = Modifier.size(48.dp)
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "Log registry is empty.\nRun calibration or optimizations to record telemetry.",
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                            color = Color.White.copy(alpha = 0.4f),
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
            }
        } else {
            items(logs, key = { it.id }) { log ->
                LogItemRow(log, timeFormat)
            }
        }

        item { Spacer(modifier = Modifier.height(16.dp)) }
    }
}

@Composable
fun LogItemRow(log: OptimizationLog, format: SimpleDateFormat) {
    val dateStr = format.format(Date(log.timestamp))

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("log_item_${log.id}"),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1E2A38))
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.Top
        ) {
            Box(
                modifier = Modifier
                    .clip(CircleShape)
                    .background(
                        when (log.eventType) {
                            "KILL_APPS" -> Color(0xFFFF5252).copy(alpha = 0.1f)
                            "BATTERY_HEALTH_MEASURE" -> Color(0xFF00E676).copy(alpha = 0.1f)
                            "TIMEOUT_OPTIMIZED" -> Color(0xFFFFB300).copy(alpha = 0.1f)
                            "LOGIN_SUCCESS" -> Color(0xFF00B0FF).copy(alpha = 0.1f)
                            "LOGOUT_SUCCESS" -> Color(0xFF90A4AE).copy(alpha = 0.1f)
                            else -> Color(0xFFBB86FC).copy(alpha = 0.1f)
                        }
                    )
                    .padding(8.dp)
            ) {
                Icon(
                    imageVector = when (log.eventType) {
                        "KILL_APPS" -> Icons.Default.PowerSettingsNew
                        "BATTERY_HEALTH_MEASURE" -> Icons.Default.Favorite
                        "TIMEOUT_OPTIMIZED" -> Icons.Default.Timer
                        "LOGIN_SUCCESS" -> Icons.Default.LockOpen
                        "LOGOUT_SUCCESS" -> Icons.Default.Lock
                        else -> Icons.Default.Bolt
                    },
                    contentDescription = log.eventType,
                    tint = when (log.eventType) {
                        "KILL_APPS" -> Color(0xFFFF5252)
                        "BATTERY_HEALTH_MEASURE" -> Color(0xFF00E676)
                        "TIMEOUT_OPTIMIZED" -> Color(0xFFFFB300)
                        "LOGIN_SUCCESS" -> Color(0xFF00B0FF)
                        "LOGOUT_SUCCESS" -> Color(0xFF90A4AE)
                        else -> Color(0xFFBB86FC)
                    },
                    modifier = Modifier.size(18.dp)
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = log.title,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Text(
                        text = dateStr,
                        style = MaterialTheme.typography.labelSmall,
                        color = Color.White.copy(alpha = 0.4f)
                    )
                }
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = log.description,
                    color = Color.White.copy(alpha = 0.7f),
                    style = MaterialTheme.typography.bodySmall
                )
                
                if (log.savingsMah != 0f) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = if (log.savingsMah > 0f) "+${log.savingsMah} mAh saved" else "${log.savingsMah} mAh consumption",
                        color = if (log.savingsMah > 0f) Color(0xFF00E676) else Color(0xFFFF5252),
                        fontWeight = FontWeight.Bold,
                        fontSize = 11.sp,
                        style = MaterialTheme.typography.labelSmall
                    )
                }
            }
        }
    }
}
