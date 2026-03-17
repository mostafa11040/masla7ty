package com.example.maslahty.presentation.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.material3.Badge
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.masla7ty.ui.theme.LocalAppColors

// ─────────────────────────────────────────────────────────────────
// PRIMARY BUTTON
// ─────────────────────────────────────────────────────────────────
@Composable
fun PrimaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    isLoading: Boolean = false,
    icon: ImageVector? = null
) {
    val appColors = LocalAppColors.current
    Button(
        onClick = onClick,
        enabled = enabled && !isLoading,
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp),
        shape = RoundedCornerShape(14.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = appColors.gold,
            contentColor = Color(0xFF0D1B3E),
            disabledContainerColor = appColors.gold.copy(alpha = 0.4f),
            disabledContentColor = Color(0xFF0D1B3E).copy(alpha = 0.4f)
        ),
        elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp)
    ) {
        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.size(22.dp),
                color = Color(0xFF0D1B3E),
                strokeWidth = 2.5.dp
            )
        } else {
            if (icon != null) {
                Icon(imageVector = icon, contentDescription = null, modifier = Modifier.size(20.dp))
                Spacer(Modifier.width(8.dp))
            }
            Text(
                text = text,
                style = MaterialTheme.typography.labelLarge.copy(fontSize = 15.sp),
                fontWeight = FontWeight.Bold
            )
        }
    }
}

// ─────────────────────────────────────────────────────────────────
// SECONDARY BUTTON
// ─────────────────────────────────────────────────────────────────
@Composable
fun SecondaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    icon: ImageVector? = null
) {
    val appColors = LocalAppColors.current
    OutlinedButton(
        onClick = onClick,
        enabled = enabled,
        modifier = modifier
            .fillMaxWidth()
            .height(52.dp),
        shape = RoundedCornerShape(14.dp),
        border = androidx.compose.foundation.BorderStroke(1.5.dp, appColors.gold.copy(alpha = 0.6f)),
        colors = ButtonDefaults.outlinedButtonColors(
            contentColor = appColors.gold,
            disabledContentColor = appColors.gold.copy(alpha = 0.4f)
        )
    ) {
        if (icon != null) {
            Icon(imageVector = icon, contentDescription = null, modifier = Modifier.size(18.dp))
            Spacer(Modifier.width(8.dp))
        }
        Text(
            text = text,
            style = MaterialTheme.typography.labelLarge.copy(fontSize = 14.sp),
            fontWeight = FontWeight.SemiBold
        )
    }
}

// ─────────────────────────────────────────────────────────────────
// PROFESSIONAL TEXT FIELD
// ─────────────────────────────────────────────────────────────────
@Composable
fun AppTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    placeholder: String = "",
    leadingIcon: ImageVector? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    keyboardType: KeyboardType = KeyboardType.Text,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    singleLine: Boolean = true,
    maxLines: Int = 1,
    enabled: Boolean = true,
    isError: Boolean = false,
    readOnly: Boolean = false
) {
    val appColors = LocalAppColors.current
    val isFocused = remember { mutableStateOf(false) }

    Column(modifier = modifier) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            color = appColors.textSecondary,
            modifier = Modifier.padding(bottom = 6.dp, start = 2.dp)
        )
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            placeholder = {
                if (placeholder.isNotEmpty())
                    Text(placeholder, color = appColors.textTertiary, fontSize = 14.sp)
            },
            leadingIcon = leadingIcon?.let {
                {
                    Icon(
                        imageVector = it,
                        contentDescription = null,
                        tint = if (isError) MaterialTheme.colorScheme.error
                        else appColors.gold.copy(alpha = 0.8f),
                        modifier = Modifier.size(20.dp)
                    )
                }
            },
            trailingIcon = trailingIcon,
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
            visualTransformation = visualTransformation,
            singleLine = singleLine,
            maxLines = maxLines,
            enabled = enabled,
            readOnly = readOnly,
            isError = isError,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = appColors.gold,
                unfocusedBorderColor = appColors.cardBorder,
                errorBorderColor = MaterialTheme.colorScheme.error,
                focusedContainerColor = appColors.cardBackground,
                unfocusedContainerColor = appColors.cardBackground,
                disabledContainerColor = appColors.cardBackground.copy(alpha = 0.6f),
                focusedTextColor = appColors.textPrimary,
                unfocusedTextColor = appColors.textPrimary,
                cursorColor = appColors.gold
            )
        )
    }
}

