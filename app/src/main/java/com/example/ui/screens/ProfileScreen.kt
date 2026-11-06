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
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
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
import com.example.ui.components.VedikaBottomBar
import com.example.ui.theme.*
import com.example.viewmodel.Screen
import com.example.viewmodel.VedikaUiState
import com.example.viewmodel.VedikaViewModel

@Composable
fun ProfileScreen(
    viewModel: VedikaViewModel,
    uiState: VedikaUiState,
    modifier: Modifier = Modifier
) {
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
                        .padding(horizontal = 20.dp, vertical = 10.dp)
                        .windowInsetsPadding(WindowInsets.statusBars),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = { viewModel.navigateTo(Screen.MoreMenu) },
                        modifier = Modifier.size(40.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Settings,
                            contentDescription = "Settings",
                            tint = VedikaTextDark,
                            modifier = Modifier.size(24.dp)
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
            contentPadding = PaddingValues(horizontal = 20.dp, vertical = 6.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // 1. Divine Aura Profile Header (Mohit Raj)
            item {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Golden aura halo wrapper
                    Box(
                        contentAlignment = Alignment.Center
                    ) {
                        // Radiant golden sunburst ring
                        Box(
                            modifier = Modifier
                                .size(108.dp)
                                .clip(CircleShape)
                                .background(
                                    brush = Brush.radialGradient(
                                        colors = listOf(
                                            Color(0xFFFFD54F),
                                            Color(0xFFFFB300),
                                            Color(0x00FF8F00)
                                        )
                                    )
                                )
                        )

                        // Devotee Photo with golden border
                        Box(
                            modifier = Modifier
                                .size(92.dp)
                                .clip(CircleShape)
                                .border(3.dp, Color(0xFFFFD54F), CircleShape)
                        ) {
                            AsyncImage(
                                model = ImageRequest.Builder(LocalContext.current)
                                    .data("https://images.unsplash.com/photo-1506794778202-cad84cf45f1d?auto=format&fit=crop&w=400&q=80")
                                    .crossfade(true)
                                    .build(),
                                contentDescription = "Mohit Raj",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.fillMaxSize()
                            )
                        }

                        // Crown badge 👑 at bottom-right
                        Surface(
                            shape = CircleShape,
                            color = Color(0xFFFFD54F),
                            border = BorderStroke(1.5.dp, Color.White),
                            modifier = Modifier
                                .size(28.dp)
                                .align(Alignment.BottomEnd)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Text(text = "👑", fontSize = 13.sp)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Name with edit pencil
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = uiState.userName,
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.Bold,
                                color = VedikaTextDark,
                                fontSize = 22.sp
                            )
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Icon(
                            imageVector = Icons.Outlined.Edit,
                            contentDescription = "Edit Profile",
                            tint = VedikaOrangeDark,
                            modifier = Modifier.size(16.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(2.dp))

                    Text(
                        text = uiState.userEmail,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = VedikaTextMuted,
                            fontSize = 13.sp
                        )
                    )
                }
            }

            // 2. Three Stats Columns Card (12 Bookings, 5 Temples, 3 Favorites)
            item {
                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = VedikaSurface,
                    border = BorderStroke(1.dp, VedikaCardBorder),
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(3.dp, RoundedCornerShape(20.dp), spotColor = Color(0x0C000000))
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 16.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        ProfileStatColumn(count = "12", label = "Bookings")
                        VerticalDivider(
                            color = VedikaDivider,
                            modifier = Modifier.height(36.dp),
                            thickness = 1.dp
                        )
                        ProfileStatColumn(count = "5", label = "Temples")
                        VerticalDivider(
                            color = VedikaDivider,
                            modifier = Modifier.height(36.dp),
                            thickness = 1.dp
                        )
                        ProfileStatColumn(count = "3", label = "Favorites")
                    }
                }
            }

            // 3. Menu List
            item {
                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = VedikaSurface,
                    border = BorderStroke(1.dp, VedikaCardBorder),
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(3.dp, RoundedCornerShape(20.dp), spotColor = Color(0x0C000000))
                ) {
                    Column(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        ProfileMenuItem(
                            icon = Icons.Outlined.ShoppingBag,
                            title = "My Bookings",
                            onClick = { viewModel.setBottomNavTab("Bookings") }
                        )
                        HorizontalDivider(color = VedikaDivider, thickness = 0.8.dp)

                        ProfileMenuItem(
                            icon = Icons.Outlined.TempleHindu,
                            title = "My Darshans",
                            onClick = { viewModel.navigateTo(Screen.TempleDarshan) }
                        )
                        HorizontalDivider(color = VedikaDivider, thickness = 0.8.dp)

                        ProfileMenuItem(
                            icon = Icons.Outlined.BookmarkBorder,
                            title = "Saved Temples",
                            onClick = { viewModel.navigateTo(Screen.TempleDarshan) }
                        )
                        HorizontalDivider(color = VedikaDivider, thickness = 0.8.dp)

                        ProfileMenuItem(
                            icon = Icons.Outlined.Group,
                            title = "My Pandits",
                            onClick = { viewModel.navigateTo(Screen.PanditBooking) }
                        )
                        HorizontalDivider(color = VedikaDivider, thickness = 0.8.dp)

                        ProfileMenuItem(
                            icon = Icons.Outlined.Notifications,
                            title = "Notifications",
                            hasRedDot = uiState.notifications.any { it.isUnread },
                            onClick = { viewModel.navigateTo(Screen.Notifications) }
                        )
                        HorizontalDivider(color = VedikaDivider, thickness = 0.8.dp)

                        ProfileMenuItem(
                            icon = Icons.Outlined.HelpOutline,
                            title = "Help & Support",
                            onClick = { viewModel.navigateTo(Screen.Chat) }
                        )
                        HorizontalDivider(color = VedikaDivider, thickness = 0.8.dp)

                        ProfileMenuItem(
                            icon = Icons.Outlined.Settings,
                            title = "Settings",
                            onClick = { viewModel.navigateTo(Screen.MoreMenu) }
                        )
                    }
                }
            }

            // Bottom logout / version info
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "VEDIKA Version 2.4.0 • Sacred & Secure",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = VedikaTextMuted,
                            fontSize = 11.sp
                        )
                    )
                }
            }
        }
    }
}

@Composable
private fun ProfileStatColumn(
    count: String,
    label: String
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.width(90.dp)
    ) {
        Text(
            text = count,
            style = MaterialTheme.typography.titleLarge.copy(
                fontWeight = FontWeight.ExtraBold,
                color = VedikaTextDark,
                fontSize = 18.sp
            )
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall.copy(
                fontWeight = FontWeight.Medium,
                color = VedikaTextMuted,
                fontSize = 12.sp
            )
        )
    }
}

@Composable
fun ProfileMenuItem(
    icon: ImageVector,
    title: String,
    hasRedDot: Boolean = false,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = VedikaOrangeDark,
                modifier = Modifier.size(22.dp)
            )
            Spacer(modifier = Modifier.width(14.dp))
            Text(
                text = title,
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.SemiBold,
                    color = VedikaTextDark,
                    fontSize = 14.sp
                )
            )
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
            if (hasRedDot) {
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .clip(CircleShape)
                        .background(VedikaRed)
                )
                Spacer(modifier = Modifier.width(8.dp))
            }
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                contentDescription = null,
                tint = Color(0xFFC4B8AB),
                modifier = Modifier.size(16.dp)
            )
        }
    }
}
