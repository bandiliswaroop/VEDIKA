package com.example.ui.screens

import android.content.Context
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.*
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.ui.theme.*

data class OnboardingStepData(
    val title: String,
    val subtitle: String,
    val description: String,
    val iconEmoji: String,
    val pillLabel: String
)

@Composable
fun OnboardingScreen(
    onFinish: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    val steps = listOf(
        OnboardingStepData(
            title = "Authentic Rituals & Services",
            subtitle = "Discover Sacred Vedic Traditions",
            description = "Explore verified Vedic pujas, havans, and sanskars performed with complete scriptural precision and purity.",
            iconEmoji = "🪔",
            pillLabel = "Authentic Rituals"
        ),
        OnboardingStepData(
            title = "Explore Verified Providers",
            subtitle = "Expert Vedic Acharyas & Priests",
            description = "Connect with verified and experienced pandits, priests, and spiritual experts available in your city.",
            iconEmoji = "🧘‍♂️",
            pillLabel = "Verified Providers"
        ),
        OnboardingStepData(
            title = "Arrange Ceremonies",
            subtitle = "Seamless Booking & Samagri",
            description = "Effortlessly arrange home ceremonies, inter-city pandit travel, and complete authentic puja samagri kits.",
            iconEmoji = "🌸",
            pillLabel = "Sacred Ceremonies"
        ),
        OnboardingStepData(
            title = "Temples & Devotional Events",
            subtitle = "Darshans, Satsangs & Festivals",
            description = "Discover revered temples, book VIP darshan passes, and stay informed on upcoming spiritual events and festivals.",
            iconEmoji = "🛕",
            pillLabel = "Temples & Events"
        ),
        OnboardingStepData(
            title = "Personal Spiritual Practice",
            subtitle = "Mantra Japa, Panchang & Checklists",
            description = "Manage your daily spiritual activities with our digital Mala japa counter, real-time Panchang, and puja prep checklists.",
            iconEmoji = "📿",
            pillLabel = "Spiritual Practice"
        )
    )

    val pagerState = rememberPagerState(pageCount = { steps.size })

    fun saveAndFinish(isGuest: Boolean) {
        val prefs = context.getSharedPreferences("vedika_prefs", Context.MODE_PRIVATE)
        prefs.edit()
            .putBoolean("onboarding_completed", true)
            .putBoolean("is_guest", isGuest)
            .putBoolean("is_logged_in", true)
            .apply()
        onFinish()
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(VedikaCream)
    ) {
        // Upper section: Temple backdrop
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.48f)
        ) {
            TempleDevoteeBackdropArt(modifier = Modifier.fillMaxSize())

            AsyncImage(
                model = ImageRequest.Builder(context)
                    .data("https://images.unsplash.com/photo-1544717305-2782549b5136?auto=format&fit=crop&w=1200&q=80")
                    .crossfade(true)
                    .build(),
                contentDescription = "Sacred Temple backdrop",
                contentScale = ContentScale.Crop,
                alpha = 0.22f,
                modifier = Modifier.fillMaxSize()
            )

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                Color.Transparent,
                                Color(0x33FBF8F2),
                                Color(0xEEFBF8F2),
                                VedikaCream
                            ),
                            startY = 120f
                        )
                    )
            )
        }

        // Lower content section: Pager + CTAs
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp)
                .windowInsetsPadding(WindowInsets.systemBars),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(60.dp))

            // Brand Emblem & Title
            TripleFlameLotusEmblem(modifier = Modifier.size(64.dp))

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "VEDIKA",
                style = MaterialTheme.typography.displayMedium.copy(
                    fontWeight = FontWeight.ExtraBold,
                    fontFamily = FontFamily.Serif,
                    letterSpacing = 4.sp,
                    color = Color(0xFF3E2723),
                    fontSize = 32.sp
                )
            )

            Text(
                text = "परंपरा, आपके द्वार।",
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.SemiBold,
                    color = VedikaGold,
                    fontSize = 14.sp
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Horizontal Pager for the 5 onboarding steps
            HorizontalPager(
                state = pagerState,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) { page ->
                val step = steps[page]
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Step icon badge
                    Surface(
                        shape = RoundedCornerShape(20.dp),
                        color = Color(0xFFF7F2EB),
                        border = BorderStroke(1.dp, Color(0xFFEDE4D6)),
                        modifier = Modifier.size(72.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Text(text = step.iconEmoji, fontSize = 32.sp)
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = step.title,
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold,
                            color = VedikaTextDark,
                            fontSize = 20.sp
                        ),
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = step.subtitle,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontWeight = FontWeight.SemiBold,
                            color = VedikaOrangeDark,
                            fontSize = 14.sp
                        ),
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = step.description,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = VedikaTextMedium,
                            fontSize = 15.sp,
                            lineHeight = 22.sp
                        ),
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(horizontal = 12.dp)
                    )
                }
            }

            // Pager Indicator Dots
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                repeat(steps.size) { index ->
                    val isSelected = pagerState.currentPage == index
                    Box(
                        modifier = Modifier
                            .padding(horizontal = 4.dp)
                            .height(8.dp)
                            .width(if (isSelected) 24.dp else 8.dp)
                            .clip(RoundedCornerShape(4.dp))
                            .background(if (isSelected) VedikaOrange else Color(0xFFD7CCC8))
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Primary CTA: Sign In / Register
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .shadow(8.dp, RoundedCornerShape(26.dp), spotColor = Color(0x44FF6B00))
                    .clip(RoundedCornerShape(26.dp))
                    .background(
                        brush = Brush.horizontalGradient(
                            colors = listOf(Color(0xFFFF6D00), Color(0xFFFF5722), Color(0xFFE64A19))
                        )
                    )
                    .clickable { saveAndFinish(isGuest = false) }
                    .semantics { contentDescription = "Sign in or register to continue" },
                contentAlignment = Alignment.Center
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = if (pagerState.currentPage < steps.size - 1) "Sign In to Continue" else "Get Started (Sign In)",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            fontSize = 16.sp
                        )
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Secondary CTA: Explore as Guest
            OutlinedButton(
                onClick = { saveAndFinish(isGuest = true) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .semantics { contentDescription = "Continue exploring as guest" },
                shape = RoundedCornerShape(24.dp),
                border = BorderStroke(1.5.dp, VedikaOrange),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = VedikaOrangeDark)
            ) {
                Text(
                    text = "Explore as Guest",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp
                    )
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun TripleFlameLotusEmblem(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val w = size.width
            val h = size.height

            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(Color(0xFFFFD54F), Color(0xFFFF9800), Color(0x00FF6D00)),
                    center = Offset(w * 0.5f, h * 0.5f),
                    radius = w * 0.5f
                ),
                radius = w * 0.5f,
                center = Offset(w * 0.5f, h * 0.5f)
            )

            val centerFlame = Path().apply {
                moveTo(w * 0.50f, h * 0.08f)
                cubicTo(w * 0.40f, h * 0.32f, w * 0.38f, h * 0.58f, w * 0.50f, h * 0.88f)
                cubicTo(w * 0.62f, h * 0.58f, w * 0.60f, h * 0.32f, w * 0.50f, h * 0.08f)
                close()
            }
            drawPath(
                path = centerFlame,
                brush = Brush.verticalGradient(
                    listOf(Color(0xFFFFF9C4), Color(0xFFFFB300), Color(0xFFE65100))
                )
            )

            val leftFlame = Path().apply {
                moveTo(w * 0.50f, h * 0.88f)
                cubicTo(w * 0.28f, h * 0.72f, w * 0.16f, h * 0.48f, w * 0.30f, h * 0.26f)
                cubicTo(w * 0.26f, h * 0.50f, w * 0.40f, h * 0.70f, w * 0.50f, h * 0.88f)
                close()
            }
            drawPath(
                path = leftFlame,
                brush = Brush.verticalGradient(
                    listOf(Color(0xFFFFCA28), Color(0xFFFF7043), Color(0xFFD84315))
                )
            )

            val rightFlame = Path().apply {
                moveTo(w * 0.50f, h * 0.88f)
                cubicTo(w * 0.72f, h * 0.72f, w * 0.84f, h * 0.48f, w * 0.70f, h * 0.26f)
                cubicTo(w * 0.74f, h * 0.50f, w * 0.60f, h * 0.70f, w * 0.50f, h * 0.88f)
                close()
            }
            drawPath(
                path = rightFlame,
                brush = Brush.verticalGradient(
                    listOf(Color(0xFFFFCA28), Color(0xFFFF7043), Color(0xFFD84315))
                )
            )

            drawCircle(
                color = Color(0xFFFFFDE7),
                radius = w * 0.09f,
                center = Offset(w * 0.50f, h * 0.58f)
            )
        }
    }
}

