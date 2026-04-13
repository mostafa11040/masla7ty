package com.example.maslahty.presentation.screens.violations

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.masla7ty.ui.theme.LocalAppColors
import com.example.maslahty.domain.entities.Violation
import com.example.maslahty.domain.entities.ViolationStatus
import com.example.maslahty.presentation.components.GradientHeader
import com.example.maslahty.presentation.components.LoadingBox
import com.example.maslahty.presentation.components.ErrorMessage
import com.example.maslahty.presentation.viewmodels.ViolationsUiState
import com.example.maslahty.presentation.viewmodels.ViolationsViewModel
import kotlinx.coroutines.delay
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun VehicleViolationsScreen(
    navController: NavHostController,
    vehicleId: String,
    viewModel: ViolationsViewModel = hiltViewModel()
) {
    val appColors = LocalAppColors.current
    val violationsState by viewModel.violationsState.collectAsState()

    LaunchedEffect(vehicleId) {
        viewModel.loadVehicleViolations(vehicleId)
    }

    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {
            // ── GRADIENT HEADER ────────────────────────────
            GradientHeader(
                title = "المخالفات المرورية",
                subtitle = "تفاصيل المخالفات المسجلة على المركبة",
                onBack = { navController.popBackStack() },
                icon = Icons.Default.Receipt
            )

            // ── CONTENT ────────────────────────────────────
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 20.dp, vertical = 24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                when (val state = violationsState) {
                    is ViolationsUiState.Loading -> {
                        LoadingBox(message = "جاري تحميل المخالفات...")
                    }
                    is ViolationsUiState.Error -> {
                        ErrorMessage(message = state.message)
                    }
                    is ViolationsUiState.ViolationsLoaded -> {
                        if (state.violations.isEmpty()) {
                            NoViolationsView()
                        } else {
                            // Summary Card
                            ViolationsSummaryCard(violations = state.violations)

                            Spacer(Modifier.height(4.dp))

                            // Section Header
                            Text(
                                text = "تفاصيل المخالفات",
                                style = MaterialTheme.typography.titleMedium,
                                color = appColors.textPrimary,
                                fontWeight = FontWeight.Bold
                            )

                            // Violations List
                            state.violations.forEachIndexed { index, violation ->
                                var visible by remember { mutableStateOf(false) }
                                LaunchedEffect(Unit) {
                                    delay(index * 120L)
                                    visible = true
                                }
                                AnimatedVisibility(
                                    visible = visible,
                                    enter = fadeIn(animationSpec = tween(400)) + slideInVertically(
                                        animationSpec = spring(
                                            dampingRatio = Spring.DampingRatioMediumBouncy,
                                            stiffness = Spring.StiffnessMediumLow
                                        ),
                                        initialOffsetY = { it / 3 }
                                    )
                                ) {
                                    ViolationCard(violation = violation)
                                }
                            }

                            // Bottom warning if unpaid
                            val unpaidCount = state.violations.count { it.status == ViolationStatus.UNPAID }
                            if (unpaidCount > 0) {
                                Spacer(Modifier.height(4.dp))
                                TransferWarningBanner(unpaidCount = unpaidCount)
                            }
                        }
                    }
                    else -> { /* Initial */ }
                }
            }
        }
    }
}

