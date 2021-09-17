package com.grommade.lazymusicianship

import androidx.compose.runtime.Composable
import androidx.navigation.*
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.grommade.lazymusicianship.ui_main.MainUi
import com.grommade.lazymusicianship.ui_piece_details.PieceUi
import com.grommade.lazymusicianship.ui_pieces.PiecesListUi
import com.grommade.lazymusicianship.ui_practice.PracticeUi
import com.grommade.lazymusicianship.ui_practice_details.PracticeDetailsUi
import com.grommade.lazymusicianship.ui_section_details.SectionUi
import com.grommade.lazymusicianship.ui_state_details.StateDetailsUi
import com.grommade.lazymusicianship.ui_states.StatesUi
import com.grommade.lazymusicianship.ui_statistics.StatisticsUi
import com.grommade.lazymusicianship.util.Keys

sealed class Screen(val route: String) {
    object Main : Screen("mainRoot")
    object Pieces : Screen("piecesRoot")
    object Practice : Screen("practiceRoot")
    object Statistics : Screen("statisticsRoot")
}

private sealed class LeafScreen(val route: String) {
    object Main : LeafScreen("main")
    object Pieces : LeafScreen("pieces")
    object Practice : LeafScreen("practice")
    object Statistics : LeafScreen("statistics")

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

    object PracticeDetails : LeafScreen("practice/{${Keys.PRACTICE_ID}}") {
        fun createRoute(practiceId: Long): String = "practice/$practiceId"
    }

    object States : LeafScreen("main/states")

    object StateDetails : LeafScreen("main/states/{${Keys.STATE_ID}}") {
        fun createRoute(stateId: Long): String = "main/states/$stateId"
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
        addStatisticsTopLevel(navController)
    }
}

private fun NavGraphBuilder.addMainTopLevel(navController: NavController) {
    navigation(
        route = Screen.Main.route,
        startDestination = LeafScreen.Main.route
    ) {
        addMain(navController)
        addStates(navController)
        addStateDetails(navController)
    }
}

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
        addPractice(navController)
        addPracticeDetails(navController)
    }
}

private fun NavGraphBuilder.addStatisticsTopLevel(navController: NavController) {
    navigation(
        route = Screen.Statistics.route,
        startDestination = LeafScreen.Statistics.route
    ) {
        addStatistics(navController)
    }
}

private fun NavGraphBuilder.addMain(navController: NavController) {
    composable(LeafScreen.Main.route) {
        MainUi(
            openStates = { navController.navigate(LeafScreen.States.route) }
        )
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

private fun NavGraphBuilder.addPractice(navController: NavController) {
    composable(LeafScreen.Practice.route) {
        PracticeUi(
            openPracticeDetails = { practiceId ->
                navController.navigate(LeafScreen.PracticeDetails.createRoute(practiceId))
            }
        )
    }
}

private fun NavGraphBuilder.addStatistics(navController: NavController) {
    composable(LeafScreen.Statistics.route) {
        StatisticsUi()
    }
}

/** =========================================== Leaf Screens ===================================================== */

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
        StatesUi(
            openState = { stateId -> navController.navigate(LeafScreen.StateDetails.createRoute(stateId)) },
            back = navController::navigateUp
        )
    }
}

private fun NavGraphBuilder.addStateDetails(navController: NavController) {
    composable(
        route = LeafScreen.StateDetails.route,
        arguments = listOf(
            navArgument(Keys.STATE_ID) { type = NavType.LongType }
        )
    ) {
        StateDetailsUi(close = navController::navigateUp)
    }
}

private fun NavGraphBuilder.addPracticeDetails(navController: NavController) {
    composable(
        route = LeafScreen.PracticeDetails.route,
        arguments = listOf(
            navArgument(Keys.PRACTICE_ID) { type = NavType.LongType }
        )
    ) {
        PracticeDetailsUi(close = navController::navigateUp)
    }
}