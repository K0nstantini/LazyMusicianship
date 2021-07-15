package com.grommade.lazymusicianship.home

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LibraryMusic
import androidx.compose.material.icons.filled.School
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.insets.LocalWindowInsets
import com.google.accompanist.insets.rememberInsetsPaddingValues
import com.google.accompanist.insets.ui.BottomNavigation
import com.google.accompanist.insets.ui.Scaffold
import com.grommade.lazymusicianship.AppNavigation
import com.grommade.lazymusicianship.R
import com.grommade.lazymusicianship.Screen

@Composable
fun Home() {
    val navController = rememberNavController()

    Scaffold(
        bottomBar = {
            val currentSelectedItem by navController.currentScreenAsState()

            HomeBottomNavigation(
                selectedNavigation = currentSelectedItem,
                onNavigationSelected = { selected ->
                    navController.navigate(selected.route) {
                        launchSingleTop = true
                        restoreState = true

                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth()
            )
        }
    ) {
        Box(Modifier.fillMaxSize()) {
            AppNavigation(navController)
        }
    }
}

@Composable
private fun NavController.currentScreenAsState(): State<Screen> {
    val selectedItem = remember { mutableStateOf<Screen>(Screen.Main) }

    DisposableEffect(this) {
        val listener = NavController.OnDestinationChangedListener { _, destination, _ ->
            when {
                destination.hierarchy.any { it.route == Screen.Main.route } -> {
                    selectedItem.value = Screen.Main
                }
                destination.hierarchy.any { it.route == Screen.Pieces.route } -> {
                    selectedItem.value = Screen.Pieces
                }
                destination.hierarchy.any { it.route == Screen.Learning.route } -> {
                    selectedItem.value = Screen.Learning
                }
            }
        }
        addOnDestinationChangedListener(listener)

        onDispose {
            removeOnDestinationChangedListener(listener)
        }
    }

    return selectedItem
}

@Composable
fun HomeBottomNavigation(
    selectedNavigation: Screen,
    onNavigationSelected: (Screen) -> Unit,
    modifier: Modifier = Modifier
) {
    BottomNavigation(
        backgroundColor = MaterialTheme.colors.surface,
        contentColor = contentColorFor(MaterialTheme.colors.surface),
        contentPadding = rememberInsetsPaddingValues(LocalWindowInsets.current.navigationBars),
        modifier = modifier
    ) {
        HomeBottomNavigationItem(
            label = stringResource(R.string.main_title),
            selected = selectedNavigation == Screen.Main,
            onClick = { onNavigationSelected(Screen.Main) },
            contentDescription = stringResource(R.string.main_title),
            selectedPainter = rememberVectorPainter(Icons.Default.Home),
            painter = rememberVectorPainter(Icons.Default.Home),
        )

        HomeBottomNavigationItem(
            label = stringResource(R.string.pieces_title),
            selected = selectedNavigation == Screen.Pieces,
            onClick = { onNavigationSelected(Screen.Pieces) },
            contentDescription = stringResource(R.string.pieces_title),
            selectedPainter = rememberVectorPainter(Icons.Default.LibraryMusic),
            painter = rememberVectorPainter(Icons.Default.LibraryMusic),
        )

        HomeBottomNavigationItem(
            label = stringResource(R.string.learning_title),
            selected = selectedNavigation == Screen.Learning,
            onClick = { onNavigationSelected(Screen.Learning) },
            contentDescription = stringResource(R.string.learning_title),
            selectedPainter = rememberVectorPainter(Icons.Default.School),
            painter = rememberVectorPainter(Icons.Default.School),
        )

    }
}

@Composable
private fun RowScope.HomeBottomNavigationItem(
    selected: Boolean,
    selectedPainter: Painter? = null,
    painter: Painter,
    contentDescription: String,
    label: String,
    onClick: () -> Unit,
) {
    BottomNavigationItem(
        icon = {
            if (selectedPainter != null) {
                Crossfade(targetState = selected) { selected ->
                    Icon(
                        painter = if (selected) selectedPainter else painter,
                        contentDescription = contentDescription
                    )
                }
            } else {
                Icon(
                    painter = painter,
                    contentDescription = contentDescription
                )
            }
        },
        label = { Text(label) },
        selected = selected,
        onClick = onClick,
    )
}