@Composable
private fun ViolationsSummaryCard(violations: List<Violation>) {
    val appColors = LocalAppColors.current
    val totalAmount = violations.sumOf { it.amount }
    val paidCount = violations.count { it.status == ViolationStatus.PAID }
    val unpaidCount = violations.count { it.status == ViolationStatus.UNPAID }
    val unpaidAmount = violations.filter { it.status == ViolationStatus.UNPAID }.sumOf { it.amount }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.linearGradient(
                        listOf(appColors.navy, appColors.gradientEnd)
                    )
                )
                .padding(20.dp)
        ) {
            // Decorative circle
            Box(
                modifier = Modifier
                    .size(120.dp)
                    .align(Alignment.TopEnd)
                    .offset(x = 30.dp, y = (-20).dp)
                    .clip(CircleShape)
                    .background(appColors.gold.copy(alpha = 0.06f))
            )

            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                // Title row
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
                            imageVector = Icons.Default.Assessment,
                            contentDescription = null,
                            tint = appColors.gold,
                            modifier = Modifier.size(22.dp)
                        )
                    }
                    Text(
                        text = "ملخص المخالفات",
                        style = MaterialTheme.typography.titleMedium,
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                }

                // Stats row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    SummaryStatItem(
                        label = "إجمالي",
                        value = violations.size.toString(),
                        icon = Icons.Default.FormatListNumbered,
                        modifier = Modifier.weight(1f)
                    )
                    SummaryStatItem(
                        label = "مدفوعة",
                        value = paidCount.toString(),
                        icon = Icons.Default.CheckCircle,
                        color = Color(0xFF4ADE80),
                        modifier = Modifier.weight(1f)
                    )
                    SummaryStatItem(
                        label = "غير مدفوعة",
                        value = unpaidCount.toString(),
                        icon = Icons.Default.ErrorOutline,
                        color = Color(0xFFFF6B6B),
                        modifier = Modifier.weight(1f)
                    )
                }

                // Total amount
                HorizontalDivider(
                    color = Color.White.copy(alpha = 0.1f),
                    thickness = 1.dp
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "إجمالي المبلغ المطلوب",
                            style = MaterialTheme.typography.labelSmall,
                            color = Color.White.copy(alpha = 0.6f)
                        )
                        Text(
                            text = "${String.format("%.0f", unpaidAmount)} جنيه",
                            style = MaterialTheme.typography.headlineSmall,
                            color = if (unpaidAmount > 0) Color(0xFFFF6B6B) else Color(0xFF4ADE80),
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .background(
                                if (unpaidCount > 0) Color(0xFFFF6B6B).copy(alpha = 0.15f)
                                else Color(0xFF4ADE80).copy(alpha = 0.15f)
                            )
                            .padding(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = if (unpaidCount > 0) "يوجد متأخرات" else "لا متأخرات",
                            style = MaterialTheme.typography.labelSmall,
                            color = if (unpaidCount > 0) Color(0xFFFF6B6B) else Color(0xFF4ADE80),
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun SummaryStatItem(
    label: String,
    value: String,
    icon: ImageVector,
    modifier: Modifier = Modifier,
    color: Color = Color.White
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(Color.White.copy(alpha = 0.08f))
            .padding(vertical = 10.dp, horizontal = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = color,
            modifier = Modifier.size(18.dp)
        )
        Text(
            text = value,
            style = MaterialTheme.typography.titleLarge,
            color = color,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = Color.White.copy(alpha = 0.6f),
            fontSize = 10.sp
        )
    }
}

@Composable
private fun ViolationCard(violation: Violation) {
    val appColors = LocalAppColors.current
    val isPaid = violation.status == ViolationStatus.PAID
    val dateFormat = remember { SimpleDateFormat("dd/MM/yyyy", Locale("ar")) }

    val violationIcon = when {
        violation.violationType.contains("سرعة") -> Icons.Default.Speed
        violation.violationType.contains("وقوف") -> Icons.Default.LocalParking
        violation.violationType.contains("إشارة") -> Icons.Default.Traffic
        violation.violationType.contains("رخصة") -> Icons.Default.Badge
        else -> Icons.Default.Warning
    }

    val statusColor = if (isPaid) appColors.statusApproved else appColors.statusRejected
    val statusBgColor = if (isPaid) appColors.statusApprovedBg else appColors.statusRejectedBg

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = appColors.cardBackground),
        border = androidx.compose.foundation.BorderStroke(
            1.dp,
            if (!isPaid) appColors.statusRejected.copy(alpha = 0.2f)
            else appColors.cardBorder
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Top row: type + status
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(statusColor.copy(alpha = 0.12f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = violationIcon,
                            contentDescription = null,
                            tint = statusColor,
                            modifier = Modifier.size(22.dp)
                        )
                    }
                    Column {
                        Text(
                            text = violation.violationType,
                            style = MaterialTheme.typography.titleSmall,
                            color = appColors.textPrimary,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = dateFormat.format(violation.date),
                            style = MaterialTheme.typography.labelSmall,
                            color = appColors.textTertiary
                        )
                    }
                }

                // Status badge
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .background(statusBgColor)
                        .padding(horizontal = 10.dp, vertical = 5.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        imageVector = if (isPaid) Icons.Default.CheckCircle else Icons.Default.Cancel,
                        contentDescription = null,
                        tint = statusColor,
                        modifier = Modifier.size(14.dp)
                    )
                    Text(
                        text = if (isPaid) "مدفوعة" else "غير مدفوعة",
                        style = MaterialTheme.typography.labelSmall,
                        color = statusColor,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            // Description
            Text(
                text = violation.description,
                style = MaterialTheme.typography.bodySmall,
                color = appColors.textSecondary,
                lineHeight = 20.sp
            )

            // Divider
            HorizontalDivider(color = appColors.cardBorder, thickness = 0.5.dp)

            // Bottom info row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Location
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.LocationOn,
                        contentDescription = null,
                        tint = appColors.textTertiary,
                        modifier = Modifier.size(14.dp)
                    )
                    Text(
                        text = violation.location,
                        style = MaterialTheme.typography.labelSmall,
                        color = appColors.textTertiary
                    )
                }

                // Amount
                Text(
                    text = "${String.format("%.0f", violation.amount)} جنيه",
                    style = MaterialTheme.typography.titleSmall,
                    color = if (isPaid) appColors.textSecondary else appColors.statusRejected,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
private fun TransferWarningBanner(unpaidCount: Int) {
    val appColors = LocalAppColors.current
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(
                Brush.linearGradient(
                    listOf(
                        Color(0xFFFF6B6B).copy(alpha = 0.12f),
                        Color(0xFFFF6B6B).copy(alpha = 0.04f)
                    )
                )
            )
            .border(
                1.dp,
                Color(0xFFFF6B6B).copy(alpha = 0.2f),
                RoundedCornerShape(14.dp)
            )
            .padding(16.dp),
        verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Box(
            modifier = Modifier
                .size(38.dp)
                .clip(CircleShape)
                .background(Color(0xFFFF6B6B).copy(alpha = 0.15f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Block,
                contentDescription = null,
                tint = Color(0xFFFF6B6B),
                modifier = Modifier.size(20.dp)
            )
        }
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = "⚠️ تنبيه: لا يمكن نقل الملكية",
                style = MaterialTheme.typography.labelLarge,
                color = Color(0xFFFF6B6B),
                fontWeight = FontWeight.Bold
            )
            Spacer(Modifier.height(4.dp))
            Text(
                text = "يوجد $unpaidCount مخالفة غير مدفوعة. يجب سداد جميع المخالفات قبل إتمام عملية نقل الملكية.",
                style = MaterialTheme.typography.bodySmall,
                color = appColors.textSecondary,
                lineHeight = 18.sp
            )
        }
    }
}

