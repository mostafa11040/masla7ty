package com.example.maslahty.presentation.screens.transfer

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
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
import com.example.maslahty.domain.entities.TransferRequest
import com.example.maslahty.domain.entities.TransferStatus
import com.example.maslahty.domain.entities.Vehicle
import com.example.maslahty.domain.utils.PriceValidationUtil
import com.example.maslahty.domain.utils.ValidationUtil
import com.example.maslahty.presentation.components.*
import com.example.maslahty.presentation.navigation.Screen
import com.example.maslahty.presentation.viewmodels.TransferRequestViewModel
import com.example.maslahty.presentation.viewmodels.TransferState
import com.example.maslahty.presentation.viewmodels.VehicleState
import com.example.maslahty.presentation.viewmodels.VehicleViewModel
import java.util.Date
import java.util.UUID

private const val CURRENT_SELLER_ID = "user1"
private const val CURRENT_SELLER_NAME = "أحمد محمد"
private const val CURRENT_SELLER_NATIONAL_ID = "1234567890"

private data class TransferDraft(
    val vehicle: Vehicle,
    val salePrice: Double? = null,
    val marketPrice: Double? = null,
    val notes: String = "",
    val buyerNationalId: String = ""
)

private object TransferDraftStore {
    val drafts = mutableStateMapOf<String, TransferDraft>()
    var lastLoadedRequests: List<TransferRequest> = emptyList()
}

