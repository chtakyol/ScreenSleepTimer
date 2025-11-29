package com.ToolCompany.screentimer.ui.screens.mainscreen

import android.util.Log
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.with
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayCircle
import androidx.compose.material.icons.filled.StopCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ToolCompany.screentimer.ui.components.AdMobBanner
import com.ToolCompany.screentimer.ui.screens.mainscreen.components.TimePicker
import com.google.accompanist.permissions.ExperimentalPermissionsApi

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun MainScreen(
    viewModel: MainViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsState()

    Scaffold(
        bottomBar = {
            AdMobBanner()
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background),
            contentAlignment = Alignment.Center,
        ) {
            state.apply {
                MainContent(
                    currentHour = displayTime.hour,
                    currentMinute = displayTime.minute,
                    isActive = isCountdownActive,
                    onTimeSelected = viewModel::onTimeSelected,
                    onStartToggle = viewModel::onToggleClick
                )
            }
        }
    }
}

@Composable
fun MainContent(
    currentHour: Int = 12,
    currentMinute: Int = 0,
    isActive: Boolean = false,
    onTimeSelected: (hour: Int, minute: Int) -> Unit,
    onStartToggle: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        TimePicker(
            isTimerActive = isActive,
            currentHour = currentHour,
            currentMinute = currentMinute,
            onTimeSelected = { hour, minute ->
                onTimeSelected(hour, minute)
                Log.d("TimePicker", "Selected Time: ${hour ?: "--"}:${minute ?: "--"}")
            }
        )

        AnimatedIconButton(isActive = isActive, onClick = onStartToggle)
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun AnimatedIconButton(
    isActive: Boolean,
    onClick: () -> Unit
) {
    IconButton(
        modifier = Modifier.size(120.dp),
        onClick = onClick) {
        AnimatedContent(
            targetState = !isActive,
            transitionSpec = {
                fadeIn(tween(300)) with fadeOut(tween(300))
            },
            label = "IconToggle"
        ) { targetState ->
            if (targetState) {
                Icon(
                    modifier = Modifier.size(96.dp),
                    imageVector = Icons.Default.PlayCircle,
                    contentDescription = "Start timer",
                    tint = MaterialTheme.colorScheme.primary
                )
            } else {
                Icon(
                    modifier = Modifier.size(96.dp),
                    imageVector = Icons.Default.StopCircle,
                    contentDescription = "Stop timer",
                    tint = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    MaterialTheme {
        MainContent(
            currentHour = 21,
            currentMinute = 2,
            isActive = false,
            onTimeSelected = { hour, minute -> },
            onStartToggle = { }
        )
    }
}