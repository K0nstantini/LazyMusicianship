package com.grommade.lazymusicianship.ui.components.diapogs

import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.grommade.lazymusicianship.R
import com.grommade.lazymusicianship.ui.components.IconChevronLeft
import com.grommade.lazymusicianship.ui.components.IconChevronRight
import com.grommade.lazymusicianship.ui.components.diapogs.AppDialog.Colors.backgroundColor
import com.grommade.lazymusicianship.ui.components.diapogs.AppDialog.Colors.lightBackgroundColor
import com.grommade.lazymusicianship.ui.components.diapogs.AppDialog.Colors.primaryColor
import com.grommade.lazymusicianship.ui.components.diapogs.AppDialog.Colors.primaryTextColor
import com.grommade.lazymusicianship.ui.components.diapogs.AppDialog.Colors.secondaryTextColor
import com.grommade.lazymusicianship.util.extentions.isEmpty
import com.grommade.lazymusicianship.util.extentions.isToday
import com.grommade.lazymusicianship.util.extentions.sameDay
import java.time.LocalDate

// FIXME: Rename package

const val COUNT_DAYS_CALENDAR_DEL = 7 * 6

@Composable
fun AppDialog.BuildPeriodDialogDel(
    dateStart: LocalDate,
    dateEnd: LocalDate,
) {
    Build {
        DrawPeriodDel(dateStart to dateEnd)
    }
}

@Composable
private fun AppDialog.DrawPeriodDel(
    period: Pair<LocalDate, LocalDate>
) {
    val daysOfWeek = stringResource(R.string.days_of_week).split(' ')

    val dateStart = when {
        period.first.isEmpty() -> LocalDate.now().minusMonths(1)
        else -> period.first
    }

    val dateEnd = when {
        period.first.isEmpty() && period.second.isEmpty()-> LocalDate.now()
        period.second.isEmpty() -> dateStart.plusMonths(1)
        else -> period.second
    }

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp, horizontal = 8.dp)
            .clipToBounds(),
        color = Color.Transparent,
        contentColor = primaryTextColor
    ) {
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(10.dp))
                .background(backgroundColor)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                ) {
                    Presets()

                }
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(2f)
                ) {
                    Column {
                        MonthBody(period, dateStart, daysOfWeek)
                        MonthBody(period, dateEnd, daysOfWeek)
                        Confirmation()
                    }
                }
            }
        }
    }
}

@Composable
private fun Presets() {
    Column {
        PresetText(R.string.preset_last_7_days)
        PresetText(R.string.preset_last_30_days)
        PresetText(R.string.preset_last_12_months)
        PresetText(R.string.preset_week_to_date)
        PresetText(R.string.preset_month_to_date)
        PresetText(R.string.preset_year_to_date)
        PresetText(R.string.preset_all_time)
    }
}

@Composable
private fun PresetText(@StringRes res: Int) {
    Text(
        text = stringResource(res),
        color = secondaryTextColor,
        modifier = Modifier.padding(bottom = 16.dp)
    )
}

@Composable
private fun MonthBody(
    period: Pair<LocalDate, LocalDate>,
    date: LocalDate,
    daysOfWeek: List<String>
) {
    MonthYearHeader(date)
    MonthGrid(period, date, daysOfWeek)
    HorizontalDivider()
}

@Composable
private fun MonthYearHeader(date: LocalDate) {
    val month = date.month.name.lowercase().replaceFirstChar { it.uppercase() }

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        IconChevronLeft(0.3f)
        Text("$month ${date.year}")
        IconChevronRight(0.3f)
    }
}

@Composable
private fun MonthGrid(
    period: Pair<LocalDate, LocalDate>,
    date: LocalDate,
    daysOfWeek: List<String>
) {
    val currentDate = when {
        date.isEmpty() -> LocalDate.now()
        else -> date
    }
    val dates = getDatesOfSection(period, currentDate)
    Column() {
        LazyVerticalGrid(cells = GridCells.Fixed(7)) {
            for (x in 0 until 7) {
                item {
                    Box(Modifier.size(36.dp), contentAlignment = Alignment.Center) {
                        Text(daysOfWeek.getOrElse(x) { "" })
                    }
                }
            }

            items(dates) {
                DateSelectionBox(it) {}
            }
        }
    }
}

