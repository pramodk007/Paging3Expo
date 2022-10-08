package com.example.paging3expo.ui.screen.userList

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.example.paging3expo.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserListViewModel @Inject constructor(
    private val repository: UserRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(UserUiState())
    val uiState: StateFlow<UserUiState> = _uiState.asStateFlow()

    init {
        getUser()
    }
    fun getUser() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            repository.getCharacter()
                .cachedIn(viewModelScope)
                .catch { e ->
                    _uiState.update { it.copy(error = e.message!!) }
                }
                .collect { user ->
                    _uiState.update { it.copy(user = user) }
                }
        }
    }
}