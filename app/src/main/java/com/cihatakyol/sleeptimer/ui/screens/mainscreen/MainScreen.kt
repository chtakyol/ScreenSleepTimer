package com.cihatakyol.sleeptimer.ui.screens.mainscreen

import android.Manifest
import android.app.admin.DevicePolicyManager
import android.content.Intent
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.cihatakyol.sleeptimer.R
import com.cihatakyol.sleeptimer.ui.screens.mainscreen.components.NumberPad
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun MainScreen(
    viewModel: MainViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsState()
    val context = LocalContext.current

    val deviceAdminLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        viewModel.updateDeviceAdminState()
    }

    val notificationPermissionState = rememberPermissionState(
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            Manifest.permission.POST_NOTIFICATIONS
        } else {
            ""
        }
    )

    LaunchedEffect(notificationPermissionState.status.isGranted) {
        if (notificationPermissionState.status.isGranted) {
            // Permission granted, proceed with starting the service
            viewModel.onStartClick()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        MainContent(
            isDeviceAdminActive = state.isDeviceAdminActive,
            onRequestAdmin = {
                val intent = Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN).apply {
                    putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, viewModel.getComponentName())
                    putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION, context.getString(R.string.device_admin_description))
                }
                deviceAdminLauncher.launch(intent)
            },
            onNumberClick = viewModel::onNumberClick,
            onRemoveClick = viewModel::onRemoveClick,
            onStartClick = {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    if (notificationPermissionState.status.isGranted) {
                        viewModel.onStartClick()
                    } else {
                        notificationPermissionState.launchPermissionRequest()
                    }
                } else {
                    viewModel.onStartClick()
                }
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
    isDeviceAdminActive: Boolean = false,
    onRequestAdmin: () -> Unit,
    onNumberClick: (Int) -> Unit,
    onRemoveClick: () -> Unit,
    onStartClick: () -> Unit,
    onStopClick: () -> Unit,
    isKeypadActive: Boolean = true,
    isActive: Boolean = true,
    displayTime: String
) {
    if (!isDeviceAdminActive) {
        DeviceAdminButton(
            onRequestAdmin = onRequestAdmin
        )
    } else {
        Column {
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

@Composable
private fun DeviceAdminButton(
    onRequestAdmin: () -> Unit
) {
    Button(
        onClick = onRequestAdmin,
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.error
        )
    ) {
        Text("Enable Device Admin")
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    MaterialTheme {
        MainContent(
            isDeviceAdminActive = true,
            onRequestAdmin = {  },
            onNumberClick = {  },
            onRemoveClick = {  },
            onStartClick = {  },
            onStopClick = {  },
            isKeypadActive = false,
            isActive = true,
            displayTime = "12:00"
        )
    }
} 