// ══════════════════════════════════════════════════════════════════
// 1️⃣  VEHICLE DETAILS SCREEN
// ══════════════════════════════════════════════════════════════════
@Composable
fun VehicleDetailsScreen(
    navController: NavHostController,
    viewModel: VehicleViewModel = hiltViewModel()
) {
    val appColors = LocalAppColors.current
    var licensePlate by remember { mutableStateOf("") }
    var chassisNumber by remember { mutableStateOf("") }
    var engineNumber by remember { mutableStateOf("") }
    var currentOwnerNationalId by remember { mutableStateOf(CURRENT_SELLER_NATIONAL_ID) }
    var newOwnerNationalId by remember { mutableStateOf("") }
    var error by remember { mutableStateOf<String?>(null) }
    var ownershipVerified by remember { mutableStateOf(false) }
    var continueAfterVerify by remember { mutableStateOf(false) }
    var verifiedVehicle by remember { mutableStateOf<Vehicle?>(null) }
    val vehicleLookupState by viewModel.getVehicleState.collectAsState()

    fun proceedToImageStep(vehicle: Vehicle) {
        val newOwnerId = newOwnerNationalId.trim()
        if (!ValidationUtil.isValidNationalId(newOwnerId)) {
            error = "رقم الرقم القومي للمالك الجديد غير صحيح"
            return
        }
        val vehicleId = "vehicle_${UUID.randomUUID()}"
        val normalizedPlate = licensePlate.replace("-", "").replace(" ", "").uppercase()
        TransferDraftStore.drafts[vehicleId] = TransferDraft(
            vehicle = vehicle.copy(id = vehicleId, licensePlate = normalizedPlate, updatedAt = Date()),
            buyerNationalId = newOwnerId
        )
        navController.navigate(Screen.ImageUploadScreen.createRoute(vehicleId))
    }

    LaunchedEffect(vehicleLookupState) {
        when (val s = vehicleLookupState) {
            is VehicleState.VehicleLoaded -> {
                if (s.vehicle.ownerId != CURRENT_SELLER_ID) {
                    ownershipVerified = false; verifiedVehicle = null
                    error = "المركبة غير مسجلة باسمك"
                } else {
                    ownershipVerified = true; verifiedVehicle = s.vehicle; error = null
                    licensePlate = s.vehicle.licensePlate
                    if (continueAfterVerify) { continueAfterVerify = false; proceedToImageStep(s.vehicle) }
                }
            }
            is VehicleState.Error -> { ownershipVerified = false; verifiedVehicle = null; continueAfterVerify = false; error = s.message }
            else -> Unit
        }
    }

    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
        Column(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)) {

            GradientHeader(
                title = "بيع مركبة",
                subtitle = "الخطوة 1 من 4 — تحقق الملكية",
                onBack = { navController.popBackStack() },
                icon = Icons.Default.DirectionsCar
            )

            // Step progress
            Box(
                modifier = Modifier.fillMaxWidth()
                    .background(MaterialTheme.colorScheme.background)
                    .padding(horizontal = 20.dp, vertical = 16.dp)
            ) {
                StepIndicator(
                    currentStep = 1,
                    totalSteps = 4,
                    stepLabels = listOf("المركبة", "الصور", "السعر", "الإرسال")
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 20.dp),
                verticalArrangement = Arrangement.spacedBy(18.dp)
            ) {

                // ─ Owner section ─
                SectionHeader(title = "بيانات المالك", icon = Icons.Default.Person)

                AppCard {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Box(
                            modifier = Modifier.size(48.dp).clip(CircleShape)
                                .background(appColors.gold.copy(alpha = 0.15f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.Person, null, tint = appColors.gold, modifier = Modifier.size(26.dp))
                        }
                        Column {
                            Text(CURRENT_SELLER_NAME, style = MaterialTheme.typography.titleSmall, color = appColors.textPrimary, fontWeight = FontWeight.Bold)
                            Text(CURRENT_SELLER_NATIONAL_ID, style = MaterialTheme.typography.bodySmall, color = appColors.textSecondary)
                        }
                        Spacer(Modifier.weight(1f))
                        Box(
                            modifier = Modifier.clip(RoundedCornerShape(20.dp))
                                .background(appColors.statusApprovedBg).padding(horizontal = 10.dp, vertical = 4.dp)
                        ) {
                            Text("مُحقَّق", style = MaterialTheme.typography.labelSmall, color = appColors.statusApproved, fontWeight = FontWeight.Bold)
                        }
                    }
                }

                // ─ Vehicle Identification ─
                SectionHeader(title = "تعريف المركبة", icon = Icons.Default.Search)

                AppTextField(
                    value = licensePlate,
                    onValueChange = { licensePlate = it.uppercase() },
                    label = "رقم اللوحة",
                    leadingIcon = Icons.Default.CreditCard,
                    placeholder = "مثال: أ ب ج 1234"
                )

                AppTextField(
                    value = chassisNumber,
                    onValueChange = { chassisNumber = it.uppercase() },
                    label = "رقم الشاسيه",
                    leadingIcon = Icons.Default.QrCode,
                    placeholder = "رقم الشاسيه VIN"
                )

                AppTextField(
                    value = engineNumber,
                    onValueChange = { engineNumber = it.uppercase() },
                    label = "رقم الموتور",
                    leadingIcon = Icons.Default.Settings,
                    placeholder = "رقم الموتور"
                )

                // ─ Buyer section ─
                SectionHeader(title = "بيانات المشتري", icon = Icons.Default.PersonSearch)

                AppTextField(
                    value = newOwnerNationalId,
                    onValueChange = { if (it.length <= 14 && it.all(Char::isDigit)) newOwnerNationalId = it },
                    label = "الرقم القومي للمشتري",
                    leadingIcon = Icons.Default.Badge,
                    placeholder = "14 رقم",
                    keyboardType = KeyboardType.Number
                )

                // Ownership verified chip
                if (ownershipVerified) {
                    Row(
                        modifier = Modifier.fillMaxWidth()
                            .clip(RoundedCornerShape(10.dp))
                            .background(appColors.statusApprovedBg)
                            .padding(12.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.CheckCircle, null, tint = appColors.statusApproved, modifier = Modifier.size(20.dp))
                        Text("تم التحقق من ملكية المركبة بنجاح", style = MaterialTheme.typography.bodySmall, color = appColors.statusApproved, fontWeight = FontWeight.Medium)
                    }
                }

                error?.let { ErrorMessage(it) }

                if (vehicleLookupState is VehicleState.Loading) {
                    LoadingBox(message = "جاري التحقق من بيانات المركبة...")
                }

                PrimaryButton(
                    text = "التالي — رفع الصور",
                    icon = Icons.Default.ArrowBack,
                    onClick = {
                        error = null
                        val plate = licensePlate.replace("-", "").replace(" ", "").uppercase()
                        when {
                            currentOwnerNationalId != CURRENT_SELLER_NATIONAL_ID -> error = "رقم المالك الحالي غير مطابق"
                            licensePlate.isBlank() -> error = "أدخل رقم اللوحة"
                            !ValidationUtil.isValidNationalId(newOwnerNationalId) -> error = "الرقم القومي للمشتري غير صحيح"
                            ownershipVerified && verifiedVehicle != null -> proceedToImageStep(verifiedVehicle!!)
                            else -> { continueAfterVerify = true; viewModel.getVehicleByPlate(plate) }
                        }
                    }
                )

                Spacer(Modifier.height(16.dp))
            }
        }
    }
}

