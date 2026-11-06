package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.data.VedikaData
import com.example.ui.components.TempleVisualArt
import com.example.ui.components.VedikaBottomBar
import com.example.ui.components.discovery.DiscoveryUnavailableState
import com.example.ui.theme.*
import com.example.viewmodel.Screen
import com.example.viewmodel.VedikaUiState
import com.example.viewmodel.VedikaViewModel

@Composable
fun TempleDarshanScreen(
    viewModel: VedikaViewModel,
    uiState: VedikaUiState,
    modifier: Modifier = Modifier
) {
    val isHindi = uiState.isHindi
    var selectedDarshanType by remember { mutableStateOf("General") }
    var isFavorite by remember { mutableStateOf(false) }

    Scaffold(
        containerColor = VedikaCream,
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
            // 1. Hero Image with Floating Back and Favorite Buttons
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(280.dp)
                ) {
                    TempleVisualArt(
                        templeName = "Baba Baidyanath Dham",
                        modifier = Modifier.fillMaxSize(),
                        cornerRadius = 0.dp
                    )

                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data("https://images.unsplash.com/photo-1567157577867-05ccb1388e66?auto=format&fit=crop&w=1200&q=80")
                            .crossfade(true)
                            .build(),
                        contentDescription = "Baidyanath Dham",
                        contentScale = ContentScale.Crop,
                        alpha = 0.6f,
                        modifier = Modifier.fillMaxSize()
                    )

                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                brush = Brush.verticalGradient(
                                    colors = listOf(
                                        Color(0x40000000),
                                        Color.Transparent,
                                        Color(0x70000000)
                                    )
                                )
                            )
                    )

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 12.dp)
                            .windowInsetsPadding(WindowInsets.statusBars),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            onClick = { viewModel.setBottomNavTab("Home") },
                            shape = CircleShape,
                            color = Color.White,
                            modifier = Modifier.size(40.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                    contentDescription = "Back",
                                    tint = VedikaTextDark,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }

                        Surface(
                            onClick = { isFavorite = !isFavorite },
                            shape = CircleShape,
                            color = Color.White,
                            modifier = Modifier.size(40.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = if (isFavorite) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                                    contentDescription = "Favorite",
                                    tint = if (isFavorite) VedikaRed else VedikaTextDark,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                    }
                }
            }

            // 2. Temple Title, Ratings, Location
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                ) {
                    Text(
                        text = if (isHindi) "बाबा बैद्यनाथ धाम, देवघर" else "Baidyanath Dham",
                        style = MaterialTheme.typography.headlineMedium.copy(
                            fontWeight = FontWeight.ExtraBold,
                            color = VedikaTextDark,
                            fontSize = 24.sp
                        )
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Star,
                            contentDescription = "Rating",
                            tint = VedikaStarAmber,
                            modifier = Modifier.size(16.dp)
                        )
                        Text(
                            text = if (isHindi) "समुदाय समीक्षाएं उपलब्ध नहीं हैं" else "Community reviews unavailable",
                            color = VedikaTextMuted,
                            fontSize = 13.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Place,
                            contentDescription = "Location",
                            tint = VedikaOrangeDark,
                            modifier = Modifier.size(15.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = if (isHindi) "देवघर, झारखंड" else "Deoghar, Jharkhand",
                            style = MaterialTheme.typography.bodyMedium.copy(
                                color = VedikaTextMedium,
                                fontSize = 13.sp
                            )
                        )
                    }
                }
            }

            // 3. Quick Action Chips: [History], [Gallery], [Timings]
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    TempleInfoChip(icon = "📖", label = if (isHindi) "इतिहास" else "History", modifier = Modifier.weight(1f))
                    TempleInfoChip(icon = "🖼️", label = if (isHindi) "गैलरी" else "Gallery", modifier = Modifier.weight(1f))
                    TempleInfoChip(icon = "⏰", label = if (isHindi) "समय" else "Timings", modifier = Modifier.weight(1f))
                }
            }

            // 4. "About" Section
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                ) {
                    Text(
                        text = if (isHindi) "मंदिर परिचय" else "About",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = VedikaTextDark,
                            fontSize = 16.sp
                        )
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = if (isHindi) "भगवान शिव के 12 ज्योतिर्लिंगों में से एक, बाबा बैद्यनाथ धाम एक पवित्र तीर्थ स्थल है जहाँ लाखों श्रद्धालु प्रतिवर्ष आते हैं।" else "One of the 12 Jyotirlingas of Lord Shiva, Baidyanath Dham is a sacred destination visited by millions of devotees every year.",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = VedikaTextMedium,
                            fontSize = 13.sp,
                            lineHeight = 19.sp
                        )
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = if (isHindi) "और पढ़ें >" else "Read More >",
                        style = MaterialTheme.typography.labelMedium.copy(
                            color = VedikaOrangeDark,
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp
                        ),
                        modifier = Modifier.clickable { }
                    )
                }
            }

            // Darshan availability unavailable state
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text(
                        text = if (isHindi) "दर्शन उपलब्धता" else "Darshan availability",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = VedikaTextDark,
                            fontSize = 16.sp
                        )
                    )

                    DiscoveryUnavailableState(
                        title = if (isHindi) "आधिकारिक दर्शन विवरण उपलब्ध नहीं है" else "Official darshan details are unavailable",
                        message = if (isHindi) "मंदिर का आधिकारिक स्रोत कनेक्ट होने के बाद ही समय और पास शुल्क प्रदर्शित किए जाएंगे।" else "Times, passes, and fees are shown only after an official temple source is connected."
                    )
                }
            }

            // 7. "Nearby Services" Section
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = if (isHindi) "आस-पास सुविधाएं" else "Nearby Services",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = VedikaTextDark,
                            fontSize = 16.sp
                        )
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        NearbyServiceIconItem(icon = Icons.Outlined.Hotel, label = if (isHindi) "होटल" else "Hotel")
                        NearbyServiceIconItem(icon = Icons.Outlined.Restaurant, label = if (isHindi) "भोजन" else "Food")
                        NearbyServiceIconItem(icon = Icons.Outlined.DirectionsCar, label = if (isHindi) "परिवहन" else "Transport")
                        NearbyServiceIconItem(icon = Icons.Outlined.Tour, label = if (isHindi) "गाइड" else "Guide")
                    }
                }
            }
        }
    }
}

@Composable
private fun TempleInfoChip(
    icon: String,
    label: String,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = Color(0xFFF7F2EB),
        border = BorderStroke(1.dp, Color(0xFFEBE3D5)),
        modifier = modifier.height(38.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = icon, fontSize = 13.sp)
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = label,
                style = MaterialTheme.typography.labelMedium.copy(
                    fontWeight = FontWeight.SemiBold,
                    color = VedikaTextDark,
                    fontSize = 12.sp
                )
            )
        }
    }
}

@Composable
private fun NearbyServiceIconItem(
    icon: ImageVector,
    label: String
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.width(68.dp)
    ) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = Color(0xFFF7F2EB),
            border = BorderStroke(1.dp, Color(0xFFEBE3D5)),
            modifier = Modifier.size(52.dp)
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
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall.copy(
                fontWeight = FontWeight.Medium,
                color = VedikaTextDark,
                fontSize = 11.sp
            )
        )
    }
}
