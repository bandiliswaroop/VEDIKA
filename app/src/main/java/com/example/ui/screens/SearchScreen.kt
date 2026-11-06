package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.VedikaData
import com.example.model.PanditProfile
import com.example.ui.components.discovery.DiscoveryUnavailableState
import com.example.ui.theme.*
import com.example.viewmodel.VedikaViewModel

@Composable
fun SearchScreen(
    viewModel: VedikaViewModel,
    uiState: com.example.viewmodel.VedikaUiState,
    modifier: Modifier = Modifier
) {
    val isHindi = uiState.isHindi
    var query by remember { mutableStateOf(uiState.searchQuery) }
    val filters = listOf("All", "Services", "Providers")
    var selectedFilter by remember { mutableStateOf("All") }

    val popularSuggestions = listOf(
        "Griha Pravesh Puja",
        "Rudrabhishek",
        "Satyanarayan Katha",
        "Mundan Sanskar"
    )

    val searchResults = remember(query, selectedFilter) {
        val q = query.trim().lowercase()
        val matchingServices = VedikaData.services.filter {
            it.title.lowercase().contains(q) || it.category.lowercase().contains(q) || it.shortDescription.lowercase().contains(q)
        }
        val matchingPandits = VedikaData.pandits.filter {
            it.name.lowercase().contains(q) || it.specializations.any { s -> s.lowercase().contains(q) } || it.city.lowercase().contains(q)
        }
        when (selectedFilter) {
            "Services" -> Pair(matchingServices, emptyList())
            "Providers" -> Pair(emptyList(), matchingPandits)
            else -> Pair(matchingServices, matchingPandits)
        }
    }

    Scaffold(
        containerColor = VedikaCream,
        topBar = {
            Surface(color = VedikaCream, modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = { viewModel.setBottomNavTab("Home") }, modifier = Modifier.size(48.dp)) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = VedikaTextDark,
                            modifier = Modifier.size(24.dp)
                        )
                    }

                    OutlinedTextField(
                        value = query,
                        onValueChange = {
                            query = it
                            viewModel.setSearchQuery(it)
                        },
                        placeholder = { Text(if (isHindi) "पंडित, पूजा, मंदिर खोजें..." else "Search for Pandit, Puja, Temple...", fontSize = 15.sp) },
                        leadingIcon = {
                            Icon(Icons.Default.Search, contentDescription = null, tint = VedikaOrange, modifier = Modifier.size(22.dp))
                        },
                        trailingIcon = {
                            if (query.isNotEmpty()) {
                                IconButton(onClick = {
                                    query = ""
                                    viewModel.setSearchQuery("")
                                }, modifier = Modifier.size(40.dp)) {
                                    Icon(Icons.Default.Close, contentDescription = "Clear", tint = VedikaTextMuted, modifier = Modifier.size(20.dp))
                                }
                            }
                        },
                        modifier = Modifier
                            .weight(1f)
                            .height(54.dp),
                        shape = RoundedCornerShape(24.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = VedikaOrange,
                            unfocusedBorderColor = VedikaCardBorder,
                            focusedContainerColor = VedikaSurface,
                            unfocusedContainerColor = VedikaSurface
                        ),
                        singleLine = true
                    )
                }
            }
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(horizontal = 20.dp, vertical = 8.dp)
        ) {
            // Filter Pills
            item {
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.padding(bottom = 12.dp)
                ) {
                    items(filters) { filter ->
                        val isSelected = selectedFilter == filter
                        val filterLabel = if (!isHindi) filter else when (filter) {
                            "All" -> "सभी"
                            "Services" -> "पूजा सेवाएं"
                            "Providers" -> "पंडित जी"
                            else -> filter
                        }

                        Surface(
                            onClick = { selectedFilter = filter },
                            shape = RoundedCornerShape(20.dp),
                            color = if (isSelected) VedikaOrange else VedikaSurface,
                            border = BorderStroke(1.dp, if (isSelected) VedikaOrange else VedikaCardBorder),
                            modifier = Modifier.height(38.dp)
                        ) {
                            Box(
                                modifier = Modifier.padding(horizontal = 16.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = filterLabel,
                                    style = MaterialTheme.typography.labelMedium.copy(
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                        color = if (isSelected) Color.White else VedikaTextDark,
                                        fontSize = 14.sp
                                    )
                                )
                            }
                        }
                    }
                }
            }

            // Quick Suggestions Chips
            item {
                Text(
                    text = if (isHindi) "लोकप्रिय खोजें" else "Popular Searches",
                    style = MaterialTheme.typography.labelMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = VedikaTextMuted
                    ),
                    modifier = Modifier.padding(vertical = 4.dp)
                )
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.padding(vertical = 6.dp)
                ) {
                    items(popularSuggestions) { suggestion ->
                        Surface(
                            onClick = {
                                query = suggestion
                                viewModel.setSearchQuery(suggestion)
                            },
                            shape = RoundedCornerShape(14.dp),
                            color = VedikaCreamDark,
                            border = BorderStroke(1.dp, VedikaCardBorder)
                        ) {
                            Text(
                                text = suggestion,
                                fontSize = 12.sp,
                                color = VedikaTextDark,
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(12.dp))
            }

            // Results Section
            val (services, pandits) = searchResults
            val resultCount = services.size + pandits.size

            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (query.isBlank()) {
                            if (isHindi) "उपलब्ध सूची खोजें" else "Browse available catalogue items"
                        } else {
                            if (isHindi) "$resultCount परिणाम मिले" else "$resultCount results"
                        },
                        style = MaterialTheme.typography.labelLarge,
                        color = VedikaTextMedium
                    )
                    if (query.isNotBlank() || selectedFilter != "All") {
                        TextButton(onClick = {
                            query = ""
                            selectedFilter = "All"
                            viewModel.setSearchQuery("")
                        }) { Text(if (isHindi) "रीसेट" else "Reset") }
                    }
                }
            }

            if (query.isBlank()) {
                item {
                    DiscoveryUnavailableState(
                        title = if (isHindi) "पूजा या पंडित जी खोजें" else "Start with a ritual or provider",
                        message = if (isHindi) "ऊपर दिए गए सुझाव पर टैप कर खोजें।" else "Try a popular search above to see matching catalogue results."
                    )
                }
            } else if (resultCount == 0) {
                item {
                    DiscoveryUnavailableState(
                        title = if (isHindi) "कोई परिणाम नहीं मिला" else "No matching results",
                        message = if (isHindi) "अन्य नाम या फ़िल्टर से प्रयास करें।" else "Try a shorter term, another spelling, or reset the active filter.",
                        actionLabel = if (isHindi) "खोज रीसेट करें" else "Reset search",
                        onAction = {
                            query = ""
                            selectedFilter = "All"
                            viewModel.setSearchQuery("")
                        }
                    )
                }
            }

            if (query.isNotBlank() && services.isNotEmpty()) {
                item {
                    Text(
                        text = if (isHindi) "मिलते-जुलते पूजा अनुष्ठान" else "Matching Pujas & Rituals",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = VedikaTextDark
                        ),
                        modifier = Modifier.padding(vertical = 6.dp)
                    )
                }

                items(services) { service ->
                    PujaServiceMockupCard(
                        service = service,
                        isHindi = isHindi,
                        onClick = { viewModel.openServiceDetail(service.id) }
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }

            if (query.isNotBlank() && pandits.isNotEmpty()) {
                item {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = if (isHindi) "उपलब्ध पंडित जी सूची" else "Provider catalogue",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = VedikaTextDark
                        ),
                        modifier = Modifier.padding(vertical = 6.dp)
                    )
                }

                items(pandits) { pandit ->
                    ProviderSearchResultCard(pandit, isHindi)
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
    }
}

@Composable
private fun ProviderSearchResultCard(pandit: PanditProfile, isHindi: Boolean = false) {
    Surface(
        shape = RoundedCornerShape(18.dp),
        color = VedikaSurface,
        border = BorderStroke(1.dp, VedikaCardBorder),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(Icons.Default.Person, contentDescription = null, tint = VedikaOrange, modifier = Modifier.size(28.dp))
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(pandit.name, fontWeight = FontWeight.Bold, color = VedikaTextDark)
                Text(pandit.specializations.joinToString(), style = MaterialTheme.typography.bodySmall, color = VedikaTextMedium)
                Text(
                    text = if (isHindi) "बुकिंग से पूर्व पंडित जी योग्यता एवं उपलब्धता की पुष्टि की जाती है।" else "Availability and credentials are confirmed before booking.",
                    style = MaterialTheme.typography.labelSmall,
                    color = VedikaTextMuted
                )
            }
        }
    }
}
