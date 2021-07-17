package com.grommade.lazymusicianship

import androidx.compose.runtime.Composable
import androidx.navigation.*
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navArgument
import com.grommade.lazymusicianship.ui_piece.PieceUi
import com.grommade.lazymusicianship.ui_pieces_list.PiecesListUi
import com.grommade.lazymusicianship.util.Keys

sealed class Screen(val route: String) {
    object Main : Screen("mainroot")
    object Pieces : Screen("piecesroot")
    object Practice : Screen("practiceroot")
}

private sealed class LeafScreen(val route: String) {
    object Main : LeafScreen("main")
    object Pieces : LeafScreen("pieces")
    object Practice : LeafScreen("practice")

    object PieceDetails : LeafScreen("piece/{${Keys.PIECE_ID}}") {
        fun createRoute(pieceId: Long): String = "piece/$pieceId"
    }
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
        addPieceDetails(navController)
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
        PiecesListUi(
            openPiece = { pieceId ->
                navController.navigate(LeafScreen.PieceDetails.createRoute(pieceId))
            }
        )
    }
}

private fun NavGraphBuilder.addLearning(navController: NavController) {
    composable(LeafScreen.Practice.route) {
//        PracticeUi()
    }
}

private fun NavGraphBuilder.addPieceDetails(navController: NavController) {
    composable(
        route = LeafScreen.PieceDetails.route,
        arguments = listOf(
            navArgument(Keys.PIECE_ID) { type = NavType.LongType }
        )
    ) {
        PieceUi(close = navController::navigateUp)
    }
}