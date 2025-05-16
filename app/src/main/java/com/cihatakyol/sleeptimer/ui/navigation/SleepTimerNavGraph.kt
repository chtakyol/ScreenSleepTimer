package com.cihatakyol.sleeptimer.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.cihatakyol.sleeptimer.ui.screens.mainscreen.MainScreen
import com.cihatakyol.sleeptimer.ui.screens.onboarding.OnboardingScreen
import com.cihatakyol.sleeptimer.utils.AdManager

@Composable
fun SleepTimerNavGraph() {
    val navController = rememberNavController()
    val actions = remember(navController) { MainActions(navController) }
    val startDestination = Screen.OnboardingScreen.route // add logic for if the user completed onboarding

    val adManager = AdManager(LocalContext.current)

    NavHost(navController = navController, startDestination = startDestination) {
        composable(Screen.OnboardingScreen.route) {
            OnboardingScreen(
                onOnboardingComplete = {
                    actions.navigateToMainScreen()
                }
            )
        }

        composable(Screen.MainScreen.route) {
            MainScreen(adManager = adManager)
        }
    }
}

class MainActions(private val navHostController: NavHostController) {
    val popBackStack: () -> Unit = {
        navHostController.popBackStack()
    }

    val navigateToOnboardingScreen: () -> Unit = {
        navHostController.navigate(Screen.OnboardingScreen.route)
    }

    val navigateToMainScreen: () -> Unit = {
        navHostController.navigate(Screen.MainScreen.route)
    }
}