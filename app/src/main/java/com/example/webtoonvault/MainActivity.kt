package com.example.webtoonvault

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.webtoonvault.presentation.SignInScreen
import com.example.webtoonvault.presentation.details.DetailsViewModel
import com.example.webtoonvault.presentation.details.WebtoonDetailScreen
import com.example.webtoonvault.presentation.fav.FavouriteViewModel
import com.example.webtoonvault.presentation.fav.FavouritesScreen
import com.example.webtoonvault.presentation.home.HomeScreen
import com.example.webtoonvault.presentation.home.HomeViewModel
import com.example.webtoonvault.ui.theme.WebtoonVaultTheme
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient

    // Sign-in state: This will be observed by Compose
    private val isSignedInState = mutableStateOf(false)

    // Register for Google Sign-In Activity result
    private val googleSignInLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
        val account = task.getResult(Exception::class.java)
        account?.let {
            firebaseAuthWithGoogle(it)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        FirebaseFirestore.setLoggingEnabled(true)

        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance()

        // Configure Google Sign-In
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id)) // Get from google-services.json
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)
        setContent {
            WebtoonVaultTheme {
                val navController = rememberNavController()
                MainApp(navController, isSignedInState.value, onGoogleSignIn = {
                    signInWithGoogle()
                })
            }
        }
    }

    // Function to initiate Google Sign-In
    private fun signInWithGoogle() {
        val signInIntent = googleSignInClient.signInIntent
        googleSignInLauncher.launch(signInIntent)
    }

    // Authenticate with Firebase using Google Account
    private fun firebaseAuthWithGoogle(account: GoogleSignInAccount) {
        val credential = GoogleAuthProvider.getCredential(account.idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    isSignedInState.value = true
                    // Usually, you'll update the state here to trigger navigation in Compose
                    // Since we are in Activity, we don't directly trigger Compose navigation from here.
                }
            }
    }
}

@Composable
fun MainApp(navController: NavHostController, isSignedIn: Boolean, onGoogleSignIn: () -> Unit) {

    NavHost(navController = navController, startDestination = if (isSignedIn) "home" else "login") {
        composable("login") {
            SignInScreen(onGoogleSignIn = onGoogleSignIn) {
                // Navigate to Home screen after sign-in

                navController.navigate("home") {
                    popUpTo("login") { inclusive = true } // Remove login from backstack
                }

            }
        }
        composable("home") {
            val viewModel: HomeViewModel = viewModel(modelClass = HomeViewModel::class)
            val webtoons by viewModel.webtoons.collectAsState()
            if (webtoons.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize()
                        .background(color = Color.Black)
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center),
                        color = Color.Yellow
                    )
                }
            } else {
                HomeScreen(
                    webtoons = webtoons,
                    onCardClick = { webtoonId ->
                        navController.navigate("details/$webtoonId")
                    },
                    onFavouriteClick = {
                        navController.navigate("favourites")
                    },
                    userName = viewModel.userName
                )
            }
        }
        composable("details/{webtoonId}") { backStackEntry ->
            val webtoonId = backStackEntry.arguments?.getString("webtoonId")
            Log.d("webtoonId", webtoonId.toString())
            val viewModel: DetailsViewModel = viewModel(modelClass = DetailsViewModel::class)
            LaunchedEffect(webtoonId) {
                webtoonId?.let {
                    viewModel.getWebtoon(it)
                }
            }
            val webtoon by viewModel.webtoon.collectAsState()
            val isFavorite by viewModel.favoriteStatus.collectAsState()

            webtoon?.let { Log.d("webtoon", it.title) }
            // Handle null webtoon (loading state)
            if (webtoon == null) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(color = Color.Black)
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center),
                        color = Color.Yellow
                    )
                }
            } else {
                WebtoonDetailScreen(
                    webtoon = webtoon!!,
                    navigateUp = { navController.navigateUp() },
                    onSubmitRating = {
                        viewModel.updateRating(webtoonId!!, it)
                        Log.d("rating", it.toString())
                    },
                    onFavoriteClick = { webtoonId, isFavourite ->
                        CoroutineScope(Dispatchers.IO).launch {
                            viewModel.toggleFavorite(webtoonId, isFavourite)
                        }
                    },
                    isFavorite = isFavorite
                )
            }
        }
        composable("favourites") {
            val viewModel: FavouriteViewModel = viewModel(modelClass = FavouriteViewModel::class)
            val webtoons by viewModel.favoriteWebtoons.collectAsState()
            LaunchedEffect(viewModel.userId) {
                viewModel.getFavoriteWebtoons(userId = viewModel.userId!!)
            }
            if (webtoons == null) {
                Box(
                    modifier = Modifier.fillMaxSize().background(color = Color.Black),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        color = Color.Yellow
                    )
                }
            }
            else{
            FavouritesScreen(
                onCardClick = { webtoonId ->
                    navController.navigate("details/$webtoonId")
                },
                webtoons!!,
                { navController.navigateUp() }
            )
        }
        }
    }

}