// ─────────────────────────────────────────────────────────────────
// OTP INPUT
// ─────────────────────────────────────────────────────────────────
@Composable
fun OTPInput(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    label: String = "أدخل رمز التحقق"
) {
    AppTextField(
        value = value,
        onValueChange = { if (it.length <= 6 && it.all { c -> c.isDigit() }) onValueChange(it) },
        label = label,
        keyboardType = KeyboardType.Number,
        leadingIcon = Icons.Default.Lock,
        modifier = modifier
    )
}

// ─────────────────────────────────────────────────────────────────
// NATIONAL ID INPUT
// ─────────────────────────────────────────────────────────────────
@Composable
fun NationalIdInput(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    label: String = "الرقم القومي"
) {
    AppTextField(
        value = value,
        onValueChange = { if (it.length <= 14 && it.all { c -> c.isDigit() }) onValueChange(it) },
        label = label,
        keyboardType = KeyboardType.Number,
        leadingIcon = Icons.Default.Badge,
        placeholder = "أدخل الرقم القومي (14 رقم)",
        modifier = modifier
    )
}

// ─────────────────────────────────────────────────────────────────
// PHONE INPUT
// ─────────────────────────────────────────────────────────────────
@Composable
fun PhoneInput(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    label: String = "رقم الهاتف"
) {
    AppTextField(
        value = value,
        onValueChange = { if (it.length <= 15 && it.all { c -> c.isDigit() }) onValueChange(it) },
        label = label,
        keyboardType = KeyboardType.Phone,
        leadingIcon = Icons.Default.Phone,
        placeholder = "01xxxxxxxxx",
        modifier = modifier
    )
}

// ─────────────────────────────────────────────────────────────────
// ERROR MESSAGE
// ─────────────────────────────────────────────────────────────────
@Composable
fun ErrorMessage(
    message: String,
    modifier: Modifier = Modifier
) {
    AnimatedVisibility(
        visible = message.isNotEmpty(),
        enter = fadeIn() + slideInVertically(),
        exit = fadeOut()
    ) {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(10.dp))
                .background(MaterialTheme.colorScheme.errorContainer)
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Warning,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.error,
                modifier = Modifier.size(18.dp)
            )
            Text(
                text = message,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

// ─────────────────────────────────────────────────────────────────
// LOADING BOX
// ─────────────────────────────────────────────────────────────────
@Composable
fun LoadingBox(
    modifier: Modifier = Modifier,
    message: String = "جاري التحميل..."
) {
    val appColors = LocalAppColors.current
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(appColors.cardBackground)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        CircularProgressIndicator(color = appColors.gold, strokeWidth = 3.dp)
        Spacer(Modifier.height(12.dp))
        Text(
            text = message,
            style = MaterialTheme.typography.bodyMedium,
            color = appColors.textSecondary,
            textAlign = TextAlign.Center
        )
    }
}

// ─────────────────────────────────────────────────────────────────
// PRICE WARNING BOX
// ─────────────────────────────────────────────────────────────────
@Composable
fun PriceWarningBox(
    message: String,
    percentage: Double,
    modifier: Modifier = Modifier
) {
    val appColors = LocalAppColors.current
    val isHighRisk = percentage > 30
    val color = if (isHighRisk) MaterialTheme.colorScheme.error else appColors.warning

    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(color.copy(alpha = if (appColors.isDark) 0.15f else 0.1f))
            .border(1.dp, color.copy(alpha = 0.3f), RoundedCornerShape(10.dp))
            .padding(12.dp),
        verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Icon(
            imageVector = if (isHighRisk) Icons.Default.Error else Icons.Default.Warning,
            contentDescription = null,
            tint = color,
            modifier = Modifier.size(20.dp).padding(top = 2.dp)
        )
        Column {
            Text(
                text = if (isHighRisk) "⚠️ سعر مرتفع جداً" else "تنبيه: فارق في السعر",
                style = MaterialTheme.typography.labelMedium,
                color = color,
                fontWeight = FontWeight.Bold
            )
            Spacer(Modifier.height(2.dp))
            Text(
                text = message,
                style = MaterialTheme.typography.bodySmall,
                color = color.copy(alpha = 0.85f)
            )
        }
    }
}

