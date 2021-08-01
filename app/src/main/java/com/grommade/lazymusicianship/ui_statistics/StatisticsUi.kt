package com.grommade.lazymusicianship.ui_statistics

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.insets.ui.Scaffold
import com.grommade.lazymusicianship.ui.LineChart
import com.grommade.lazymusicianship.ui.common.rememberFlowWithLifecycle

@Composable
fun StatisticsUi() {
    StatisticsUi(viewModel = hiltViewModel())
}

@Composable
fun StatisticsUi(viewModel: StatisticsViewModel) {
    val viewState by rememberFlowWithLifecycle(viewModel.state)
        .collectAsState(initial = StatisticsViewState.Empty)

    StatisticsUi(viewState) { action ->
        when (action) {
            StatisticsActions.Something -> {
            }
            else -> viewModel.submitAction(action)
        }
    }
}

@Composable
fun StatisticsUi(
    viewState: StatisticsViewState,
    actioner: (StatisticsActions) -> Unit
) {
    Scaffold(modifier = Modifier.fillMaxSize()) {
        LineChart(
            values = viewState.timesByDays,
            textLabelY = "Hours",
            textLabelX = "Days",
            offsetTopLeft = Offset(64f, 64f),
            offsetBottomRight = Offset(64f, 265f),
            medium = true
        ).Built()
    }

}

@Preview
@Composable
fun StatisticsUiPreview() {
    StatisticsUi(StatisticsViewState()) {}
}
