package com.example.maslahty.presentation.screens.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.masla7ty.ui.theme.LocalAppColors
import com.example.maslahty.domain.utils.ValidationUtil
import com.example.maslahty.presentation.components.*
import com.example.maslahty.presentation.navigation.Screen
import com.example.maslahty.presentation.viewmodels.AuthState
import com.example.maslahty.presentation.viewmodels.AuthViewModel

@Composable
fun OTPVerificationScreen(
    navController: NavHostController,
    phoneNumber: String,
    viewModel: AuthViewModel = hiltViewModel()
) {
    val appColors = LocalAppColors.current
    var otpCode by remember { mutableStateOf("") }
    var localError by remember { mutableStateOf<String?>(null) }
    val verifyState by viewModel.otpVerifyState.collectAsState()

    LaunchedEffect(verifyState) {
        when (verifyState) {
            is AuthState.LoginSuccess -> {
                navController.navigate(Screen.HomeScreen.route) {
                    popUpTo(Screen.LoginScreen.route) { inclusive = true }
                }
                viewModel.resetState()
            }
            is AuthState.Error -> localError = (verifyState as AuthState.Error).message
            else -> Unit
        }
    }

    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(appColors.navy)
        ) {
            // Background circle
            Box(
                modifier = Modifier
                    .size(280.dp)
                    .align(Alignment.TopEnd)
                    .offset(x = 80.dp, y = (-60).dp)
                    .clip(CircleShape)
                    .background(appColors.gold.copy(alpha = 0.08f))
            )

            Column(modifier = Modifier.fillMaxSize()) {
                // Header
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp, vertical = 48.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Back button
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Start
                    ) {
                        IconButton(
                            onClick = { navController.popBackStack() },
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(Color.White.copy(alpha = 0.12f))
                        ) {
                            Icon(
                                imageVector = Icons.Default.ArrowForward,
                                contentDescription = "رجوع",
                                tint = Color.White
                            )
                        }
                    }

                    Spacer(Modifier.height(24.dp))

                    Box(
                        modifier = Modifier
                            .size(80.dp)
                            .clip(CircleShape)
                            .background(appColors.gold.copy(alpha = 0.15f))
                            .border(2.dp, appColors.gold.copy(alpha = 0.4f), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Sms,
                            contentDescription = null,
                            tint = appColors.gold,
                            modifier = Modifier.size(38.dp)
                        )
                    }

                    Spacer(Modifier.height(16.dp))

                    Text(
                        "رمز التحقق",
                        style = MaterialTheme.typography.headlineMedium,
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(Modifier.height(6.dp))
                    Text(
                        "تم إرسال رمز مكوّن من 6 أرقام إلى",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.White.copy(alpha = 0.65f),
                        textAlign = TextAlign.Center
                    )
                    Text(
                        text = phoneNumber,
                        style = MaterialTheme.typography.titleMedium,
                        color = appColors.gold,
                        fontWeight = FontWeight.Bold
                    )
                }

                // Form bottom sheet
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .clip(RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp))
                        .background(MaterialTheme.colorScheme.background)
                        .padding(horizontal = 24.dp, vertical = 32.dp)
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(24.dp)) {

                        // OTP BOXES
                        OTPBoxInput(
                            value = otpCode,
                            onValueChange = { otpCode = it },
                            length = 6
                        )

                        // Error
                        localError?.let { ErrorMessage(it) }

                        // Loading
                        if (verifyState is AuthState.Loading) {
                            LoadingBox(message = "جاري التحقق من الرمز...")
                        }

                        PrimaryButton(
                            text = "تأكيد الرمز",
                            icon = Icons.Default.Verified,
                            onClick = {
                                localError = null
                                if (!ValidationUtil.isValidOTPCode(otpCode)) {
                                    localError = "رمز OTP يجب أن يكون من 4 إلى 6 أرقام"
                                } else {
                                    viewModel.verifyOTP(phoneNumber, otpCode)
                                }
                            }
                        )

                        // Resend row
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                "لم تستقبل الرمز؟ ",
                                style = MaterialTheme.typography.bodySmall,
                                color = appColors.textSecondary
                            )
                            TextButton(onClick = {}) {
                                Text(
                                    "إعادة الإرسال",
                                    style = MaterialTheme.typography.labelMedium,
                                    color = appColors.gold,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun OTPBoxInput(
    value: String,
    onValueChange: (String) -> Unit,
    length: Int
) {
    val appColors = LocalAppColors.current
    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(Unit) { focusRequester.requestFocus() }

    Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxWidth()) {
        BasicTextField(
            value = value,
            onValueChange = { if (it.length <= length && it.all(Char::isDigit)) onValueChange(it) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
            modifier = Modifier
                .size(0.dp)
                .focusRequester(focusRequester),
            decorationBox = { }
        )
        Row(
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            repeat(length) { index ->
                val char = value.getOrNull(index)
                val isCurrent = index == value.length
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(
                            if (char != null) appColors.gold.copy(alpha = 0.12f)
                            else if (isCurrent) appColors.navy.copy(alpha = 0.08f)
                            else appColors.cardBackground
                        )
                        .border(
                            width = if (isCurrent) 2.dp else 1.5.dp,
                            color = if (char != null) appColors.gold
                            else if (isCurrent) appColors.navy
                            else appColors.cardBorder,
                            shape = RoundedCornerShape(12.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = char?.toString() ?: if (isCurrent) "|" else "",
                        style = MaterialTheme.typography.titleLarge,
                        color = if (char != null) appColors.navy else appColors.textTertiary,
                        fontWeight = FontWeight.Bold,
                        fontSize = if (isCurrent && char == null) 20.sp else 22.sp
                    )
                }
            }
        }
    }
}
