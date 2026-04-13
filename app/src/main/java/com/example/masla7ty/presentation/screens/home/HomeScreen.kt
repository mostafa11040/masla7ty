package com.example.maslahty.presentation.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Assignment
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.masla7ty.ui.theme.LocalAppColors
import com.example.maslahty.presentation.navigation.Screen

@Composable
fun HomeScreen(navController: NavHostController) {
    val appColors = LocalAppColors.current

    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .verticalScroll(rememberScrollState())
        ) {
            // ── TOP GRADIENT HEADER ────────────────────────────
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Brush.linearGradient(
                            listOf(appColors.navy, appColors.gradientEnd)
                        )
                    )
                    .padding(horizontal = 24.dp, vertical = 32.dp)
            ) {
                // Background circles
                Box(
                    modifier = Modifier
                        .size(180.dp)
                        .align(Alignment.TopEnd)
                        .offset(x = 50.dp, y = (-30).dp)
                        .clip(CircleShape)
                        .background(appColors.gold.copy(alpha = 0.08f))
                )

                Column {
                    // Top row: logo + logout
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Logo
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(40.dp)
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(appColors.gold.copy(alpha = 0.2f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.DirectionsCar,
                                    contentDescription = null,
                                    tint = appColors.gold,
                                    modifier = Modifier.size(22.dp)
                                )
                            }
                            Text(
                                text = "مصلحتي",
                                style = MaterialTheme.typography.titleLarge,
                                color = Color.White,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        // Logout
                        IconButton(
                            onClick = {
                                navController.navigate(Screen.LoginScreen.route) {
                                    popUpTo(0) { inclusive = true }
                                }
                            },
                            modifier = Modifier
                                .size(38.dp)
                                .clip(CircleShape)
                                .background(Color.White.copy(alpha = 0.1f))
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.Logout,
                                contentDescription = "تسجيل خروج",
                                tint = Color.White,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }

                    Spacer(Modifier.height(24.dp))

                    // Greeting
                    Text(
                        text = "مرحباً بك 👋",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.White.copy(alpha = 0.7f)
                    )
                    Text(
                        text = "أحمد محمد",
                        style = MaterialTheme.typography.headlineMedium,
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(Modifier.height(20.dp))

                    // Quick stats bar
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        QuickStatChip(label = "مركباتي", value = "3", modifier = Modifier.weight(1f))
                        QuickStatChip(label = "طلبات نشطة", value = "1", modifier = Modifier.weight(1f))
                        QuickStatChip(label = "مكتملة", value = "5", modifier = Modifier.weight(1f))
                    }
                }
            }

            // ── MAIN ACTIONS ───────────────────────────────────
            Column(
                modifier = Modifier.padding(horizontal = 20.dp, vertical = 24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = "الخدمات المتاحة",
                    style = MaterialTheme.typography.titleLarge,
                    color = appColors.textPrimary,
                    fontWeight = FontWeight.Bold
                )

                // Primary action - Sell Vehicle
                MainActionCard(
                    title = "بيع مركبة",
                    subtitle = "ابدأ عملية نقل الملكية كبائع",
                    icon = Icons.Default.Sell,
                    gradient = listOf(appColors.gold, Color(0xFF9A6E1A)),
                    textColor = Color(0xFF0D1B3E),
                    onClick = { navController.navigate(Screen.VehicleDetailsScreen.route) },
                    badgeText = "بائع"
                )

                // Secondary action - Manage requests
                MainActionCard(
                    title = "طلباتي",
                    subtitle = "عرض وإدارة طلبات نقل الملكية",
                    icon = Icons.AutoMirrored.Filled.Assignment,
                    gradient = listOf(appColors.navy, appColors.gradientEnd),
                    textColor = Color.White,
                    onClick = { navController.navigate(Screen.RequestsManagementScreen.route) },
                    badgeText = "مشتري / بائع"
                )

                // Violations inquiry action
                MainActionCard(
                    title = "الاستعلام عن المخالفات",
                    subtitle = "تحقق من المخالفات المرورية لمركباتك",
                    icon = Icons.Default.GppBad,
                    gradient = listOf(Color(0xFFDC2626), Color(0xFFEF4444)),
                    textColor = Color.White,
                    onClick = { navController.navigate(Screen.ViolationsMenuScreen.route) },
                    badgeText = "خدمة جديدة"
                )

                Spacer(Modifier.height(8.dp))

                // Info section
                Text(
                    text = "كيف يعمل النظام؟",
                    style = MaterialTheme.typography.titleMedium,
                    color = appColors.textPrimary,
                    fontWeight = FontWeight.Bold
                )

                // Steps
                listOf(
                    Triple(Icons.Default.HowToReg, "التحقق من الهوية", "الرقم القومي + OTP"),
                    Triple(Icons.Default.DirectionsCar, "تسجيل المركبة", "رفع بيانات وصور السيارة"),
                    Triple(Icons.Default.AttachMoney, "تحديد السعر", "مقارنة مع متوسط السوق"),
                    Triple(Icons.Default.NearMe, "إرسال للمشتري", "إشعار فوري بالرقم القومي"),
                    Triple(Icons.Default.Verified, "إتمام النقل", "موافقة المشتري وإتمام العملية"),
                ).forEachIndexed { index, (icon, title, sub) ->
                    StepInfoRow(step = index + 1, icon = icon, title = title, subtitle = sub)
                }
            }
        }
    }
}

@Composable
private fun QuickStatChip(
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    val appColors = LocalAppColors.current
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(Color.White.copy(alpha = 0.1f))
            .padding(vertical = 10.dp, horizontal = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(value, style = MaterialTheme.typography.titleLarge, color = appColors.gold, fontWeight = FontWeight.Bold)
        Text(label, style = MaterialTheme.typography.labelSmall, color = Color.White.copy(alpha = 0.7f))
    }
}

@Composable
private fun MainActionCard(
    title: String,
    subtitle: String,
    icon: ImageVector,
    gradient: List<Color>,
    textColor: Color,
    onClick: () -> Unit,
    badgeText: String
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(Brush.linearGradient(gradient))
            .clickable(onClick = onClick)
            .padding(20.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                // Badge
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .background(textColor.copy(alpha = 0.15f))
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = badgeText,
                        style = MaterialTheme.typography.labelSmall,
                        color = textColor.copy(alpha = 0.8f),
                        fontWeight = FontWeight.SemiBold
                    )
                }
                Spacer(Modifier.height(8.dp))
                Text(
                    text = title,
                    style = MaterialTheme.typography.headlineSmall,
                    color = textColor,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = textColor.copy(alpha = 0.75f)
                )
            }
            Box(
                modifier = Modifier
                    .size(60.dp)
                    .clip(CircleShape)
                    .background(textColor.copy(alpha = 0.12f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = textColor,
                    modifier = Modifier.size(32.dp)
                )
            }
        }
    }
}

@Composable
private fun StepInfoRow(
    step: Int,
    icon: ImageVector,
    title: String,
    subtitle: String
) {
    val appColors = LocalAppColors.current
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Box(
            modifier = Modifier
                .size(38.dp)
                .clip(CircleShape)
                .background(appColors.gold.copy(alpha = 0.12f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = appColors.gold,
                modifier = Modifier.size(20.dp)
            )
        }
        Column(modifier = Modifier.weight(1f)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Text(
                    text = "$step.",
                    style = MaterialTheme.typography.labelMedium,
                    color = appColors.gold,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleSmall,
                    color = appColors.textPrimary,
                    fontWeight = FontWeight.SemiBold
                )
            }
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall,
                color = appColors.textSecondary
            )
        }
    }
}
