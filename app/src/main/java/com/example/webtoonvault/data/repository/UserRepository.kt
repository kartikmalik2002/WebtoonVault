package com.example.webtoonvault.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class UserRepository {

    private val _currentUser = MutableStateFlow<FirebaseUser?>(null)
    val currentUser: StateFlow<FirebaseUser?> get() = _currentUser

    init {
        // Get current user when the ViewModel is initialized
        _currentUser.value = FirebaseAuth.getInstance().currentUser
    }

    // Refresh the current user data
    fun refreshCurrentUser() {
        _currentUser.value = FirebaseAuth.getInstance().currentUser
    }

    // Get User ID if user is authenticated
    fun getUserId(): String? {
        return _currentUser.value?.uid
    }

    // Logout user
    fun logout() {
        FirebaseAuth.getInstance().signOut()
        _currentUser.value = null
    }
}