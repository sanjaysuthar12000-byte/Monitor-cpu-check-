package com.example

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.data.AppDatabase
import com.example.data.OptimizationRepository
import com.example.ui.*
import com.example.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        
        setContent {
            MyApplicationTheme(darkTheme = true, dynamicColor = false) {
                val context = LocalContext.current
                val database = AppDatabase.getDatabase(context)
                val repository = OptimizationRepository(database.optimizationDao())
                val factory = SystemMonitorViewModelFactory(context, repository)
                val viewModel: SystemMonitorViewModel = viewModel(factory = factory)

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color(0xFF0F172A) // Sleek Slate-Dark Palette base color
                ) {
                    val isLoggedIn by viewModel.isLoggedIn.collectAsState()
                    if (isLoggedIn) {
                        MainScreen(viewModel = viewModel)
                    } else {
                        LoginScreen(viewModel = viewModel)
                    }
                }
            }
        }
    }
}

@Composable
fun MainScreen(viewModel: SystemMonitorViewModel) {
    var selectedTab by remember { mutableIntStateOf(0) }
    var showSettingsDialog by remember { mutableStateOf(false) }
    val currentProfile by viewModel.cpuProfileMode.collectAsState()

    Scaffold(
        bottomBar = {
            NavigationBar(
                modifier = Modifier.fillMaxWidth().testTag("app_navigation_bar"),
                containerColor = Color(0xFF1E293B)
            ) {
                NavigationBarItem(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    icon = { Icon(Icons.Default.BatteryChargingFull, contentDescription = "Dashboard") },
                    label = { Text("Dashboard", fontSize = 11.sp) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Color(0xFF00E676),
                        unselectedIconColor = Color.White.copy(alpha = 0.5f),
                        selectedTextColor = Color(0xFF00E676),
                        unselectedTextColor = Color.White.copy(alpha = 0.5f),
                        indicatorColor = Color.Transparent
                    ),
                    modifier = Modifier.testTag("tab_dashboard")
                )
                NavigationBarItem(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    icon = { Icon(Icons.Default.QueryStats, contentDescription = "Usage") },
                    label = { Text("Usage", fontSize = 11.sp) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Color(0xFF00E676),
                        unselectedIconColor = Color.White.copy(alpha = 0.5f),
                        selectedTextColor = Color(0xFF00E676),
                        unselectedTextColor = Color.White.copy(alpha = 0.5f),
                        indicatorColor = Color.Transparent
                    ),
                    modifier = Modifier.testTag("tab_usage")
                )
                NavigationBarItem(
                    selected = selectedTab == 2,
                    onClick = { selectedTab = 2 },
                    icon = { Icon(Icons.Default.OfflineBolt, contentDescription = "Tips") },
                    label = { Text("Tips", fontSize = 11.sp) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Color(0xFF00E676),
                        unselectedIconColor = Color.White.copy(alpha = 0.5f),
                        selectedTextColor = Color(0xFF00E676),
                        unselectedTextColor = Color.White.copy(alpha = 0.5f),
                        indicatorColor = Color.Transparent
                    ),
                    modifier = Modifier.testTag("tab_tips")
                )
                NavigationBarItem(
                    selected = selectedTab == 3,
                    onClick = { selectedTab = 3 },
                    icon = { Icon(Icons.Default.DeveloperMode, contentDescription = "Hardware") },
                    label = { Text("Hardware", fontSize = 11.sp) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Color(0xFF00E676),
                        unselectedIconColor = Color.White.copy(alpha = 0.5f),
                        selectedTextColor = Color(0xFF00E676),
                        unselectedTextColor = Color.White.copy(alpha = 0.5f),
                        indicatorColor = Color.Transparent
                    ),
                    modifier = Modifier.testTag("tab_hardware")
                )
                NavigationBarItem(
                    selected = selectedTab == 4,
                    onClick = { selectedTab = 4 },
                    icon = { Icon(Icons.Default.History, contentDescription = "Logs") },
                    label = { Text("Logs", fontSize = 11.sp) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Color(0xFF00E676),
                        unselectedIconColor = Color.White.copy(alpha = 0.5f),
                        selectedTextColor = Color(0xFF00E676),
                        unselectedTextColor = Color.White.copy(alpha = 0.5f),
                        indicatorColor = Color.Transparent
                    ),
                    modifier = Modifier.testTag("tab_logs")
                )
            }
        },
        containerColor = Color(0xFF0F172A)
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(Color(0xFF0F172A))
        ) {
            // Header bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Settings action top left
                IconButton(
                    onClick = { showSettingsDialog = true },
                    modifier = Modifier
                        .padding(end = 8.dp)
                        .testTag("top_left_settings_btn")
                ) {
                    Icon(
                        imageVector = Icons.Default.Settings,
                        contentDescription = "Settings Panel",
                        tint = Color(0xFF00E676),
                        modifier = Modifier.size(26.dp)
                    )
                }

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "BATTERY HERO",
                        style = MaterialTheme.typography.titleMedium,
                        color = Color.White,
                        fontWeight = FontWeight.Black,
                        letterSpacing = 1.sp
                    )
                    Text(
                        text = "Advanced Hardware Calibration Suite",
                        style = MaterialTheme.typography.labelSmall,
                        color = Color.White.copy(alpha = 0.5f)
                    )
                }

                // Active CPU Mode Indicator badge
                Box(
                    modifier = Modifier
                        .background(
                            color = when (currentProfile) {
                                "Full Power" -> Color(0xFFB71C1C)
                                "Power Saver" -> Color(0xFF1B5E20)
                                else -> Color(0xFF0D47A1)
                            },
                            shape = RoundedCornerShape(12.dp)
                        )
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = currentProfile.uppercase(),
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }

            // Main Tab View switcher
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                when (selectedTab) {
                    0 -> DashboardTab(viewModel = viewModel)
                    1 -> BatteryUsageTab(viewModel = viewModel)
                    2 -> BatteryTipsTab(viewModel = viewModel)
                    3 -> {
                        // Display combined Hardware metrics and CPU Governor selection
                        Column(modifier = Modifier.fillMaxSize()) {
                            // Section Header for CPU Governor
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp, vertical = 8.dp),
                                shape = RoundedCornerShape(16.dp),
                                colors = CardDefaults.cardColors(containerColor = Color(0xFF1E2A38))
                            ) {
                                Column(modifier = Modifier.padding(16.dp)) {
                                    Text(
                                        text = "CPU CLOCK GOVERNOR SELECTOR",
                                        style = MaterialTheme.typography.labelMedium,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFF00E676),
                                        letterSpacing = 1.sp,
                                        modifier = Modifier.padding(bottom = 12.dp)
                                    )
                                    
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        Button(
                                            onClick = { viewModel.setCpuProfileMode("Power Saver") },
                                            modifier = Modifier.weight(1f),
                                            colors = ButtonDefaults.buttonColors(
                                                containerColor = if (currentProfile == "Power Saver") Color(0xFF1B5E20) else Color.DarkGray,
                                                contentColor = Color.White
                                            ),
                                            shape = RoundedCornerShape(8.dp),
                                            contentPadding = PaddingValues(horizontal = 4.dp, vertical = 6.dp)
                                        ) {
                                            Text("Saver", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                        }
                                        Button(
                                            onClick = { viewModel.setCpuProfileMode("Balanced") },
                                            modifier = Modifier.weight(1f),
                                            colors = ButtonDefaults.buttonColors(
                                                containerColor = if (currentProfile == "Balanced") Color(0xFF0D47A1) else Color.DarkGray,
                                                contentColor = Color.White
                                            ),
                                            shape = RoundedCornerShape(8.dp),
                                            contentPadding = PaddingValues(horizontal = 4.dp, vertical = 6.dp)
                                        ) {
                                            Text("Balanced", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                        }
                                        Button(
                                            onClick = { viewModel.setCpuProfileMode("Full Power") },
                                            modifier = Modifier.weight(1f),
                                            colors = ButtonDefaults.buttonColors(
                                                containerColor = if (currentProfile == "Full Power") Color(0xFFB71C1C) else Color.DarkGray,
                                                contentColor = Color.White
                                            ),
                                            shape = RoundedCornerShape(8.dp),
                                            contentPadding = PaddingValues(horizontal = 4.dp, vertical = 6.dp)
                                        ) {
                                            Text("Gaming", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                        }
                                    }
                                }
                            }
                            Box(modifier = Modifier.weight(1f)) {
                                HardwareTab(viewModel = viewModel)
                            }
                        }
                    }
                    4 -> LogsTab(viewModel = viewModel)
                }
            }
        }
    }

    if (showSettingsDialog) {
        SettingsDialog(
            viewModel = viewModel,
            onDismiss = { showSettingsDialog = false }
        )
    }
}
