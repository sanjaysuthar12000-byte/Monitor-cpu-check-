package com.example.ui

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.SystemMonitorViewModel
import java.util.Locale

@Composable
fun HardwareTab(viewModel: SystemMonitorViewModel) {
    val context = LocalContext.current
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { granted ->
            viewModel.checkHardwarePermissionsAndValues()
        }
    )

    val coreCount by viewModel.cpuCoreCount.collectAsState()
    val ramTotal by viewModel.ramTotalBytes.collectAsState()
    val ramUsed by viewModel.ramUsedBytes.collectAsState()
    val storageTotal by viewModel.storageTotalBytes.collectAsState()
    val storageUsed by viewModel.storageUsedBytes.collectAsState()
    
    // CPU Diagnostics additions
    val coreFreqs by viewModel.coreFrequencies.collectAsState()
    val thermalThrottle by viewModel.thermalThrottleLevel.collectAsState()
    val cacheInfo by viewModel.cpuCacheInfo.collectAsState()

    // Permissions & diagnostics states
    val systemScreenTimeout by viewModel.systemScreenTimeout.collectAsState()
    val hasWriteSettingsPermission by viewModel.hasWriteSettingsPermission.collectAsState()
    val isAccessibilityEnabled by viewModel.isAccessibilityEnabled.collectAsState()
    val carrierName by viewModel.carrierName.collectAsState()
    val simState by viewModel.simState.collectAsState()
    val phoneType by viewModel.phoneType.collectAsState()
    val hasPhoneStatePermission by viewModel.hasPhoneStatePermission.collectAsState()

    // Trigger initial check
    LaunchedEffect(Unit) {
        viewModel.checkHardwarePermissionsAndValues()
    }

    val ramUsedGb = ramUsed / 1024.0 / 1024.0 / 1024.0
    val ramTotalGb = ramTotal / 1024.0 / 1024.0 / 1024.0
    val ramPercent = (ramUsed.toFloat() / ramTotal.coerceAtLeast(1).toFloat()).coerceIn(0f, 1f)

    val storageUsedGb = storageUsed / 1024.0 / 1024.0 / 1024.0
    val storageTotalGb = storageTotal / 1024.0 / 1024.0 / 1024.0
    val storagePercent = (storageUsed.toFloat() / storageTotal.coerceAtLeast(1).toFloat()).coerceIn(0f, 1f)

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            // CPU Chip Specs Card with advanced diagnostics
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
                    .testTag("hardware_specs_card"),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF1E2A38))
            ) {
                Column(
                    modifier = Modifier.padding(20.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .clip(CircleShape)
                                .background(Color(0xFF00E676).copy(alpha = 0.1f))
                                .padding(8.dp)
                        ) {
                            Icon(
                                Icons.Default.Speed,
                                contentDescription = "Processor details",
                                tint = Color(0xFF00E676)
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = "CPU Diagnostics Suite",
                                style = MaterialTheme.typography.titleMedium,
                                color = Color.White,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "Live physical clock thread metrics",
                                style = MaterialTheme.typography.labelSmall,
                                color = Color.White.copy(alpha = 0.6f)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text("Cores Available", style = MaterialTheme.typography.labelSmall, color = Color.White.copy(alpha = 0.5f))
                            Text("$coreCount Cores", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = Color.White)
                        }
                        Column(horizontalAlignment = Alignment.End) {
                            Text("Architecture", style = MaterialTheme.typography.labelSmall, color = Color.White.copy(alpha = 0.5f))
                            Text(System.getProperty("os.arch") ?: "arm64-v8a", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = Color.White)
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))
                    Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(Color.White.copy(alpha = 0.1f)))
                    Spacer(modifier = Modifier.height(12.dp))

                    // Core frequencies display
                    Text(
                        text = "THREAD CORE CLOCKS (GHz)",
                        style = MaterialTheme.typography.labelLarge,
                        color = Color(0xFF00E676),
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    // Simplified manual flow for cores
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        coreFreqs.chunked(4).forEach { rowCores ->
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                rowCores.forEachIndexed { colIndex, freq ->
                                    Card(
                                        colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.05f)),
                                        shape = RoundedCornerShape(8.dp),
                                        modifier = Modifier
                                            .weight(1f)
                                            .padding(horizontal = 2.dp)
                                    ) {
                                        Column(
                                            modifier = Modifier.padding(6.dp),
                                            horizontalAlignment = Alignment.CenterHorizontally
                                        ) {
                                            Text("Core", style = MaterialTheme.typography.labelSmall, color = Color.White.copy(alpha = 0.5f))
                                            Text(
                                                text = String.format(Locale.US, "%.2f", freq),
                                                style = MaterialTheme.typography.bodyMedium,
                                                fontWeight = FontWeight.Bold,
                                                color = Color.White
                                            )
                                        }
                                    }
                                }
                                // Pad empty columns if less than 4
                                if (rowCores.size < 4) {
                                    for (i in 0 until (4 - rowCores.size)) {
                                        Spacer(modifier = Modifier.weight(1f).padding(horizontal = 2.dp))
                                    }
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))
                    Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(Color.White.copy(alpha = 0.1f)))
                    Spacer(modifier = Modifier.height(12.dp))

                    // Thermal and Cache info
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Column {
                            Text("CPU Cache Profile", style = MaterialTheme.typography.labelSmall, color = Color.White.copy(alpha = 0.5f))
                            Text(cacheInfo, style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.Bold, color = Color.White)
                        }
                        Column(horizontalAlignment = Alignment.End) {
                            Text("Thermal Throttle State", style = MaterialTheme.typography.labelSmall, color = Color.White.copy(alpha = 0.5f))
                            Text(
                                thermalThrottle,
                                style = MaterialTheme.typography.bodySmall,
                                fontWeight = FontWeight.Bold,
                                color = if (thermalThrottle.contains("Optimal")) Color(0xFF00E676) else Color(0xFFFF5252)
                            )
                        }
                    }
                }
            }
        }

        item {
            // Memory RAM Status
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF1E2A38))
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                Icons.Default.Memory,
                                contentDescription = "RAM info",
                                tint = Color(0xFFBB86FC)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "RAM Usage",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                        Text(
                            text = String.format(Locale.US, "%.1f%% Used", ramPercent * 100),
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFBB86FC)
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))
                    
                    LinearProgressIndicator(
                        progress = { ramPercent },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(10.dp)
                            .clip(CircleShape),
                        color = Color(0xFFBB86FC),
                        trackColor = Color.White.copy(alpha = 0.05f)
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = String.format(Locale.US, "Used: %.2f GB", ramUsedGb),
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.White.copy(alpha = 0.6f)
                        )
                        Text(
                            text = String.format(Locale.US, "Total Capacity: %.2f GB", ramTotalGb),
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.White.copy(alpha = 0.6f)
                        )
                    }
                }
            }
        }

        item {
            // Storage Capacity Status
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF1E2A38))
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                Icons.Default.Storage,
                                contentDescription = "Storage info",
                                tint = Color(0xFF03A9F4)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Internal Storage",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                        Text(
                            text = String.format(Locale.US, "%.1f%% Full", storagePercent * 100),
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF03A9F4)
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))
                    
                    LinearProgressIndicator(
                        progress = { storagePercent },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(10.dp)
                            .clip(CircleShape),
                        color = Color(0xFF03A9F4),
                        trackColor = Color.White.copy(alpha = 0.05f)
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = String.format(Locale.US, "Used: %.1f GB", storageUsedGb),
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.White.copy(alpha = 0.6f)
                        )
                        Text(
                            text = String.format(Locale.US, "Total Storage: %.1f GB", storageTotalGb),
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.White.copy(alpha = 0.6f)
                        )
                    }
                }
            }
        }

        item {
            // Full Phone Diagnostics Suite Card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("full_diagnostics_card"),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF1E2A38))
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .clip(CircleShape)
                                .background(Color(0xFF03A9F4).copy(alpha = 0.1f))
                                .padding(8.dp)
                        ) {
                            Icon(
                                Icons.Default.AddModerator,
                                contentDescription = "System Diagnostics",
                                tint = Color(0xFF03A9F4)
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = "Full Phone Diagnostics Suite",
                                style = MaterialTheme.typography.titleMedium,
                                color = Color.White,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "Software permission & accessibility control center",
                                style = MaterialTheme.typography.labelSmall,
                                color = Color.White.copy(alpha = 0.6f)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // 1. Accessibility Service Control Section
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                "Accessibility Diagnostic Control",
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                            Text(
                                if (isAccessibilityEnabled) "Active: Service monitoring core events" else "Inactive: Click to configure in Accessibility Settings",
                                style = MaterialTheme.typography.bodySmall,
                                color = if (isAccessibilityEnabled) Color(0xFF00E676) else Color.White.copy(alpha = 0.6f)
                            )
                        }
                        Button(
                            onClick = { viewModel.requestAccessibilityPermission() },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (isAccessibilityEnabled) Color(0xFF0D47A1) else Color(0xFFFFB300),
                                contentColor = Color.White
                            ),
                            shape = RoundedCornerShape(8.dp),
                            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp),
                            modifier = Modifier.testTag("accessibility_settings_btn")
                        ) {
                            Text(
                                text = if (isAccessibilityEnabled) "CONFIGURE" else "ENABLE",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.Black
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))
                    Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(Color.White.copy(alpha = 0.05f)))
                    Spacer(modifier = Modifier.height(12.dp))

                    // 2. Phone State Diagnostics Section
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                "Telephony Hardware State",
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                            if (hasPhoneStatePermission) {
                                Text(
                                    "Carrier: $carrierName | SIM: $simState | Type: $phoneType",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = Color(0xFF00E676)
                                )
                            } else {
                                Text(
                                    "Permission blocked: Tap to grant Phone state diagnostic permission",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = Color.White.copy(alpha = 0.6f)
                                )
                            }
                        }
                        if (!hasPhoneStatePermission) {
                            Button(
                                onClick = { launcher.launch(android.Manifest.permission.READ_PHONE_STATE) },
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00E676), contentColor = Color.Black),
                                shape = RoundedCornerShape(8.dp),
                                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp),
                                modifier = Modifier.testTag("phone_permission_btn")
                            ) {
                                Text("GRANT", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            }
                        } else {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = "Granted",
                                tint = Color(0xFF00E676),
                                modifier = Modifier.padding(end = 8.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))
                    Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(Color.White.copy(alpha = 0.05f)))
                    Spacer(modifier = Modifier.height(12.dp))

                    // 3. Screen Timeout Control Section
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                "Physical Screen Timeout Manager",
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                            Text(
                                if (hasWriteSettingsPermission) "System Timeout: ${systemScreenTimeout / 1000} seconds" else "Write settings permission is required",
                                style = MaterialTheme.typography.bodySmall,
                                color = if (hasWriteSettingsPermission) Color(0xFF00E676) else Color.White.copy(alpha = 0.6f)
                            )
                        }
                        if (!hasWriteSettingsPermission) {
                            Button(
                                onClick = { viewModel.requestWriteSettingsPermission() },
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00E676), contentColor = Color.Black),
                                shape = RoundedCornerShape(8.dp),
                                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp),
                                modifier = Modifier.testTag("write_settings_permission_btn")
                            ) {
                                Text("GRANT", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            }
                        } else {
                            Button(
                                onClick = { viewModel.setSystemScreenTimeout(if (systemScreenTimeout == 15000) 30000 else 15000) },
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0D47A1), contentColor = Color.White),
                                shape = RoundedCornerShape(8.dp),
                                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp),
                                modifier = Modifier.testTag("toggle_timeout_btn")
                            ) {
                                Text(
                                    text = if (systemScreenTimeout == 15000) "SET 30s" else "SET 15s",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            }
                        }
                    }
                }
            }
        }

        item { Spacer(modifier = Modifier.height(16.dp)) }
    }
}
