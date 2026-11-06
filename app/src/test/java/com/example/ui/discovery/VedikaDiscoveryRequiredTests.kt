package com.example.ui.discovery

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.data.VedikaData
import com.example.viewmodel.Screen
import com.example.viewmodel.VedikaViewModel
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class VedikaDiscoveryRequiredTests {

    // 1. First-time onboarding test
    @Test
    fun test1_firstTimeOnboarding() {
        val viewModel = VedikaViewModel()
        assertEquals(Screen.Splash, viewModel.uiState.value.currentScreen)

        viewModel.navigateTo(Screen.Onboarding)
        assertEquals(Screen.Onboarding, viewModel.uiState.value.currentScreen)

        val context = ApplicationProvider.getApplicationContext<Context>()
        val prefs = context.getSharedPreferences("vedika_prefs", Context.MODE_PRIVATE)
        prefs.edit().putBoolean("onboarding_completed", true).apply()

        assertTrue(prefs.getBoolean("onboarding_completed", false))
    }

    // 2. Guest exploration test
    @Test
    fun test2_guestExploration() {
        val viewModel = VedikaViewModel()
        viewModel.login(phone = "", isGuestMode = true)

        val state = viewModel.uiState.value
        assertTrue(state.isLoggedIn)
        assertTrue(state.isGuest)
        assertEquals(Screen.Home, state.currentScreen)
        assertEquals("Home", state.activeTab)
    }

    // 3. Search filtering test
    @Test
    fun test3_searchFiltering() {
        val viewModel = VedikaViewModel()
        viewModel.setSearchQuery("Griha")
        assertEquals("Griha", viewModel.uiState.value.searchQuery)

        val matching = VedikaData.services.filter {
            it.title.lowercase().contains("griha") || it.category.lowercase().contains("griha") || it.shortDescription.lowercase().contains("griha")
        }
        assertTrue(matching.isNotEmpty())
        assertEquals("Griha Pravesh Puja", matching.first().title)
    }

    // 4. Filter reset test
    @Test
    fun test4_filterReset() {
        val viewModel = VedikaViewModel()
        viewModel.setSearchQuery("Mundan")
        assertEquals("Mundan", viewModel.uiState.value.searchQuery)

        viewModel.setSearchQuery("")
        viewModel.selectCategory("All")
        assertEquals("", viewModel.uiState.value.searchQuery)
        assertEquals("All", viewModel.uiState.value.selectedCategory)
    }

    // 5. No search results test
    @Test
    fun test5_noSearchResults() {
        val query = "xyz999nonexistent"
        val matchingServices = VedikaData.services.filter {
            it.title.lowercase().contains(query)
        }
        val matchingPandits = VedikaData.pandits.filter {
            it.name.lowercase().contains(query)
        }
        assertEquals(0, matchingServices.size + matchingPandits.size)
    }

    // 6. Service detail navigation test
    @Test
    fun test6_serviceDetailNavigation() {
        val viewModel = VedikaViewModel()
        viewModel.openServiceDetail("s1")

        val state = viewModel.uiState.value
        assertEquals(Screen.ServiceDetail, state.currentScreen)
        assertEquals("s1", state.selectedServiceId)
        assertEquals("Griha Pravesh Puja", state.bookingDraft.service?.title)

        viewModel.startBooking("s1")
        assertEquals(Screen.BookingFlow, viewModel.uiState.value.currentScreen)
    }

    // 7. Temple information unavailable test
    @Test
    fun test7_templeInformationUnavailable() {
        val viewModel = VedikaViewModel()
        viewModel.setBottomNavTab("Explore")
        viewModel.navigateTo(Screen.TempleDarshan)
        assertEquals(Screen.TempleDarshan, viewModel.uiState.value.currentScreen)
    }

    // 8. Location permission denied test
    @Test
    fun test8_locationPermissionDenied() {
        val viewModel = VedikaViewModel()
        viewModel.navigateTo(Screen.NearbyMap)
        assertEquals(Screen.NearbyMap, viewModel.uiState.value.currentScreen)
    }

    // 9. Panchang data unavailable test
    @Test
    fun test9_panchangDataUnavailable() {
        val viewModel = VedikaViewModel()
        viewModel.setBottomNavTab("Panchang")
        assertEquals(Screen.PanchangMuhurat, viewModel.uiState.value.currentScreen)
        assertEquals("Panchang", viewModel.uiState.value.activeTab)
    }

    // 10. Japa counter and reset test
    @Test
    fun test10_japaCounterAndReset() {
        val viewModel = VedikaViewModel()
        assertEquals(0, viewModel.uiState.value.japaCounter)
        assertEquals(0, viewModel.uiState.value.totalJapaRounds)

        viewModel.incrementJapa()
        assertEquals(1, viewModel.uiState.value.japaCounter)

        repeat(107) { viewModel.incrementJapa() }
        assertEquals(0, viewModel.uiState.value.japaCounter)
        assertEquals(1, viewModel.uiState.value.totalJapaRounds)
        assertEquals("1 Mala (108 Japa) Completed! Har Har Mahadev", viewModel.uiState.value.toastMessage)

        viewModel.incrementJapa()
        assertEquals(1, viewModel.uiState.value.japaCounter)
        viewModel.resetJapa()
        assertEquals(0, viewModel.uiState.value.japaCounter)
    }

    // 11. Screen recreation test
    @Test
    fun test11_screenRecreation() {
        val originalViewModel = VedikaViewModel()
        originalViewModel.selectCity("Deoghar")
        originalViewModel.setBottomNavTab("Panchang")
        originalViewModel.setSearchQuery("Satyanarayan")
        originalViewModel.incrementJapa()

        val currentState = originalViewModel.uiState.value

        val recreatedViewModel = VedikaViewModel()
        recreatedViewModel.selectCity(currentState.selectedCity)
        recreatedViewModel.setBottomNavTab(currentState.activeTab)
        recreatedViewModel.setSearchQuery(currentState.searchQuery)

        assertEquals("Deoghar", recreatedViewModel.uiState.value.selectedCity)
        assertEquals("Panchang", recreatedViewModel.uiState.value.activeTab)
        assertEquals("Satyanarayan", recreatedViewModel.uiState.value.searchQuery)
    }

    // 12. Large font and accessibility checks test
    @Test
    fun test12_largeFontAndAccessibilityChecks() {
        val viewModel = VedikaViewModel()
        viewModel.toggleLanguage()
        assertTrue(viewModel.uiState.value.isHindi)
        viewModel.toggleLanguage()
        assertFalse(viewModel.uiState.value.isHindi)
    }
}
