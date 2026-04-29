package com.example.planner

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.time.DayOfWeek
import java.time.YearMonth

// Monthly Calendar data class
data class CalendarDay(
    val dayNumber: Int,
    val isCurrentMonth: Boolean
)
// Placeholder data class for upcoming events
data class UpcomingPlacementItem(
    val title: String,
    val isCompleted: Boolean = false
)

@Composable
fun MonthlyView() {
    var expanded by remember { mutableStateOf(false) }
    var selectedYear by remember { mutableIntStateOf(2026) }

    val selectedMonth = 3 // March
    val calendarDays = remember(selectedYear) {
        getCalendarDays(selectedYear, selectedMonth)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(20.dp)
    ) {
        Text(
            text = "March",
            fontSize = 28.sp,
            color = Color.Black
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 4.dp),
            contentAlignment = Alignment.CenterEnd
        ) {
            Box {
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .clickable { expanded = !expanded }
                        .padding(horizontal = 8.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = selectedYear.toString(),
                        fontSize = 20.sp,
                        color = Color.Black
                    )

                    Icon(
                        imageVector = Icons.Filled.ArrowDropDown,
                        contentDescription = "Select year",
                        tint = Color.Black
                    )
                }

                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                ) {
                    (2020..2030).forEach { year ->
                        DropdownMenuItem(
                            text = {
                                Text(
                                    text = year.toString(),
                                    color = Color.Black
                                )
                            },
                            onClick = {
                                selectedYear = year
                                expanded = false
                            }
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(380.dp)
                .background(
                    color = Color(0xFFF8F5F0),
                    shape = RoundedCornerShape(20.dp)
                )
                .padding(16.dp)
        ) {
            CalendarContent(calendarDays = calendarDays)
        }

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = "Deadlines and Upcoming",
            fontSize = 22.sp,
            color = Color.Black,
            fontWeight = FontWeight.Medium
        )

        Spacer(modifier = Modifier.height(16.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(350.dp)
                .background(
                    color = Color(0xFFF8F5F0),
                    shape = RoundedCornerShape(20.dp)
                )
                .padding(12.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
            ) {
                UpcomingPlacementList(items = sampleUpcomingPlacement())
            }
        }
    }
}

@Composable
fun CalendarContent(calendarDays: List<CalendarDay>) {
    // Calendar format: weekday headers (Monday → Sunday)
    val weekdays = listOf("M", "T", "W", "T", "F", "S", "S")

    Column(modifier = Modifier.fillMaxSize()) {

        Spacer(modifier = Modifier.height(14.dp))
        // Calendar format: row displaying weekday labels
        Row(modifier = Modifier.fillMaxWidth()) {
            weekdays.forEach { day ->
                Box(
                    modifier = Modifier.weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = day,
                        fontSize = 12.sp,
                        color = Color(0xFF4A3A45),
                        fontWeight = FontWeight.ExtraBold
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))
        // Calendar format: main grid layout (7 columns = 7 days/week)
        LazyVerticalGrid(
            columns = GridCells.Fixed(7),
            modifier = Modifier.fillMaxWidth(),
            userScrollEnabled = false,
            verticalArrangement = Arrangement.spacedBy(10.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            items(calendarDays) { day ->
                CalendarDayCell(day)
            }
        }
    }
}

@Composable
fun CalendarDayCell(day: CalendarDay) {
    val textColor = if (day.isCurrentMonth) {
        Color(0xFF4A3A45) // main month
    } else {
        Color(0xFFACC2B6) // faded (prev/next month)
    }

    Box(
        modifier = Modifier
            .aspectRatio(1f),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = day.dayNumber.toString(),
            fontSize = 13.sp,
            color = textColor
        )
    }
}
// Calendar format: generates full calendar grid for given month/year
// Includes:
// - leading days from previous month
// - current month days
// - trailing days from next month
fun getCalendarDays(year: Int, month: Int): List<CalendarDay> {
    val yearMonth = YearMonth.of(year, month)
    val firstDayOfMonth = yearMonth.atDay(1)
    val daysInMonth = yearMonth.lengthOfMonth()

    val startOffset = when (firstDayOfMonth.dayOfWeek) {
        DayOfWeek.MONDAY -> 0
        DayOfWeek.TUESDAY -> 1
        DayOfWeek.WEDNESDAY -> 2
        DayOfWeek.THURSDAY -> 3
        DayOfWeek.FRIDAY -> 4
        DayOfWeek.SATURDAY -> 5
        DayOfWeek.SUNDAY -> 6
    }
    // Calendar format: previous month info (for leading days)
    val previousMonth = yearMonth.minusMonths(1)
    val previousMonthDays = previousMonth.lengthOfMonth()

    val result = mutableListOf<CalendarDay>()
    // Calendar format: add trailing days from previous month
    for (i in startOffset downTo 1) {
        result.add(
            CalendarDay(
                dayNumber = previousMonthDays - i + 1,
                isCurrentMonth = false
            )
        )
    }
    // Calendar format: add all days of current month
    for (day in 1..daysInMonth) {
        result.add(
            CalendarDay(
                dayNumber = day,
                isCurrentMonth = true
            )
        )
    }

    // Calendar format: fill remaining cells with next month days
    var nextMonthDay = 1
    while (result.size < 35) {
        result.add(
            CalendarDay(
                dayNumber = nextMonthDay,
                isCurrentMonth = false
            )
        )
        nextMonthDay++
    }

    return result
}
// Sample upcoming placement items for development purposes
fun sampleUpcomingPlacement(): List<UpcomingPlacementItem> {
    return listOf(
        UpcomingPlacementItem("Father's Day (UK) - In 17 days"),
        UpcomingPlacementItem("Thomas Birthday - In 23 days"),
        UpcomingPlacementItem("Dad's Birthday - In 34 days"),
        UpcomingPlacementItem("Arlo Christening - In 40 days"),
        UpcomingPlacementItem("Graduation Ceremony - In 48 days")
    )
}
@Composable
fun UpcomingPlacementList(items: List<UpcomingPlacementItem>) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        items.forEach { item ->
            UpcomingPlacementCard(item)
            Spacer(modifier = Modifier.height(10.dp))
        }
    }
}
@Composable
fun UpcomingPlacementCard(item: UpcomingPlacementItem) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = Color(0xFFF6F1EA),
                shape = RoundedCornerShape(14.dp)
            )
            .padding(horizontal = 12.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(18.dp)
                .clip(CircleShape)
                .background(
                    if (item.isCompleted) Color(0xFFACC2B6) else Color.White
                )
                .border(1.dp, Color(0xFFD8D2C8), CircleShape)
        )

        Spacer(modifier = Modifier.width(12.dp))

        Text(
            text = item.title,
            fontSize = 15.sp,
            color = if (item.isCompleted) Color.Gray else Color.Black
        )
    }
}
