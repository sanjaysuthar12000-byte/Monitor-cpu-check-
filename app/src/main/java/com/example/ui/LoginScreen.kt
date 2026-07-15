package com.example.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.SystemMonitorViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.random.Random

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(viewModel: SystemMonitorViewModel) {
    var selectedMethod by remember { mutableStateOf<LoginMethod?>(null) }
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    
    // Inputs
    var phoneInput by remember { mutableStateOf("") }
    var emailInput by remember { mutableStateOf("") }
    var otpInput by remember { mutableStateOf("") }
    
    // Simulators
    var generatedOtp by remember { mutableStateOf("") }
    var showSmsSimulator by remember { mutableStateOf(false) }
    var showEmailSimulator by remember { mutableStateOf(false) }
    
    val coroutineScope = rememberCoroutineScope()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF0F2027),
                        Color(0xFF203A43),
                        Color(0xFF2C5364)
                    )
                )
            )
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .widthIn(max = 420.dp),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF1E2A38).copy(alpha = 0.9f)),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Icon Header
                Box(
                    modifier = Modifier
                        .size(64.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color(0xFF00E676).copy(alpha = 0.15f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.BatteryChargingFull,
                        contentDescription = "App Logo",
                        tint = Color(0xFF00E676),
                        modifier = Modifier.size(36.dp)
                    )
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                
                Text(
                    text = "BATTERY HERO",
                    style = MaterialTheme.typography.headlineMedium,
                    color = Color.White,
                    fontWeight = FontWeight.Black,
                    letterSpacing = 2.sp
                )
                
                Text(
                    text = "Secure energy-tuning workspace",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.White.copy(alpha = 0.6f),
                    modifier = Modifier.padding(bottom = 24.dp)
                )

                if (isLoading) {
                    CircularProgressIndicator(
                        color = Color(0xFF00E676),
                        modifier = Modifier
                            .padding(24.dp)
                            .testTag("login_progress")
                    )
                    Text(
                        text = "Authenticating secure channel...",
                        color = Color.White.copy(alpha = 0.8f),
                        style = MaterialTheme.typography.bodyMedium
                    )
                } else if (selectedMethod == null) {
                    // Method Selection Screen
                    Text(
                        text = "CHOOSE SIGN IN METHOD",
                        style = MaterialTheme.typography.labelMedium,
                        color = Color(0xFF00E676),
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )

                    // Google login button
                    Button(
                        onClick = {
                            coroutineScope.launch {
                                isLoading = true
                                delay(1200)
                                viewModel.performLogin("google.user@gmail.com", "Google")
                                isLoading = false
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                            .testTag("login_google_btn"),
                        colors = ButtonDefaults.buttonColors(containerColor = Color.White, contentColor = Color(0xFF1E2A38)),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.AccountCircle, contentDescription = "Google Icon", modifier = Modifier.size(20.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Continue with Google", fontWeight = FontWeight.Bold)
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Facebook login button
                    Button(
                        onClick = {
                            coroutineScope.launch {
                                isLoading = true
                                delay(1200)
                                viewModel.performLogin("fb.profile_942", "Facebook")
                                isLoading = false
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                            .testTag("login_facebook_btn"),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1877F2), contentColor = Color.White),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.ThumbUp, contentDescription = "Facebook Icon", modifier = Modifier.size(20.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Continue with Facebook", fontWeight = FontWeight.Bold)
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Mobile login button
                    Button(
                        onClick = { selectedMethod = LoginMethod.PHONE },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                            .testTag("login_phone_btn"),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2E7D32), contentColor = Color.White),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Phone, contentDescription = "Phone Icon")
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Indian Mobile OTP", fontWeight = FontWeight.Bold)
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Email login button
                    Button(
                        onClick = { selectedMethod = LoginMethod.EMAIL },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                            .testTag("login_email_btn"),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00B0FF), contentColor = Color.White),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Email, contentDescription = "Email Icon")
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Email Verification OTP", fontWeight = FontWeight.Bold)
                        }
                    }
                } else {
                    // Form Flow (Phone or Email)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Start,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(onClick = {
                            selectedMethod = null
                            errorMessage = null
                            showSmsSimulator = false
                            showEmailSimulator = false
                            generatedOtp = ""
                            otpInput = ""
                        }) {
                            Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
                        }
                        Text(
                            text = if (selectedMethod == LoginMethod.PHONE) "Mobile Gateway" else "Email Gateway",
                            style = MaterialTheme.typography.titleMedium,
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    if (errorMessage != null) {
                        Text(
                            text = errorMessage!!,
                            color = Color(0xFFFF5252),
                            style = MaterialTheme.typography.bodySmall,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(bottom = 12.dp)
                        )
                    }

                    if (generatedOtp.isEmpty()) {
                        // Input view
                        if (selectedMethod == LoginMethod.PHONE) {
                            OutlinedTextField(
                                value = phoneInput,
                                onValueChange = { if (it.length <= 10) phoneInput = it },
                                label = { Text("Phone Number (India)") },
                                leadingIcon = {
                                    Text(
                                        text = "+91 ",
                                        color = Color.White.copy(alpha = 0.8f),
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.padding(start = 12.dp)
                                    )
                                },
                                placeholder = { Text("10 digit number") },
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = Color(0xFF00E676),
                                    unfocusedBorderColor = Color.White.copy(alpha = 0.3f),
                                    focusedLabelColor = Color(0xFF00E676),
                                    unfocusedLabelColor = Color.White.copy(alpha = 0.6f),
                                    focusedTextColor = Color.White,
                                    unfocusedTextColor = Color.White
                                ),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("phone_input_field"),
                                shape = RoundedCornerShape(12.dp)
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            Button(
                                onClick = {
                                    if (phoneInput.length != 10 || phoneInput.any { !it.isDigit() }) {
                                        errorMessage = "Please enter a valid 10-digit Indian mobile number."
                                    } else {
                                        errorMessage = null
                                        coroutineScope.launch {
                                            isLoading = true
                                            delay(1000)
                                            generatedOtp = String.format("%04d", Random.nextInt(1000, 9999))
                                            showSmsSimulator = true
                                            isLoading = false
                                        }
                                    }
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(48.dp)
                                    .testTag("get_otp_btn"),
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00E676), contentColor = Color(0xFF1E2A38)),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Text("GET OTP via SMS", fontWeight = FontWeight.Bold)
                            }
                        } else {
                            OutlinedTextField(
                                value = emailInput,
                                onValueChange = { emailInput = it },
                                label = { Text("Email Address") },
                                leadingIcon = { Icon(Icons.Default.Email, contentDescription = "Email", tint = Color.White.copy(alpha = 0.6f)) },
                                placeholder = { Text("name@domain.com") },
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = Color(0xFF00B0FF),
                                    unfocusedBorderColor = Color.White.copy(alpha = 0.3f),
                                    focusedLabelColor = Color(0xFF00B0FF),
                                    unfocusedLabelColor = Color.White.copy(alpha = 0.6f),
                                    focusedTextColor = Color.White,
                                    unfocusedTextColor = Color.White
                                ),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("email_input_field"),
                                shape = RoundedCornerShape(12.dp)
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            Button(
                                onClick = {
                                    if (!emailInput.contains("@") || !emailInput.contains(".")) {
                                        errorMessage = "Please enter a valid email address."
                                    } else {
                                        errorMessage = null
                                        coroutineScope.launch {
                                            isLoading = true
                                            delay(1000)
                                            generatedOtp = String.format("%04d", Random.nextInt(1000, 9999))
                                            showEmailSimulator = true
                                            isLoading = false
                                        }
                                    }
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(48.dp)
                                    .testTag("get_email_otp_btn"),
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00B0FF), contentColor = Color.White),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Text("SEND OTP TO EMAIL", fontWeight = FontWeight.Bold)
                            }
                        }
                    } else {
                        // OTP verification view
                        Text(
                            text = if (selectedMethod == LoginMethod.PHONE) 
                                "Enter the 4-digit verification code sent to +91 $phoneInput" 
                            else 
                                "Enter the 4-digit verification code sent to $emailInput",
                            color = Color.White.copy(alpha = 0.8f),
                            style = MaterialTheme.typography.bodySmall,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(bottom = 16.dp)
                        )

                        OutlinedTextField(
                            value = otpInput,
                            onValueChange = { if (it.length <= 4) otpInput = it },
                            label = { Text("Enter OTP") },
                            leadingIcon = { Icon(Icons.Default.Lock, contentDescription = "OTP Icon", tint = Color.White.copy(alpha = 0.6f)) },
                            placeholder = { Text("4-digit code") },
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = Color(0xFF00E676),
                                unfocusedBorderColor = Color.White.copy(alpha = 0.3f),
                                focusedLabelColor = Color(0xFF00E676),
                                unfocusedLabelColor = Color.White.copy(alpha = 0.6f),
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color.White
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("otp_input_field"),
                            shape = RoundedCornerShape(12.dp)
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Button(
                            onClick = {
                                if (otpInput == generatedOtp) {
                                    val user = if (selectedMethod == LoginMethod.PHONE) "+91 $phoneInput" else emailInput
                                    viewModel.performLogin(user, if (selectedMethod == LoginMethod.PHONE) "Mobile OTP" else "Email OTP")
                                } else {
                                    errorMessage = "Invalid verification code. Please try again."
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp)
                                .testTag("verify_otp_btn"),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (selectedMethod == LoginMethod.PHONE) Color(0xFF00E676) else Color(0xFF00B0FF),
                                contentColor = Color.White
                            ),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text("VERIFY & SIGN IN", fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }

        // Active SMS / Email simulator popups placed at the bottom
        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(bottom = 12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            AnimatedVisibility(
                visible = showSmsSimulator,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF2C3E50)),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.PhoneAndroid, contentDescription = "SMS", tint = Color(0xFF00E676))
                        Spacer(modifier = Modifier.width(12.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text("SIMULATED SMS GATEWAY (+91$phoneInput)", style = MaterialTheme.typography.labelSmall, color = Color(0xFF00E676), fontWeight = FontWeight.Bold)
                            Text("Your Battery Hero verification code is: $generatedOtp", style = MaterialTheme.typography.bodyMedium, color = Color.White, fontWeight = FontWeight.Bold)
                        }
                        IconButton(onClick = { showSmsSimulator = false }) {
                            Icon(Icons.Default.Close, contentDescription = "Close", tint = Color.White)
                        }
                    }
                }
            }

            AnimatedVisibility(
                visible = showEmailSimulator,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF34495E)),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.Inbox, contentDescription = "Email Received", tint = Color(0xFF00B0FF))
                        Spacer(modifier = Modifier.width(12.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text("SIMULATED EMAIL INBOX ($emailInput)", style = MaterialTheme.typography.labelSmall, color = Color(0xFF00B0FF), fontWeight = FontWeight.Bold)
                            Text("Real OTP Code: $generatedOtp for signing in securely.", style = MaterialTheme.typography.bodyMedium, color = Color.White, fontWeight = FontWeight.Bold)
                        }
                        IconButton(onClick = { showEmailSimulator = false }) {
                            Icon(Icons.Default.Close, contentDescription = "Close", tint = Color.White)
                        }
                    }
                }
            }
        }
    }
}

enum class LoginMethod {
    PHONE, EMAIL
}