@Composable
private fun NoViolationsView() {
    val appColors = LocalAppColors.current
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 60.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Box(
            modifier = Modifier
                .size(88.dp)
                .clip(CircleShape)
                .background(
                    Brush.linearGradient(
                        listOf(
                            Color(0xFF4ADE80).copy(alpha = 0.15f),
                            Color(0xFF22C55E).copy(alpha = 0.08f)
                        )
                    )
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Verified,
                contentDescription = null,
                tint = Color(0xFF22C55E),
                modifier = Modifier.size(44.dp)
            )
        }
        Text(
            text = "لا توجد مخالفات! 🎉",
            style = MaterialTheme.typography.titleMedium,
            color = appColors.textPrimary,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "هذه المركبة ليس عليها أي مخالفات مرورية\nيمكنك إتمام عملية نقل الملكية بأمان",
            style = MaterialTheme.typography.bodySmall,
            color = appColors.textSecondary,
            textAlign = TextAlign.Center,
            lineHeight = 20.sp
        )

        // Success badge
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(20.dp))
                .background(Color(0xFF22C55E).copy(alpha = 0.1f))
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = null,
                    tint = Color(0xFF22C55E),
                    modifier = Modifier.size(16.dp)
                )
                Text(
                    text = "جاهزة لنقل الملكية",
                    style = MaterialTheme.typography.labelMedium,
                    color = Color(0xFF22C55E),
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}
