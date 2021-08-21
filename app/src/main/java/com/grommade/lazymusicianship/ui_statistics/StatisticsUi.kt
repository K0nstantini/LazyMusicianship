package com.grommade.lazymusicianship.ui_statistics

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.insets.ui.Scaffold
import com.grommade.lazymusicianship.R
import com.grommade.lazymusicianship.ui.charts.line_chart.transformLineChartText
import com.grommade.lazymusicianship.ui.charts.line_chart_core.LineChart
import com.grommade.lazymusicianship.ui.charts.line_chart_core.LineChartText
import com.grommade.lazymusicianship.ui.charts.line_chart_core.LineChartTextFormat
import com.grommade.lazymusicianship.ui.common.rememberFlowWithLifecycle
import com.grommade.lazymusicianship.ui.components.dialogs.AppDialog
import com.grommade.lazymusicianship.ui.components.dialogs.BuildPeriodDialog
import com.grommade.lazymusicianship.ui.theme.DarkYellow
import com.grommade.lazymusicianship.ui.theme.LazyMusicianshipTheme
import com.grommade.lazymusicianship.ui.theme.LightPurple
import com.grommade.lazymusicianship.util.extentions.isEmpty
import com.grommade.lazymusicianship.util.extentions.toStringFormat
import java.time.LocalDate

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
            else -> viewModel.submitAction(action)
        }
    }
}

@Composable
fun StatisticsUi(
    viewState: StatisticsViewState,
    actioner: (StatisticsActions) -> Unit
) {
    val (values, filter, overallTime, averageTime) = viewState

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        contentColor = LightPurple
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
            Filters(filter, actioner)
            ChartBox(values, filter.timeMode, overallTime, averageTime)
        }
    }
}

