package com.cihatakyol.sleeptimer.ui.screens.mainscreen

import android.content.ComponentName
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cihatakyol.sleeptimer.data.repository.TimerSettingsRepository
import com.cihatakyol.sleeptimer.receiver.CountdownReceiver
import com.cihatakyol.sleeptimer.utils.AdManager
import com.cihatakyol.sleeptimer.utils.ScreenManager
import com.cihatakyol.sleeptimer.utils.ServiceManager
import com.cihatakyol.sleeptimer.utils.TimeFormatter
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val screenManager: ScreenManager,
    private val serviceManager: ServiceManager,
    private val timeFormatter: TimeFormatter,
    private val timerSettingsRepository: TimerSettingsRepository,
) : ViewModel() {
    private val _state = MutableStateFlow(DurationEntryState())
    val state: StateFlow<DurationEntryState> = _state.asStateFlow()

    init {
        updateDeviceAdminState()
        setupCountdownReceiver()
        observeServiceState()
        loadLastDuration()
    }

    private fun loadLastDuration() {
        viewModelScope.launch {
            val settings = timerSettingsRepository.getTimerSettings()
            if (settings.lastDuration > 0) {
                _state.update { currentState ->
                    currentState.copy(
                        currentInput = settings.lastDuration.toString(),
                        totalSeconds = settings.lastDuration,
                        displayTime = timeFormatter.formatTime(settings.lastDuration * 1000L)
                    )
                }
            }
        }
    }

    private fun observeServiceState() {
        viewModelScope.launch {
            serviceManager.isServiceRunning.collect { isRunning ->
                _state.update { currentState ->
                    currentState.copy(
                        isCountdownActive = isRunning,
                        isActive = isRunning
                    )
                }
            }
        }
    }

    private fun setupCountdownReceiver() {
        CountdownReceiver.onCountdownUpdate = { remainingTime, isActive ->
            _state.update { currentState ->
                currentState.copy(
                    displayTime = timeFormatter.formatTime(remainingTime),
                )
            }
        }
    }

    fun updateDeviceAdminState() {
        _state.update { it.copy(isDeviceAdminActive = screenManager.isDeviceAdminActive()) }
    }

    fun getComponentName(): ComponentName {
        return screenManager.getComponentName()
    }

    fun onNumberClick(number: Int) {
        if (_state.value.currentInput.length < 4) {
            _state.update { currentState ->
                currentState.copy(
                    currentInput = currentState.currentInput + number,
                    totalSeconds = (currentState.currentInput + number).toIntOrNull() ?: 0
                )
            }
            updateDisplayTime()
        }
    }

    fun onRemoveClick() {
        val currentInput = _state.value.currentInput
        if (currentInput.isNotEmpty()) {
            val newInput = currentInput.substring(0, currentInput.length - 1)
            _state.update { currentState ->
                currentState.copy(
                    currentInput = newInput,
                    totalSeconds = newInput.toIntOrNull() ?: 0
                )
            }
            updateDisplayTime()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun onStartClick() {
        if (_state.value.totalSeconds > 0) {
            viewModelScope.launch {
                timerSettingsRepository.saveLastDuration(_state.value.totalSeconds)
                startCountdown()
            }
        }
    }

    fun onStopClick() {
        stopCountdown()
    }

    private fun stopCountdown() {
        serviceManager.stopTimer()
        viewModelScope.launch {
            val settings = timerSettingsRepository.getTimerSettings()
            _state.update { currentState ->
                currentState.copy(
                    currentInput = settings.lastDuration.toString(),
                    totalSeconds = settings.lastDuration,
                    displayTime = timeFormatter.formatTime(settings.lastDuration * 1000L)
                )
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun startCountdown() {
        val duration = _state.value.totalSeconds * 1000L
        serviceManager.startTimer(duration)
    }

    private fun updateDisplayTime(seconds: Int = _state.value.totalSeconds) {
        _state.update { currentState ->
            currentState.copy(
                displayTime = timeFormatter.formatTime(seconds * 1000L)
            )
        }
    }

    override fun onCleared() {
        super.onCleared()
        CountdownReceiver.onCountdownUpdate = null
    }
}

data class DurationEntryState(
    val currentInput: String = "",
    val totalSeconds: Int = 0,
    val displayTime: String = "00:00:00",
    val isCountdownActive: Boolean = false,
    val isDeviceAdminActive: Boolean = false,
    val isActive: Boolean = false
) 