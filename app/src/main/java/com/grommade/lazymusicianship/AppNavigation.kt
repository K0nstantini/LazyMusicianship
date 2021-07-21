package com.grommade.lazymusicianship

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.navigation.*
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navArgument
import com.grommade.lazymusicianship.ui_main.MainUi
import com.grommade.lazymusicianship.ui_piece.PieceUi
import com.grommade.lazymusicianship.ui_pieces_list.PiecesListUi
import com.grommade.lazymusicianship.ui_section.SectionUi
import com.grommade.lazymusicianship.util.Keys
import kotlinx.coroutines.ExperimentalCoroutinesApi

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

    object SectionDetails : LeafScreen(
        "piece/{${Keys.PIECE_ID}}/section/{${Keys.SECTION_ID}}?${Keys.PARENT_ID}={${Keys.PARENT_ID}}"
    ) {
        fun createRoute(
            pieceId: Long,
            sectionId: Long,
            parentId: Long
        ): String = "piece/$pieceId/section/$sectionId?${Keys.PARENT_ID}=$parentId"
    }

    object States : LeafScreen("main/states")
}

@ExperimentalCoroutinesApi
@ExperimentalFoundationApi
@ExperimentalMaterialApi
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
        addStates(navController)
    }
}

@ExperimentalCoroutinesApi
@ExperimentalFoundationApi
@ExperimentalMaterialApi
private fun NavGraphBuilder.addPiecesTopLevel(navController: NavController) {
    navigation(
        route = Screen.Pieces.route,
        startDestination = LeafScreen.Pieces.route
    ) {
        addPieces(navController)
        addPieceDetails(navController)
        addSectionDetails(navController)
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
        MainUi(
            openStates = { navController.navigate(LeafScreen.States.route) }
        )
    }
}

@ExperimentalFoundationApi
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

@ExperimentalMaterialApi
@ExperimentalCoroutinesApi
@ExperimentalFoundationApi
private fun NavGraphBuilder.addPieceDetails(navController: NavController) {
    composable(
        route = LeafScreen.PieceDetails.route,
        arguments = listOf(
            navArgument(Keys.PIECE_ID) { type = NavType.LongType }
        )
    ) {
        PieceUi(
            openSection = { pieceId, sectionId, parentId ->
                navController.navigate(LeafScreen.SectionDetails.createRoute(pieceId, sectionId, parentId))
            },
            close = navController::navigateUp
        )
    }
}

@ExperimentalMaterialApi
private fun NavGraphBuilder.addSectionDetails(navController: NavController) {
    composable(
        route = LeafScreen.SectionDetails.route,
        arguments = listOf(
            navArgument(Keys.PIECE_ID) { type = NavType.LongType },
            navArgument(Keys.SECTION_ID) { type = NavType.LongType },
            navArgument(Keys.PARENT_ID) { defaultValue = 0L },
        )
    ) {
        SectionUi(close = navController::navigateUp)
    }
}

private fun NavGraphBuilder.addStates(navController: NavController) {
    composable(route = LeafScreen.States.route) {
//        StatesUi(back = navController::navigateUp)
    }
}