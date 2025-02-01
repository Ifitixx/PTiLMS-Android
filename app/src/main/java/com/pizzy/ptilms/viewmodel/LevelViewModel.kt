package com.pizzy.ptilms.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pizzy.ptilms.data.model.LevelEntity
import com.pizzy.ptilms.data.repository.LevelRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LevelViewModel @Inject constructor(
    private val levelRepository: LevelRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState<List<LevelEntity>>>(UiState.Loading)
    val uiState: StateFlow<UiState<List<LevelEntity>>> = _uiState.asStateFlow()

    init {
        loadLevels()
    }

    private fun loadLevels() {
        viewModelScope.launch(Dispatchers.IO) {
            _uiState.value = UiState.Loading
            try {
                val levels = levelRepository.getAllLevels().first()
                _uiState.value = UiState.Success(levels)
            } catch (e: Exception) {
                _uiState.value = UiState.Error("Failed to load levels", e)
            }
        }
    }

    suspend fun getLevelByName(name: String): LevelEntity? {
        return levelRepository.getLevelByName(name)
    }
}