// ─────────────────────────────────────────────────────────────────
// STATUS BADGE
// ─────────────────────────────────────────────────────────────────
@Composable
fun StatusBadge(
    status: String,
    modifier: Modifier = Modifier
) {
    val appColors = LocalAppColors.current
    val (bgColor, textColor, label, icon) = when (status.uppercase()) {
        "PENDING" -> Quad(
            appColors.statusPendingBg, appColors.statusPending,
            "قيد الانتظار", Icons.Default.Schedule
        )
        "APPROVED_BY_BUYER" -> Quad(
            appColors.statusApprovedBg, appColors.statusApproved,
            "موافق المشتري", Icons.Default.CheckCircle
        )
        "COMPLETED" -> Quad(
            appColors.statusCompletedBg, appColors.statusCompleted,
            "مكتمل", Icons.Default.Verified
        )
        "REJECTED_BY_BUYER" -> Quad(
            appColors.statusRejectedBg, appColors.statusRejected,
            "مرفوض", Icons.Default.Cancel
        )
        else -> Quad(
            appColors.cardBackground, appColors.textSecondary,
            status, Icons.Default.Info
        )
    }

    Row(
        modifier = modifier
            .clip(RoundedCornerShape(20.dp))
            .background(bgColor)
            .padding(horizontal = 10.dp, vertical = 5.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = textColor,
            modifier = Modifier.size(14.dp)
        )
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = textColor,
            fontWeight = FontWeight.SemiBold
        )
    }
}

private data class Quad<A, B, C, D>(val first: A, val second: B, val third: C, val fourth: D)

// ─────────────────────────────────────────────────────────────────
// INFO CARD ROW
// ─────────────────────────────────────────────────────────────────
@Composable
fun InfoRow(
    label: String,
    value: String,
    icon: ImageVector? = null,
    modifier: Modifier = Modifier
) {
    val appColors = LocalAppColors.current
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (icon != null) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = appColors.gold,
                modifier = Modifier.size(18.dp)
            )
            Spacer(Modifier.width(10.dp))
        }
        Column(modifier = Modifier.weight(1f)) {
            Text(text = label, style = MaterialTheme.typography.labelSmall, color = appColors.textTertiary)
            Text(
                text = value.ifBlank { "—" },
                style = MaterialTheme.typography.bodyMedium,
                color = appColors.textPrimary,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

// ─────────────────────────────────────────────────────────────────
// SECTION HEADER
// ─────────────────────────────────────────────────────────────────
@Composable
fun SectionHeader(
    title: String,
    modifier: Modifier = Modifier,
    icon: ImageVector? = null
) {
    val appColors = LocalAppColors.current
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Box(
            modifier = Modifier
                .width(4.dp)
                .height(22.dp)
                .clip(RoundedCornerShape(2.dp))
                .background(appColors.gold)
        )
        if (icon != null) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = appColors.textPrimary,
                modifier = Modifier.size(18.dp)
            )
        }
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            color = appColors.textPrimary,
            fontWeight = FontWeight.Bold
        )
    }
}

// ─────────────────────────────────────────────────────────────────
// APP CARD
// ─────────────────────────────────────────────────────────────────
@Composable
fun AppCard(
    modifier: Modifier = Modifier,
    elevation: Dp = 0.dp,
    content: @Composable ColumnScope.() -> Unit
) {
    val appColors = LocalAppColors.current
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = appColors.cardBackground),
        border = androidx.compose.foundation.BorderStroke(1.dp, appColors.cardBorder),
        elevation = CardDefaults.cardElevation(defaultElevation = elevation)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            content = content
        )
    }
}