// ══════════════════════════════════════════════════════════════════
// 2️⃣  IMAGE UPLOAD SCREEN
// ══════════════════════════════════════════════════════════════════
@Composable
fun ImageUploadScreen(navController: NavHostController, vehicleId: String) {
    val appColors = LocalAppColors.current
    val draft = TransferDraftStore.drafts[vehicleId]
    var licenseImageUrl by remember { mutableStateOf(draft?.vehicle?.licenseImageUrl.orEmpty()) }
    var vehicleImageUrl by remember { mutableStateOf(draft?.vehicle?.vehicleImageUrl.orEmpty()) }
    var chassisImageUrl by remember { mutableStateOf(draft?.vehicle?.chassisImageUrl.orEmpty()) }
    var engineImageUrl by remember { mutableStateOf(draft?.vehicle?.engineImageUrl.orEmpty()) }
    var error by remember { mutableStateOf<String?>(null) }

    val uploadedCount = listOf(licenseImageUrl, vehicleImageUrl, chassisImageUrl, engineImageUrl).count { it.isNotBlank() }

    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
        Column(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)) {

            GradientHeader(
                title = "رفع الصور",
                subtitle = "الخطوة 2 من 4 — وثائق المركبة",
                onBack = { navController.popBackStack() },
                icon = Icons.Default.CameraAlt
            )

            Box(modifier = Modifier.fillMaxWidth().background(MaterialTheme.colorScheme.background).padding(horizontal = 20.dp, vertical = 16.dp)) {
                StepIndicator(currentStep = 2, totalSteps = 4, stepLabels = listOf("المركبة", "الصور", "السعر", "الإرسال"))
            }

            Column(
                modifier = Modifier.fillMaxWidth().weight(1f)
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 20.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Progress
                AppCard {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("الصور المرفوعة", style = MaterialTheme.typography.titleSmall, color = appColors.textPrimary, fontWeight = FontWeight.Bold)
                        Text(
                            "$uploadedCount / 4",
                            style = MaterialTheme.typography.titleMedium,
                            color = if (uploadedCount == 4) appColors.success else appColors.gold,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Spacer(Modifier.height(8.dp))
                    LinearProgressIndicator(
                        progress = { uploadedCount / 4f },
                        modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(4.dp)),
                        color = if (uploadedCount == 4) appColors.success else appColors.gold,
                        trackColor = appColors.cardBorder
                    )
                }

                SectionHeader(title = "الوثائق المطلوبة", icon = Icons.Default.Photo)

                // Info note
                Row(
                    modifier = Modifier.fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp))
                        .background(appColors.gold.copy(alpha = 0.08f))
                        .padding(12.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.Info, null, tint = appColors.gold, modifier = Modifier.size(18.dp))
                    Text(
                        "أدخل روابط الصور (URL) أو سيتم ربط الكاميرا في الإصدار القادم",
                        style = MaterialTheme.typography.bodySmall,
                        color = appColors.textSecondary
                    )
                }

                ImageUploadSlot(
                    label = "رخصة السيارة (إلزامي)",
                    icon = Icons.Default.Article,
                    value = licenseImageUrl,
                    onValueChange = { licenseImageUrl = it }
                )
                ImageUploadSlot(
                    label = "صورة السيارة الخارجية (إلزامي)",
                    icon = Icons.Default.DirectionsCar,
                    value = vehicleImageUrl,
                    onValueChange = { vehicleImageUrl = it }
                )
                ImageUploadSlot(
                    label = "صورة رقم الشاسيه (إلزامي)",
                    icon = Icons.Default.QrCode,
                    value = chassisImageUrl,
                    onValueChange = { chassisImageUrl = it }
                )
                ImageUploadSlot(
                    label = "صورة رقم الموتور (إلزامي)",
                    icon = Icons.Default.Settings,
                    value = engineImageUrl,
                    onValueChange = { engineImageUrl = it }
                )

                error?.let { ErrorMessage(it) }

                PrimaryButton(
                    text = "التالي — تحديد السعر",
                    icon = Icons.Default.ArrowBack,
                    enabled = uploadedCount == 4,
                    onClick = {
                        when {
                            uploadedCount < 4 -> error = "كل الصور الأربع إلزامية قبل المتابعة"
                            draft == null -> error = "تعذر تحميل بيانات المركبة"
                            else -> {
                                TransferDraftStore.drafts[vehicleId] = draft.copy(
                                    vehicle = draft.vehicle.copy(
                                        licenseImageUrl = licenseImageUrl,
                                        vehicleImageUrl = vehicleImageUrl,
                                        chassisImageUrl = chassisImageUrl,
                                        engineImageUrl = engineImageUrl,
                                        updatedAt = Date()
                                    )
                                )
                                navController.navigate(Screen.PricingScreen.createRoute(vehicleId))
                            }
                        }
                    }
                )
                Spacer(Modifier.height(16.dp))
            }
        }
    }
}

