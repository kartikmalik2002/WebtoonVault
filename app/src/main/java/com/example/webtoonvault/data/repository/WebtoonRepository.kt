package com.example.webtoonvault.data.repository


import android.util.Log
import com.example.webtoonvault.data.models.User
import com.example.webtoonvault.data.models.Webtoon
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class WebtoonRepository {

    private val firestore = FirebaseFirestore.getInstance()

    private val _favoriteWebtoons = MutableStateFlow<List<Webtoon>?>(null)
    val favoriteWebtoons: StateFlow<List<Webtoon>?> = _favoriteWebtoons


    private val _webtoon = MutableStateFlow<Webtoon?>(null)
    val webtoon: StateFlow<Webtoon?> get() = _webtoon

    private val _favoriteState = MutableStateFlow<Boolean>(false)
    val favoriteState: StateFlow<Boolean> get() = _favoriteState


    // Firestore collections
    private val webtoonsCollection = firestore.collection("webtoons")
    private val usersCollection = firestore.collection("users")

    suspend fun getWebtoons(): List<Webtoon> {
        return try {
            val result = webtoonsCollection.get().await() // Using coroutines with await
            result.documents.mapNotNull { document ->
                Log.e("firestore webtoon", document.toString())

                document.toObject(Webtoon::class.java)?.copy(id = document.id)
            }
        } catch (e: Exception) {
            Log.e(
                "FirestoreError",
                "Error fetching webtoons: ${e.message}"
            )// Handle any errors that occur during fetching
            emptyList() // Return an empty list on failure
        }
    }

    suspend fun getWebtoon(webtoonId: String) {
        return try {
            val document = webtoonsCollection.document(webtoonId).get().await()
            _webtoon.value = document.toObject(Webtoon::class.java)?.copy(id = document.id)
        } catch (e: Exception) {
            // Handle any errors during fetching
            e.printStackTrace() // Return null if the webtoon is not found
        }
    }

    suspend fun updateRating(webtoonId: String, userId: String, rating: Int) {
        withContext(Dispatchers.IO) {
            try {
                // Step 1: Get the current rating for the user
                val userDocument = usersCollection.document(userId).get().await()
                Log.d("FirestoreUpdate", "Fetched user document for userId: $userId")

                val user = if (!userDocument.exists()) {
                    // Create new user with no ratings
                    val newUser = User(userId, webtoonToRating = hashMapOf())
                    usersCollection.document(userId).set(newUser).await()
                    Log.d("FirestoreUpdate", "Created new user document")
                    newUser  // Return the newly created user
                } else {
                    // If the user exists, get the user object
                    userDocument.toObject(User::class.java)
                }

                // Step 2: Check if the user has rated the webtoon before
                val previousRating = user?.webtoonToRating?.get(webtoonId) ?: 0
                Log.d("FirestoreUpdate", "Previous rating: $previousRating")

                // Step 3: Update the rating for the user
                user?.webtoonToRating?.set(webtoonId, rating)
                usersCollection.document(userId).set(user!!, SetOptions.merge())
                    .await()  // Merge instead of overwrite
                Log.d("FirestoreUpdate", "Updated user document with new rating: $rating")

                // Step 4: Update the webtoon's total rating
                val webtoonDocument = webtoonsCollection.document(webtoonId).get().await()
                val webtoon = webtoonDocument.toObject(Webtoon::class.java)
                Log.d("FirestoreUpdate", "Fetched webtoon document for webtoonId: $webtoonId")

                webtoon?.let {
                    val newTotalRatingValue = it.totalRatingValue + (rating - previousRating)
                    val newRatingCount =
                        if (previousRating == 0) it.ratingCount + 1 else it.ratingCount
                    val newAverageRating = newTotalRatingValue.toFloat() / newRatingCount

                    // Update webtoon rating data
                    val updatedWebtoon = it.copy(
                        averageRating = newAverageRating,
                        ratingCount = newRatingCount,
                        totalRatingValue = newTotalRatingValue
                    )

                    webtoonsCollection.document(webtoonId).set(updatedWebtoon).await()
                    Log.d("FirestoreUpdate", "Updated webtoon document with new rating")
                    _webtoon.value = updatedWebtoon
                }

            } catch (e: Exception) {
                Log.e("FirestoreError", "Error updating rating: ${e.message}")
                e.printStackTrace()  // Print full stack trace for debugging
            }
        }
    }

    suspend fun toggleFavorite(webtoonId: String, userId: String, isFavourite: Boolean) {
        val userDocRef = usersCollection.document(userId)
        val favoritesCollectionRef = userDocRef.collection("favorites")

        withContext(Dispatchers.IO) {
            if (isFavourite) {
                // Add webtoon to favorites
                val favoriteData = hashMapOf("webtoonId" to webtoonId)
                favoritesCollectionRef.document(webtoonId).set(favoriteData)
                    .await()
                Log.d("Firestore", "Webtoon added to favorites")
                _favoriteState.value = true

            } else {
                // Remove webtoon from favorites
                favoritesCollectionRef.document(webtoonId).delete().await()

                Log.d("Firestore", "Webtoon removed from favorites")
                _favoriteState.value = false
            }
        }
    }

    suspend fun getFavoriteWebtoons(userId: String) {

         try {
            val favoriteResult = usersCollection
                .document(userId)
                .collection("favorites")
                .get()
                .await()

            // Step 2: Map through the favorite results and fetch each webtoon from the "webtoons" collection
             _favoriteWebtoons.value = favoriteResult.documents.mapNotNull { favoriteDoc ->
                val webtoonId = favoriteDoc.id  // The document ID is the webtoonId
                Log.d("FavoriteWebtoonId", webtoonId)

                // Step 3: Fetch the full Webtoon object from the "webtoons" collection using the webtoonId
                val webtoonSnapshot = webtoonsCollection.document(webtoonId).get().await()

                webtoonSnapshot.toObject(Webtoon::class.java)?.copy(id = webtoonSnapshot.id)
            }
        } catch (e: Exception) {
            _favoriteWebtoons.value = emptyList()  // Handle failure by emitting an empty list
        }
    }

    suspend fun checkIfWebtoonIsFavorite(webtoonId: String, userId: String) {

            val favoriteDocRef = usersCollection
                .document(userId)
                .collection("favorites")
                .document(webtoonId)


            try{
                val document = favoriteDocRef.get().await()
                _favoriteState.value = document.exists() // True if favorite, false if not
            } catch (e: Exception) {
                e.printStackTrace()
            }


    }

}


