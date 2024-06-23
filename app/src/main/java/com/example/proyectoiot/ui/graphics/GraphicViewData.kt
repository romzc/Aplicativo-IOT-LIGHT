package com.example.proyectoiot.ui.graphics

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.proyectoiot.ui.composables.ObjectData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GraphicViewData @Inject constructor(
    private val api: GraphicApiData
) : ViewModel() {
    private val _graphicData = MutableLiveData<List<ObjectData>>()
    val graphicData: LiveData<List<ObjectData>>
    get() = _graphicData
    init {
        fetchDataFromApi()
    }
    fun fetchDataFromApi() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val data = api.getDays(7)
                _graphicData.postValue(data)
            } catch (e: Exception) { }
        }
    }
}