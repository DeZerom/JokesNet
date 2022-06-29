package ru.dezerom.jokesnet.screens.profile

import android.util.Log
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavController

@Composable
fun ProfileScreen() {
    Text(text = "some profile")
    Log.i("Profile", "navigated here")
}