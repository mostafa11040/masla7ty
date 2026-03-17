package com.example.maslahty.presentation.screens.auth

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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.masla7ty.ui.theme.LocalAppColors
import com.example.maslahty.presentation.components.*
import com.example.maslahty.presentation.navigation.Screen
import com.example.maslahty.presentation.viewmodels.AuthViewModel

@Composable
fun RegistrationScreen(
    navController: NavHostController,
    viewModel: AuthViewModel = hiltViewModel()
) {
    val appColors = LocalAppColors.current
    var fullName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }
    var nationalId by remember { mutableStateOf("") }
    var userType by remember { mutableStateOf("SELLER") }
    var address by remember { mutableStateOf("") }
    var city by remember { mutableStateOf("") }
    var error by remember { mutableStateOf<String?>(null) }

    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(appColors.navy)
        ) {
            Box(
                modifier = Modifier
                    .size(200.dp)
                    .offset(x = (-50).dp, y = (-50).dp)
                    .clip(CircleShape)
                    .background(appColors.gold.copy(alpha = 0.08f))
            )

            Column(modifier = Modifier.fillMaxSize()) {
                // Header
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp, vertical = 36.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
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

                    Spacer(Modifier.height(20.dp))

                    Box(
                        modifier = Modifier
                            .size(72.dp)
                            .clip(RoundedCornerShape(20.dp))
                            .background(
                                Brush.linearGradient(listOf(appColors.gold, appColors.goldLight))
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.PersonAdd,
                            contentDescription = null,
                            tint = Color(0xFF0D1B3E),
                            modifier = Modifier.size(36.dp)
                        )
                    }

                    Spacer(Modifier.height(14.dp))

                    Text(
                        "إنشاء حساب جديد",
                        style = MaterialTheme.typography.headlineMedium,
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        "أدخل بياناتك الشخصية",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.White.copy(alpha = 0.65f)
                    )
                }

                // Form
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .clip(RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp))
                        .background(MaterialTheme.colorScheme.background)
                        .padding(horizontal = 24.dp, vertical = 28.dp)
                ) {
                    Column(
                        modifier = Modifier.verticalScroll(rememberScrollState()),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {

                        SectionHeader(title = "البيانات الشخصية", icon = Icons.Default.Person)

                        AppTextField(
                            value = fullName,
                            onValueChange = { fullName = it },
                            label = "الاسم الكامل",
                            leadingIcon = Icons.Default.Person,
                            placeholder = "أدخل اسمك الكامل"
                        )

                        AppTextField(
                            value = nationalId,
                            onValueChange = { if (it.length <= 14 && it.all(Char::isDigit)) nationalId = it },
                            label = "الرقم القومي",
                            leadingIcon = Icons.Default.Badge,
                            placeholder = "14 رقم",
                            keyboardType = KeyboardType.Number
                        )

                        AppTextField(
                            value = phoneNumber,
                            onValueChange = { if (it.length <= 11 && it.all(Char::isDigit)) phoneNumber = it },
                            label = "رقم الهاتف",
                            leadingIcon = Icons.Default.Phone,
                            placeholder = "01xxxxxxxxx",
                            keyboardType = KeyboardType.Phone
                        )

                        AppTextField(
                            value = email,
                            onValueChange = { email = it },
                            label = "البريد الإلكتروني (اختياري)",
                            leadingIcon = Icons.Default.Email,
                            placeholder = "example@email.com",
                            keyboardType = KeyboardType.Email
                        )

                        Divider(color = appColors.cardBorder)

                        SectionHeader(title = "العنوان", icon = Icons.Default.LocationOn)

                        AppTextField(
                            value = city,
                            onValueChange = { city = it },
                            label = "المحافظة / المدينة",
                            leadingIcon = Icons.Default.LocationCity,
                            placeholder = "القاهرة"
                        )

                        AppTextField(
                            value = address,
                            onValueChange = { address = it },
                            label = "العنوان بالتفصيل",
                            leadingIcon = Icons.Default.Home,
                            placeholder = "الحي، الشارع، رقم المبنى...",
                            singleLine = false,
                            maxLines = 3
                        )

                        Divider(color = appColors.cardBorder)

                        SectionHeader(title = "نوع الحساب", icon = Icons.Default.ManageAccounts)

                        // Role selector
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            listOf("SELLER" to "بائع", "BUYER" to "مشتري").forEach { (key, label) ->
                                val isSelected = userType == key
                                OutlinedButton(
                                    onClick = { userType = key },
                                    modifier = Modifier.weight(1f).height(52.dp),
                                    shape = RoundedCornerShape(12.dp),
                                    colors = ButtonDefaults.outlinedButtonColors(
                                        containerColor = if (isSelected) appColors.gold.copy(alpha = 0.12f) else Color.Transparent,
                                        contentColor = if (isSelected) appColors.gold else appColors.textSecondary
                                    ),
                                    border = androidx.compose.foundation.BorderStroke(
                                        width = if (isSelected) 2.dp else 1.dp,
                                        color = if (isSelected) appColors.gold else appColors.cardBorder
                                    )
                                ) {
                                    Icon(
                                        imageVector = if (key == "SELLER") Icons.Default.Sell else Icons.Default.ShoppingCart,
                                        contentDescription = null,
                                        modifier = Modifier.size(18.dp)
                                    )
                                    Spacer(Modifier.width(6.dp))
                                    Text(label, fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal)
                                }
                            }
                        }

                        error?.let { ErrorMessage(it) }

                        PrimaryButton(
                            text = "إنشاء الحساب",
                            icon = Icons.Default.Check,
                            onClick = {
                                when {
                                    fullName.isBlank() -> error = "أدخل الاسم الكامل"
                                    nationalId.length < 10 -> error = "الرقم القومي غير صحيح"
                                    phoneNumber.length < 11 -> error = "رقم الهاتف غير صحيح"
                                    city.isBlank() -> error = "أدخل المحافظة/المدينة"
                                    else -> {
                                        navController.navigate(Screen.HomeScreen.route) {
                                            popUpTo(Screen.LoginScreen.route) { inclusive = true }
                                        }
                                    }
                                }
                            }
                        )

                        Spacer(Modifier.height(8.dp))
                    }
                }
            }
        }
    }
}
