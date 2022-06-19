package ru.dezerom.jokesnet

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import ru.dezerom.jokesnet.screens.Screens
import ru.dezerom.jokesnet.screens.auth.login.Login
import ru.dezerom.jokesnet.screens.auth.registration.Registration
import ru.dezerom.jokesnet.ui.theme.JokesNetTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            JokesNetTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    val navController = rememberNavController()
                    NavHost(
                        navController = navController,
                        startDestination = Screens.LOGIN.route(),
                    ) {
                        composable(Screens.LOGIN.route()) { Login(navController) }
                        composable(Screens.REGISTRATION.route()) { Registration(navController) }
                    }
                }
            }
        }
    }
}