@Composable
private fun DateSelectionBox(dateState: DateStateDel, onClick: () -> Unit) {
    Box(
        Modifier
            .size(36.dp)
            .clickable(
                interactionSource = MutableInteractionSource(),
                onClick = onClick,
                indication = null
            )
            .clip(dateState.shape)
            .background(dateState.colorBackground),
        contentAlignment = Alignment.Center
    ) {
        Text(
            dateState.name,
            modifier = Modifier
                .size(32.dp)
                .clip(CircleShape)
                .wrapContentSize(Alignment.Center),
            style = TextStyle(
                color = dateState.colorText,
                fontSize = 12.sp
            )
        )
    }
}

@Composable
private fun Confirmation() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp),
        horizontalArrangement = Arrangement.End
    ) {
        ButtonCancel()
        ButtonOk()
    }
}

@Composable
private fun ButtonCancel() {
    Button(
        onClick = { /*TODO*/ },
        modifier = Modifier.padding(end = 8.dp),
        colors = ButtonDefaults.buttonColors(
            backgroundColor = lightBackgroundColor,
            contentColor = secondaryTextColor
        ),
    ) {
        Text(text = "Cancel")
    }
}

@Composable
private fun ButtonOk() {
    Button(
        onClick = { /*TODO*/ },
        colors = ButtonDefaults.buttonColors(backgroundColor = primaryColor),
    ) {
        Text(text = "OK")
    }
}

@Composable
private fun HorizontalDivider() {
    Divider(
        modifier = Modifier.padding(vertical = 8.dp),
        color = MaterialTheme.colors.onSurface.copy(alpha = 0.05f)
    )
}

private fun getDatesOfSection(
    period: Pair<LocalDate, LocalDate>,
    date: LocalDate
): List<DateStateDel> {
    val monthDays = getDatesOfMonth(period, date, true)
    val lastMonthDays = getDatesOfMonth(period, date.minusMonths(1))
        .takeLast(monthDays.first().date.dayOfWeek.ordinal)
    val nextMonthDays = getDatesOfMonth(period, date.plusMonths(1))
        .take(COUNT_DAYS_CALENDAR_DEL - monthDays.count() - lastMonthDays.count())

    return lastMonthDays + monthDays + nextMonthDays

}

private fun getDatesOfMonth(
    period: Pair<LocalDate, LocalDate>,
    date: LocalDate,
    currentMonth: Boolean = false
): List<DateStateDel> {
    return (1..date.month.length(date.isLeapYear))
        .map {
            val localDate = date.withDayOfMonth(it)
            DateStateDel(
                date = localDate,
                currentMonth = currentMonth,
                selected = currentMonth && (localDate.isToday() || localDate.inside(period)),
                isFirst = currentMonth && localDate.sameDay(period.first),
                isLast = currentMonth && localDate.sameDay(period.second),
            )
        }
}

private fun LocalDate.inside(period: Pair<LocalDate, LocalDate>) =
    this >= period.first && this <= period.second

data class DateStateDel(
    val date: LocalDate,
    private val currentMonth: Boolean = false,
    private val selected: Boolean = false,
    private val isFirst: Boolean = false,
    private val isLast: Boolean = false,
) {

    val name = date.dayOfMonth.toString()

    val colorBackground = when {
        isFirst || isLast -> primaryColor
        selected -> lightBackgroundColor
        else -> backgroundColor
    }
    val colorText = when {
        selected -> primaryTextColor
        currentMonth -> secondaryTextColor
        else -> secondaryTextColor.copy(alpha = 0.4f)
    }

    private val roundSize = 10f
    val shape = when {
        isFirst -> RoundedCornerShape(topStart = roundSize, bottomStart = roundSize)
        isLast -> RoundedCornerShape(topEnd = roundSize, bottomEnd = roundSize)
        else -> RoundedCornerShape(0f)
    }
}

@Preview
@Composable
private fun DrawPeriodPreview() {
    AppDialog().DrawPeriodDel(
//        LocalDate.now().minusDays(20) to LocalDate.now().minusDays(-35)
//        LocalDate.MIN to LocalDate.MAX
//        LocalDate.MIN to LocalDate.now()
//        LocalDate.now() to LocalDate.now().plusDays(35)
        LocalDate.now().plusDays(52) to LocalDate.MAX
    )
}