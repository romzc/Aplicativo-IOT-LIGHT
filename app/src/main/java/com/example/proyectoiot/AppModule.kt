package com.example.proyectoiot

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingSource
import com.example.proyectoiot.ui.graphics.GraphicApiData
import com.example.proyectoiot.ui.pagging.PagingApiData
import com.example.proyectoiot.ui.pagging.PagingGetData
import com.example.proyectoiot.ui.composables.ObjectData

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@OptIn(ExperimentalPagingApi::class)
@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun providePagingSource(api: PagingApiData): PagingSource<Int, ObjectData> {
        return PagingGetData(api)
    }

    @Provides
    @Singleton
    fun providePagingApi(): PagingApiData {
        return Retrofit.Builder()
            .baseUrl(PagingApiData.BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
            .create(PagingApiData::class.java)
    }

    @Provides
    @Singleton
    fun provideDataPager(api: PagingApiData, pagingSource: PagingSource<Int, ObjectData>): Pager<Int, ObjectData> {
        return Pager(
            config = PagingConfig(pageSize = 10),
            pagingSourceFactory = { pagingSource }
        )
    }

    @Provides
    @Singleton
    fun provideGraphicApi(): GraphicApiData {
        return Retrofit.Builder()
            .baseUrl(GraphicApiData.BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
            .create(GraphicApiData::class.java)
    }
}
