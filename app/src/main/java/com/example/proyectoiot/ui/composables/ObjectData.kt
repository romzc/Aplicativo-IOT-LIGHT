package com.example.proyectoiot.ui.composables


data class ObjectData(
    val SampleOrder: Long,
    val SampleTime: Long,
    val DeviceData: deviceData
) {
    data class deviceData(
        val calendar: String,
        val hour: String,
        val luminosity: Int,
        val detection: Boolean,
    )
}