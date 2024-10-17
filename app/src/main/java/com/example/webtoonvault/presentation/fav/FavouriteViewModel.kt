package com.example.webtoonvault.presentation.fav

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.webtoonvault.data.models.Webtoon
import com.example.webtoonvault.data.repository.UserRepository
import com.example.webtoonvault.data.repository.WebtoonRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FavouriteViewModel(
    private val repository: WebtoonRepository = WebtoonRepository(),
    private val userRepository: UserRepository = UserRepository()
): ViewModel(){
    // StateFlow to hold the list of webtoons
    val favoriteWebtoons: StateFlow<List<Webtoon>?> get() = repository.favoriteWebtoons
    val userId = userRepository.getUserId()

    // Fetch all webtoons
    suspend fun getFavoriteWebtoons(userId: String) {
        withContext(Dispatchers.IO) {
            repository.getFavoriteWebtoons(userId) // Update the StateFlow
        }
    }


}