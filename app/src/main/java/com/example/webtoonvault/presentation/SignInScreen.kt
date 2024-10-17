package com.example.webtoonvault.presentation

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun SignInScreen(onGoogleSignIn: () -> Unit, onSignInSuccess: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Sign In with Google")
        Spacer(modifier = Modifier.height(16.dp))

        // Google Sign-In Button
        Button(onClick = { onGoogleSignIn() }) {
            Text(text = "Sign In")
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun SignInScreenPreview() {
    SignInScreen(onGoogleSignIn = { /*TODO*/ }) {

    }
}