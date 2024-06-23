package com.example.proyectoiot.ui.pagging

import com.example.proyectoiot.ui.composables.ObjectData
import retrofit2.http.GET
import retrofit2.http.Query

interface PagingApiData {

    @GET("items")
    suspend fun getData(
        @Query("page") page: Int,
        @Query("per_page") pageCount: Int
    ): List<ObjectData>

    companion object {
        const val BASE_URL = "https://4pi5mrrhqa.execute-api.us-east-2.amazonaws.com/"
    }

}