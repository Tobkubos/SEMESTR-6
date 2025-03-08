package com.example.zad1


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.zad1.ui.theme.Zad1Theme
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import androidx.compose.material3.DatePicker
import java.text.SimpleDateFormat
import java.util.*
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.graphics.Color

import androidx.compose.material3.SegmentedButton
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Zad1Theme {
                MainScreen()
            }
        }
    }
}

fun calculateDate(num: String, selectedIndex: Int, selectedDate: Long?, selectedDirection: Int, hours: Int?, minutes: Int?, onNewDateStringChange: (String) -> Unit) {
    val convertedNum = num.toLongOrNull()

    val updatedDateString = if (selectedDate != null && convertedNum != null && hours != null && minutes != null) {
        val dir = if (selectedDirection == 0) -1 else 1
        val newDateMillis = when (selectedIndex) {
            0 -> (selectedDate + (hours * 60 * 60 * 1000L) + (minutes * 60 * 1000L)) + dir * convertedNum * 60 * 60 * 1000L // Godziny
            1 -> (selectedDate + (hours * 60 * 60 * 1000L) + (minutes * 60 * 1000L)) + dir * convertedNum * 24 * 60 * 60 * 1000L // Dni
            2 -> (selectedDate + (hours * 60 * 60 * 1000L) + (minutes * 60 * 1000L)) + dir * convertedNum * 7 * 24 * 60 * 60 * 1000L // Tygodnie
            else -> selectedDate
        }
        convertMillisToDate(newDateMillis, 1)
    } else {
        "Invalid date or number"
    }
    onNewDateStringChange(updatedDateString)
}

