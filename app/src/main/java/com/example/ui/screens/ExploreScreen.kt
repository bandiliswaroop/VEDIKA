package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.VedikaData
import com.example.model.ServiceItem
import com.example.ui.components.ServiceVisualArt
import com.example.ui.components.VedikaBottomBar
import com.example.ui.components.discovery.DiscoveryUnavailableState
import com.example.ui.theme.*
import com.example.viewmodel.Screen
import com.example.viewmodel.VedikaUiState
import com.example.viewmodel.VedikaViewModel

@Composable
fun ExploreScreen(
    viewModel: VedikaViewModel,
    uiState: VedikaUiState,
    modifier: Modifier = Modifier
) {
    val isHindi = uiState.isHindi

    val categoryFilters = listOf("All", "Griha Puja", "Temple Puja", "Special", "Sanskar", "Katha & Bhajan", "Consultation")
    var selectedCategory by remember { mutableStateOf("All") }
    var selectedSortOrder by remember { mutableStateOf("Recommended") }

    val filteredServices = remember(selectedCategory, selectedSortOrder) {
        val baseList = if (selectedCategory == "All") {
            VedikaData.services
        } else {
            VedikaData.services.filter { it.category.contains(selectedCategory, ignoreCase = true) }
        }

        when (selectedSortOrder) {
            "Price: Low to High" -> baseList.sortedBy { it.price }
            "Price: High to Low" -> baseList.sortedByDescending { it.price }
            "Top Rated" -> baseList.sortedByDescending { it.rating }
            else -> baseList
        }
    }

    Scaffold(
        containerColor = VedikaCream,
        topBar = {
            Surface(
                color = VedikaCream,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(
                            onClick = { viewModel.setBottomNavTab("Home") },
                            modifier = Modifier
                                .size(40.dp)
                                .semantics { contentDescription = "Back to home" }
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Back",
                                tint = VedikaTextDark,
                                modifier = Modifier.size(24.dp)
                            )
                        }

                        Text(
                            text = if (isHindi) "पूजा एवं अनुष्ठान खोजें" else "Explore Puja & Services",
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
                            onClick = { viewModel.navigateTo(Screen.Search) },
                            modifier = Modifier
                                .size(40.dp)
                                .semantics { contentDescription = "Open search" }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = "Search",
                                tint = VedikaTextDark,
                                modifier = Modifier.size(22.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(2.dp))

                    Text(
                        text = if (isHindi) "श्रेणी एवं प्रकार के अनुसार पूजा अनुष्ठान खोजें।" else "Filter by Puja type, category, and sorting preferences.",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = VedikaTextMuted,
                            fontSize = 13.sp
                        ),
                        modifier = Modifier.padding(start = 48.dp)
                    )
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
            contentPadding = PaddingValues(bottom = 32.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Horizontal Category Filter Pills
            item {
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 20.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.padding(vertical = 6.dp)
                ) {
                    items(categoryFilters) { cat ->
                        val isSelected = selectedCategory == cat
                        val displayCat = if (!isHindi) cat else when (cat) {
                            "All" -> "सभी"
                            "Griha Puja" -> "गृह पूजा"
                            "Temple Puja" -> "मंदिर पूजा"
                            "Special" -> "विशेष अनुष्ठान"
                            "Sanskar" -> "संस्कार"
                            "Katha & Bhajan" -> "कथा एवं भजन"
                            "Consultation" -> "ज्योतिष परामर्श"
                            else -> cat
                        }

                        Surface(
                            onClick = { selectedCategory = cat },
                            shape = RoundedCornerShape(20.dp),
                            color = if (isSelected) VedikaOrange else Color(0xFFF7F2EB),
                            border = BorderStroke(
                                1.dp,
                                if (isSelected) VedikaOrange else Color(0xFFEBE3D5)
                            ),
                            modifier = Modifier
                                .height(38.dp)
                                .semantics { contentDescription = "Filter category $cat" }
                        ) {
                            Box(
                                modifier = Modifier.padding(horizontal = 18.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = displayCat,
                                    style = MaterialTheme.typography.labelMedium.copy(
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                        color = if (isSelected) Color.White else VedikaTextMedium,
                                        fontSize = 13.sp
                                    )
                                )
                            }
                        }
                    }
                }
            }

            // Sorting & Result Count Bar
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (isHindi) "${filteredServices.size} पूजा सेवाएं उपलब्ध" else "${filteredServices.size} services found",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = VedikaTextMuted,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 13.sp
                        )
                    )

                    // Reset filter button if filtered
                    if (selectedCategory != "All" || selectedSortOrder != "Recommended") {
                        TextButton(
                            onClick = {
                                selectedCategory = "All"
                                selectedSortOrder = "Recommended"
                            }
                        ) {
                            Text(
                                text = if (isHindi) "फ़िल्टर रीसेट करें" else "Reset Filters",
                                color = VedikaOrangeDark,
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp
                            )
                        }
                    }
                }
            }

            // List of Filtered Puja Service Cards
            if (filteredServices.isEmpty()) {
                item {
                    DiscoveryUnavailableState(
                        title = if (isHindi) "कोई अनुष्ठान नहीं मिला" else "No services match your filter",
                        message = if (isHindi) "अन्य श्रेणी चुनें या फ़िल्टर रीसेट करें।" else "Try resetting filters or selecting another category.",
                        modifier = Modifier.padding(horizontal = 20.dp),
                        actionLabel = if (isHindi) "सभी फ़िल्टर रीसेट करें" else "Reset all filters",
                        onAction = {
                            selectedCategory = "All"
                            selectedSortOrder = "Recommended"
                        }
                    )
                }
            } else {
                items(filteredServices) { service ->
                    PujaServiceMockupCard(
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

@Composable
fun PujaServiceMockupCard(
    service: ServiceItem,
    isHindi: Boolean = false,
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
                    .size(72.dp)
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

                Spacer(modifier = Modifier.height(3.dp))

                Text(
                    text = if (isHindi) "बुकिंग से पूर्व सूची विवरण उपलब्ध" else "Catalogue details available before booking",
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
