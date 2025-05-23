package com.cihatakyol.sleeptimer.ui.screens.onboarding

import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cihatakyol.sleeptimer.R

@Composable
fun OnboardingPage3(
    isNotificationPermissionGiven: Boolean = false,
    onRequestNotificationPermission: () -> Unit,
    onContinueButtonClick: () -> Unit
) {
    if (!isNotificationPermissionGiven) {
        OnboardingPage(
            icon = {
                Icon(
                    painter = painterResource(id = R.drawable.ic_notification),
                    contentDescription = "Notification Icon",
                    modifier = Modifier.size(120.dp),
                    tint = MaterialTheme.colorScheme.primary
                )
            },
            title = "Notification Permission",
            message = "Sleep Timer needs notification permission to show you the countdown timer and allow you to extend the time if needed.",
            buttonText = "Enable Notifications",
            onButtonClick = onRequestNotificationPermission
        )
    } else {
        OnboardingPage3NotificationPermissionAlreadyGiven(onContinueButtonClick = onContinueButtonClick)
    }
}

@Composable
private fun OnboardingPage3NotificationPermissionAlreadyGiven(
    onContinueButtonClick: () -> Unit
) {
    OnboardingPage(
        icon = {
            Icon(
                painter = painterResource(id = R.drawable.ic_launcher_foreground),
                contentDescription = "Device Admin Icon",
                modifier = Modifier.size(120.dp),
                tint = MaterialTheme.colorScheme.onBackground
            )
        },
        title = "Notification Permission",
        message = "Notification permission already provided.",
        buttonText = "Continue",
        onButtonClick = onContinueButtonClick
    )
}

@Preview
@Composable
private fun PreviewOnboardingPage3() {
    OnboardingPage3(
        isNotificationPermissionGiven = false,
        onRequestNotificationPermission = { },
        onContinueButtonClick = { }
    )
}