@Composable
fun MainScreen() {

    var num by remember{ mutableStateOf("") }
    var selectedIndex by remember { mutableIntStateOf(0) }
    var selectedDirection by remember { mutableIntStateOf(0) }

    val currentDate = Calendar.getInstance().apply {
        set(Calendar.HOUR_OF_DAY, 0)   // Ustawiamy godzinę na 0
        set(Calendar.MINUTE, 0)         // Ustawiamy minutę na 0
        set(Calendar.SECOND, 0)         // Ustawiamy sekundę na 0
        set(Calendar.MILLISECOND, 0)    // Ustawiamy milisekundy na 0
    }
    val currentDateMillis = currentDate.timeInMillis

    var selectedDate by remember { mutableStateOf<Long?>(currentDateMillis) }
    var newDateString by remember { mutableStateOf("") }
    var selectedHour by remember { mutableIntStateOf(12) }
    var selectedMinute by remember { mutableIntStateOf(0) }

    LaunchedEffect(num, selectedIndex, selectedDate, selectedDirection, selectedHour, selectedMinute) {
        calculateDate(num, selectedIndex, selectedDate, selectedDirection, hours = selectedHour, minutes = selectedMinute) {
            newDateString = it
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(55.dp), // Odstęp od krawędzi ekranu
        verticalArrangement = Arrangement.spacedBy(10.dp, Alignment.Top),
        horizontalAlignment = Alignment.CenterHorizontally // Wyśrodkowanie w poziomie
    ) {
        ThemeLabel("Time Traveller")
        Label1("Step 1. Choose date and time")
        DatePickerDocked(onDateSelected = { selectedDate = it })
        TimePickerDocked(onTimeSelected = { hour, minute ->
                                                selectedHour = hour
                                                selectedMinute = minute})
        Label1("Step 2. Choose value and type")
        Input1(num = num, onNumChange = { num = it })
        SingleChoiceSegmentedButton(selectedIndex = selectedIndex, onSelectedIndexChange = { selectedIndex = it })
        SingleChoiceSegmentedButton2(selectedIndex = selectedDirection, onSelectedIndexChange = { selectedDirection = it })
        //TimeTravelButton(num, selectedIndex, selectedDate, selectedDirection, onNewDateStringChange = {newDateString = it})
        HorizontalDivider(thickness = 2.dp)
        Label3(output = newDateString, num = num, idx = selectedIndex, direction = selectedDirection, selectedHour, selectedMinute)
        HorizontalDivider(thickness = 2.dp)
    }
}

@Composable
fun SingleChoiceSegmentedButton(selectedIndex: Int, onSelectedIndexChange: (Int) -> Unit) {

    val options = listOf("Hour", "Day", "Week")

    SingleChoiceSegmentedButtonRow (
        modifier = Modifier.fillMaxWidth() // Rozciąga cały wiersz na szerokość)
    ){
        options.forEachIndexed { index, label ->
            SegmentedButton(
                shape = SegmentedButtonDefaults.itemShape(
                    index = index,
                    count = options.size
                ),
                onClick = { onSelectedIndexChange(index) },
                selected = index == selectedIndex,
                label = { Text(label) }
            )
        }
    }
}

@Composable
fun SingleChoiceSegmentedButton2(selectedIndex: Int, onSelectedIndexChange: (Int) -> Unit) {

    val options = listOf("Past", "Future")

    SingleChoiceSegmentedButtonRow (
        modifier = Modifier.fillMaxWidth() // Rozciąga cały wiersz na szerokość)
    ){
        options.forEachIndexed { index, label ->
            SegmentedButton(
                shape = SegmentedButtonDefaults.itemShape(
                    index = index,
                    count = options.size
                ),
                onClick = { onSelectedIndexChange(index) },
                selected = index == selectedIndex,
                label = { Text(label) }

            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    Zad1Theme {
        MainScreen()
    }
}

@Composable
fun Input1(num: String, onNumChange: (String) -> Unit) {
    Column {

        val keyboardController = LocalSoftwareKeyboardController.current

        TextField(
            value = num,
            onValueChange = { newNum ->
                if (newNum.isEmpty() || (newNum.all { it.isDigit() } && newNum.length <= 5)) {
                    onNumChange(newNum)
                }
                if(newNum.length == 1 && newNum[0] == '0'){
                    onNumChange("")
                }
            },
            label = { Text(text = "value") },
            singleLine = true,

            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Number
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    keyboardController?.hide()
                }
            )
        )
    }
}


@Composable
fun Label1(name: String) {
    Column(
        modifier = Modifier
            .height(25.dp),
        horizontalAlignment = Alignment.CenterHorizontally, // Wyśrodkowanie w poziomie
        verticalArrangement = Arrangement.Top
    ) {
        Text(
            text = name,
        )
    }
}

@Composable
fun ThemeLabel(name: String) {
    Column(
        modifier = Modifier
            .height(100.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = name,
            fontSize = 25.sp,
            maxLines = 1,
            fontWeight = FontWeight.W900
        )
    }
}

@Composable
fun Label3(output: String, num: String, idx: Int, direction: Int, h: Int, m: Int) {
    val shouldShow = output.isNotEmpty() && num.isNotEmpty()

    Column(
        modifier = Modifier
            .height(65.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        if (shouldShow) {
            val idxText = when (idx) {
                0 -> "Hours"
                1 -> "Days"
                2 -> "Weeks"
                else -> "Hours"
            }

            val directionText = if (direction == 0) "Past" else "Future"

            Text(
                text = "in the $directionText of $num $idxText: $output",
                fontSize = 20.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        } else {
            Text(
                text = "please insert value to calculate time travel",
                color = Color.Red,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
            //Spacer(modifier = Modifier.height(24.dp))
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerDocked(onDateSelected: (Long?) -> Unit) {
    var showDatePicker by remember { mutableStateOf(false) }

    val currentDateMillis = Calendar.getInstance().timeInMillis

    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = currentDateMillis // Ustawienie domyślnej daty na bieżącą
    )


    //ustawienie godziny na 00:00
    val selectedDate = datePickerState.selectedDateMillis?.let {
        val calendar = Calendar.getInstance().apply {
            timeInMillis = it
            set(Calendar.HOUR_OF_DAY, 0)     // Ustawienie godziny na 0
            set(Calendar.MINUTE, 0)          // Ustawienie minut na 0
            set(Calendar.SECOND, 0)          // Ustawienie sekund na 0
            set(Calendar.MILLISECOND, 0)     // Ustawienie milisekund na 0
        }
        calendar.timeInMillis
    }

    Box(
        modifier = Modifier.fillMaxWidth()
    ) {
        OutlinedTextField(
            value = selectedDate?.let { convertMillisToDate(it, 0) } ?: "", // Wyświetlenie daty z zerowym czasem
            onValueChange = { },
            label = { Text("Date") },
            readOnly = true,
            trailingIcon = {
                IconButton(onClick = { showDatePicker = !showDatePicker }) {
                    Icon(
                        imageVector = Icons.Default.DateRange,
                        contentDescription = "Select date"
                    )
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp)
        )

        if (showDatePicker) {
            Popup(
                onDismissRequest = { showDatePicker = false },
                alignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .offset(y = 64.dp)
                        .shadow(elevation = 4.dp)
                        .background(MaterialTheme.colorScheme.surface)
                        .padding(16.dp)
                ) {
                    DatePicker(
                        state = datePickerState,
                        showModeToggle = false
                    )
                    LaunchedEffect(datePickerState.selectedDateMillis) {
                        // Przekształcenie daty na 00:00 i przekazanie do onDateSelected
                        onDateSelected(selectedDate)
                    }
                }
            }
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimePickerDocked(onTimeSelected: (Int, Int) -> Unit) {
    var showTimePicker by remember { mutableStateOf(false) }

    val timePickerState = rememberTimePickerState(
        initialHour = 12,
        initialMinute = 0,
        is24Hour = true
    )

    // Przygotowanie wyświetlanej godziny
    val selectedTime = String.format("%02d:%02d", timePickerState.hour, timePickerState.minute)

    Box(
        modifier = Modifier.fillMaxWidth(),
    ) {
        OutlinedTextField(
            value = selectedTime,
            onValueChange = { },
            label = { Text("Time") },
            readOnly = true,
            trailingIcon = {
                IconButton(onClick = { showTimePicker = !showTimePicker }) {
                    Icon(
                        imageVector = Icons.Default.Add, // Można zmienić ikonę na zegar
                        contentDescription = "Select time"
                    )
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp)
        )

        if (showTimePicker) {
            Popup(
                onDismissRequest = { showTimePicker = false },
                alignment = Alignment.Center // Ustawić na środku ekranu
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize() // Wypełnia cały dostępny obszar ekranu
                        .wrapContentSize(align = Alignment.Center) // Ustawi zawartość na środku
                        .shadow(elevation = 4.dp)
                        .background(MaterialTheme.colorScheme.surface)
                        .padding(16.dp)
                ) {
                    Column {
                        TimePicker(
                            state = timePickerState,
                        )

                        Button(onClick = {
                            // Zatwierdzenie godziny
                            onTimeSelected(timePickerState.hour, timePickerState.minute)
                            showTimePicker = false
                        },
                            modifier = Modifier.align(Alignment.End)
                            ){
                            Text("Confirm")

                        }
                    }
                }
            }
        }
    }
}

fun convertMillisToDate(millis: Long, formatType: Int): String {
    val formatter: SimpleDateFormat
    if(formatType == 1) {
        formatter = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
    }
    else {
    formatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    }
    return formatter.format(Date(millis))
}

