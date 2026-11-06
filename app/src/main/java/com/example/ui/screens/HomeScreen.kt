package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.data.VedikaData
import com.example.model.ServiceItem
import com.example.ui.components.*
import com.example.ui.components.discovery.DiscoveryUnavailableState
import com.example.ui.theme.*
import com.example.viewmodel.Screen
import com.example.viewmodel.VedikaUiState
import com.example.viewmodel.VedikaViewModel

@Composable
fun HomeScreen(
    viewModel: VedikaViewModel,
    uiState: VedikaUiState,
    modifier: Modifier = Modifier
) {
    val isHindi = uiState.isHindi

    val greetingName = uiState.userName
        .takeIf { !uiState.isGuest && uiState.userName.isNotBlank() }
        ?.split(" ")
        ?.firstOrNull()

    val currentHour = java.util.Calendar.getInstance().get(java.util.Calendar.HOUR_OF_DAY)
    val timeGreeting = when (currentHour) {
        in 0..11 -> if (isHindi) "शुभ प्रभात" else "Good Morning"
        in 12..16 -> if (isHindi) "शुभ दोपहर" else "Good Afternoon"
        else -> if (isHindi) "शुभ संध्या" else "Good Evening"
    }

    var isLoading by remember { mutableStateOf(false) }
    var hasError by remember { mutableStateOf(false) }

    Scaffold(
        containerColor = VedikaCream,
        topBar = {
            VedikaHomeTopBar(
                currentCity = uiState.selectedCity,
                onCitySelected = { viewModel.selectCity(it) },
                onNotificationClick = { viewModel.navigateTo(Screen.Notifications) },
                hasUnreadNotifications = uiState.notifications.any { it.isUnread },
                isHindi = isHindi,
                onToggleLanguage = { viewModel.toggleLanguage() }
            )
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
            contentPadding = PaddingValues(bottom = 32.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // 1. Personalized Greeting Header
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = if (greetingName == null) {
                                if (isHindi) "वेदिका में आपका स्वागत है" else "Welcome to VEDIKA"
                            } else {
                                "$timeGreeting\n$greetingName 👋"
                            },
                            style = MaterialTheme.typography.headlineMedium.copy(
                                fontWeight = FontWeight.ExtraBold,
                                color = VedikaTextDark,
                                fontSize = 26.sp,
                                lineHeight = 32.sp
                            )
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = if (isHindi) "आपका दिन सुख और शांति से परिपूर्ण हो ✨" else "May your day be filled with\npeace and positivity ✨",
                            style = MaterialTheme.typography.bodyMedium.copy(
                                color = VedikaTextMuted,
                                fontSize = 13.sp,
                                lineHeight = 18.sp
                            )
                        )
                    }

                    Surface(
                        onClick = { viewModel.setBottomNavTab("Profile") },
                        shape = CircleShape,
                        color = Color(0xFFFFF3E0),
                        border = BorderStroke(2.dp, VedikaGold),
                        modifier = Modifier
                            .size(54.dp)
                            .shadow(4.dp, CircleShape, spotColor = Color(0x20000000))
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            if (uiState.isGuest) {
                                Icon(
                                    imageVector = Icons.Outlined.Person,
                                    contentDescription = "Guest profile",
                                    tint = VedikaOrangeDark,
                                    modifier = Modifier.size(28.dp)
                                )
                            } else {
                                AsyncImage(
                                    model = ImageRequest.Builder(LocalContext.current)
                                        .data("https://images.unsplash.com/photo-1506794778202-cad84cf45f1d?auto=format&fit=crop&w=300&q=80")
                                        .crossfade(true)
                                        .build(),
                                    contentDescription = "Profile",
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier.fillMaxSize()
                                )
                            }
                        }
                    }
                }
            }

            // 2. Search Bar
            item {
                Surface(
                    onClick = { viewModel.navigateTo(Screen.Search) },
                    shape = RoundedCornerShape(20.dp),
                    color = VedikaSurface,
                    border = BorderStroke(1.dp, VedikaCardBorder),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                        .height(54.dp)
                        .shadow(3.dp, RoundedCornerShape(20.dp), spotColor = Color(0x0C000000))
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Search",
                            tint = VedikaTextMuted,
                            modifier = Modifier.size(22.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = if (isHindi) "पूजा, मंदिर, पंडित जी खोजें..." else "Search puja, temple, pandit...",
                            style = MaterialTheme.typography.bodyMedium.copy(
                                color = VedikaTextMuted,
                                fontSize = 14.sp
                            ),
                            modifier = Modifier.weight(1f)
                        )
                        Icon(
                            imageVector = Icons.Default.Mic,
                            contentDescription = "Voice Search",
                            tint = VedikaTextMuted,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Icon(
                            imageVector = Icons.Default.Tune,
                            contentDescription = "Filter",
                            tint = VedikaOrangeDark,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }

            // 3. Golden Temple Promo Banner Card
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                ) {
                    Surface(
                        onClick = { viewModel.openServiceDetail("s1") },
                        shape = RoundedCornerShape(22.dp),
                        color = Color(0xFFFFF7ED),
                        border = BorderStroke(1.dp, Color(0xFFFFE0B2)),
                        modifier = Modifier
                            .fillMaxWidth()
                            .shadow(6.dp, RoundedCornerShape(22.dp), spotColor = Color(0x18000000))
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(175.dp)
                                .background(
                                    brush = Brush.horizontalGradient(
                                        colors = listOf(
                                            Color(0xFFFFFAF2),
                                            Color(0xFFFFF3E0),
                                            Color(0xFFFFE6CC)
                                        )
                                    )
                                )
                        ) {
                            Box(
                                modifier = Modifier
                                    .align(Alignment.CenterEnd)
                                    .fillMaxHeight()
                                    .width(148.dp)
                                    .padding(end = 8.dp, top = 8.dp, bottom = 8.dp)
                                    .clip(RoundedCornerShape(18.dp))
                            ) {
                                TempleVisualArt(
                                    templeName = "Temple discovery",
                                    modifier = Modifier.fillMaxSize(),
                                    cornerRadius = 18.dp
                                )
                            }

                            Column(
                                modifier = Modifier
                                    .fillMaxHeight()
                                    .fillMaxWidth(0.54f)
                                    .padding(start = 16.dp, top = 16.dp, bottom = 16.dp),
                                verticalArrangement = Arrangement.SpaceBetween
                            ) {
                                Column {
                                    Text(
                                        text = if (isHindi) "वैदिक पूजा\nसेवाएं देखें" else "Explore\nPuja Services",
                                        style = MaterialTheme.typography.titleLarge.copy(
                                            fontWeight = FontWeight.ExtraBold,
                                            color = VedikaTextDark,
                                            fontSize = 18.sp,
                                            lineHeight = 22.sp
                                        )
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = if (isHindi) "अनुष्ठान एवं पूजा विवरण देखें।" else "Browse catalogue & review service details.",
                                        style = MaterialTheme.typography.bodySmall.copy(
                                            color = VedikaTextMedium,
                                            fontSize = 11.sp,
                                            lineHeight = 15.sp
                                        ),
                                        maxLines = 2,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                }

                                Box(
                                    modifier = Modifier
                                        .height(38.dp)
                                        .shadow(4.dp, RoundedCornerShape(19.dp), spotColor = Color(0x30FF6B00))
                                        .clip(RoundedCornerShape(19.dp))
                                        .background(
                                            brush = Brush.horizontalGradient(
                                                colors = listOf(Color(0xFFFF6D00), Color(0xFFFF4500))
                                            )
                                        )
                                        .clickable { viewModel.setBottomNavTab("Explore") }
                                        .padding(horizontal = 16.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text(
                                            text = if (isHindi) "देखें" else "Explore",
                                            style = MaterialTheme.typography.labelMedium.copy(
                                                fontWeight = FontWeight.Bold,
                                                color = Color.White,
                                                fontSize = 13.sp
                                            )
                                        )
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text(
                                            text = "→",
                                            fontWeight = FontWeight.Bold,
                                            color = Color.White,
                                            fontSize = 13.sp
                                        )
                                    }
                                }
                            }
                        }
                    }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 10.dp),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(7.dp)
                                .clip(CircleShape)
                                .background(VedikaOrange)
                        )
                        Spacer(modifier = Modifier.width(5.dp))
                        repeat(3) {
                            Box(
                                modifier = Modifier
                                    .size(5.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFFD6CBC0))
                            )
                            Spacer(modifier = Modifier.width(5.dp))
                        }
                    }
                }
            }

            // 4. Quick Tool Shortcuts Grid
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    Text(
                        text = if (isHindi) "धार्मिक सुविधाएं एवं साधन" else "Devotional Tools & Services",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = VedikaTextDark,
                            fontSize = 18.sp
                        )
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        MockupActionIcon(
                            icon = Icons.Outlined.DateRange,
                            label = if (isHindi) "पंचांग" else "Panchang",
                            onClick = { viewModel.setBottomNavTab("Panchang") }
                        )
                        MockupActionIcon(
                            icon = Icons.Outlined.SelfImprovement,
                            label = if (isHindi) "जप माला" else "Japa Mala",
                            onClick = { viewModel.navigateTo(Screen.MantraJapa) }
                        )
                        MockupActionIcon(
                            icon = Icons.Outlined.ShoppingBag,
                            label = if (isHindi) "पूजा सामग्री" else "Puja Samagri",
                            onClick = { viewModel.navigateTo(Screen.PujaSamagri) }
                        )
                        MockupActionIcon(
                            icon = Icons.Outlined.MenuBook,
                            label = if (isHindi) "कथा एवं भजन" else "Katha & Bhajan",
                            onClick = { viewModel.navigateTo(Screen.KathaBhajan) }
                        )
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        MockupActionIcon(
                            icon = Icons.Outlined.Map,
                            label = if (isHindi) "पास के मंदिर" else "Nearby Map",
                            onClick = { viewModel.navigateTo(Screen.NearbyMap) }
                        )
                        MockupActionIcon(
                            icon = Icons.Outlined.TempleHindu,
                            label = if (isHindi) "मंदिर दर्शन" else "Temple Darshan",
                            onClick = { viewModel.navigateTo(Screen.TempleDarshan) }
                        )
                        MockupActionIcon(
                            icon = Icons.Outlined.Person,
                            label = if (isHindi) "पंडित जी बुकिंग" else "Book Pandit",
                            onClick = { viewModel.navigateTo(Screen.PanditBooking) }
                        )
                        MockupActionIcon(
                            icon = Icons.Outlined.Explore,
                            label = if (isHindi) "सभी देखें" else "Explore All",
                            onClick = { viewModel.setBottomNavTab("Explore") }
                        )
                    }
                }
            }

            // 5. Popular Pujas Section Header
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 2.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (isHindi) "लोकप्रिय पूजा अनुष्ठान" else "Popular Pujas",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = VedikaTextDark,
                            fontSize = 18.sp
                        )
                    )

                    Row(
                        modifier = Modifier
                            .clickable { viewModel.setBottomNavTab("Explore") }
                            .padding(4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = if (isHindi) "सभी देखें" else "View All",
                            style = MaterialTheme.typography.labelMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = VedikaOrangeDark,
                                fontSize = 13.sp
                            )
                        )
                        Spacer(modifier = Modifier.width(3.dp))
                        Text(
                            text = "→",
                            fontWeight = FontWeight.Bold,
                            color = VedikaOrangeDark,
                            fontSize = 13.sp
                        )
                    }
                }
            }

            // 6. Dynamic Content States: Loading, Error & Retry, Empty, Success
            when {
                isLoading -> {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(120.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(color = VedikaOrange)
                        }
                    }
                }
                hasError -> {
                    item {
                        Surface(
                            shape = RoundedCornerShape(16.dp),
                            color = Color(0xFFFFEBEE),
                            border = BorderStroke(1.dp, Color(0xFFFFCDD2)),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 20.dp)
                        ) {
                            Column(
                                modifier = Modifier.padding(16.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = if (isHindi) "पूजा सूची लोड करने में विफल" else "Failed to load dynamic service catalogue",
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFFC62828),
                                    fontSize = 14.sp
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Button(
                                    onClick = {
                                        hasError = false
                                        isLoading = false
                                    },
                                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFC62828)),
                                    modifier = Modifier.height(36.dp)
                                ) {
                                    Text(if (isHindi) "पुनः प्रयास करें" else "Retry", color = Color.White, fontSize = 13.sp)
                                }
                            }
                        }
                    }
                }
                VedikaData.services.isEmpty() -> {
                    item {
                        DiscoveryUnavailableState(
                            title = if (isHindi) "पूजा सूची उपलब्ध नहीं है" else "Service catalogue unavailable",
                            message = if (isHindi) "सेवाएं उपलब्ध होने पर यहां दिखाई देंगी।" else "Services will appear here when the catalogue is available.",
                            modifier = Modifier.padding(horizontal = 20.dp)
                        )
                    }
                }
                else -> {
                    items(VedikaData.services.take(3)) { service ->
                        PopularPujaCard(
                            service = service,
                            isHindi = isHindi,
                            onClick = { viewModel.openServiceDetail(service.id) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 20.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun MockupActionIcon(
    icon: ImageVector,
    label: String,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .width(76.dp)
            .clickable(onClick = onClick)
    ) {
        Surface(
            shape = RoundedCornerShape(18.dp),
            color = Color(0xFFFFF2E6),
            border = BorderStroke(1.dp, Color(0xFFFFDEC2)),
            modifier = Modifier.size(56.dp)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    imageVector = icon,
                    contentDescription = label,
                    tint = VedikaOrangeDark,
                    modifier = Modifier.size(26.dp)
                )
            }
        }
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall.copy(
                fontWeight = FontWeight.Medium,
                color = VedikaTextDark,
                fontSize = 11.sp,
                lineHeight = 14.sp
            ),
            textAlign = androidx.compose.ui.text.style.TextAlign.Center,
            maxLines = 2
        )
    }
}

