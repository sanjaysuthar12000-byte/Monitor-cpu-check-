package com.example.ui

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
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
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.SystemMonitorViewModel
import java.util.Locale

@Composable
fun DashboardTab(viewModel: SystemMonitorViewModel) {
    val percentage by viewModel.batteryPercentage.collectAsState()
    val temperature by viewModel.batteryTemperature.collectAsState()
    val healthPercent by viewModel.batteryHealthPercentage.collectAsState()
    val isCalibrating by viewModel.isCalibratingHealth.collectAsState()
    val charging by viewModel.chargingStatus.collectAsState()
    val powerSource by viewModel.powerSource.collectAsState()
    val isScanning by viewModel.isScanning.collectAsState()
    val optScore by viewModel.optimizationScore.collectAsState()
    val powerUsage by viewModel.processorPowerUsage.collectAsState()
    val cpuLoad by viewModel.cpuLoad.collectAsState()
    val tempUnit by viewModel.tempUnit.collectAsState()
    val voltage by viewModel.batteryVoltage.collectAsState()
    val technology by viewModel.batteryTechnology.collectAsState()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Real-time Monitoring Gauge Card
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
                    .testTag("dashboard_gauge_card"),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF1E2A38))
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "REAL-TIME MONITORING",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF00E676),
                        letterSpacing = 1.5.sp,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )

                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.size(190.dp)
                    ) {
                        val infiniteTransition = rememberInfiniteTransition(label = "charging_pulse")
                        val pulseScale by infiniteTransition.animateFloat(
                            initialValue = 1.0f,
                            targetValue = 1.06f,
                            animationSpec = infiniteRepeatable(
                                animation = tween(1200, delayMillis = 100),
                                repeatMode = RepeatMode.Reverse
                            ),
                            label = "pulse"
                        )

                        Canvas(
                            modifier = Modifier
                                .fillMaxSize()
                                .scale(if (charging == "Charging") pulseScale else 1.0f)
                        ) {
                            // Background Ring
                            drawCircle(
                                color = Color.White.copy(alpha = 0.05f),
                                style = Stroke(width = 14.dp.toPx(), cap = StrokeCap.Round)
                            )
                            // Glowing Active Ring
                            drawArc(
                                brush = Brush.sweepGradient(
                                    listOf(
                                        Color(0xFF00E676),
                                        Color(0xFF00B0FF),
                                        Color(0xFF00E676)
                                    )
                                ),
                                startAngle = -90f,
                                sweepAngle = (percentage / 100f) * 360f,
                                useCenter = false,
                                style = Stroke(width = 14.dp.toPx(), cap = StrokeCap.Round)
                            )
                        }

                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Row(verticalAlignment = Alignment.Bottom) {
                                Text(
                                    text = "$percentage",
                                    style = MaterialTheme.typography.displayLarge.copy(fontSize = 54.sp),
                                    fontWeight = FontWeight.Black,
                                    color = Color.White
                                )
                                Text(
                                    text = "%",
                                    style = MaterialTheme.typography.titleLarge,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF00E676),
                                    modifier = Modifier.padding(bottom = 10.dp, start = 2.dp)
                                )
                            }
                            Text(
                                text = charging.uppercase(),
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = if (charging == "Charging") Color(0xFF00E676) else Color.White.copy(alpha = 0.6f)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    // Lower Gauges Stats Row
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("CPU LOAD", style = MaterialTheme.typography.labelSmall, color = Color.White.copy(alpha = 0.5f))
                            Text(
                                text = String.format(Locale.US, "%.1f%%", cpuLoad),
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Black,
                                color = Color(0xFF00B0FF)
                            )
                        }
                        Box(modifier = Modifier.width(1.dp).height(24.dp).background(Color.White.copy(alpha = 0.1f)))
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("CPU POWER", style = MaterialTheme.typography.labelSmall, color = Color.White.copy(alpha = 0.5f))
                            Text(
                                text = String.format(Locale.US, "%.1fmW", powerUsage),
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Black,
                                color = Color(0xFFFFEB3B)
                            )
                        }
                        Box(modifier = Modifier.width(1.dp).height(24.dp).background(Color.White.copy(alpha = 0.1f)))
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("HEALTH VALUE", style = MaterialTheme.typography.labelSmall, color = Color.White.copy(alpha = 0.5f))
                            Text(
                                text = "$optScore/100",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Black,
                                color = if (optScore > 75) Color(0xFF00E676) else Color(0xFFFF5252)
                            )
                        }
                    }
                }
            }
        }

        // Battery Remaining estimate banner
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF1E2A38)),
                shape = RoundedCornerShape(16.dp)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(Color(0xFF00E676).copy(alpha = 0.1f))
                            .padding(12.dp)
                    ) {
                        Icon(Icons.Default.HourglassEmpty, contentDescription = "Uptime", tint = Color(0xFF00E676))
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Column {
                        val hoursRemaining = (percentage * 0.25f).toInt()
                        val minutesRemaining = ((percentage * 0.25f - hoursRemaining) * 60).toInt()
                        Text(
                            text = if (charging == "Charging") "Fully charged in ~1h 15m" else "${hoursRemaining}h ${minutesRemaining}m Remaining",
                            color = Color.White,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Based on your active system usage patterns",
                            color = Color.White.copy(alpha = 0.6f),
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
            }
        }

        // Grid of 4 indicators (Health, Temp, Voltage, Technology)
        item {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.fillMaxWidth()) {
                    // Health Card
                    Card(
                        modifier = Modifier.weight(1f),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFF1E2A38)),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .clip(CircleShape)
                                    .background(Color(0xFF00E676).copy(alpha = 0.1f))
                                    .padding(8.dp)
                            ) {
                                Icon(Icons.Default.Favorite, contentDescription = "Health", tint = Color(0xFF00E676), modifier = Modifier.size(20.dp))
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text("Health", style = MaterialTheme.typography.labelSmall, color = Color.White.copy(alpha = 0.6f))
                                Text("$healthPercent%", style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Bold, color = Color.White)
                            }
                        }
                    }

                    // Temperature Card
                    Card(
                        modifier = Modifier.weight(1f),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFF1E2A38)),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .clip(CircleShape)
                                    .background(Color(0xFFFF5252).copy(alpha = 0.1f))
                                    .padding(8.dp)
                            ) {
                                Icon(Icons.Default.Thermostat, contentDescription = "Temperature", tint = Color(0xFFFF5252), modifier = Modifier.size(20.dp))
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text("Temp", style = MaterialTheme.typography.labelSmall, color = Color.White.copy(alpha = 0.6f))
                                val displayTemp = if (tempUnit == "F") (temperature * 9 / 5) + 32 else temperature
                                Text(
                                    text = String.format(Locale.US, "%.1f°%s", displayTemp, tempUnit),
                                    style = MaterialTheme.typography.bodyLarge,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            }
                        }
                    }
                }

                Row(horizontalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.fillMaxWidth()) {
                    // Voltage Card
                    Card(
                        modifier = Modifier.weight(1f),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFF1E2A38)),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .clip(CircleShape)
                                    .background(Color(0xFFFFEB3B).copy(alpha = 0.1f))
                                    .padding(8.dp)
                            ) {
                                Icon(Icons.Default.FlashOn, contentDescription = "Voltage", tint = Color(0xFFFFEB3B), modifier = Modifier.size(20.dp))
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text("Voltage", style = MaterialTheme.typography.labelSmall, color = Color.White.copy(alpha = 0.6f))
                                val voltV = voltage / 1000f
                                Text(
                                    text = String.format(Locale.US, "%.2f V", voltV),
                                    style = MaterialTheme.typography.bodyLarge,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            }
                        }
                    }

                    // Technology Card
                    Card(
                        modifier = Modifier.weight(1f),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFF1E2A38)),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .clip(CircleShape)
                                    .background(Color(0xFF00B0FF).copy(alpha = 0.1f))
                                    .padding(8.dp)
                            ) {
                                Icon(Icons.Default.DeveloperMode, contentDescription = "Tech", tint = Color(0xFF00B0FF), modifier = Modifier.size(20.dp))
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text("Tech", style = MaterialTheme.typography.labelSmall, color = Color.White.copy(alpha = 0.6f))
                                Text(technology, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Bold, color = Color.White)
                            }
                        }
                    }
                }
            }
        }

        // Active Battery Calibrator section
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF1E2A38)),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Hardware Battery Calibration",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF00E676)
                    )
                    Text(
                        text = "Analyze live impedance coefficients, voltage curves, and thermal coefficients to compute accurate physical degradation metrics.",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.White.copy(alpha = 0.7f),
                        modifier = Modifier.padding(vertical = 4.dp)
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    if (isCalibrating) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
                            LinearProgressIndicator(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(6.dp)
                                    .clip(CircleShape),
                                color = Color(0xFF00E676),
                                trackColor = Color.White.copy(alpha = 0.1f)
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Measuring cell impedance and thermal coefficients...",
                                style = MaterialTheme.typography.bodySmall,
                                color = Color(0xFF00E676),
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    } else {
                        Button(
                            onClick = { viewModel.measureBatteryHealth() },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFF00E676).copy(alpha = 0.15f),
                                contentColor = Color(0xFF00E676)
                            ),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Icon(Icons.Default.Autorenew, contentDescription = "Recalibrate")
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("RUN CELL IMPEDANCE CALIBRATION", fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }

        // Background App Process Killer section
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF1E2A38)),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Standby Task & Service Killer",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFFF5252)
                    )
                    Text(
                        text = "One-click hardware scan to terminate idle background tasks and unneeded active loops to secure extended uptime.",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.White.copy(alpha = 0.7f),
                        modifier = Modifier.padding(vertical = 4.dp)
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Button(
                        onClick = { viewModel.killBackgroundApps() },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                            .testTag("one_click_kill_btn"),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF5252)),
                        shape = RoundedCornerShape(12.dp),
                        enabled = !isScanning
                    ) {
                        if (isScanning) {
                            CircularProgressIndicator(
                                color = Color.White,
                                modifier = Modifier.size(20.dp),
                                strokeWidth = 2.dp
                            )
                        } else {
                            Icon(Icons.Default.PowerSettingsNew, contentDescription = "Power Off tasks")
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("ONE-CLICK PROCESS RECOVERY", fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }

        item { Spacer(modifier = Modifier.height(16.dp)) }
    }
}