// ══════════════════════════════════════════════════════════════════
// 3️⃣  PRICING SCREEN
// ══════════════════════════════════════════════════════════════════
@Composable
fun PricingScreen(navController: NavHostController, vehicleId: String) {
    val appColors = LocalAppColors.current
    val draft = TransferDraftStore.drafts[vehicleId]
    var salePriceInput by remember { mutableStateOf(draft?.salePrice?.toString().orEmpty()) }
    var error by remember { mutableStateOf<String?>(null) }

    val marketPrice = draft?.vehicle?.let {
        PriceValidationUtil.getMarketPriceEstimate(
            model = it.model, year = it.manufacturingYear,
            kilometers = it.kilometers, condition = it.condition.name
        )
    } ?: 0.0

    val askedPrice = salePriceInput.toDoubleOrNull() ?: 0.0
    val warning = if (askedPrice > 0 && marketPrice > 0) PriceValidationUtil.validatePrice(askedPrice, marketPrice) else null

    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
        Column(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)) {

            GradientHeader(
                title = "تحديد السعر",
                subtitle = "الخطوة 3 من 4 — سعر البيع",
                onBack = { navController.popBackStack() },
                icon = Icons.Default.AttachMoney
            )

            Box(modifier = Modifier.fillMaxWidth().background(MaterialTheme.colorScheme.background).padding(horizontal = 20.dp, vertical = 16.dp)) {
                StepIndicator(currentStep = 3, totalSteps = 4, stepLabels = listOf("المركبة", "الصور", "السعر", "الإرسال"))
            }

            Column(
                modifier = Modifier.fillMaxWidth().weight(1f)
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 20.dp),
                verticalArrangement = Arrangement.spacedBy(18.dp)
            ) {
                // Market price card
                AppCard {
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                        Column {
                            Text("متوسط سعر السوق", style = MaterialTheme.typography.labelMedium, color = appColors.textSecondary)
                            Text(
                                "EGP ${"%.0f".format(marketPrice)}",
                                style = MaterialTheme.typography.headlineSmall,
                                color = appColors.textPrimary,
                                fontWeight = FontWeight.Bold
                            )
                            Text("بناءً على الموديل والحالة والكيلومترات", style = MaterialTheme.typography.bodySmall, color = appColors.textTertiary)
                        }
                        Box(
                            modifier = Modifier.size(52.dp).clip(RoundedCornerShape(14.dp))
                                .background(appColors.gold.copy(alpha = 0.12f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.TrendingUp, null, tint = appColors.gold, modifier = Modifier.size(28.dp))
                        }
                    }
                }

                SectionHeader(title = "سعر البيع", icon = Icons.Default.Sell)

                AppTextField(
                    value = salePriceInput,
                    onValueChange = { salePriceInput = it.filter { c -> c.isDigit() || c == '.' } },
                    label = "السعر المطلوب (جنيه مصري)",
                    leadingIcon = Icons.Default.AttachMoney,
                    placeholder = "0",
                    keyboardType = KeyboardType.Decimal
                )

                // Price comparison
                if (askedPrice > 0 && marketPrice > 0) {
                    val diff = askedPrice - marketPrice
                    val pct = (diff / marketPrice * 100)
                    AppCard {
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            PriceCompareItem("سعرك", "EGP ${"%.0f".format(askedPrice)}", appColors.textPrimary)
                            Box(modifier = Modifier.width(1.dp).height(40.dp).background(appColors.cardBorder).align(Alignment.CenterVertically))
                            PriceCompareItem(
                                "الفارق",
                                "${if (diff >= 0) "+" else ""}${"%.0f".format(pct)}%",
                                if (kotlin.math.abs(pct) > 30) appColors.statusRejected
                                else if (kotlin.math.abs(pct) > 15) appColors.warning
                                else appColors.success
                            )
                            Box(modifier = Modifier.width(1.dp).height(40.dp).background(appColors.cardBorder).align(Alignment.CenterVertically))
                            PriceCompareItem("السوق", "EGP ${"%.0f".format(marketPrice)}", appColors.textSecondary)
                        }
                    }
                }

                warning?.let { PriceWarningBox(message = it.message, percentage = it.percentage) }
                error?.let { ErrorMessage(it) }

                PrimaryButton(
                    text = "التالي — إرسال للمشتري",
                    icon = Icons.Default.ArrowBack,
                    onClick = {
                        val price = salePriceInput.toDoubleOrNull()
                        when {
                            draft == null -> error = "تعذر تحميل بيانات المركبة"
                            price == null || !ValidationUtil.isValidPrice(price) -> error = "أدخل سعراً صحيحاً"
                            else -> {
                                TransferDraftStore.drafts[vehicleId] = draft.copy(salePrice = price, marketPrice = marketPrice)
                                navController.navigate(Screen.TransferRequestScreen.createRoute(vehicleId))
                            }
                        }
                    }
                )
                Spacer(Modifier.height(16.dp))
            }
        }
    }
}

