package com.example.maslahty.presentation.screens.violations

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import com.example.maslahty.domain.entities.Vehicle
import com.example.maslahty.presentation.components.GradientHeader
import com.example.maslahty.presentation.components.LoadingBox
import com.example.maslahty.presentation.components.ErrorMessage
import com.example.maslahty.presentation.navigation.Screen
import com.example.maslahty.presentation.viewmodels.ViolationsUiState
import com.example.maslahty.presentation.viewmodels.ViolationsViewModel
import kotlinx.coroutines.delay

@Composable
fun ViolationsMenuScreen(
    navController: NavHostController,
    viewModel: ViolationsViewModel = hiltViewModel()
) {
    val appColors = LocalAppColors.current
    val vehiclesState by viewModel.vehiclesState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadUserVehicles()
    }

    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {
            // ── GRADIENT HEADER ────────────────────────────
            GradientHeader(
                title = "الاستعلام عن المخالفات",
                subtitle = "اختر المركبة للاطلاع على المخالفات",
                onBack = { navController.popBackStack() },
                icon = Icons.Default.GppBad
            )

            // ── CONTENT ────────────────────────────────────
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 20.dp, vertical = 24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Info banner
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(14.dp))
                        .background(
                            Brush.linearGradient(
                                listOf(
                                    appColors.gold.copy(alpha = 0.12f),
                                    appColors.gold.copy(alpha = 0.04f)
                                )
                            )
                        )
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(42.dp)
                            .clip(CircleShape)
                            .background(appColors.gold.copy(alpha = 0.15f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Info,
                            contentDescription = null,
                            tint = appColors.gold,
                            modifier = Modifier.size(22.dp)
                        )
                    }
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "اختر مركبتك",
                            style = MaterialTheme.typography.labelLarge,
                            color = appColors.textPrimary,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "اضغط على أي مركبة لعرض المخالفات المسجلة عليها",
                            style = MaterialTheme.typography.bodySmall,
                            color = appColors.textSecondary
                        )
                    }
                }

                // Section title
                Text(
                    text = "المركبات المسجلة",
                    style = MaterialTheme.typography.titleMedium,
                    color = appColors.textPrimary,
                    fontWeight = FontWeight.Bold
                )

                when (val state = vehiclesState) {
                    is ViolationsUiState.Loading -> {
                        LoadingBox(message = "جاري تحميل المركبات...")
                    }
                    is ViolationsUiState.Error -> {
                        ErrorMessage(message = state.message)
                    }
                    is ViolationsUiState.VehiclesLoaded -> {
                        if (state.vehicles.isEmpty()) {
                            EmptyVehiclesView()
                        } else {
                            state.vehicles.forEachIndexed { index, vehicle ->
                                var visible by remember { mutableStateOf(false) }
                                LaunchedEffect(Unit) {
                                    delay(index * 100L)
                                    visible = true
                                }
                                AnimatedVisibility(
                                    visible = visible,
                                    enter = fadeIn() + slideInVertically(
                                        animationSpec = spring(
                                            dampingRatio = Spring.DampingRatioMediumBouncy,
                                            stiffness = Spring.StiffnessMediumLow
                                        ),
                                        initialOffsetY = { it / 2 }
                                    )
                                ) {
                                    VehicleCard(
                                        vehicle = vehicle,
                                        onClick = {
                                            navController.navigate(
                                                Screen.VehicleViolationsScreen.createRoute(vehicle.id)
                                            )
                                        }
                                    )
                                }
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
private fun VehicleCard(
    vehicle: Vehicle,
    onClick: () -> Unit
) {
    val appColors = LocalAppColors.current

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = appColors.cardBackground),
        border = androidx.compose.foundation.BorderStroke(1.dp, appColors.cardBorder),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Car icon
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(
                        Brush.linearGradient(
                            listOf(appColors.navy, appColors.gradientEnd)
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.DirectionsCar,
                    contentDescription = null,
                    tint = appColors.gold,
                    modifier = Modifier.size(28.dp)
                )
            }

            // Vehicle info
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = vehicle.model,
                    style = MaterialTheme.typography.titleSmall,
                    color = appColors.textPrimary,
                    fontWeight = FontWeight.Bold
                )
                Row(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Pin,
                        contentDescription = null,
                        tint = appColors.gold.copy(alpha = 0.7f),
                        modifier = Modifier.size(14.dp)
                    )
                    Text(
                        text = vehicle.licensePlate,
                        style = MaterialTheme.typography.bodySmall,
                        color = appColors.textSecondary,
                        fontWeight = FontWeight.Medium
                    )
                }
                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    DetailChip(
                        icon = Icons.Default.CalendarMonth,
                        text = vehicle.manufacturingYear.toString()
                    )
                    DetailChip(
                        icon = Icons.Default.Palette,
                        text = vehicle.color
                    )
                }
            }

            // Arrow
            Icon(
                imageVector = Icons.Default.ChevronLeft,
                contentDescription = "عرض المخالفات",
                tint = appColors.textTertiary,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}

@Composable
private fun DetailChip(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    text: String
) {
    val appColors = LocalAppColors.current
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = appColors.textTertiary,
            modifier = Modifier.size(12.dp)
        )
        Text(
            text = text,
            style = MaterialTheme.typography.labelSmall,
            color = appColors.textTertiary,
            fontSize = 11.sp
        )
    }
}

@Composable
private fun EmptyVehiclesView() {
    val appColors = LocalAppColors.current
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 40.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Box(
            modifier = Modifier
                .size(72.dp)
                .clip(CircleShape)
                .background(appColors.gold.copy(alpha = 0.1f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.DirectionsCar,
                contentDescription = null,
                tint = appColors.gold,
                modifier = Modifier.size(36.dp)
            )
        }
        Text(
            text = "لا توجد مركبات مسجلة",
            style = MaterialTheme.typography.titleSmall,
            color = appColors.textPrimary,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "قم بتسجيل مركبة أولاً للاستعلام عن المخالفات",
            style = MaterialTheme.typography.bodySmall,
            color = appColors.textSecondary,
            textAlign = TextAlign.Center
        )
    }
}
