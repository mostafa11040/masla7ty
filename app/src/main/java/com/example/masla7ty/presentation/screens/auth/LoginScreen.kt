package com.example.maslahty.presentation.screens.auth

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.font.FontWeight
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
fun LoginScreen(
    navController: NavHostController,
    viewModel: AuthViewModel = hiltViewModel()
) {
    val appColors = LocalAppColors.current
    var nationalId by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }
    var localError by remember { mutableStateOf<String?>(null) }
    val otpSentState by viewModel.otpSentState.collectAsState()

    LaunchedEffect(otpSentState) {
        when (otpSentState) {
            is AuthState.OTPSent -> {
                navController.navigate(Screen.OTPVerificationScreen.createRoute(phoneNumber))
                viewModel.resetState()
            }
            is AuthState.Error -> localError = (otpSentState as AuthState.Error).message
            else -> Unit
        }
    }

    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(appColors.navy)
        ) {
            // Background decorative circles
            Box(
                modifier = Modifier
                    .size(300.dp)
                    .offset(x = (-80).dp, y = (-80).dp)
                    .clip(CircleShape)
                    .background(appColors.gold.copy(alpha = 0.07f))
            )
            Box(
                modifier = Modifier
                    .size(200.dp)
                    .align(Alignment.TopEnd)
                    .offset(x = 60.dp, y = 60.dp)
                    .clip(CircleShape)
                    .background(Color.White.copy(alpha = 0.04f))
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
            ) {
                // ── HERO SECTION ──────────────────────────────
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 28.dp, vertical = 60.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Logo badge
                    Box(
                        modifier = Modifier
                            .size(90.dp)
                            .clip(RoundedCornerShape(24.dp))
                            .background(
                                Brush.linearGradient(
                                    listOf(appColors.gold, appColors.goldLight)
                                )
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.DirectionsCar,
                            contentDescription = null,
                            tint = Color(0xFF0D1B3E),
                            modifier = Modifier.size(48.dp)
                        )
                    }

                    Spacer(Modifier.height(20.dp))

                    Text(
                        text = "مصلحتي",
                        style = MaterialTheme.typography.displayMedium,
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "منصة نقل ملكية المركبات",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.White.copy(alpha = 0.65f),
                        textAlign = TextAlign.Center
                    )
                }

                // ── FORM CARD ─────────────────────────────────
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp))
                        .background(MaterialTheme.colorScheme.background)
                        .padding(horizontal = 24.dp, vertical = 30.dp)
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(20.dp)) {

                        // Title
                        Column {
                            Text(
                                text = "تسجيل الدخول",
                                style = MaterialTheme.typography.headlineMedium,
                                color = appColors.textPrimary,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "أدخل بياناتك للمتابعة",
                                style = MaterialTheme.typography.bodyMedium,
                                color = appColors.textSecondary
                            )
                        }

                        // National ID steps hint
                        AppCard {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Info,
                                    contentDescription = null,
                                    tint = appColors.gold,
                                    modifier = Modifier.size(20.dp)
                                )
                                Column {
                                    Text(
                                        text = "الهوية الرقمية",
                                        style = MaterialTheme.typography.labelMedium,
                                        color = appColors.textPrimary,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        text = "الرقم القومي + رمز OTP يضمنان أمان كامل",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = appColors.textSecondary
                                    )
                                }
                            }
                        }

                        // Fields
                        NationalIdInput(
                            value = nationalId,
                            onValueChange = { nationalId = it }
                        )

                        PhoneInput(
                            value = phoneNumber,
                            onValueChange = { phoneNumber = it }
                        )

                        // Error
                        localError?.let { ErrorMessage(it) }

                        // Loading
                        if (otpSentState is AuthState.Loading) {
                            LoadingBox(message = "جاري إرسال رمز التحقق...")
                        }

                        // Login Button
                        PrimaryButton(
                            text = "إرسال رمز التحقق",
                            icon = Icons.Default.Send,
                            onClick = {
                                localError = null
                                when {
                                    nationalId.length < 10 ->
                                        localError = "الرقم القومي يجب أن يكون على الأقل 10 أرقام"
                                    !ValidationUtil.isValidPhoneNumber(phoneNumber) ->
                                        localError = "رقم الهاتف غير صحيح"
                                    else -> viewModel.sendOTP(nationalId, phoneNumber)
                                }
                            }
                        )

                        Divider(color = appColors.cardBorder)

                        // Register link
                        SecondaryButton(
                            text = "مستخدم جديد؟ إنشاء حساب",
                            icon = Icons.Default.PersonAdd,
                            onClick = { navController.navigate(Screen.RegistrationScreen.route) }
                        )

                        // Security badges
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                Icons.Default.Security,
                                contentDescription = null,
                                tint = appColors.textTertiary,
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(Modifier.width(4.dp))
                            Text(
                                text = "مؤمّن ومشفّر - جهة حكومية معتمدة",
                                style = MaterialTheme.typography.labelSmall,
                                color = appColors.textTertiary,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            }
        }
    }
}
