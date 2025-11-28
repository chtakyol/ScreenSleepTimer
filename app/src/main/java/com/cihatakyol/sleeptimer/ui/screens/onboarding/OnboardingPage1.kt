package com.cihatakyol.sleeptimer.ui.screens.onboarding

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cihatakyol.sleeptimer.R

@Composable
fun OnboardingPage1(
    onButtonClick: () -> Unit
) {
    OnboardingPage(
        icon = {
            Icon(
                painter = painterResource(id = R.drawable.ic_notification),
                contentDescription = "Device Admin Icon",
                modifier = Modifier.size(120.dp),
                tint = MaterialTheme.colorScheme.primary
            )
        },
        title = stringResource(R.string.onboarding_screen_1_title),
        message = stringResource(R.string.onboarding_screen_1_message),
        buttonText = stringResource(R.string.onboarding_screen_1_button),
        onButtonClick = onButtonClick
    )
}

@Preview
@Composable
private fun PreviewOnboardingScreen1() {
    OnboardingPage1() {

    }
}