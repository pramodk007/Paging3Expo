package com.example.paging3expo.data.repository

import androidx.paging.*
import com.example.paging3expo.data.model.Data
import com.example.paging3expo.data.remote.api.APIService
import com.example.paging3expo.data.remote.datasource.UserDataSource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val apiService: APIService
){
    fun getCharacter(): Flow<PagingData<Data>>{
        return Pager(
            config = PagingConfig(
                pageSize = 1
            ),
            pagingSourceFactory = {
                UserDataSource(apiService)
            }
        ).flow
    }

}