// ─────────────────────────────────────────────────────────────────
// GRADIENT HEADER
// ─────────────────────────────────────────────────────────────────
@Composable
fun GradientHeader(
    title: String,
    subtitle: String = "",
    onBack: (() -> Unit)? = null,
    icon: ImageVector? = null
) {
    val appColors = LocalAppColors.current
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                Brush.linearGradient(
                    colors = listOf(appColors.navy, appColors.gradientEnd.copy(alpha = 0.9f))
                )
            )
            .padding(horizontal = 20.dp, vertical = 24.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            if (onBack != null) {
                IconButton(
                    onClick = onBack,
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(Color.White.copy(alpha = 0.12f))
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowForward,
                        contentDescription = "رجوع",
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
            if (icon != null) {
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(appColors.gold.copy(alpha = 0.2f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(imageVector = icon, contentDescription = null, tint = appColors.gold, modifier = Modifier.size(24.dp))
                }
            }
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleLarge,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
                if (subtitle.isNotEmpty()) {
                    Text(
                        text = subtitle,
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.White.copy(alpha = 0.7f)
                    )
                }
            }
        }
    }
}

// ─────────────────────────────────────────────────────────────────
// STEP INDICATOR
// ─────────────────────────────────────────────────────────────────
@Composable
fun StepIndicator(
    currentStep: Int,
    totalSteps: Int,
    stepLabels: List<String>,
    modifier: Modifier = Modifier
) {
    val appColors = LocalAppColors.current
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.Top
    ) {
        stepLabels.forEachIndexed { index, label ->
            val step = index + 1
            val isDone = step < currentStep
            val isActive = step == currentStep
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.weight(1f)
            ) {
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape)
                        .background(
                            when {
                                isDone -> appColors.success
                                isActive -> appColors.gold
                                else -> appColors.cardBorder
                            }
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    if (isDone) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(16.dp)
                        )
                    } else {
                        Text(
                            text = step.toString(),
                            style = MaterialTheme.typography.labelSmall,
                            color = if (isActive) Color(0xFF0D1B3E) else appColors.textTertiary,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
                Spacer(Modifier.height(4.dp))
                Text(
                    text = label,
                    style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                    color = when {
                        isDone -> appColors.success
                        isActive -> appColors.gold
                        else -> appColors.textTertiary
                    },
                    textAlign = TextAlign.Center,
                    fontWeight = if (isActive) FontWeight.Bold else FontWeight.Normal
                )
            }
            if (index < stepLabels.size - 1) {
                Box(
                    modifier = Modifier
                        .weight(0.5f)
                        .height(2.dp)
                        .padding(top = 15.dp)
                        .background(
                            if (step < currentStep) appColors.success
                            else appColors.cardBorder
                        )
                )
            }
        }
    }
}

// ─────────────────────────────────────────────────────────────────
// IMAGE UPLOAD SLOT
// ─────────────────────────────────────────────────────────────────
@Composable
fun ImageUploadSlot(
    label: String,
    icon: ImageVector,
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val appColors = LocalAppColors.current
    val hasValue = value.isNotBlank()
    AppCard(modifier = modifier) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(52.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(
                        if (hasValue) appColors.success.copy(alpha = 0.15f)
                        else appColors.gold.copy(alpha = 0.1f)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = if (hasValue) Icons.Default.CheckCircle else icon,
                    contentDescription = null,
                    tint = if (hasValue) appColors.success else appColors.gold,
                    modifier = Modifier.size(28.dp)
                )
            }
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = label,
                    style = MaterialTheme.typography.labelMedium,
                    color = appColors.textSecondary,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(Modifier.height(4.dp))
                OutlinedTextField(
                    value = value,
                    onValueChange = onValueChange,
                    placeholder = { Text("أدخل رابط الصورة أو URL", fontSize = 12.sp) },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp),
                    textStyle = MaterialTheme.typography.bodySmall,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = appColors.gold,
                        unfocusedBorderColor = appColors.cardBorder,
                        focusedContainerColor = appColors.cardBackground,
                        unfocusedContainerColor = appColors.cardBackground,
                        focusedTextColor = appColors.textPrimary,
                        unfocusedTextColor = appColors.textPrimary
                    )
                )
            }
        }
    }
}
