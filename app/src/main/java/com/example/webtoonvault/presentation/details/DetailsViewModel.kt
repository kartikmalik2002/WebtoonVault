package com.example.webtoonvault.presentation.details

import android.util.Log
import androidx.compose.ui.platform.LocalGraphicsContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.webtoonvault.data.models.Webtoon
import com.example.webtoonvault.data.repository.UserRepository
import com.example.webtoonvault.data.repository.WebtoonRepository
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class DetailsViewModel(
    private val repository: WebtoonRepository = WebtoonRepository(),
    userRepository: UserRepository = UserRepository()
): ViewModel() {

    val userId = userRepository.getUserId()
    val webtoon: StateFlow<Webtoon?> get() = repository.webtoon
    val favoriteStatus: StateFlow<Boolean> get() = repository.favoriteState


    // Fetch a specific webtoon
    fun getWebtoon(webtoonId: String?) {
        viewModelScope.launch {
            webtoonId?.let { repository.getWebtoon(it) }
        }
        viewModelScope.launch {
            userId?.let {
                repository.checkIfWebtoonIsFavorite(webtoonId!!, userId!!)
            }
        }
    }

    // Update webtoon rating
    fun updateRating(webtoonId: String, rating: Int) {
        Log.d("userId" , userId.toString())
        userId?.let {
            viewModelScope.launch {
                repository.updateRating(webtoonId, userId, rating)
            }
        }
    }

    suspend fun toggleFavorite(webtoonId: String, isFavourite: Boolean) {
        userId?.let {
            repository.toggleFavorite(webtoonId, userId, isFavourite)
        }
    }
}