package com.ToolCompany.screentimer.ui.screens.mainscreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ToolCompany.screentimer.data.repository.TimerSettingsRepository
import com.ToolCompany.screentimer.utils.ScreenManager
import com.ToolCompany.screentimer.utils.TimeFormatter
import com.ToolCompany.screentimer.utils.TimerScheduler
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val timerScheduler: TimerScheduler,
    private val screenManager: ScreenManager,
    private val timeFormatter: TimeFormatter,
    private val timerSettingsRepository: TimerSettingsRepository,
) : ViewModel() {
    private val _state = MutableStateFlow(MainScreenUIState())
    val state: StateFlow<MainScreenUIState> = _state.asStateFlow()

    private var countdownJob: Job? = null

    init {
        restoreTimerIfNeeded()
        loadLastDuration()
    }

    private fun restoreTimerIfNeeded() {
        if (timerScheduler.getRemainingTime() > 0) {
            _state.update { it.copy(isCountdownActive = true) }
            startCountdownFlow(timerScheduler.getRemainingTime())
        }
    }

    private fun loadLastDuration() {
        viewModelScope.launch {
            val settings = timerSettingsRepository.getTimerSettings()
            if (settings.lastSetDurationByUser > 0) {
                _state.update { currentState ->
                    currentState.copy(
                        displayTime = DisplayTime(
                            hour = timeFormatter.parseFromMillis(settings.lastSetDurationByUser).first,
                            minute = timeFormatter.parseFromMillis(settings.lastSetDurationByUser).second
                        )
                    )
                }
            }
        }
    }

    fun onTimeSelected(hour: Int, minute: Int) {
        _state.update { currentState ->
            currentState.copy(
                displayTime = DisplayTime(hour = hour, minute = minute),
                selectedDurationInMillis = timeFormatter.formatToMillis(Pair(hour, minute))
            )
        }
    }

    fun onStartClick() {
        if (_state.value.selectedDurationInMillis > 0) {
            viewModelScope.launch {
                timerSettingsRepository.saveLastDuration(_state.value.selectedDurationInMillis)
                startCountdown()
            }
        }
    }

    fun onToggleClick() {
        if (state.value.isCountdownActive) {
            onStopClick()
        } else {
            onStartClick()
        }
    }

    fun onStopClick() {
        countdownJob?.cancel()
        countdownJob = null
        timerScheduler.cancelTimer()
        _state.update { it.copy(isCountdownActive = false) }
        restoreLastDurationDisplay()
    }

    private fun restoreLastDurationDisplay() {
        viewModelScope.launch {
            val settings = timerSettingsRepository.getTimerSettings()
            _state.update { currentState ->
                currentState.copy(
                    displayTime = DisplayTime(
                        hour = timeFormatter.parseFromMillis(settings.lastSetDurationByUser).first,
                        minute = timeFormatter.parseFromMillis(settings.lastSetDurationByUser).second
                    )
                )
            }
        }
    }

    private fun startCountdown() {
        timerScheduler.scheduleTimer(state.value.selectedDurationInMillis)
        _state.update { it.copy(isCountdownActive = true) }
        startCountdownFlow(state.value.selectedDurationInMillis)
    }

    private fun startCountdownFlow(durationMillis: Long) {
        countdownJob?.cancel()
        val endTime = System.currentTimeMillis() + durationMillis
        countdownJob = viewModelScope.launch {
            while (true) {
                val remaining = endTime - System.currentTimeMillis()
                if (remaining <= 0) {
                    screenManager.lockScreen()
                    timerScheduler.cancelTimer()
                    _state.update { it.copy(isCountdownActive = false) }
                    restoreLastDurationDisplay()
                    break
                }
                _state.update { currentState ->
                    currentState.copy(
                        displayTime = DisplayTime(
                            hour = timeFormatter.parseFromMillis(remaining).first,
                            minute = timeFormatter.parseFromMillis(remaining).second
                        )
                    )
                }
                delay(1000)
            }
        }
    }
}

data class MainScreenUIState(
    val selectedDurationInMillis: Long = 0,
    val displayTime: DisplayTime = DisplayTime(hour = 0, minute = 0),
    val isCountdownActive: Boolean = false,
)

data class DisplayTime(
    val hour: Int,
    val minute: Int
)