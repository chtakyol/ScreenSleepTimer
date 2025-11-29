package com.ToolCompany.screentimer.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.ToolCompany.screentimer.ui.screens.mainscreen.MainScreen
import com.ToolCompany.screentimer.ui.screens.onboarding.OnboardingScreen
import com.ToolCompany.screentimer.ui.screens.onboarding.OnboardingViewModel

@Composable
fun SleepTimerNavGraph(
    onSplashScreenFinished: () -> Unit = {}
) {
    val navController = rememberNavController()
    val actions = remember(navController) { MainActions(navController) }
    val onboardingViewModel: OnboardingViewModel = hiltViewModel()
    val onboardingState by onboardingViewModel.state.collectAsState()

    if (onboardingState.isLoading) {
        return
    }

    onSplashScreenFinished()

    NavHost(
        navController = navController,
        startDestination = if (onboardingState.isCompleted) Screen.MainScreen.route else Screen.OnboardingScreen.route
    ) {
        composable(Screen.OnboardingScreen.route) {
            OnboardingScreen(
                navigateToMainScreen = {
                    actions.navigateToMainScreen()
                }
            )
        }

        composable(Screen.MainScreen.route) {
            MainScreen()
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
        navHostController.navigate(Screen.MainScreen.route) {
            popUpTo(Screen.OnboardingScreen.route) { inclusive = true }
        }
    }
}