@Composable
private fun PopularPujaCard(
    service: ServiceItem,
    isHindi: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(18.dp),
        color = VedikaSurface,
        border = BorderStroke(1.dp, VedikaCardBorder),
        modifier = modifier.shadow(3.dp, RoundedCornerShape(18.dp), spotColor = Color(0x0E000000))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(68.dp)
                    .clip(RoundedCornerShape(14.dp))
            ) {
                ServiceVisualArt(
                    serviceTitle = service.title,
                    modifier = Modifier.fillMaxSize(),
                    cornerRadius = 14.dp
                )
            }

            Spacer(modifier = Modifier.width(14.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = service.title,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = VedikaTextDark,
                        fontSize = 16.sp
                    )
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = if (isHindi) "बुकिंग से पूर्व विवरण देखें" else "Catalogue details available before booking",
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = VedikaTextMuted,
                        fontSize = 12.sp
                    )
                )
                Spacer(modifier = Modifier.height(3.dp))
                Text(
                    text = service.shortDescription,
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = VedikaTextMedium,
                        fontSize = 12.sp
                    ),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "₹${service.price}",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.ExtraBold,
                        color = VedikaOrangeDark,
                        fontSize = 16.sp
                    )
                )
            }

            Surface(
                shape = CircleShape,
                color = Color(0xFFFFF2E6),
                modifier = Modifier.size(32.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = "Open",
                        tint = VedikaOrangeDark,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }
    }
}
