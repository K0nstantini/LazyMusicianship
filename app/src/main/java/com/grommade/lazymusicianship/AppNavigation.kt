package com.grommade.lazymusicianship

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navigation

sealed class Screen(val route: String) {
    object Main : Screen("mainroot")
    object Pieces : Screen("piecesroot")
    object Practice : Screen("practiceroot")
}

private sealed class LeafScreen(val route: String) {
    object Main : LeafScreen("main")
    object Pieces : LeafScreen("pieces")
    object Practice : LeafScreen("practice")
}

@Composable
fun AppNavigation(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Screen.Main.route
    ) {
        addMainTopLevel(navController)
        addPiecesTopLevel(navController)
        addPracticeTopLevel(navController)
    }
}

private fun NavGraphBuilder.addMainTopLevel(navController: NavController) {
    navigation(
        route = Screen.Main.route,
        startDestination = LeafScreen.Main.route
    ) {
        addMain(navController)
    }
}

private fun NavGraphBuilder.addPiecesTopLevel(navController: NavController) {
    navigation(
        route = Screen.Pieces.route,
        startDestination = LeafScreen.Pieces.route
    ) {
        addPieces(navController)
    }
}

private fun NavGraphBuilder.addPracticeTopLevel(navController: NavController) {
    navigation(
        route = Screen.Practice.route,
        startDestination = LeafScreen.Practice.route
    ) {
        addLearning(navController)
    }
}

private fun NavGraphBuilder.addMain(navController: NavController) {
    composable(LeafScreen.Main.route) {
//        MainUi()
    }
}

private fun NavGraphBuilder.addPieces(navController: NavController) {
    composable(LeafScreen.Pieces.route) {
//        PiecesUi()
    }
}

private fun NavGraphBuilder.addLearning(navController: NavController) {
    composable(LeafScreen.Practice.route) {
//        PracticeUi()
    }
}