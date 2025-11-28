package com.cihatakyol.sleeptimer.ui.screens.onboarding

import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LockOpen
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun OnboardingPage2(
    isDeviceAdminEnabled: Boolean = false,
    onRequestDeviceAdmin: () -> Unit,
    onContinueButtonClick: () -> Unit
) {
    if (!isDeviceAdminEnabled) {
        OnboardingPage(
            icon = {
                Icon(
                    imageVector = Icons.Default.LockOpen,
                    contentDescription = "Device Admin Icon",
                    modifier = Modifier.size(120.dp),
                    tint = MaterialTheme.colorScheme.primary
                )
            },
            title = "Device Admin Permission",
            message = "Sleep Timer needs device admin permission to lock your screen when the timer ends. This is the only permission needed for the app to function.",
            buttonText = "Enable Device Admin",
            onButtonClick = onRequestDeviceAdmin
        )
    } else {
        OnboardingPage2DeviceAdminAlreadyEnabled(onContinueButtonClick = onContinueButtonClick)
    }

}

@Composable
private fun OnboardingPage2DeviceAdminAlreadyEnabled(
    onContinueButtonClick: () -> Unit
) {
    OnboardingPage(
        icon = {
            Icon(
                imageVector = Icons.Default.LockOpen,
                contentDescription = "Device Admin Icon",
                modifier = Modifier.size(120.dp),
                tint = MaterialTheme.colorScheme.primary
            )
        },
        title = "Device Admin Permission",
        message = "Device admin already provided.",
        buttonText = "Continue",
        onButtonClick = onContinueButtonClick
    )
}

@Preview
@Composable
private fun PreviewOnboardingPage2DeviceAdminAlreadyEnabled() {
    OnboardingPage2DeviceAdminAlreadyEnabled { }
}