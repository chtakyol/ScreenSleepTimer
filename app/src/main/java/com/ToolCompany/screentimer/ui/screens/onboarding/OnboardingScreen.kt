package com.ToolCompany.screentimer.ui.screens.onboarding

import android.Manifest
import android.app.admin.DevicePolicyManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.BottomCenter
import androidx.compose.ui.Modifier
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ToolCompany.screentimer.R
import com.ToolCompany.screentimer.receiver.SleepTimerDeviceAdmin
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class, ExperimentalPermissionsApi::class)
@Composable
fun OnboardingScreen(
    viewModel: OnboardingViewModel = hiltViewModel(),
    navigateToMainScreen: () -> Unit
) {
    val context = LocalContext.current

    val deviceAdminEnabled = remember { mutableStateOf(false) }

    // Device Admin permission
    val deviceAdminLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        // Check if device admin was granted
        val devicePolicyManager =
            context.getSystemService(Context.DEVICE_POLICY_SERVICE) as DevicePolicyManager
        val componentName = ComponentName(context, SleepTimerDeviceAdmin::class.java)
        deviceAdminEnabled.value = devicePolicyManager.isAdminActive(componentName)
    }

    // Notification permission
    val notificationPermissionState = rememberPermissionState(
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            Manifest.permission.POST_NOTIFICATIONS
        } else {
            ""
        }
    )

    // Check if all permissions are granted
    LaunchedEffect(notificationPermissionState.status.isGranted) {
        val devicePolicyManager =
            context.getSystemService(Context.DEVICE_POLICY_SERVICE) as DevicePolicyManager
        val componentName = ComponentName(context, SleepTimerDeviceAdmin::class.java)
        val isDeviceAdminActive = devicePolicyManager.isAdminActive(componentName)

        if (isDeviceAdminActive &&
            (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU || notificationPermissionState.status.isGranted)
        ) {
            viewModel.completeOnboarding()
        }
    }

    OnboardingScreenContent(
        modifier = Modifier,
        isDeviceAdminEnabled = deviceAdminEnabled.value,
        isNotificationPermissionGiven = notificationPermissionState.status.isGranted,
        onRequestDeviceAdminButtonClick = {
            val intent = Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN).apply {
                putExtra(
                    DevicePolicyManager.EXTRA_DEVICE_ADMIN,
                    ComponentName(context, SleepTimerDeviceAdmin::class.java)
                )
                putExtra(
                    DevicePolicyManager.EXTRA_ADD_EXPLANATION,
                    context.getString(R.string.device_admin_description)
                )
            }
            deviceAdminLauncher.launch(intent)
        },
        onNotificationPermissionRequestButtonClick = {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                notificationPermissionState.launchPermissionRequest()
            } else {
                viewModel.completeOnboarding()
            }
        },
        onOnboardingDone = {
            viewModel.completeOnboarding()
            navigateToMainScreen()
        }
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun OnboardingScreenContent(
    modifier: Modifier = Modifier,
    isDeviceAdminEnabled: Boolean = false,
    isNotificationPermissionGiven: Boolean = false,
    onRequestDeviceAdminButtonClick: () -> Unit,
    onNotificationPermissionRequestButtonClick: () -> Unit,
    onOnboardingDone: () -> Unit
) {
    val pagerState = rememberPagerState(pageCount = { 3 })
    val coroutineScope = rememberCoroutineScope()

    Box {
        HorizontalPager(
            state = pagerState,
            modifier = modifier
                .background(MaterialTheme.colorScheme.background)
                .fillMaxSize()
        ) { page ->
            when (page) {
                0 -> OnboardingPage1(onButtonClick = {
                    coroutineScope.launch { pagerState.animateScrollToPage(1) }
                })

                1 -> OnboardingPage2(
                    isDeviceAdminEnabled = isDeviceAdminEnabled,
                    onRequestDeviceAdmin = onRequestDeviceAdminButtonClick,
                    onContinueButtonClick = {
                        coroutineScope.launch {
                            pagerState.animateScrollToPage(2)
                        }
                    }
                )

                2 -> OnboardingPage3(
                    isNotificationPermissionGiven = isNotificationPermissionGiven,
                    onRequestNotificationPermission = onNotificationPermissionRequestButtonClick,
                    onContinueButtonClick =onOnboardingDone
                )
            }
        }
        PagerIndicator(
            modifier = Modifier.align(BottomCenter),
            currentPage = pagerState.currentPage
        )
    }
}

@Composable
fun PagerIndicator(
    modifier: Modifier = Modifier,
    currentPage: Int
) {
    // Use theme colors for pager indicator
    val activeColor = MaterialTheme.colorScheme.primary
    val inactiveColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp),
        horizontalArrangement = Arrangement.Center
    ) {
        repeat(3) { iteration ->
            val color = if (currentPage == iteration) {
                activeColor
            } else {
                inactiveColor
            }

            Box(
                modifier = Modifier
                    .padding(2.dp)
                    .size(8.dp)
                    .align(Alignment.CenterVertically)
                    .background(color)
            )

            if (iteration < 2) {
                Spacer(modifier = Modifier.width(8.dp))
            }
        }
    }
}

@Preview
@Composable
private fun PreviewOnboardingScreen() {
    OnboardingScreenContent(
        modifier = Modifier,
        onRequestDeviceAdminButtonClick = { },
        onNotificationPermissionRequestButtonClick = { },
        isDeviceAdminEnabled = false,
        isNotificationPermissionGiven = false,
        onOnboardingDone = {  }
    )
}
