package com.ToolCompany.screentimer.ui.screens.onboarding

import android.app.admin.DevicePolicyManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ToolCompany.screentimer.R
import com.ToolCompany.screentimer.receiver.SleepTimerDeviceAdmin
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun OnboardingScreen(
    viewModel: OnboardingViewModel = hiltViewModel(),
    navigateToMainScreen: () -> Unit
) {
    val context = LocalContext.current

    val deviceAdminEnabled = remember { mutableStateOf(false) }

    val deviceAdminLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { _ ->
        val devicePolicyManager =
            context.getSystemService(Context.DEVICE_POLICY_SERVICE) as DevicePolicyManager
        val componentName = ComponentName(context, SleepTimerDeviceAdmin::class.java)
        deviceAdminEnabled.value = devicePolicyManager.isAdminActive(componentName)
    }

    LaunchedEffect(deviceAdminEnabled.value) {
        val devicePolicyManager =
            context.getSystemService(Context.DEVICE_POLICY_SERVICE) as DevicePolicyManager
        val componentName = ComponentName(context, SleepTimerDeviceAdmin::class.java)
        if (devicePolicyManager.isAdminActive(componentName)) {
            viewModel.completeOnboarding()
        }
    }

    OnboardingScreenContent(
        modifier = Modifier,
        isDeviceAdminEnabled = deviceAdminEnabled.value,
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
    onRequestDeviceAdminButtonClick: () -> Unit,
    onOnboardingDone: () -> Unit
) {
    val pagerState = rememberPagerState(pageCount = { 2 })
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
                    onContinueButtonClick = onOnboardingDone
                )
            }
        }
        PagerIndicator(
            modifier = Modifier.align(BottomCenter),
            currentPage = pagerState.currentPage,
            pageCount = 2
        )
    }
}

@Composable
fun PagerIndicator(
    modifier: Modifier = Modifier,
    currentPage: Int,
    pageCount: Int = 2
) {
    val activeColor = MaterialTheme.colorScheme.primary
    val inactiveColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp),
        horizontalArrangement = Arrangement.Center
    ) {
        repeat(pageCount) { iteration ->
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

            if (iteration < pageCount - 1) {
                Spacer(modifier = Modifier.width(8.dp))
            }
        }
    }
}