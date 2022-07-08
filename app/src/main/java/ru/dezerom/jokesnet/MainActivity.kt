package ru.dezerom.jokesnet

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.*
import dagger.hilt.android.AndroidEntryPoint
import ru.dezerom.jokesnet.screens.FirstLevelDestinations
import ru.dezerom.jokesnet.screens.NestedDestinations
import ru.dezerom.jokesnet.screens.auth.login.Login
import ru.dezerom.jokesnet.screens.auth.registration.Registration
import ru.dezerom.jokesnet.screens.joke_add.AddJokeScreen
import ru.dezerom.jokesnet.screens.profile.ProfileScreen
import ru.dezerom.jokesnet.ui.theme.JokesNetTheme
import ru.dezerom.jokesnet.ui.theme.Teal200
import kotlin.system.exitProcess

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            JokesNetTheme {
                val navController = rememberNavController()
                val currentDestination by navController.currentBackStackEntryAsState()
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    bottomBar = {
                        val route = currentDestination?.destination?.route
                        if (route != FirstLevelDestinations.LOGIN.route()
                            && route != FirstLevelDestinations.REGISTRATION.route()
                            && route != null
                        ) {
                            BottomNavigationBar(navController = navController)
                        }
                    }
                ) {
                    BackHandler(true) {
                        val route = currentDestination?.destination?.route
                        if (route != FirstLevelDestinations.LOGIN.route() &&
                            route != FirstLevelDestinations.REGISTRATION.route()) {
                            finishAffinity()
                            exitProcess(0)
                        }
                    }
                    NavHost(
                        navController = navController,
                        startDestination = FirstLevelDestinations.LOGIN.route(),
                    ) {
                        composable(FirstLevelDestinations.LOGIN.route()) {
                            Login(
                                navController,
                                hiltViewModel()
                            )
                        }
                        composable(FirstLevelDestinations.REGISTRATION.route()) {
                            Registration(
                                navController,
                                hiltViewModel()
                            )
                        }
                        navigation(
                            startDestination = NestedDestinations.PROFILE.route,
                            route = FirstLevelDestinations.NESTED_SCREENS.route()
                        ) {
                            composable(NestedDestinations.PROFILE.route) {
                                ProfileScreen(navController, hiltViewModel())
                            }
                            composable(NestedDestinations.ADD_JOKE.route) {
                                AddJokeScreen(viewModel = hiltViewModel())
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun BottomNavigationBar(navController: NavController) {
    val items = listOf(NestedDestinations.PROFILE, NestedDestinations.ADD_JOKE)

    BottomNavigation(
        backgroundColor = Teal200,
        contentColor = Color.Black
    ) {
        val backStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = backStackEntry?.destination?.route
        items.forEach {
            BottomNavigationItem(
                selected = currentRoute == it.route,
                onClick = {
                    navController.navigate(it.route) {

                        navController.graph.startDestinationRoute?.let { screen_route ->
                            popUpTo(screen_route) {
                                saveState = true
                            }
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                icon = {
                    Icon(
                        painter = painterResource(id = it.iconRes),
                        contentDescription = stringResource(it.titleRes)
                    )
                },
                label = { Text(text = stringResource(it.titleRes), fontSize = 9.sp) },
                selectedContentColor = Color.Black,
                unselectedContentColor = Color.Black.copy(0.4F),
                alwaysShowLabel = true
            )
        }
    }
}
