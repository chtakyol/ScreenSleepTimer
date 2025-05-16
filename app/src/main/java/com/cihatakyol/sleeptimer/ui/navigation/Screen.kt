package com.cihatakyol.sleeptimer.ui.navigation

sealed class Screen(val route: String) {
    data object OnboardingScreen : Screen(route = "onboarding_screen")
    data object MainScreen : Screen(route = "main_screen")
}

sealed class GraphGroup(val group: String) {
    data object MainGroup : GraphGroup(group = "main")
}