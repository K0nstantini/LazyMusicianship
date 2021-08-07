package com.grommade.lazymusicianship.ui_statistics

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.SnackbarDefaults.backgroundColor
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.insets.ui.Scaffold
import com.grommade.lazymusicianship.R
import com.grommade.lazymusicianship.data.entity.StateStudy
import com.grommade.lazymusicianship.ui.common.rememberFlowWithLifecycle
import com.grommade.lazymusicianship.ui.components.*
import com.grommade.lazymusicianship.ui.components.diapogs.AppDialog
import com.grommade.lazymusicianship.ui.components.diapogs.BuildPeriodDialog
import com.grommade.lazymusicianship.ui.components.material_dialogs.core.MaterialDialog
import com.grommade.lazymusicianship.ui.theme.LazyMusicianshipTheme
import com.grommade.lazymusicianship.ui.theme.TextPurple
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
        modifier = Modifier.fillMaxSize(),
        contentColor = TextPurple
    ) {

        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                ChartItem("Time", Color(0xFF22531D))
                ChartItem("Pieces")
                ChartItem("Details")
            }
            Header()
            Filters()
        }

//        LineChart(
//            values = viewState.timesByDays,
//            textLabelY = "Hours",
//            textLabelX = "Days",
//            offsetTopLeft = Offset(64f, 64f),
//            offsetBottomRight = Offset(64f, 265f),
//            medium = true
//        ).Built()
    }
}

@Composable
fun ChartItem(
    text: String,
    color: Color = MaterialTheme.colors.background,
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text, fontSize = 12.sp)
        Card(
            modifier = Modifier
                .size(height = 50.dp, width = 90.dp)
                .padding(top = 8.dp),
            backgroundColor = color,
            shape = RoundedCornerShape(15.dp),
            border = border()
        ) {}
    }
}

@Composable
fun Header() {
    Text(
        text = "TIME",
        modifier = Modifier.padding(start = 16.dp, top = 16.dp),
        fontSize = 18.sp,
        fontWeight = FontWeight.Bold
    )
}

@Composable
fun Filters() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(32.dp)
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(horizontalArrangement = Arrangement.SpaceBetween) {
            FilterByPeriod()
            FilterPeriod()
        }
        FilterOther()
    }
}

@Composable
fun FilterByPeriod() {
    SmoothBox(
        contentColor = Color(0xFFDE395A),
        onClick = { /*TODO*/ }) {
        Row(
            modifier = Modifier.padding(horizontal = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("By months",fontSize = 12.sp)
            Icon(
                imageVector = Icons.Default.ExpandMore,
                modifier = Modifier.padding(start = 4.dp),
                contentDescription = stringResource(R.string.cd_expand_more_icon)
            )
        }
    }
}

@Composable
fun FilterPeriod() {
    SmoothBox(modifier = Modifier.padding(start = 16.dp),onClick = { /*TODO*/ }) {
        Text(
            text = "01.08.2021 - 06.08.2021",
            modifier = Modifier.padding(horizontal = 16.dp),
            fontSize = 12.sp
        )
    }
}

@Composable
fun FilterOther() {
    Box(
        modifier = Modifier
            .clip(CircleShape)
            .border(1.dp, Color(0xFF69921F), CircleShape)
            .size(32.dp)
            .clickable(onClick = { /*TODO*/ }),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = Icons.Default.FilterList,
            tint = Color(0xFF69921F),
            contentDescription = stringResource(R.string.cd_filter_icon)
        )
    }
}

@Composable
fun SmoothBox(
    modifier: Modifier = Modifier,
    contentColor: Color = contentColorFor(backgroundColor),
    onClick: () -> Unit,
    content: @Composable () -> Unit
) {
    val shape = RoundedCornerShape(10.dp)
    Surface(
        color = MaterialTheme.colors.background,
        contentColor = contentColor
    ) {
        Box(
            modifier = modifier
                .clip(shape)
                .fillMaxHeight()
                .border(1.dp, TextPurple, shape)
                .clickable(onClick = onClick),
            contentAlignment = Alignment.Center
        ) {
            content()
        }
    }
}

@Composable
fun border() =
    BorderStroke(1.dp, color = TextPurple)

@Composable
fun FilterDel(
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
        BuildPeriodDialog(
            dateStart = LocalDate.now().minusDays(20),
            dateEnd = LocalDate.now().minusDays(2)
        ) { _, _ -> }
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
//    val randomFloat = {
//        (0..3).random().toFloat() + (0..59).random().toFloat() / 60
//    }
//    for (i in 0..30) {
//        values.add(i.toString() to randomFloat())
//    }
    values.add("1" to 3.1f)
    values.add("2" to 0.3f)
    values.add("3" to 4.1f)
    val state = StatisticsViewState(
        timesByDays = values
    )
    LazyMusicianshipTheme {
        StatisticsUi(state) {}
    }
}