@Composable
private fun PriceCompareItem(label: String, value: String, color: Color) {
    val appColors = LocalAppColors.current
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(label, style = MaterialTheme.typography.labelSmall, color = appColors.textTertiary)
        Text(value, style = MaterialTheme.typography.titleSmall, color = color, fontWeight = FontWeight.Bold)
    }
}

// ══════════════════════════════════════════════════════════════════
// 4️⃣  TRANSFER REQUEST SCREEN
// ══════════════════════════════════════════════════════════════════
@Composable
fun TransferRequestScreen(
    navController: NavHostController,
    vehicleId: String,
    viewModel: TransferRequestViewModel = hiltViewModel()
) {
    val appColors = LocalAppColors.current
    val draft = TransferDraftStore.drafts[vehicleId]
    val createState by viewModel.createRequestState.collectAsState()
    var buyerNationalId by remember { mutableStateOf(draft?.buyerNationalId.orEmpty()) }
    var notes by remember { mutableStateOf(draft?.notes.orEmpty()) }
    var error by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(createState) {
        when (createState) {
            is TransferState.RequestCreated -> {
                TransferDraftStore.drafts.remove(vehicleId)
                navController.navigate(Screen.HomeScreen.route) { popUpTo(Screen.HomeScreen.route) { inclusive = true } }
            }
            is TransferState.Error -> error = (createState as TransferState.Error).message
            else -> Unit
        }
    }

    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
        Column(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)) {

            GradientHeader(
                title = "إرسال طلب النقل",
                subtitle = "الخطوة 4 من 4 — تأكيد وإرسال",
                onBack = { navController.popBackStack() },
                icon = Icons.Default.Send
            )

            Box(modifier = Modifier.fillMaxWidth().background(MaterialTheme.colorScheme.background).padding(horizontal = 20.dp, vertical = 16.dp)) {
                StepIndicator(currentStep = 4, totalSteps = 4, stepLabels = listOf("المركبة", "الصور", "السعر", "الإرسال"))
            }

            Column(
                modifier = Modifier.fillMaxWidth().weight(1f)
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 20.dp),
                verticalArrangement = Arrangement.spacedBy(18.dp)
            ) {
                // Summary card
                SectionHeader(title = "ملخص الطلب", icon = Icons.Default.Summarize)

                AppCard {
                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        InfoRow(label = "البائع", value = CURRENT_SELLER_NAME, icon = Icons.Default.Sell)
                        Divider(color = appColors.cardBorder)
                        InfoRow(label = "المركبة", value = draft?.vehicle?.licensePlate ?: "—", icon = Icons.Default.DirectionsCar)
                        Divider(color = appColors.cardBorder)
                        InfoRow(label = "سعر البيع", value = "EGP ${"%.0f".format(draft?.salePrice ?: 0.0)}", icon = Icons.Default.AttachMoney)
                        Divider(color = appColors.cardBorder)
                        InfoRow(label = "الصور المرفوعة", value = "4 صور ✓", icon = Icons.Default.Photo)
                    }
                }

                SectionHeader(title = "بيانات المشتري", icon = Icons.Default.PersonSearch)

                AppTextField(
                    value = buyerNationalId,
                    onValueChange = { if (it.length <= 14 && it.all(Char::isDigit)) buyerNationalId = it },
                    label = "الرقم القومي للمشتري",
                    leadingIcon = Icons.Default.Badge,
                    placeholder = "14 رقم",
                    keyboardType = KeyboardType.Number
                )

                AppTextField(
                    value = notes,
                    onValueChange = { notes = it },
                    label = "ملاحظات إضافية (اختياري)",
                    leadingIcon = Icons.Default.Notes,
                    placeholder = "أي ملاحظات تريد إضافتها...",
                    singleLine = false,
                    maxLines = 4
                )

                // Warning about sending
                Row(
                    modifier = Modifier.fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp))
                        .background(appColors.navy.copy(alpha = 0.08f))
                        .padding(12.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.NotificationsActive, null, tint = appColors.navy, modifier = Modifier.size(18.dp))
                    Text(
                        "سيصل إشعار فوري للمشتري بالرقم القومي المُدخَل",
                        style = MaterialTheme.typography.bodySmall,
                        color = appColors.textSecondary
                    )
                }

                error?.let { ErrorMessage(it) }
                if (createState is TransferState.Loading) LoadingBox(message = "جاري إرسال الطلب...")

                PrimaryButton(
                    text = "تأكيد وإرسال الطلب",
                    icon = Icons.Default.Send,
                    onClick = {
                        val salePrice = draft?.salePrice
                        when {
                            draft == null || salePrice == null -> error = "بيانات الطلب غير مكتملة"
                            !ValidationUtil.isValidNationalId(buyerNationalId) -> error = "الرقم القومي للمشتري غير صحيح"
                            else -> {
                                val warningObj = draft.marketPrice?.let { PriceValidationUtil.validatePrice(salePrice, it) }
                                viewModel.createTransferRequest(
                                    TransferRequest(
                                        id = "request_${UUID.randomUUID()}",
                                        vehicleId = vehicleId,
                                        sellerId = CURRENT_SELLER_ID,
                                        buyerId = buyerNationalId,
                                        price = salePrice,
                                        status = TransferStatus.PENDING,
                                        sellerName = CURRENT_SELLER_NAME,
                                        buyerName = "",
                                        createdAt = Date(),
                                        updatedAt = Date(),
                                        notes = notes,
                                        priceWarning = warningObj
                                    )
                                )
                            }
                        }
                    }
                )
                Spacer(Modifier.height(16.dp))
            }
        }
    }
}

