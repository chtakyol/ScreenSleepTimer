package com.cihatakyol.sleeptimer.ui.screens.mainscreen

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.cihatakyol.sleeptimer.ui.components.AdMobBanner
import com.cihatakyol.sleeptimer.ui.screens.mainscreen.components.NumberPad
import com.cihatakyol.sleeptimer.utils.AdManager
import com.google.accompanist.permissions.ExperimentalPermissionsApi

private fun Context.findActivity(): Activity? = when (this) {
    is Activity -> this
    is ContextWrapper -> baseContext.findActivity()
    else -> null
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun MainScreen(
    viewModel: MainViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsState()
    val context = LocalContext.current

    val adManager = AdManager(context)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        MainContent(
            onNumberClick = viewModel::onNumberClick,
            onRemoveClick = viewModel::onRemoveClick,
            onStartClick = {
                context.findActivity()?.let {
                    adManager.showInterstitialAd(
                        activity = it,
                        onAdClosed = { viewModel.onStartClick() }
                    )
                }

//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
//                    if (notificationPermissionState.status.isGranted) {
//                        context.findActivity()?.let {
//                            adManager.showInterstitialAd(
//                                activity = it,
//                                onAdClosed = { viewModel.onStartClick() }
//                            )
//                        }
//                    } else {
//                        notificationPermissionState.launchPermissionRequest()
//                    }
//                } else {
//
//                }
            },
            onStopClick = viewModel::onStopClick,
            isKeypadActive = state.isCountdownActive,
            isActive = state.isActive,
            displayTime = state.displayTime
        )
    }
}

@Composable
fun MainContent(
    onNumberClick: (Int) -> Unit,
    onRemoveClick: () -> Unit,
    onStartClick: () -> Unit,
    onStopClick: () -> Unit,
    isKeypadActive: Boolean = true,
    isActive: Boolean = true,
    displayTime: String
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
    ) {
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = "Screen Timer",
            style = MaterialTheme.typography.displayLarge,
            textAlign = TextAlign.Center
        )
        AdMobBanner(modifier = Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.size(128.dp))
        Column(
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(32.dp)
        ) {
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = if (isActive) {
                    displayTime
                } else {
                    displayTime
                },
                style = MaterialTheme.typography.displayLarge,
                textAlign = TextAlign.Center
            )
            NumberPad(
                onNumberClick = onNumberClick,
                onRemoveClick = onRemoveClick,
                onStartClick = onStartClick,
                onStopClick = onStopClick,
                isActive = isKeypadActive
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    MaterialTheme {
        MainContent(
            onNumberClick = { },
            onRemoveClick = { },
            onStartClick = { },
            onStopClick = { },
            isKeypadActive = false,
            isActive = true,
            displayTime = "12:00"
        )
    }
}