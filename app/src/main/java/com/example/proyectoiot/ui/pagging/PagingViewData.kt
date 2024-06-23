package com.example.proyectoiot.ui.pagging

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.proyectoiot.ui.composables.ObjectData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PagingViewData @Inject constructor(
    private val pager: Pager<Int, ObjectData>
) : ViewModel() {
    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()
    private val _isFlowPaused = MutableStateFlow(false)

    val sensorPagingFlow: Flow<PagingData<ObjectData>> = _isFlowPaused.flatMapLatest { isPaused ->
        if (isPaused) {
            emptyFlow()
        } else {
            pager.flow.cachedIn(viewModelScope)
        }
    }

    fun pauseFlow() {
        viewModelScope.launch {
            _isFlowPaused.value = true
            _isLoading.value = true
            delay(1000L)
            resumeFlow()
        }
    }

    fun resumeFlow() {
        viewModelScope.launch {
            _isLoading.value = false
            _isFlowPaused.value = false
        }
    }
}