@Composable
fun TempleDevoteeBackdropArt(
    modifier: Modifier = Modifier
) {
    Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height

        drawRect(
            brush = Brush.verticalGradient(
                colors = listOf(
                    Color(0xFFFFCC80),
                    Color(0xFFFFE0B2),
                    Color(0xFFFFF3E0),
                    Color(0xFFFBF8F2)
                )
            ),
            size = Size(w, h)
        )

        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(Color(0xFFFFFDE7), Color(0xFFFFE082), Color(0x00FFA726)),
                center = Offset(w * 0.5f, h * 0.35f),
                radius = w * 0.48f
            ),
            radius = w * 0.48f,
            center = Offset(w * 0.5f, h * 0.35f)
        )

        val centerSpire = Path().apply {
            moveTo(w * 0.50f, h * 0.08f)
            lineTo(w * 0.56f, h * 0.28f)
            lineTo(w * 0.62f, h * 0.44f)
            lineTo(w * 0.68f, h * 0.56f)
            lineTo(w * 0.32f, h * 0.56f)
            lineTo(w * 0.38f, h * 0.44f)
            lineTo(w * 0.44f, h * 0.28f)
            close()
        }
        drawPath(
            path = centerSpire,
            brush = Brush.verticalGradient(
                colors = listOf(Color(0xFFD7A76E), Color(0xFFBD8650), Color(0xFF8D5B2F))
            )
        )

        drawLine(
            color = Color(0xFFBF360C),
            start = Offset(w * 0.50f, h * 0.04f),
            end = Offset(w * 0.50f, h * 0.09f),
            strokeWidth = 3f
        )
        val flag = Path().apply {
            moveTo(w * 0.50f, h * 0.04f)
            lineTo(w * 0.58f, h * 0.06f)
            lineTo(w * 0.50f, h * 0.08f)
            close()
        }
        drawPath(flag, color = Color(0xFFFF6D00))

        val leftSpire = Path().apply {
            moveTo(w * 0.26f, h * 0.20f)
            lineTo(w * 0.34f, h * 0.40f)
            lineTo(w * 0.38f, h * 0.56f)
            lineTo(w * 0.16f, h * 0.56f)
            lineTo(w * 0.20f, h * 0.38f)
            close()
        }
        drawPath(
            path = leftSpire,
            brush = Brush.verticalGradient(
                colors = listOf(Color(0xFFD19F68), Color(0xFF9E6533))
            )
        )

        val rightSpire = Path().apply {
            moveTo(w * 0.74f, h * 0.20f)
            lineTo(w * 0.84f, h * 0.38f)
            lineTo(w * 0.84f, h * 0.56f)
            lineTo(w * 0.62f, h * 0.56f)
            lineTo(w * 0.66f, h * 0.40f)
            close()
        }
        drawPath(
            path = rightSpire,
            brush = Brush.verticalGradient(
                colors = listOf(Color(0xFFD19F68), Color(0xFF9E6533))
            )
        )

        drawRect(
            brush = Brush.verticalGradient(
                colors = listOf(Color(0xFFBD8650), Color(0xFF8D5B2F))
            ),
            topLeft = Offset(w * 0.14f, h * 0.56f),
            size = Size(w * 0.72f, h * 0.10f)
        )

        for (i in 0..3) {
            val stepY = h * (0.66f + i * 0.035f)
            val stepW = w * (0.80f + i * 0.06f)
            val stepX = (w - stepW) / 2
            drawRect(
                color = if (i % 2 == 0) Color(0xFFDEB887) else Color(0xFFD2A374),
                topLeft = Offset(stepX, stepY),
                size = Size(stepW, h * 0.035f)
            )
        }

        drawRect(
            brush = Brush.verticalGradient(
                colors = listOf(Color(0x88FFE082), Color(0x33FFB74D), Color(0x00FBF8F2))
            ),
            topLeft = Offset(0f, h * 0.80f),
            size = Size(w, h * 0.20f)
        )
    }
}
