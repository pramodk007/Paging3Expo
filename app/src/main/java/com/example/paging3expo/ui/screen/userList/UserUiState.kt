package com.example.paging3expo.ui.screen.userList

import androidx.paging.PagingData
import com.example.paging3expo.data.model.Data

data class UserUiState(
    val isLoading: Boolean = false,
    val user: PagingData<Data>? = null,
    val error: String = ""
)