// ══════════════════════════════════════════════════════════════════
// 5️⃣  REQUESTS MANAGEMENT SCREEN
// ══════════════════════════════════════════════════════════════════
@Composable
fun RequestsManagementScreen(
    navController: NavHostController,
    viewModel: TransferRequestViewModel = hiltViewModel()
) {
    val appColors = LocalAppColors.current
    var buyerIdOrNationalId by remember { mutableStateOf("9876543210") }
    val requestsState by viewModel.buyerRequestsState.collectAsState()

    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
        Column(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)) {

            GradientHeader(
                title = "إدارة الطلبات",
                subtitle = "عرض وإدارة طلبات نقل الملكية",
                onBack = { navController.popBackStack() },
                icon = Icons.Default.Assignment
            )

            Column(
                modifier = Modifier.fillMaxWidth().weight(1f)
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 20.dp, vertical = 20.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                SectionHeader(title = "البحث بالرقم القومي", icon = Icons.Default.Search)

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalAlignment = Alignment.Bottom
                ) {
                    AppTextField(
                        value = buyerIdOrNationalId,
                        onValueChange = { buyerIdOrNationalId = it.trim() },
                        label = "الرقم القومي للمشتري",
                        leadingIcon = Icons.Default.Badge,
                        modifier = Modifier.weight(1f)
                    )
                    Button(
                        onClick = { viewModel.getBuyerRequests(buyerIdOrNationalId) },
                        modifier = Modifier.height(56.dp),
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = appColors.navy)
                    ) {
                        Icon(Icons.Default.Search, null, Modifier.size(20.dp))
                    }
                }

                when (val state = requestsState) {
                    is TransferState.Loading -> LoadingBox(message = "جاري تحميل الطلبات...")
                    is TransferState.Error -> ErrorMessage(state.message)
                    is TransferState.RequestsLoaded -> {
                        TransferDraftStore.lastLoadedRequests = state.requests
                        if (state.requests.isEmpty()) {
                            Box(modifier = Modifier.fillMaxWidth().padding(40.dp), contentAlignment = Alignment.Center) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Icon(Icons.Default.Inbox, null, tint = appColors.textTertiary, modifier = Modifier.size(52.dp))
                                    Spacer(Modifier.height(12.dp))
                                    Text("لا توجد طلبات حالياً", style = MaterialTheme.typography.titleMedium, color = appColors.textSecondary)
                                }
                            }
                        } else {
                            Text(
                                "${state.requests.size} طلب",
                                style = MaterialTheme.typography.titleSmall,
                                color = appColors.textSecondary
                            )
                            state.requests.forEach { request ->
                                RequestCard(
                                    request = request,
                                    onClick = { navController.navigate(Screen.RequestDetailsScreen.createRoute(request.id)) }
                                )
                            }
                        }
                    }
                    else -> Box(modifier = Modifier.fillMaxWidth().padding(32.dp), contentAlignment = Alignment.Center) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(Icons.Default.Search, null, tint = appColors.textTertiary, modifier = Modifier.size(44.dp))
                            Spacer(Modifier.height(8.dp))
                            Text("اضغط بحث لعرض الطلبات", style = MaterialTheme.typography.bodyMedium, color = appColors.textSecondary)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun RequestCard(request: TransferRequest, onClick: () -> Unit) {
    val appColors = LocalAppColors.current
    AppCard(modifier = Modifier.clickable(onClick = onClick)) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.Top) {
            Column(modifier = Modifier.weight(1f)) {
                Text("طلب #${request.id.takeLast(6)}", style = MaterialTheme.typography.titleSmall, color = appColors.textPrimary, fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(4.dp))
                Text("EGP ${"%.0f".format(request.price)}", style = MaterialTheme.typography.bodyMedium, color = appColors.gold, fontWeight = FontWeight.SemiBold)
                Spacer(Modifier.height(4.dp))
                Text("البائع: ${request.sellerName}", style = MaterialTheme.typography.bodySmall, color = appColors.textSecondary)
            }
            Column(horizontalAlignment = Alignment.End, verticalArrangement = Arrangement.spacedBy(8.dp)) {
                StatusBadge(status = request.status.name)
                Icon(Icons.Default.ChevronLeft, null, tint = appColors.textTertiary, modifier = Modifier.size(20.dp))
            }
        }
    }
}

