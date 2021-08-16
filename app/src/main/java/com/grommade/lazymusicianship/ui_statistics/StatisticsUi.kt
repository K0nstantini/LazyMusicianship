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
import com.grommade.lazymusicianship.ui.charts.line_chart.transformLineChartText
import com.grommade.lazymusicianship.ui.charts.line_chart_core.LineChart
import com.grommade.lazymusicianship.ui.charts.line_chart_core.LineChartText
import com.grommade.lazymusicianship.ui.charts.line_chart_core.LineChartTextFormat
import com.grommade.lazymusicianship.ui.common.rememberFlowWithLifecycle
import com.grommade.lazymusicianship.ui.theme.LazyMusicianshipTheme
import com.grommade.lazymusicianship.ui.theme.TextPurple
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
            ChartBox(viewState.overTimeChartData)
        }
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
            border = BorderStroke(1.dp, color = TextPurple)
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
            .padding(start = 16.dp, end = 16.dp, top = 16.dp)
            .height(32.dp),
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
            Text("By months", fontSize = 12.sp)
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
    SmoothBox(
        modifier = Modifier.padding(start = 16.dp),
        onClick = { /*TODO*/ }
    ) {
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
fun ChartBox(values: List<Pair<LocalDate, Float>>) {
    val shape = RoundedCornerShape(30.dp)
    Box(
        modifier = Modifier
            .padding(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 80.dp)
            .fillMaxSize()
            .clip(shape)
            .border(1.dp, TextPurple, shape)
    ) {
        if (values.isEmpty()) {
            Text(
                text = "No Data Available",
                modifier = Modifier.align(Alignment.Center),
                color = Color(0xFF2B2F44),
            )
        } else {
            Column {
                Stats()
                Chart(values)
            }
        }
    }
}

@Composable
fun Stats() {
    val modifier = Modifier.fillMaxWidth()
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
                    Text12("Overall:")
                    Text12("10.7h", Color(0xFFAAB694))
                }
                Row(
                    modifier = modifier.padding(top = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text12("Average:")
                    Text12("1.0h", Color(0xFFAAB694))
                }
            }
        }
    }
}

@Composable
fun Chart(values: List<Pair<LocalDate, Float>>) {
    val chart = remember { LineChart<LocalDate>() }

    val transformY = { format: LineChartTextFormat, value: Float ->
        val text = if (value == 0f) "" else value.toInt().toString() + "h"
        LineChartText(text, format)
    }
    val transformX = transformLineChartText(
        dates = values.map { it.first },
        intervalX = chart.intervalX
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
    color: Color = Color.Unspecified
) {
    Text(text, fontSize = 12.sp, color = color)
}


@Preview
@Composable
fun StatisticsUiPreview8() {

    val dates = mutableListOf<Pair<LocalDate, Float>>()
    for (i in 0..22) {
        dates.add(LocalDate.now().minusDays(-120).plusDays(i.toLong()) to 3.2f)
    }
//    val data = listOf(
//        startDay.plusDays(1) to 0.5f,
//        startDay.plusDays(2) to 1.1f,
//        startDay.plusDays(3) to 2.2f,
//        startDay.plusDays(4) to 0.3f,
//        startDay.plusDays(5) to 1.5f,
//        startDay to 2.7f,
//    )
    val state = StatisticsViewState(
        overTimeChartData = dates
    )
    LazyMusicianshipTheme {
        StatisticsUi(state) {}
    }
}
