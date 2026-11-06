package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.VedikaData
import com.example.ui.components.ServiceVisualArt
import com.example.ui.theme.*
import com.example.viewmodel.Screen
import com.example.viewmodel.VedikaUiState
import com.example.viewmodel.VedikaViewModel

@Composable
fun BookingFlowScreen(
    viewModel: VedikaViewModel,
    uiState: VedikaUiState,
    modifier: Modifier = Modifier
) {
    val draft = uiState.bookingDraft
    val service = draft.service ?: VedikaData.services.find { it.title.contains("Satyanarayan") } ?: VedikaData.services.first()

    var selectedDay by remember { mutableIntStateOf(18) }
    var selectedTimeSlot by remember { mutableStateOf("8:00 AM") }

    val timeSlots = listOf(
        "6:00 AM", "8:00 AM", "10:00 AM",
        "12:00 PM", "4:00 PM", "6:00 PM"
    )

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
                        onClick = { viewModel.navigateTo(Screen.ServiceDetail) },
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
                        text = "Book Puja",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold,
                            color = VedikaTextDark,
                            fontSize = 20.sp
                        ),
                        modifier = Modifier
                            .weight(1f)
                            .padding(horizontal = 8.dp)
                    )
                }
            }
        },
        bottomBar = {
            Surface(
                color = VedikaSurface,
                shadowElevation = 16.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 14.dp)
                        .windowInsetsPadding(WindowInsets.navigationBars)
                ) {
                    // Pill CTA Button "Next →"
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp)
                            .shadow(8.dp, RoundedCornerShape(26.dp), spotColor = Color(0x35FF6B00))
                            .clip(RoundedCornerShape(26.dp))
                            .background(
                                brush = Brush.horizontalGradient(
                                    colors = listOf(Color(0xFFFF6D00), Color(0xFFFF4500))
                                )
                            )
                            .clickable {
                                viewModel.selectBookingDate(
                                    date = selectedDay.toString(),
                                    dayName = "Sun"
                                )
                                viewModel.selectBookingTime(selectedTimeSlot)
                                viewModel.proceedToPayment()
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "Next",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White,
                                    fontSize = 16.sp
                                )
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                                contentDescription = "Next",
                                tint = Color.White,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                }
            }
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(horizontal = 20.dp, vertical = 10.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // 1. Four-Step Stepper: [Puja (active)] - [Details] - [Pandit] - [Payment]
            item {
                BookingStepperHeader(currentStep = 1)
            }

            // 2. Selected Puja Summary Card
            item {
                Surface(
                    shape = RoundedCornerShape(18.dp),
                    color = VedikaSurface,
                    border = BorderStroke(1.dp, VedikaCardBorder),
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(3.dp, RoundedCornerShape(18.dp), spotColor = Color(0x0C000000))
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(64.dp)
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
                                text = service.shortDescription,
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = VedikaTextMedium,
                                    fontSize = 12.sp
                                ),
                                maxLines = 1
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
                    }
                }
            }

            // 3. Calendar "Select Date"
            item {
                Column(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Select Date",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = VedikaTextDark,
                            fontSize = 16.sp
                        )
                    )

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
                                .padding(16.dp)
                        ) {
                            // Month Header with arrows: < October 2026 >
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                IconButton(onClick = { }, modifier = Modifier.size(32.dp)) {
                                    Icon(
                                        imageVector = Icons.Default.ChevronLeft,
                                        contentDescription = "Previous Month",
                                        tint = VedikaTextDark
                                    )
                                }

                                Text(
                                    text = "October 2026",
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = VedikaTextDark,
                                        fontSize = 15.sp
                                    )
                                )

                                IconButton(onClick = { }, modifier = Modifier.size(32.dp)) {
                                    Icon(
                                        imageVector = Icons.Default.ChevronRight,
                                        contentDescription = "Next Month",
                                        tint = VedikaTextDark
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            // Days header: Sun Mon Tue Wed Thu Fri Sat
                            val dayNames = listOf("S", "M", "T", "W", "T", "F", "S")
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceAround
                            ) {
                                dayNames.forEach { day ->
                                    Text(
                                        text = day,
                                        style = MaterialTheme.typography.labelMedium.copy(
                                            color = VedikaTextMuted,
                                            fontWeight = FontWeight.SemiBold,
                                            fontSize = 12.sp
                                        ),
                                        modifier = Modifier.width(36.dp),
                                        textAlign = TextAlign.Center
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            // October 2026 Calendar Grid (1st begins on Thursday: 4 leading blanks)
                            // Rows of 7 days
                            val monthGrid = listOf(
                                listOf(null, null, null, null, 1, 2, 3),
                                listOf(4, 5, 6, 7, 8, 9, 10),
                                listOf(11, 12, 13, 14, 15, 16, 17),
                                listOf(18, 19, 20, 21, 22, 23, 24),
                                listOf(25, 26, 27, 28, 29, 30, 31)
                            )

                            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                monthGrid.forEach { week ->
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceAround,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        week.forEach { dayNumber ->
                                            if (dayNumber != null) {
                                                val isSelected = dayNumber == selectedDay
                                                Box(
                                                    modifier = Modifier
                                                        .size(36.dp)
                                                        .clip(CircleShape)
                                                        .background(
                                                            if (isSelected) VedikaOrange else Color.Transparent
                                                        )
                                                        .clickable { selectedDay = dayNumber },
                                                    contentAlignment = Alignment.Center
                                                ) {
                                                    Text(
                                                        text = dayNumber.toString(),
                                                        style = MaterialTheme.typography.bodyMedium.copy(
                                                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                                            color = if (isSelected) Color.White else VedikaTextDark,
                                                            fontSize = 13.sp
                                                        )
                                                    )
                                                }
                                            } else {
                                                Spacer(modifier = Modifier.size(36.dp))
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // 4. "Preferred Time" Section
            item {
                Column(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Preferred Time",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = VedikaTextDark,
                            fontSize = 16.sp
                        )
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    // 2 rows of 3 time pills
                    val row1 = timeSlots.take(3)
                    val row2 = timeSlots.drop(3)

                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            row1.forEach { time ->
                                TimeSlotPill(
                                    time = time,
                                    isSelected = time == selectedTimeSlot,
                                    onClick = { selectedTimeSlot = time },
                                    modifier = Modifier.weight(1f)
                                )
                            }
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            row2.forEach { time ->
                                TimeSlotPill(
                                    time = time,
                                    isSelected = time == selectedTimeSlot,
                                    onClick = { selectedTimeSlot = time },
                                    modifier = Modifier.weight(1f)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun BookingStepperHeader(currentStep: Int) {
    val steps = listOf("Puja", "Details", "Pandit", "Payment")

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        steps.forEachIndexed { index, stepName ->
            val stepNumber = index + 1
            val isActive = stepNumber == currentStep
            val isDone = stepNumber < currentStep

            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape)
                        .background(
                            when {
                                isActive -> VedikaOrange
                                isDone -> VedikaOrangeDark
                                else -> Color(0xFFEBE3D5)
                            }
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    if (isActive || isDone) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(16.dp)
                        )
                    } else {
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .clip(CircleShape)
                                .background(Color(0xFFB0A59A))
                        )
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = stepName,
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontWeight = if (isActive) FontWeight.Bold else FontWeight.Medium,
                        color = if (isActive) VedikaOrangeDark else VedikaTextMuted,
                        fontSize = 11.sp
                    )
                )
            }

            if (index < steps.lastIndex) {
                HorizontalDivider(
                    color = if (stepNumber < currentStep) VedikaOrange else Color(0xFFE0D8CB),
                    thickness = 1.2.dp,
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 6.dp)
                        .offset(y = (-8).dp)
                )
            }
        }
    }
}

@Composable
private fun TimeSlotPill(
    time: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(14.dp),
        color = if (isSelected) VedikaOrange else Color(0xFFF7F2EB),
        border = BorderStroke(
            1.dp,
            if (isSelected) VedikaOrange else Color(0xFFEBE3D5)
        ),
        modifier = modifier.height(40.dp)
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = time,
                style = MaterialTheme.typography.labelMedium.copy(
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                    color = if (isSelected) Color.White else VedikaTextDark,
                    fontSize = 12.sp
                )
            )
        }
    }
}
