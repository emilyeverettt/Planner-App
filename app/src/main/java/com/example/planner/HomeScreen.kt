package com.example.planner

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.Locale

// Weekday data class
data class WeekDayItem(
    val date: LocalDate,
    val dayLabel: String,
    val dayNumber: Int,
    val isToday: Boolean
)

// DailyEvents data class
data class DailyEvent(
    val title: String,
    val startHour: Int,
    val startMinute: Int,
    val endHour: Int,
    val endMinute: Int
)

// DailyTo-Do data class
data class ToDoItem(
    val title: String,
    val isCompleted: Boolean = false
)

@Composable
fun HomeScreen() {
    val today = getFormattedDate()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(20.dp)
    ) {
        // Intro Welcome Message
        Column {
            Text(
                text = "Welcome, Emily!",
                fontSize = 28.sp,
                color = Color.Black
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Daily Date Display
            Text(
                text = today,
                fontSize = 16.sp,
                color = Color.Gray,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Right
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Weekly Calendar Strip
            WeeklyCalendarStrip()

            Spacer(modifier = Modifier.height(15.dp))

            // Daily Schedule Display
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Daily Schedule",
                    fontSize = 20.sp,
                    color = Color.Black,
                    fontWeight = FontWeight.Medium
                )

                Text(
                    text = "April Fools Day UK",
                    fontSize = 16.sp,
                    color = Color.Gray,
                    fontStyle = FontStyle.Italic
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
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
                    DailyTimeline(events = sampleEvents())
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // To-Do list
            Text(
                text = "To-Do",
                fontSize = 20.sp,
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
                    ToDoList(items = sampleToDo())
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

        }
    }
}

@Composable
// Weekly Calendar Strip UI
fun WeeklyCalendarStrip() {
    val weekDates = getCurrentWeekDates()

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        weekDates.forEach { item ->
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = item.dayLabel,
                    color = Color.DarkGray,
                    fontSize = 12.sp
                )

                Spacer(modifier = Modifier.height(8.dp))
                // Today's date highlighted
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(
                            if (item.isToday) Color(0xFFACC2B6) else Color(0xFFF7F7F7)
                        )
                        .then(
                            if (!item.isToday) {
                                Modifier.border(1.dp, Color(0xFFE5E5E5), CircleShape)
                            } else {
                                Modifier
                            }
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = item.dayNumber.toString(),
                        color = Color.Black,
                        fontSize = 14.sp,
                        fontWeight = if (item.isToday) FontWeight.SemiBold else FontWeight.Normal
                    )
                }
            }
        }
    }
}

@Composable
// Daily Event UI
fun DailyTimeline(events: List<DailyEvent>) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        for (hour in 0..23) {
            HourRow(
                hour = hour,
                events = events.filter { event ->
                    event.startHour == hour || (event.startHour < hour && event.endHour > hour)
                }
            )
        }
    }
}

@Composable
// Daily Schedule UI
fun EventCard(event: DailyEvent) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = Color(0xFFF6F1EA),
                shape = RoundedCornerShape(14.dp)
            )
            .padding(12.dp)
    ) {
        Column {
            Text(
                text = event.title,
                fontSize = 15.sp,
                color = Color.Black,
                fontWeight = FontWeight.Medium
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = formatEventTime(event),
                fontSize = 13.sp,
                color = Color.Gray
            )
        }
    }
}

// Sample Events for development purposes
fun sampleEvents(): List<DailyEvent> {
    return listOf(
        DailyEvent("Doctor appointment", 7, 30, 8, 0),
        DailyEvent("Meeting", 13, 0, 15, 0),
        DailyEvent("Gym", 18, 30, 19, 30)
    )
}

// Sample To-Do items for developmental purposes
fun sampleToDo(): List<ToDoItem> {
    return listOf(
        ToDoItem("Water Plants"),
        ToDoItem("Book Doctor's Appointment"),
        ToDoItem("Change Bedding"),
        ToDoItem("Pick up parcel"),
        ToDoItem("Call Thomas"),
        ToDoItem("Email Ben back")
    )
}


fun formatEventTime(event: DailyEvent): String {
    fun toAmPm(hour: Int, minute: Int): String {
        val displayHour = when {
            hour == 0 -> 12
            hour > 12 -> hour - 12
            else -> hour
        }
        val amPm = if (hour < 12) "am" else "pm"
        return String.format("%d:%02d%s", displayHour, minute, amPm)
    }

    return "${toAmPm(event.startHour, event.startMinute)} - ${toAmPm(event.endHour, event.endMinute)}"
}

@Composable
fun HourRow(hour: Int, events: List<DailyEvent>) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        verticalAlignment = Alignment.Top
    ) {
        Text(
            text = String.format("%02d:00", hour),
            fontSize = 13.sp,
            color = Color.Gray,
            modifier = Modifier.width(56.dp)
        )

        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(Color(0xFFEAEAEA))
            )

            Spacer(modifier = Modifier.height(8.dp))

            events.forEach { event ->
                EventCard(event)
                Spacer(modifier = Modifier.height(8.dp))
            }

            if (events.isEmpty()) {
                Spacer(modifier = Modifier.height(12.dp))
            }
        }
    }
}

@Composable
fun ToDoList(items: List<ToDoItem>) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        items.forEach { item ->
            ToDoCard(item)
            Spacer(modifier = Modifier.height(10.dp))
        }
    }
}

@Composable
fun ToDoCard(item: ToDoItem) {
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

// Get Date, Format Date
fun getFormattedDate(): String {
    val today = LocalDate.now()

    val dayName = today.dayOfWeek.getDisplayName(TextStyle.FULL, Locale.getDefault())
    val day = today.dayOfMonth
    val month = today.month.getDisplayName(TextStyle.FULL, Locale.getDefault())

    val suffix = when {
        day in 11..13 -> "th"
        day % 10 == 1 -> "st"
        day % 10 == 2 -> "nd"
        day % 10 == 3 -> "rd"
        else -> "th"
    }

    return "$dayName $day$suffix $month"
}

// Top day selector
fun getCurrentWeekDates(): List<WeekDayItem> {
    val today = LocalDate.now()
    val startOfWeek = today.minusDays((today.dayOfWeek.value - DayOfWeek.MONDAY.value).toLong())

    return (0..6).map { index ->
        val date = startOfWeek.plusDays(index.toLong())
        WeekDayItem(
            date = date,
            dayLabel = date.dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.getDefault()),
            dayNumber = date.dayOfMonth,
            isToday = date == today
        )
    }
}

@Preview(showBackground = true)
@Composable
fun HomePreview() {
    HomeScreen()
}