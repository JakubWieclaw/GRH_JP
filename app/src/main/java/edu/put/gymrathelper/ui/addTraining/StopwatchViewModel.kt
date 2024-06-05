package edu.put.gymrathelper.ui.addTraining

import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.savedstate.SavedStateRegistryOwner
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class StopwatchViewModel(private val savedStateHandle: SavedStateHandle) : ViewModel() {
    private var timerJob: Job? = null

    val elapsedTime = savedStateHandle.getLiveData("elapsed_time", 0L)
    val isRunning = savedStateHandle.getLiveData("is_running", false)

    fun startTimer() {
        if (timerJob == null) {
            timerJob = viewModelScope.launch {
                while (true) {
                    delay(1000)
                    elapsedTime.value = (elapsedTime.value ?: 0L) + 1000L
                }
            }
        }
    }

    fun stopTimer() {
        timerJob?.cancel()
        timerJob = null
    }

    fun resetTimer() {
        stopTimer()
        elapsedTime.value = 0L
    }

    fun toggleTimer() {
        if (isRunning.value == true) {
            stopTimer()
        } else {
            startTimer()
        }
        isRunning.value = !(isRunning.value ?: false)
    }

    override fun onCleared() {
        super.onCleared()
        stopTimer()
    }
}

class StopwatchViewModelFactory(
    owner: SavedStateRegistryOwner
) : AbstractSavedStateViewModelFactory(owner, null) {
    override fun <T : ViewModel> create(
        key: String,
        modelClass: Class<T>,
        handle: SavedStateHandle
    ): T {
        return StopwatchViewModel(handle) as T
    }
}
