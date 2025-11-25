package com.cihatakyol.sleeptimer.ui.screens.mainscreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cihatakyol.sleeptimer.data.repository.TimerSettingsRepository
import com.cihatakyol.sleeptimer.receiver.CountdownReceiver
import com.cihatakyol.sleeptimer.utils.ServiceManager
import com.cihatakyol.sleeptimer.utils.TimeFormatter
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
    private val serviceManager: ServiceManager,
    private val timeFormatter: TimeFormatter,
    private val timerSettingsRepository: TimerSettingsRepository,
) : ViewModel() {
    private val _state = MutableStateFlow(MainScreenUIState())
    val state: StateFlow<MainScreenUIState> = _state.asStateFlow()

    init {
        setupCountdownReceiver()
        observeServiceState()
        loadLastDuration()
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

    private fun observeServiceState() {
        viewModelScope.launch {
            serviceManager.isServiceRunning.collect { isRunning ->
                _state.update { currentState ->
                    currentState.copy(
                        isCountdownActive = isRunning,
                    )
                }
                if (!isRunning) {
                    stopCountdown()
                }
            }
        }
    }

    private fun setupCountdownReceiver() {
        CountdownReceiver.onCountdownUpdate = { remainingTime, isActive ->
            if (isActive) {
                _state.update { currentState ->
                    currentState.copy(
                        displayTime = DisplayTime(
                            hour = timeFormatter.parseFromMillis(remainingTime).first,
                            minute = timeFormatter.parseFromMillis(remainingTime).second
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
        stopCountdown()
    }

    private fun stopCountdown() {
        serviceManager.stopTimer()
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
        serviceManager.startTimer(state.value.selectedDurationInMillis)
    }


    override fun onCleared() {
        super.onCleared()
        CountdownReceiver.onCountdownUpdate = null
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