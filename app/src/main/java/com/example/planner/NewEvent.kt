package com.example.planner

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
    var description by remember { mutableStateOf("") }
    var showDescription by remember { mutableStateOf(false) }

    var repeatOption by remember { mutableStateOf("Never") }
    var organiseOption by remember { mutableStateOf("None") }

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

    val calendarDays = remember {
        getCalendarDays(2026, 3)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Add New", fontSize = 28.sp, color = Color.Black)

        OutlinedTextField(
            value = title,
            onValueChange = { title = it },
            label = { Text("Title") },
            placeholder = { Text("Enter event title") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        if (showDescription) {
            Column(
                verticalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Description") },
                    placeholder = { Text("Enter description") },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 3
                )

                TextButton(
                    onClick = {
                        showDescription = false
                        description = ""
                    },
                    contentPadding = PaddingValues(0.dp)
                ) {
                    Text("- Remove", color = Color.Red)
                }
            }
        } else {
            Button(
                onClick = { showDescription = true },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFACC2B6)
                )
            ) {
                Text("+ Add Description")
            }
        }

        Text("Date:", fontSize = 20.sp, color = Color.Black)

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

        Text("Repeat:", fontSize = 18.sp, color = Color.Black)

        RepeatDropdownRow(
            selectedOption = repeatOption,
            onOptionSelected = { repeatOption = it }
        )

        if (showStartTime) {
            Text("Start Time:", fontSize = 18.sp, color = Color.Black)

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
                },
                contentPadding = PaddingValues(0.dp)
            ) {
                Text("- Remove Time", color = Color.Red)
            }

            if (showFinishTime) {
                Text("Finish Time:", fontSize = 18.sp, color = Color.Black)

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
                    onClick = { showFinishTime = false },
                    contentPadding = PaddingValues(0.dp)
                ) {
                    Text("- Remove Finish Time", color = Color.Red)
                }
            } else {
                Button(
                    onClick = { showFinishTime = true },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFACC2B6)
                    )
                ) {
                    Text("+ Add Finish Time")
                }
            }
        } else {
            Button(
                onClick = { showStartTime = true },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFACC2B6)
                )
            ) {
                Text("+ Add Time")
            }
        }

        Text("Organise:", fontSize = 18.sp, color = Color.Black)

        OrganiseDropdownRow(
            selectedOption = organiseOption,
            onOptionSelected = { organiseOption = it }
        )

        Text("Preview:", fontSize = 18.sp, color = Color.Black)

        PreviewBox(
            title = title,
            description = description,
            showDescription = showDescription,
            showStartTime = showStartTime,
            showFinishTime = showFinishTime,
            startTime = "$selectedStartHour:$selectedStartMinute",
            finishTime = "$selectedFinishHour:$selectedFinishMinute",
            repeat = repeatOption,
            organise = organiseOption
        )

        Button(
            onClick = { },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFACC2B6)
            )
        ) {
            Text("Save")
        }
    }
}

@Composable
fun PreviewBox(
    title: String,
    description: String,
    showDescription: Boolean,
    showStartTime: Boolean,
    showFinishTime: Boolean,
    startTime: String,
    finishTime: String,
    repeat: String,
    organise: String
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = Color(0xFFF8F5F0),
                shape = RoundedCornerShape(20.dp)
            )
            .padding(16.dp)
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {

            Text(
                text = if (title.isBlank()) "Untitled Event" else title,
                fontSize = 18.sp,
                color = Color.Black
            )

            if (showDescription && description.isNotBlank()) {
                Text(description, fontSize = 14.sp, color = Color.DarkGray)
            }

            Text("Date: March 2026", fontSize = 14.sp, color = Color.DarkGray)
            Text("Repeat: $repeat", fontSize = 14.sp, color = Color.DarkGray)
            Text("Organise: $organise", fontSize = 14.sp, color = Color.DarkGray)

            if (showStartTime) {
                Text(
                    text = if (showFinishTime) {
                        "Time: $startTime - $finishTime"
                    } else {
                        "Time: $startTime"
                    },
                    fontSize = 14.sp,
                    color = Color.DarkGray
                )
            } else {
                Text("Time: No time set", fontSize = 14.sp, color = Color.DarkGray)
            }
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RepeatDropdownRow(
    selectedOption: String,
    onOptionSelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    val options = listOf(
        "Never",
        "Daily",
        "Weekly",
        "Monthly",
        "Custom"
    )

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
        modifier = Modifier.fillMaxWidth()
    ) {
        OutlinedTextField(
            value = selectedOption,
            onValueChange = {},
            readOnly = true,
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth(),
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
            }
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option) },
                    onClick = {
                        onOptionSelected(option)
                        expanded = false
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrganiseDropdownRow(
    selectedOption: String,
    onOptionSelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    val options = listOf(
        "None",
        "Chores",
        "Medications",
        "Houseplants"
    )

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
        modifier = Modifier.fillMaxWidth()
    ) {
        OutlinedTextField(
            value = selectedOption,
            onValueChange = {},
            readOnly = true,
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth(),
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
            }
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option) },
                    onClick = {
                        onOptionSelected(option)
                        expanded = false
                    }
                )
            }
        }
    }
}