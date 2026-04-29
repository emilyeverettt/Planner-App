package com.example.planner

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewEvent() {

    var title by remember { mutableStateOf("") }

    var showStartTime by remember { mutableStateOf(false) }
    var showFinishTime by remember { mutableStateOf(false) }

    var startHourExpanded by remember { mutableStateOf(false) }
    var startMinuteExpanded by remember { mutableStateOf(false) }

    var finishHourExpanded by remember { mutableStateOf(false) }
    var finishMinuteExpanded by remember { mutableStateOf(false) }

    var selectedStartHour by remember { mutableStateOf("09") }
    var selectedStartMinute by remember { mutableStateOf("00") }

    var selectedFinishHour by remember { mutableStateOf("10") }
    var selectedFinishMinute by remember { mutableStateOf("00") }

    val hours = (0..23).map { it.toString().padStart(2, '0') }
    val minutes = (0..59).map { it.toString().padStart(2, '0') }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("New Event", fontSize = 28.sp, color = Color.Black)

        OutlinedTextField(
            value = title,
            onValueChange = { title = it },
            label = { Text("Title") },
            placeholder = { Text("Enter event title") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Text("Date", fontSize = 28.sp, color = Color.Black)

        if (showStartTime) {
            Text("Start Time", fontSize = 18.sp, color = Color.Black)

            TimeDropdownRow(
                selectedHour = selectedStartHour,
                selectedMinute = selectedStartMinute,
                hourExpanded = startHourExpanded,
                minuteExpanded = startMinuteExpanded,
                onHourExpandedChange = { startHourExpanded = it },
                onMinuteExpandedChange = { startMinuteExpanded = it },
                onHourSelected = { selectedStartHour = it },
                onMinuteSelected = { selectedStartMinute = it },
                hours = hours,
                minutes = minutes
            )

            TextButton(
                onClick = {
                    showStartTime = false
                    showFinishTime = false
                }
            ) {
                Text("- Remove Time")
            }

            if (showFinishTime) {
                Text("Finish Time", fontSize = 18.sp, color = Color.Black)

                TimeDropdownRow(
                    selectedHour = selectedFinishHour,
                    selectedMinute = selectedFinishMinute,
                    hourExpanded = finishHourExpanded,
                    minuteExpanded = finishMinuteExpanded,
                    onHourExpandedChange = { finishHourExpanded = it },
                    onMinuteExpandedChange = { finishMinuteExpanded = it },
                    onHourSelected = { selectedFinishHour = it },
                    onMinuteSelected = { selectedFinishMinute = it },
                    hours = hours,
                    minutes = minutes
                )

                TextButton(
                    onClick = { showFinishTime = false }
                ) {
                    Text("- Remove Finish Time")
                }
            } else {
                Button(
                    onClick = { showFinishTime = true },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("+ Add Finish Time")
                }
            }
        } else {
            Button(
                onClick = { showStartTime = true },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("+ Add Time")
            }
        }

        Text("Preview", fontSize = 18.sp, color = Color.Black)

        Button(
            onClick = { },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Save")
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimeDropdownRow(
    selectedHour: String,
    selectedMinute: String,
    hourExpanded: Boolean,
    minuteExpanded: Boolean,
    onHourExpandedChange: (Boolean) -> Unit,
    onMinuteExpandedChange: (Boolean) -> Unit,
    onHourSelected: (String) -> Unit,
    onMinuteSelected: (String) -> Unit,
    hours: List<String>,
    minutes: List<String>
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        ExposedDropdownMenuBox(
            expanded = hourExpanded,
            onExpandedChange = { onHourExpandedChange(!hourExpanded) },
            modifier = Modifier.weight(1f)
        ) {
            OutlinedTextField(
                value = selectedHour,
                onValueChange = {},
                readOnly = true,
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth(),
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = hourExpanded)
                }
            )

            ExposedDropdownMenu(
                expanded = hourExpanded,
                onDismissRequest = { onHourExpandedChange(false) }
            ) {
                hours.forEach { hour ->
                    DropdownMenuItem(
                        text = { Text(hour) },
                        onClick = {
                            onHourSelected(hour)
                            onHourExpandedChange(false)
                        }
                    )
                }
            }
        }

        Text(":", fontSize = 28.sp, color = Color.Black)

        ExposedDropdownMenuBox(
            expanded = minuteExpanded,
            onExpandedChange = { onMinuteExpandedChange(!minuteExpanded) },
            modifier = Modifier.weight(1f)
        ) {
            OutlinedTextField(
                value = selectedMinute,
                onValueChange = {},
                readOnly = true,
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth(),
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = minuteExpanded)
                }
            )

            ExposedDropdownMenu(
                expanded = minuteExpanded,
                onDismissRequest = { onMinuteExpandedChange(false) }
            ) {
                minutes.forEach { minute ->
                    DropdownMenuItem(
                        text = { Text(minute) },
                        onClick = {
                            onMinuteSelected(minute)
                            onMinuteExpandedChange(false)
                        }
                    )
                }
            }
        }
    }
}