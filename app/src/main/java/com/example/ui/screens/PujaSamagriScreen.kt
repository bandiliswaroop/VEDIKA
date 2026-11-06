package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.VedikaData
import com.example.model.SamagriProduct
import com.example.ui.components.VedikaBottomBar
import com.example.ui.components.VedikaChildTopBar
import com.example.ui.components.VedikaSamagriImage
import com.example.ui.theme.*
import com.example.viewmodel.Screen
import com.example.viewmodel.VedikaViewModel

@Composable
fun PujaSamagriScreen(
    viewModel: VedikaViewModel,
    uiState: com.example.viewmodel.VedikaUiState,
    modifier: Modifier = Modifier
) {
    val isHindi = uiState.isHindi
    val categories = listOf("All", "Puja Kits", "Havan Samagri", "Idols", "Diyas")
    var selectedCategory by remember { mutableStateOf("All") }
    var searchQuery by remember { mutableStateOf("") }

    val filteredProducts = remember(selectedCategory, searchQuery) {
        VedikaData.samagriProducts.filter { product ->
            val matchesCategory = if (selectedCategory == "All") true else product.category == selectedCategory
            val matchesQuery = if (searchQuery.isBlank()) true
            else product.name.contains(searchQuery, ignoreCase = true) || product.hindiName.contains(searchQuery)
            matchesCategory && matchesQuery
        }
    }

    val totalCartCount = uiState.cartItems.values.sum()

    Scaffold(
        containerColor = VedikaCream,
        topBar = {
            VedikaChildTopBar(
                title = if (isHindi) "पूजा सामग्री एवं किट" else "Puja Samagri",
                onBackClick = { viewModel.navigateTo(Screen.Home) },
                actions = {
                    Box(modifier = Modifier.padding(end = 8.dp), contentAlignment = Alignment.TopEnd) {
                        IconButton(onClick = { }, modifier = Modifier.size(48.dp)) {
                            Icon(Icons.Default.ShoppingBag, contentDescription = "Cart", tint = VedikaOrange, modifier = Modifier.size(24.dp))
                        }
                        if (totalCartCount > 0) {
                            Surface(
                                shape = RoundedCornerShape(10.dp),
                                color = VedikaOrange,
                                modifier = Modifier.size(20.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Text(
                                        text = "$totalCartCount",
                                        color = Color.White,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                    }
                }
            )
        },
        bottomBar = {
            VedikaBottomBar(
                currentTab = uiState.activeTab,
                onTabSelected = { viewModel.setBottomNavTab(it) }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Search Input (54dp height)
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = { Text(if (isHindi) "सामग्री, किट, दीया खोजें..." else "Search for samagri, kits, diyas...", fontSize = 15.sp) },
                leadingIcon = {
                    Icon(Icons.Default.Search, contentDescription = null, tint = VedikaOrange, modifier = Modifier.size(22.dp))
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 6.dp),
                shape = RoundedCornerShape(18.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = VedikaOrange,
                    unfocusedBorderColor = VedikaCardBorder,
                    focusedContainerColor = VedikaSurface,
                    unfocusedContainerColor = VedikaSurface
                ),
                singleLine = true
            )

            // Category filter pills
            LazyRow(
                contentPadding = PaddingValues(horizontal = 20.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.padding(vertical = 10.dp)
            ) {
                items(categories) { category ->
                    val isSelected = selectedCategory == category
                    val displayCat = if (!isHindi) category else when (category) {
                        "All" -> "सभी"
                        "Puja Kits" -> "पूजा किट"
                        "Havan Samagri" -> "हवन सामग्री"
                        "Idols" -> "प्रतिमाएं"
                        "Diyas" -> "दीया"
                        else -> category
                    }

                    Surface(
                        onClick = { selectedCategory = category },
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
                                text = displayCat,
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

            // Products 2-column Grid
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(horizontal = 18.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(14.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(filteredProducts) { product ->
                    SamagriGridCard(
                        product = product,
                        isHindi = isHindi,
                        onAdd = { viewModel.addToCart(product.id) }
                    )
                }
            }
        }
    }
}

@Composable
fun SamagriGridCard(
    product: SamagriProduct,
    isHindi: Boolean = false,
    onAdd: () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(18.dp),
        color = VedikaSurface,
        border = BorderStroke(1.dp, VedikaCardBorder),
        modifier = Modifier
            .fillMaxWidth()
            .shadow(2.dp, RoundedCornerShape(18.dp), spotColor = Color(0x0A000000))
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(130.dp)
                    .clip(RoundedCornerShape(14.dp))
            ) {
                VedikaSamagriImage(
                    product = product,
                    modifier = Modifier.fillMaxSize(),
                    cornerRadius = 14.dp
                )

                IconButton(
                    onClick = { },
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(6.dp)
                        .size(32.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.FavoriteBorder,
                        contentDescription = "Favorite",
                        tint = VedikaTextDark,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = if (isHindi && product.hindiName.isNotBlank()) product.hindiName else product.name,
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = VedikaTextDark,
                    fontSize = 16.sp
                ),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Text(
                text = product.hindiName,
                style = MaterialTheme.typography.labelSmall.copy(
                    color = VedikaGold,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold
                ),
                maxLines = 1
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "₹ ${product.price}",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.ExtraBold,
                        color = VedikaOrangeDark,
                        fontSize = 18.sp
                    )
                )

                Button(
                    onClick = onAdd,
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = VedikaOrange),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 6.dp),
                    modifier = Modifier.height(38.dp)
                ) {
                    Text(if (isHindi) "जोड़ें" else "Add", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                }
            }
        }
    }
}
