package com.example.paging3expo.data.remote.api

import com.example.paging3expo.data.model.ApiResponse
import com.example.paging3expo.data.model.Data
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface APIService {

    @GET("character")
    suspend fun getAllCharacters(
        @Query("page") page: Int
    ): Response<ApiResponse>
}