@Composable
fun ChartItem(
    text: String,
    color: Color = MaterialTheme.colors.background,
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text12(text)
        Card(
            modifier = Modifier
                .size(height = 50.dp, width = 90.dp)
                .padding(top = 8.dp),
            backgroundColor = color,
            shape = RoundedCornerShape(15.dp),
            border = BorderStroke(1.dp, color = LightPurple)
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
fun Filters(
    filter: StatisticsFilter,
    actioner: (StatisticsActions) -> Unit
) {
    val (dateStart, dateEnd, mode) = filter
    val changeFilter = { flt: StatisticsFilter ->
        actioner(StatisticsActions.ChangeFilter(flt))
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp, top = 16.dp)
            .height(32.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(horizontalArrangement = Arrangement.SpaceBetween) {
            FilterByPeriod(mode) {
                changeFilter(filter.copy(timeMode = it))
            }
            FilterPeriod(dateStart, dateEnd) { dateStart: LocalDate, dateEnd: LocalDate ->
                changeFilter(filter.copy(dateStart = dateStart, dateEnd = dateEnd))
            }
        }
        FilterOther()
    }
}

@Composable
fun FilterByPeriod(
    timeMode: TimeChartMode,
    changeByPeriod: (TimeChartMode) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    val items = TimeChartMode.values().map { it.getTitle(LocalContext.current) }

    SmoothBox(
        contentColor = Color(0xFFDE395A),
        onClick = { expanded = true }) {
        Row(
            modifier = Modifier.padding(horizontal = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text12(items[timeMode.ordinal])
            Icon(
                imageVector = Icons.Default.ExpandMore,
                modifier = Modifier.padding(start = 4.dp),
                contentDescription = stringResource(R.string.cd_expand_more_icon)
            )
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.background(Color(0xFF0F1624))
        ) {
            items.forEachIndexed { ind, title ->
                DropdownMenuItem(onClick = {
                    changeByPeriod(TimeChartMode.values()[ind])
                    expanded = false
                }) {
                    val color = if (ind == timeMode.ordinal) Color(0xFF69921F) else LightPurple
                    Text12(title, color = color)
                }
            }
        }
    }
}

@Composable
fun FilterPeriod(
    dateStart: LocalDate,
    dateEnd: LocalDate,
    changePeriod: (LocalDate, LocalDate) -> Unit
) {

    val dialog = remember { AppDialog() }.apply {
        BuildPeriodDialog(dateStart, dateEnd, changePeriod)
    }
    SmoothBox(
        modifier = Modifier.padding(start = 16.dp),
        onClick = dialog::show
    ) {
        val dateText = { date: LocalDate ->
            if (date.isEmpty()) "..." else date.toStringFormat()
        }

        val text = when {
            dateStart.isEmpty() && dateEnd.isEmpty() -> "All time"
            else -> "${dateText(dateStart)} - ${dateText(dateEnd)}"
        }

        Text12(text, Modifier.padding(horizontal = 16.dp))
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
                .border(1.dp, LightPurple, shape)
                .clickable(onClick = onClick),
            contentAlignment = Alignment.Center
        ) {
            content()
        }
    }
}

@Composable
fun ChartBox(
    values: List<Pair<LocalDate, Float>>,
    timeMode: TimeChartMode,
    overallTime: Float,
    averageTime: Float
) {
    val shape = RoundedCornerShape(30.dp)
    Box(
        modifier = Modifier
            .padding(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 80.dp)
            .fillMaxSize()
            .clip(shape)
            .border(1.dp, LightPurple, shape)
    ) {
        if (values.isEmpty()) {
            Text(
                text = "No Data Available",
                modifier = Modifier.align(Alignment.Center),
                color = Color(0xFF2B2F44),
            )
        } else {
            Column {
                Stats(overallTime, averageTime)
                Chart(values, timeMode)
            }
        }
    }
}

@Composable
fun Stats(
    overallTime: Float,
    averageTime: Float
) {
    val modifier = Modifier.fillMaxWidth()

    val round = {value: Float ->
        "%.1f".format(value).replace(",", ".")
    }
    Row(
        modifier = modifier.padding(end = 32.dp, top = 8.dp),
        horizontalArrangement = Arrangement.End
    ) {
        Box(modifier = Modifier.width(90.dp)) {
            Column {
                Row(
                    modifier = modifier,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text12(stringResource(R.string.time_chart_title_overall_time))
                    Text12(stringResource(R.string.time_chart_overall_time, round(overallTime)), color = DarkYellow)
                }
                Row(
                    modifier = modifier.padding(top = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text12(stringResource(R.string.time_chart_title_average_time))
                    Text12(stringResource(R.string.time_chart_average_time, round(averageTime)), color = DarkYellow)
                }
            }
        }
    }
}

@Composable
fun Chart(
    values: List<Pair<LocalDate, Float>>,
    timeMode: TimeChartMode
) {
    val chart = remember { LineChart<LocalDate>() }

    val transformY = { format: LineChartTextFormat, value: Float ->
        val text = if (value == 0f) "" else value.toInt().toString() + "h"
        LineChartText(text, format)
    }
    val transformX = transformLineChartText(
        dates = values.map { it.first },
        intervalX = chart.intervalX,
        mode = timeMode
    )

    chart.Built(
        values = values,
        transformY = transformY,
        transformX = transformX
    )
    chart.show()
}


@Composable
fun Text12(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = Color.Unspecified
) {
    Text(
        text = text,
        modifier = modifier,
        fontSize = 12.sp,
        color = color
    )
}


@Preview
@Composable
fun StatisticsUiPreview8() {

//    val dates = mutableListOf<Pair<LocalDate, Float>>()
//    for (i in 0..15) {
//        dates.add(LocalDate.now().minusDays(-120).plusDays(i.toLong()) to 3.2f)
//    }
//    val startDay = LocalDate.now().plusDays(10)
//    var i = 0L
//    val dates = listOf(
//        startDay.plusDays(i++) to 0.5f,
//        startDay.plusDays(i++) to 1.1f,
//        startDay.plusDays(i++) to 2.2f,
//        startDay.plusDays(i++) to 0.4f,
//        startDay.plusDays(i++) to 1.5f,
//        startDay.plusDays(i++) to 2.5f,
//        startDay.plusDays(i++) to 3.5f,
//        startDay.plusDays(i++) to 3.2f,
//    )
    val startDay = LocalDate.now().minusMonths(8)
    var i = 0L
    val dates = listOf(
        startDay.plusYears(i++) to 0.5f,
//        startDay.plusYears(i++) to 1.1f,
//        startDay.plusYears(i++) to 2.2f,
//        startDay.plusMonths(i++) to 0.4f,
//        startDay.plusMonths(i++) to 1.5f,
//        startDay.plusMonths(i++) to 2.5f,
//        startDay.plusMonths(i++) to 3.5f,
//        startDay.plusMonths(i++) to 3.2f,
    )
    val state = StatisticsViewState(
        overTimeChartData = dates,
    )
    LazyMusicianshipTheme {
        StatisticsUi(state) {}
    }
}
