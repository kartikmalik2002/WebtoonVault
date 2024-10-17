package com.example.webtoonvault.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.webtoonvault.data.models.Webtoon
import com.example.webtoonvault.data.repository.UserRepository
import com.example.webtoonvault.data.repository.WebtoonRepository
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomeViewModel(
    private val repository: WebtoonRepository = WebtoonRepository(),
    userRepository: UserRepository = UserRepository()
) : ViewModel() {


    val userName = userRepository.currentUser.value?.displayName

    private val _webtoons = MutableStateFlow<List<Webtoon>>(emptyList())
    val webtoons: StateFlow<List<Webtoon>> = _webtoons

    init {
        viewModelScope.launch {
            getWebtoons()
        }
    }

    // Fetch all webtoons
    suspend fun getWebtoons() {
        return withContext(Dispatchers.IO) {
            // Get webtoons from the repository
            val webtoonsFromRepo = repository.getWebtoons()
            _webtoons.value = webtoonsFromRepo // Update the StateFlow

        }
    }
}
