package com.grommade.lazymusicianship.ui.components.dialogs

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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.compositeOver
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.grommade.lazymusicianship.R
import com.grommade.lazymusicianship.ui.components.IconChevronLeft
import com.grommade.lazymusicianship.ui.components.IconChevronRight
import com.grommade.lazymusicianship.ui.components.dialogs.AppDialog.Colors.backgroundColor
import com.grommade.lazymusicianship.ui.components.dialogs.AppDialog.Colors.lightBackgroundColor
import com.grommade.lazymusicianship.ui.components.dialogs.AppDialog.Colors.primaryColor
import com.grommade.lazymusicianship.ui.components.dialogs.AppDialog.Colors.primaryTextColor
import com.grommade.lazymusicianship.ui.components.dialogs.AppDialog.Colors.secondaryTextColor
import com.grommade.lazymusicianship.ui.components.timepicker.yearMonth
import com.grommade.lazymusicianship.util.extentions.*
import java.time.LocalDate
import java.time.YearMonth

// FIXME: Rename package

private const val COUNT_DAYS_CALENDAR = 7 * 6

@Composable
fun AppDialog.BuildPeriodDialog(
    dateStart: LocalDate = LocalDate.MIN,
    dateEnd: LocalDate = LocalDate.MAX,
    callback: (LocalDate, LocalDate) -> Unit
) {
    Build {
        DrawPeriod(dateStart to dateEnd, callback)
    }
}

@Composable
private fun AppDialog.DrawPeriod(
    period: Pair<LocalDate, LocalDate>,
    callback: (LocalDate, LocalDate) -> Unit
) {
    val focusManager = LocalFocusManager.current
    val daysOfWeek = stringResource(R.string.days_of_week).split(' ')

    val (dateStart, setDateStart) = remember { mutableStateOf(period.first) }
    val (dateEnd, setDateEnd) = remember { mutableStateOf(period.second) }

    val (monthStart, setMonthStart) = remember {
        mutableStateOf(
            when {
                period.first.isEmpty() -> LocalDate.now().minusMonths(1)
                else -> period.first
            }.yearMonth
        )
    }

    val (monthEnd, setMonthEnd) = remember {
        mutableStateOf(
            when {
                period.first.isEmpty() && period.second.isEmpty() -> LocalDate.now()
                period.second.isEmpty() -> dateStart.plusMonths(1)
                else -> period.second
            }.yearMonth
        )
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
            Column(Modifier.padding(16.dp)) {
                MonthBody(dateStart to dateEnd, dateStart, daysOfWeek, monthStart, setMonthStart, setDateStart)
                MonthBody(dateStart to dateEnd, dateEnd, daysOfWeek, monthEnd, setMonthEnd, setDateEnd)
                Confirmation(
                    enabledOk = dateStart <= dateEnd,
                    onClickOk = {
                        callback(dateStart, dateEnd)
                        hide(focusManager)
                    },
                    onClickCancel = { hide(focusManager) }
                )
            }
        }
    }
}

@Composable
private fun MonthBody(
    period: Pair<LocalDate, LocalDate>,
    date: LocalDate,
    daysOfWeek: List<String>,
    yearMonth: YearMonth,
    setMonth: (YearMonth) -> Unit,
    setDate: (LocalDate) -> Unit,
) {
    MonthYearHeader(yearMonth, setMonth)
    MonthGrid(period, daysOfWeek, yearMonth, setMonth, setDate)
    ClearDate(
        enabled = date.isNoEmpty(),
        emptyValue = if (date == period.first) LocalDate.MIN else LocalDate.MAX,
        setDate = setDate
    )
    HorizontalDivider()
}

@Composable
private fun MonthYearHeader(
    month: YearMonth,
    setMonth: (YearMonth) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconChevronLeft(0.3f) { setMonth(month.minusMonths(1)) }
        Text(month.asString())
        IconChevronRight(0.3f) { setMonth(month.plusMonths(1)) }
    }
}

