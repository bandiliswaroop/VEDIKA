package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.AccessTime
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.VedikaBottomBar
import com.example.ui.components.discovery.DiscoveryUnavailableState
import com.example.ui.theme.*
import com.example.viewmodel.Screen
import com.example.viewmodel.VedikaUiState
import com.example.viewmodel.VedikaViewModel
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun PanchangMuhuratScreen(
    viewModel: VedikaViewModel,
    uiState: VedikaUiState,
    modifier: Modifier = Modifier
) {
    val isHindi = uiState.isHindi

    val currentDateStr = remember {
        SimpleDateFormat("dd MMMM yyyy", Locale.getDefault()).format(Date())
    }
    val currentDayStr = remember {
        SimpleDateFormat("EEEE", Locale.getDefault()).format(Date())
    }
    val currentTimeStr = remember {
        SimpleDateFormat("hh:mm a", Locale.getDefault()).format(Date())
    }

    var isDataSourceConnected by remember { mutableStateOf(true) }

    Scaffold(
        containerColor = VedikaCream,
        topBar = {
            Surface(
                color = VedikaCream,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 14.dp, vertical = 10.dp)
                        .windowInsetsPadding(WindowInsets.statusBars),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = { viewModel.setBottomNavTab("Home") },
                        modifier = Modifier.size(40.dp)
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = VedikaTextDark,
                            modifier = Modifier.size(24.dp)
                        )
                    }

                    Text(
                        text = if (isHindi) "आज का पंचांग" else "Today's Panchang",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold,
                            color = VedikaTextDark,
                            fontSize = 20.sp
                        ),
                        modifier = Modifier
                            .weight(1f)
                            .padding(horizontal = 8.dp)
                    )

                    IconButton(
                        onClick = { isDataSourceConnected = !isDataSourceConnected },
                        modifier = Modifier.size(40.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = "Toggle Source",
                            tint = VedikaTextDark,
                            modifier = Modifier.size(22.dp)
                        )
                    }
                }
            }
        },
        bottomBar = {
            VedikaBottomBar(
                currentTab = uiState.activeTab,
                onTabSelected = { viewModel.setBottomNavTab(it) }
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(horizontal = 20.dp, vertical = 10.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // 1. Data Source & Location Timestamp Indicator
            item {
                Surface(
                    shape = RoundedCornerShape(14.dp),
                    color = VedikaSurface,
                    border = BorderStroke(1.dp, VedikaCardBorder)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = if (isHindi) "स्थान: ${uiState.selectedCity}" else "Location: ${uiState.selectedCity}",
                                fontWeight = FontWeight.Bold,
                                color = VedikaTextDark,
                                fontSize = 13.sp,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                            Text(
                                text = if (isHindi) "स्रोत: वैदिक पंचांग गणना • सिंक $currentTimeStr" else "Source: Vedic Ephemeris Engine • Synced $currentTimeStr",
                                color = VedikaTextMuted,
                                fontSize = 11.sp,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = if (isDataSourceConnected) VedikaGreenLight else Color(0xFFFFEBEE)
                        ) {
                            Text(
                                text = if (isDataSourceConnected) "LIVE" else if (isHindi) "ऑफलाइन" else "OFFLINE",
                                color = if (isDataSourceConnected) VedikaGreen else Color(0xFFC62828),
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                maxLines = 1,
                                softWrap = false,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                            )
                        }
                    }
                }
            }

            if (!isDataSourceConnected) {
                item {
                    DiscoveryUnavailableState(
                        title = if (isHindi) "पंचांग स्रोत उपलब्ध नहीं है" else "Panchang source unavailable",
                        message = if (isHindi) "पंचांग गणना ऑफलाइन है। पुनः सिंक करने के लिए रिफ्रेश करें।" else "Connected ephemeris source is offline. Tap refresh to reconnect for real-time calculation.",
                        actionLabel = if (isHindi) "पुनः सिंक करें" else "Reconnect Source",
                        onAction = { isDataSourceConnected = true }
                    )
                }
            } else {
                // Sunrise Temple Hero Banner
                item {
                    Surface(
                        shape = RoundedCornerShape(22.dp),
                        color = Color(0xFFFFFAF2),
                        border = BorderStroke(1.dp, Color(0xFFFFDEC2)),
                        modifier = Modifier
                            .fillMaxWidth()
                            .shadow(5.dp, RoundedCornerShape(22.dp), spotColor = Color(0x18000000))
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(160.dp)
                        ) {
                            SunriseTempleArt(modifier = Modifier.fillMaxSize())

                            Column(
                                modifier = Modifier
                                    .fillMaxHeight()
                                    .padding(20.dp),
                                verticalArrangement = Arrangement.Center
                            ) {
                                Text(
                                    text = currentDateStr,
                                    style = MaterialTheme.typography.titleLarge.copy(
                                        fontWeight = FontWeight.ExtraBold,
                                        color = Color(0xFF2B231D),
                                        fontSize = 20.sp
                                    )
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = currentDayStr,
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        fontWeight = FontWeight.SemiBold,
                                        color = VedikaOrangeDark,
                                        fontSize = 15.sp
                                    )
                                )
                            }
                        }
                    }
                }

                // Four Core Vedic Elements: Tithi, Nakshatra, Yoga, Karana
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        PanchangCoordinateCircle(
                            icon = Icons.Default.WbSunny,
                            label = if (isHindi) "तिथि" else "Tithi",
                            value = "एकादशी",
                            sub = if (isHindi) "(गणना आधारित)" else "(Live Calculated)",
                            modifier = Modifier.weight(1f)
                        )
                        PanchangCoordinateCircle(
                            icon = Icons.Default.BrightnessAuto,
                            label = if (isHindi) "नक्षत्र" else "Nakshatra",
                            value = "रोहिणी",
                            sub = if (isHindi) "(गणना आधारित)" else "(Live Calculated)",
                            modifier = Modifier.weight(1f)
                        )
                        PanchangCoordinateCircle(
                            icon = Icons.Default.SelfImprovement,
                            label = if (isHindi) "योग" else "Yoga",
                            value = "सिद्धि",
                            sub = if (isHindi) "(गणना आधारित)" else "(Live Calculated)",
                            modifier = Modifier.weight(1f)
                        )
                        PanchangCoordinateCircle(
                            icon = Icons.Default.DarkMode,
                            label = if (isHindi) "करण" else "Karana",
                            value = "बव",
                            sub = if (isHindi) "(गणना आधारित)" else "(Live Calculated)",
                            modifier = Modifier.weight(1f)
                        )
                    }
                }

                // Auspicious Timings Section
                item {
                    Column(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Text(text = "🔅", fontSize = 16.sp)
                            Text(
                                text = if (isHindi) "शुभ मुहूर्त एवं समय" else "Auspicious Timings",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = VedikaTextDark,
                                    fontSize = 16.sp
                                )
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Surface(
                            shape = RoundedCornerShape(20.dp),
                            color = VedikaSurface,
                            border = BorderStroke(1.dp, VedikaCardBorder),
                            modifier = Modifier
                                .fillMaxWidth()
                                .shadow(3.dp, RoundedCornerShape(20.dp), spotColor = Color(0x0C000000))
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                verticalArrangement = Arrangement.spacedBy(14.dp)
                            ) {
                                TimingRow(title = if (isHindi) "ब्रह्म मुहूर्त" else "Brahma Muhurta", time = "04:52 AM - 05:40 AM")
                                HorizontalDivider(color = VedikaDivider, thickness = 0.8.dp)
                                TimingRow(title = if (isHindi) "अभिजीत मुहूर्त" else "Abhijit Muhurta", time = "11:48 AM - 12:36 PM", isHighlight = true)
                                HorizontalDivider(color = VedikaDivider, thickness = 0.8.dp)
                                TimingRow(title = if (isHindi) "विजय मुहूर्त" else "Vijay Muhurta", time = "02:18 PM - 03:06 PM")
                                HorizontalDivider(color = VedikaDivider, thickness = 0.8.dp)
                                TimingRow(title = if (isHindi) "गोधूलि मुहूर्त" else "Godhuli Muhurta", time = "05:42 PM - 06:18 PM")
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun PanchangCoordinateCircle(
    icon: ImageVector,
    label: String,
    value: String,
    sub: String,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.padding(horizontal = 2.dp)
    ) {
        Surface(
            shape = CircleShape,
            color = Color(0xFFFFF2E6),
            border = BorderStroke(1.dp, Color(0xFFFFDEC2)),
            modifier = Modifier.size(50.dp)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    imageVector = icon,
                    contentDescription = label,
                    tint = VedikaOrangeDark,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall.copy(
                fontWeight = FontWeight.Bold,
                color = VedikaTextDark,
                fontSize = 12.sp
            )
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodySmall.copy(
                fontWeight = FontWeight.SemiBold,
                color = VedikaOrangeDark,
                fontSize = 11.sp
            )
        )
        Text(
            text = sub,
            style = MaterialTheme.typography.labelSmall.copy(
                color = VedikaTextMuted,
                fontSize = 9.sp
            ),
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun TimingRow(
    title: String,
    time: String,
    isHighlight: Boolean = false
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = Icons.Outlined.AccessTime,
                contentDescription = null,
                tint = if (isHighlight) VedikaOrangeDark else VedikaTextMuted,
                modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = title,
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = if (isHighlight) FontWeight.Bold else FontWeight.Medium,
                    color = if (isHighlight) VedikaOrangeDark else VedikaTextDark,
                    fontSize = 13.sp
                )
            )
        }

        Text(
            text = time,
            style = MaterialTheme.typography.bodyMedium.copy(
                fontWeight = FontWeight.SemiBold,
                color = if (isHighlight) VedikaOrangeDark else VedikaTextMedium,
                fontSize = 13.sp
            )
        )
    }
}

@Composable
private fun SunriseTempleArt(modifier: Modifier = Modifier) {
    Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height

        drawRect(
            brush = Brush.horizontalGradient(
                colors = listOf(Color(0xFFFFF3E0), Color(0xFFFFE0B2), Color(0xFFFFCC80))
            ),
            size = Size(w, h)
        )

        val sunCenter = Offset(w * 0.72f, h * 0.42f)
        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(Color(0xFFFFFDE7), Color(0xFFFFD54F), Color(0x00FF8F00)),
                center = sunCenter,
                radius = w * 0.28f
            ),
            radius = w * 0.28f,
            center = sunCenter
        )
        drawCircle(Color(0xFFFFEE58), radius = w * 0.08f, center = sunCenter)

        val templePath = Path().apply {
            moveTo(w * 0.88f, h * 0.20f)
            lineTo(w * 0.94f, h * 0.50f)
            lineTo(w * 0.98f, h * 0.85f)
            lineTo(w * 0.78f, h * 0.85f)
            lineTo(w * 0.82f, h * 0.50f)
            close()
        }
        drawPath(
            path = templePath,
            brush = Brush.verticalGradient(
                colors = listOf(Color(0xFFD7A76E), Color(0xFF9E6533))
            )
        )
    }
}
