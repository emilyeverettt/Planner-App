package com.example.planner

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.Locale


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            HomeScreen()
        }
    }
}

data class WeekDayItem(
    val date: LocalDate,
    val dayLabel: String,
    val dayNumber: Int,
    val isToday: Boolean
)

@Composable
fun HomeScreen() {
    val today = getFormattedDate()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(24.dp)
    ) {
        Column {
            Text(
                text = "Welcome, Nina!",
                fontSize = 28.sp,
                color = Color.Black
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = today,
                fontSize = 16.sp,
                color = Color.Gray,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Right
            )

            Spacer(modifier = Modifier.height(20.dp))

            WeeklyCalendarStrip()
        }
    }
}

@Composable
fun WeeklyCalendarStrip() {
    val weekDates = getCurrentWeekDates()

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        weekDates.forEach { item ->
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = item.dayLabel,
                    color = Color.Gray,
                    fontSize = 12.sp
                )

                Spacer(modifier = Modifier.height(8.dp))

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