@Composable
private fun MonthGrid(
    period: Pair<LocalDate, LocalDate>,
    daysOfWeek: List<String>,
    yearMonth: YearMonth,
    setMonth: (YearMonth) -> Unit,
    setDate: (LocalDate) -> Unit,
) {
    val dates = getDatesOfSection(period, yearMonth)
    Column {
        LazyVerticalGrid(cells = GridCells.Fixed(7)) {
            for (x in 0 until 7) {
                item {
                    Box(Modifier.size(32.dp), contentAlignment = Alignment.Center) {
                        Text(daysOfWeek.getOrElse(x) { "" })
                    }
                }
            }

            items(dates) {
                DateSelectionBox(it, setMonth, setDate)
            }
        }
    }
}

@Composable
fun ClearDate(
    enabled: Boolean,
    emptyValue: LocalDate,
    setDate: (LocalDate) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.End
    ) {
        Text(
            text = "Clear",
            fontSize = 12.sp,
            color = if (enabled) primaryColor else secondaryTextColor.copy(alpha = 0.4f),
            modifier = Modifier.clickable(enabled = enabled) { setDate(emptyValue) }
        )
    }
}

@Composable
private fun DateSelectionBox(
    dateState: DateState,
    setMonth: (YearMonth) -> Unit,
    setDate: (LocalDate) -> Unit
) {
    Box(
        Modifier
            .size(32.dp)
            .clickable(
                interactionSource = MutableInteractionSource(),
                onClick = {
                    setDate(dateState.date)
                    setMonth(dateState.date.yearMonth)
                },
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
private fun Confirmation(
    enabledOk: Boolean,
    onClickOk: () -> Unit,
    onClickCancel: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp),
        horizontalArrangement = Arrangement.End
    ) {
        ButtonCancel(onClickCancel)
        ButtonOk(enabledOk, onClickOk)
    }
}

@Composable
private fun ButtonCancel(onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .padding(end = 8.dp)
            .clip(RoundedCornerShape(8.dp)),
        colors = ButtonDefaults.buttonColors(
            backgroundColor = lightBackgroundColor,
            contentColor = secondaryTextColor
        ),
    ) {
        Text(stringResource(R.string.calendar_negative_button))
    }
}

@Composable
private fun ButtonOk(
    enabled: Boolean,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = Modifier.clip(RoundedCornerShape(8.dp)),
        colors = ButtonDefaults.buttonColors(
            backgroundColor = primaryColor,
            disabledBackgroundColor = primaryTextColor.copy(alpha = 0.12f)
                .compositeOver(primaryColor.copy(alpha = 0.32f))
        ),
        enabled = enabled,
    ) {
        Text(stringResource(R.string.calendar_positive_button))
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
    yearMonth: YearMonth
): List<DateState> {
    val monthDays = getDatesOfMonth(period, yearMonth, true)
    val lastMonthDays = getDatesOfMonth(period, yearMonth.minusMonths(1))
        .takeLast(monthDays.first().date.dayOfWeek.ordinal)
    val nextMonthDays = getDatesOfMonth(period, yearMonth.plusMonths(1))
        .take(COUNT_DAYS_CALENDAR - monthDays.count() - lastMonthDays.count())

    return lastMonthDays + monthDays + nextMonthDays

}

private fun getDatesOfMonth(
    period: Pair<LocalDate, LocalDate>,
    yearMonth: YearMonth,
    dateBelongCurrentMonth: Boolean = false
): List<DateState> {
    return (1..yearMonth.month.length(yearMonth.isLeapYear))
        .map {
            val shownDate = yearMonth.atEndOfMonth().withDayOfMonth(it)
            DateState(
                date = shownDate,
                currentMonth = dateBelongCurrentMonth,
                selected = dateBelongCurrentMonth && (shownDate.isToday() || shownDate.inside(period)),
                isFirst = dateBelongCurrentMonth && shownDate.sameDay(period.first),
                isLast = dateBelongCurrentMonth && shownDate.sameDay(period.second),
            )
        }
}

private fun LocalDate.inside(period: Pair<LocalDate, LocalDate>) =
    this >= period.first && this <= period.second

data class DateState(
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
    AppDialog().DrawPeriod(
        LocalDate.now().minusDays(2) to LocalDate.now().plusDays(20)
    ) { _, _ -> }
}