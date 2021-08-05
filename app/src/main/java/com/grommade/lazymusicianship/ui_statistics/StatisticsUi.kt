package com.grommade.lazymusicianship.ui_statistics

import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.insets.ui.Scaffold
import com.grommade.lazymusicianship.R
import com.grommade.lazymusicianship.data.entity.StateStudy
import com.grommade.lazymusicianship.ui.LineChart
import com.grommade.lazymusicianship.ui.common.rememberFlowWithLifecycle
import com.grommade.lazymusicianship.ui.components.*
import com.grommade.lazymusicianship.ui.components.diapogs.AppDialog
import com.grommade.lazymusicianship.ui.components.diapogs.BuildPeriodDialog
import com.grommade.lazymusicianship.ui.components.material_dialogs.core.MaterialDialog
import com.grommade.lazymusicianship.util.extentions.isNoEmpty
import com.grommade.lazymusicianship.util.extentions.toString
import java.time.LocalDate

@Composable
fun StatisticsUi() {
    StatisticsUi(viewModel = hiltViewModel())
}

@Composable
fun StatisticsUi(viewModel: StatisticsViewModel) {
    val viewState by rememberFlowWithLifecycle(viewModel.state)
        .collectAsState(initial = StatisticsViewState.Empty)

    val coroutineScope = rememberCoroutineScope()

    StatisticsUi(viewState) { action ->
        when (action) {
            else -> viewModel.submitAction(action)
        }
    }
}

@Composable
fun StatisticsUi(
    viewState: StatisticsViewState,
    actioner: (StatisticsActions) -> Unit
) {
    Scaffold(
        modifier = Modifier.fillMaxSize()
    ) {
        Filter(viewState, actioner)

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

@Composable
fun Filter(
    viewState: StatisticsViewState,
    actioner: (StatisticsActions) -> Unit
) {
    var dateStart by remember { mutableStateOf(LocalDate.MIN) }
    var dateEnd by remember { mutableStateOf(LocalDate.MIN) }
    var statesStudies by remember { mutableStateOf(emptyList<StateStudy>()) }

    val dateStartDialog = remember { MaterialDialog() }.apply {
        BuiltDateDialog() { dateStart = it }
    }

    val dateEndDialog = remember { MaterialDialog() }.apply {
        BuiltDateDialog() { dateEnd = it }
    }

    val statesDialog = remember { MaterialDialog() }.apply {
        BuiltMultipleChoiceDialog(
            title = stringResource(R.string.states_title),
            list = viewState.allStatesStudy.map { it.name })
        { states ->
            statesStudies = states.map { viewState.allStatesStudy.getOrElse(it) { StateStudy() } }
        }
    }

    val filterDialog = remember { MaterialDialog() }.apply {
        BuiltCustomOkCancelDialog(
            title = "Filter",
            callback = {}
        ) {
            Column(modifier = Modifier.padding(horizontal = 24.dp)) {
                SetItemWithClear(
                    title = "From",
                    value = dateStart.toString("No restriction"),
                    showClear = dateStart.isNoEmpty(),
                    onClick = dateStartDialog::show,
                    onClickClear = { dateStart = LocalDate.MIN }
                )
                SetItemWithClear(
                    title = "To",
                    value = dateEnd.toString("No restriction"),
                    showClear = dateEnd.isNoEmpty(),
                    onClick = dateEndDialog::show,
                    onClickClear = { dateEnd = LocalDate.MIN }
                )
                SetItemDefault(
                    title = "States",
                    value = "All",
                    onClick = statesDialog::show
                )
            }
        }
    }

    val myPeriod = remember { AppDialog() }.apply {
        BuildPeriodDialog()
    }

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.End
    ) {
        IconButton(onClick = myPeriod::show) {
            Icon(
                imageVector = Icons.Filled.FilterList,
                contentDescription = stringResource(R.string.cd_filter_icon)
            )
        }
    }
}

@Preview
@Composable
fun StatisticsUiPreview() {
    val values = mutableListOf<Pair<String, Float>>()
    val randomFloat = {
        (0..3).random().toFloat() + (0..59).random().toFloat() / 60
    }
    for (i in 0..30) {
        values.add(i.toString() to randomFloat())
    }
    val state = StatisticsViewState(
        timesByDays = values
    )
    StatisticsUi(state) {}
}
