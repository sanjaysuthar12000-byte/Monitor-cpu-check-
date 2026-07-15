package com.example.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.window.Dialog
import com.example.SystemMonitorViewModel

@Composable
fun SettingsDialog(
    viewModel: SystemMonitorViewModel,
    onDismiss: () -> Unit
) {
    val tempUnit by viewModel.tempUnit.collectAsState()
    val batteryDesignCapacity by viewModel.batteryDesignCapacity.collectAsState()
    val highPowerThreshold by viewModel.highPowerThreshold.collectAsState()

    // Alert settings states from ViewModel
    val chargingEnabled by viewModel.chargingNotificationEnabled.collectAsState()
    val chargingValue by viewModel.chargingNotificationValue.collectAsState()
    
    val lowEnabled by viewModel.lowBatteryNotificationEnabled.collectAsState()
    val lowValue by viewModel.lowBatteryNotificationValue.collectAsState()
    
    val overheatEnabled by viewModel.overheatNotificationEnabled.collectAsState()
    val overheatValue by viewModel.overheatNotificationValue.collectAsState()
    
    val weeklySummaryEnabled by viewModel.weeklySummaryEnabled.collectAsState()
    val realTimeEnabled by viewModel.realTimeMonitoringEnabled.collectAsState()

    // Logged user details
    val loggedUser by viewModel.loggedInUser.collectAsState()
    val loginType by viewModel.loginType.collectAsState()

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.85f)
                .testTag("settings_dialog_card"),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF1E2A38)),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp)
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Settings,
                            contentDescription = "Settings",
                            tint = Color(0xFF00E676),
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Settings & Alerts",
                            style = MaterialTheme.typography.titleLarge,
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier.testTag("close_settings_btn")
                    ) {
                        Icon(Icons.Default.Close, contentDescription = "Close", tint = Color.White)
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Custom Alerts Section
                    Text(
                        text = "CUSTOM ALERTS",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF00E676),
                        letterSpacing = 1.sp
                    )

                    // Charging Alert
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color.White.copy(alpha = 0.05f))
                            .padding(12.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("Charging Notification", color = Color.White, fontWeight = FontWeight.SemiBold)
                            Switch(
                                checked = chargingEnabled,
                                onCheckedChange = { viewModel.setChargingNotificationEnabled(it) },
                                colors = SwitchDefaults.colors(
                                    checkedThumbColor = Color(0xFF00E676),
                                    checkedTrackColor = Color(0xFF00E676).copy(alpha = 0.3f)
                                ),
                                modifier = Modifier.testTag("toggle_charging_alert")
                            )
                        }
                        if (chargingEnabled) {
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Notify at: $chargingValue%",
                                style = MaterialTheme.typography.bodySmall,
                                color = Color.White.copy(alpha = 0.7f)
                            )
                            Slider(
                                value = chargingValue.toFloat(),
                                onValueChange = { viewModel.setChargingNotificationValue(it.toInt()) },
                                valueRange = 50f..100f,
                                colors = SliderDefaults.colors(
                                    thumbColor = Color(0xFF00E676),
                                    activeTrackColor = Color(0xFF00E676)
                                ),
                                modifier = Modifier.testTag("slider_charging_alert")
                            )
                        }
                    }

                    // Low Battery Alert
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color.White.copy(alpha = 0.05f))
                            .padding(12.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("Low Battery Notification", color = Color.White, fontWeight = FontWeight.SemiBold)
                            Switch(
                                checked = lowEnabled,
                                onCheckedChange = { viewModel.setLowBatteryNotificationEnabled(it) },
                                colors = SwitchDefaults.colors(
                                    checkedThumbColor = Color(0xFF00E676),
                                    checkedTrackColor = Color(0xFF00E676).copy(alpha = 0.3f)
                                ),
                                modifier = Modifier.testTag("toggle_low_battery_alert")
                            )
                        }
                        if (lowEnabled) {
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Notify below: $lowValue%",
                                style = MaterialTheme.typography.bodySmall,
                                color = Color.White.copy(alpha = 0.7f)
                            )
                            Slider(
                                value = lowValue.toFloat(),
                                onValueChange = { viewModel.setLowBatteryNotificationValue(it.toInt()) },
                                valueRange = 10f..50f,
                                colors = SliderDefaults.colors(
                                    thumbColor = Color(0xFF00E676),
                                    activeTrackColor = Color(0xFF00E676)
                                ),
                                modifier = Modifier.testTag("slider_low_battery_alert")
                            )
                        }
                    }

                    // Overheat Alert
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color.White.copy(alpha = 0.05f))
                            .padding(12.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("Overheat Notification", color = Color.White, fontWeight = FontWeight.SemiBold)
                            Switch(
                                checked = overheatEnabled,
                                onCheckedChange = { viewModel.setOverheatNotificationEnabled(it) },
                                colors = SwitchDefaults.colors(
                                    checkedThumbColor = Color(0xFF00E676),
                                    checkedTrackColor = Color(0xFF00E676).copy(alpha = 0.3f)
                                ),
                                modifier = Modifier.testTag("toggle_overheat_alert")
                            )
                        }
                        if (overheatEnabled) {
                            Spacer(modifier = Modifier.height(4.dp))
                            val levelStr = when {
                                overheatValue > 43 -> "Critical (${overheatValue}°C)"
                                overheatValue > 38 -> "Hot (${overheatValue}°C)"
                                else -> "Warm (${overheatValue}°C)"
                            }
                            Text(
                                text = "Trigger Level: $levelStr",
                                style = MaterialTheme.typography.bodySmall,
                                color = Color.White.copy(alpha = 0.7f)
                            )
                            Slider(
                                value = overheatValue.toFloat(),
                                onValueChange = { viewModel.setOverheatNotificationValue(it.toInt()) },
                                valueRange = 30f..50f,
                                colors = SliderDefaults.colors(
                                    thumbColor = Color(0xFF00E676),
                                    activeTrackColor = Color(0xFF00E676)
                                ),
                                modifier = Modifier.testTag("slider_overheat_alert")
                            )
                        }
                    }

                    // General Alert Toggles
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color.White.copy(alpha = 0.05f))
                            .padding(12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Weekly Summary Report", color = Color.White, style = MaterialTheme.typography.bodyMedium)
                        Switch(
                            checked = weeklySummaryEnabled,
                            onCheckedChange = { viewModel.setWeeklySummaryEnabled(it) },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = Color(0xFF00E676),
                                checkedTrackColor = Color(0xFF00E676).copy(alpha = 0.3f)
                            )
                        )
                    }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color.White.copy(alpha = 0.05f))
                            .padding(12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Real-time Active Monitoring", color = Color.White, style = MaterialTheme.typography.bodyMedium)
                        Switch(
                            checked = realTimeEnabled,
                            onCheckedChange = { viewModel.setRealTimeMonitoringEnabled(it) },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = Color(0xFF00E676),
                                checkedTrackColor = Color(0xFF00E676).copy(alpha = 0.3f)
                            )
                        )
                    }

                    // Hardware tuning section
                    Text(
                        text = "HARDWARE PROFILE",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF00E676),
                        letterSpacing = 1.sp
                    )

                    // Design capacity setup
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color.White.copy(alpha = 0.05f))
                            .padding(12.dp)
                    ) {
                        Text(
                            text = "Battery Design Capacity: $batteryDesignCapacity mAh",
                            color = Color.White,
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.SemiBold
                        )
                        Slider(
                            value = batteryDesignCapacity.toFloat(),
                            onValueChange = { viewModel.updateDesignCapacity(it.toInt()) },
                            valueRange = 2000f..7000f,
                            colors = SliderDefaults.colors(
                                thumbColor = Color(0xFF00E676),
                                activeTrackColor = Color(0xFF00E676)
                            ),
                            modifier = Modifier.testTag("design_capacity_slider")
                        )
                    }

                    // Temp Unit Switcher
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color.White.copy(alpha = 0.05f))
                            .padding(12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Temperature Scale", color = Color.White)
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Button(
                                onClick = { viewModel.updateTempUnit("C") },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = if (tempUnit == "C") Color(0xFF00E676) else Color.DarkGray,
                                    contentColor = if (tempUnit == "C") Color.Black else Color.White
                                ),
                                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                            ) {
                                Text("°C")
                            }
                            Button(
                                onClick = { viewModel.updateTempUnit("F") },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = if (tempUnit == "F") Color(0xFF00E676) else Color.DarkGray,
                                    contentColor = if (tempUnit == "F") Color.Black else Color.White
                                ),
                                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                            ) {
                                Text("°F")
                            }
                        }
                    }

                    // User Profile Section
                    Text(
                        text = "ACCOUNT & SECURITY",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF00E676),
                        letterSpacing = 1.sp
                    )

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color.White.copy(alpha = 0.05f))
                            .padding(12.dp)
                    ) {
                        Text(
                            text = "Signed in as: $loggedUser",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Text(
                            text = "Authentication: $loginType",
                            color = Color.White.copy(alpha = 0.6f),
                            style = MaterialTheme.typography.bodySmall
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Button(
                            onClick = {
                                viewModel.performLogout()
                                onDismiss()
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF5252), contentColor = Color.White),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(40.dp)
                                .testTag("logout_btn")
                        ) {
                            Icon(Icons.Default.ExitToApp, contentDescription = "Logout")
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("SIGN OUT / DISCONNECT", fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}