// ══════════════════════════════════════════════════════════════════
// 6️⃣  REQUEST DETAILS SCREEN
// ══════════════════════════════════════════════════════════════════
@Composable
fun RequestDetailsScreen(navController: NavHostController, requestId: String) {
    val appColors = LocalAppColors.current
    val request = TransferDraftStore.lastLoadedRequests.firstOrNull { it.id == requestId }

    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
        Column(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)) {

            GradientHeader(
                title = "تفاصيل الطلب",
                subtitle = "عرض كامل بيانات طلب النقل",
                onBack = { navController.popBackStack() },
                icon = Icons.Default.Info
            )

            if (request == null) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    ErrorMessage("لا يمكن تحميل بيانات الطلب. ارجع وحمّل الطلبات مرة أخرى.")
                }
            } else {
                Column(
                    modifier = Modifier.fillMaxWidth().weight(1f)
                        .verticalScroll(rememberScrollState())
                        .padding(horizontal = 20.dp, vertical = 20.dp),
                    verticalArrangement = Arrangement.spacedBy(18.dp)
                ) {
                    // Status header
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text("رقم الطلب", style = MaterialTheme.typography.labelSmall, color = appColors.textTertiary)
                            Text("#${requestId.takeLast(8)}", style = MaterialTheme.typography.titleMedium, color = appColors.textPrimary, fontWeight = FontWeight.Bold)
                        }
                        StatusBadge(status = request.status.name)
                    }

                    // Parties card
                    SectionHeader(title = "أطراف العملية", icon = Icons.Default.Group)
                    AppCard {
                        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                            PartyRow(role = "البائع", name = request.sellerName, id = request.sellerId, icon = Icons.Default.Sell, color = appColors.gold)
                            Divider(color = appColors.cardBorder)
                            PartyRow(role = "المشتري", name = request.buyerName.ifBlank { "—" }, id = request.buyerId, icon = Icons.Default.ShoppingCart, color = appColors.navy)
                        }
                    }

                    // Vehicle & price
                    SectionHeader(title = "بيانات الصفقة", icon = Icons.Default.DirectionsCar)
                    AppCard {
                        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                            InfoRow(label = "المركبة", value = request.vehicleId, icon = Icons.Default.DirectionsCar)
                            Divider(color = appColors.cardBorder)
                            InfoRow(label = "سعر البيع", value = "EGP ${"%.0f".format(request.price)}", icon = Icons.Default.AttachMoney)
                            Divider(color = appColors.cardBorder)
                            InfoRow(label = "الملاحظات", value = request.notes.ifBlank { "لا توجد ملاحظات" }, icon = Icons.Default.Notes)
                        }
                    }

                    request.priceWarning?.let {
                        PriceWarningBox(message = it.message, percentage = it.percentage)
                    }

                    // Action buttons
                    if (request.status == TransferStatus.PENDING) {
                        PrimaryButton(
                            text = "الموافقة على الطلب",
                            icon = Icons.Default.CheckCircle,
                            onClick = { navController.navigate(Screen.ApprovalScreen.createRoute(requestId)) }
                        )
                        SecondaryButton(
                            text = "رفض الطلب",
                            icon = Icons.Default.Cancel,
                            onClick = { navController.popBackStack() }
                        )
                    } else {
                        SecondaryButton(
                            text = "رجوع",
                            icon = Icons.Default.ArrowForward,
                            onClick = { navController.popBackStack() }
                        )
                    }

                    Spacer(Modifier.height(16.dp))
                }
            }
        }
    }
}

