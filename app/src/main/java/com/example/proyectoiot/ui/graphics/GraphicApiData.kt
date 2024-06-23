package com.example.proyectoiot.ui.graphics

import com.example.proyectoiot.ui.composables.ObjectData
import retrofit2.http.GET
import retrofit2.http.Query

interface GraphicApiData {
    @GET("items/days")
    suspend fun getDays(
        @Query("ago") page: Int,
    ): List<ObjectData>

    companion object {
        const val BASE_URL = "https://4pi5mrrhqa.execute-api.us-east-2.amazonaws.com/"
    }
}