@Composable
private fun PartyRow(role: String, name: String, id: String, icon: ImageVector, color: Color) {
    val appColors = LocalAppColors.current
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Box(
            modifier = Modifier.size(44.dp).clip(CircleShape).background(color.copy(alpha = 0.12f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(imageVector = icon, contentDescription = null, tint = color, modifier = Modifier.size(22.dp))
        }
        Column(modifier = Modifier.weight(1f)) {
            Text(role, style = MaterialTheme.typography.labelSmall, color = appColors.textTertiary)
            Text(name, style = MaterialTheme.typography.titleSmall, color = appColors.textPrimary, fontWeight = FontWeight.SemiBold)
            Text(id, style = MaterialTheme.typography.bodySmall, color = appColors.textSecondary)
        }
    }
}

// ══════════════════════════════════════════════════════════════════
// 7️⃣  APPROVAL SCREEN
// ══════════════════════════════════════════════════════════════════
@Composable
fun ApprovalScreen(
    navController: NavHostController,
    requestId: String,
    viewModel: TransferRequestViewModel = hiltViewModel()
) {
    val appColors = LocalAppColors.current
    var buyerId by remember { mutableStateOf("user2") }
    var error by remember { mutableStateOf<String?>(null) }
    val approveState by viewModel.approveRequestState.collectAsState()

    LaunchedEffect(approveState) {
        when (approveState) {
            is TransferState.RequestApproved -> navController.popBackStack()
            is TransferState.Error -> error = (approveState as TransferState.Error).message
            else -> Unit
        }
    }

    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
        Column(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)) {

            GradientHeader(
                title = "اعتماد الطلب",
                subtitle = "موافقة المشتري على نقل الملكية",
                onBack = { navController.popBackStack() },
                icon = Icons.Default.Verified
            )

            Column(
                modifier = Modifier.fillMaxWidth().weight(1f)
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 20.dp, vertical = 24.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                // Confirmation banner
                Box(
                    modifier = Modifier.fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(Brush.linearGradient(listOf(Color(0xFF1B5E20), Color(0xFF2E7D32))))
                        .padding(20.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(14.dp)) {
                        Box(
                            modifier = Modifier.size(52.dp).clip(CircleShape).background(Color.White.copy(alpha = 0.15f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.Verified, null, tint = Color.White, modifier = Modifier.size(30.dp))
                        }
                        Column {
                            Text("تأكيد استلام إشعار النقل", style = MaterialTheme.typography.titleMedium, color = Color.White, fontWeight = FontWeight.Bold)
                            Text("رقم الطلب: #${requestId.takeLast(8)}", style = MaterialTheme.typography.bodySmall, color = Color.White.copy(alpha = 0.7f))
                        }
                    }
                }

                SectionHeader(title = "تأكيد الهوية", icon = Icons.Default.Security)

                AppTextField(
                    value = buyerId,
                    onValueChange = { buyerId = it },
                    label = "معرّف المشتري",
                    leadingIcon = Icons.Default.Person
                )

                // Security note
                AppCard {
                    Row(verticalAlignment = Alignment.Top, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        Icon(Icons.Default.Lock, null, tint = appColors.gold, modifier = Modifier.size(20.dp))
                        Column {
                            Text("تنبيه أمني", style = MaterialTheme.typography.labelMedium, color = appColors.textPrimary, fontWeight = FontWeight.Bold)
                            Text(
                                "بالموافقة يُعدّ نقل الملكية رسمياً ملزماً قانونياً ولا يمكن التراجع عنه.",
                                style = MaterialTheme.typography.bodySmall,
                                color = appColors.textSecondary
                            )
                        }
                    }
                }

                error?.let { ErrorMessage(it) }
                if (approveState is TransferState.Loading) LoadingBox(message = "جاري اعتماد الطلب...")

                PrimaryButton(
                    text = "موافق — إتمام نقل الملكية",
                    icon = Icons.Default.CheckCircle,
                    onClick = {
                        if (buyerId.isBlank()) error = "أدخل معرّف المشتري"
                        else viewModel.approveRequest(requestId = requestId, buyerId = buyerId)
                    }
                )

                SecondaryButton(
                    text = "رفض الطلب",
                    icon = Icons.Default.Cancel,
                    onClick = { navController.popBackStack() }
                )

                Spacer(Modifier.height(16.dp))
            }
        }
    }
}
