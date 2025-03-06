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
import androidx.compose.material3.Icon
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.KeyboardType

import androidx.compose.material3.SegmentedButton
import androidx.compose.ui.text.font.FontWeight


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

@Composable
fun SingleChoiceSegmentedButton(selectedIndex: Int, onSelectedIndexChange: (Int) -> Unit) {

    val options = listOf("Hour", "Day", "Week")

    SingleChoiceSegmentedButtonRow {
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
fun MainScreen() {

    var num by remember{ mutableStateOf("") }
    var selectedIndex by remember { mutableIntStateOf(0) }
    var selectedDate by remember { mutableStateOf<Long?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(55.dp), // Odstęp od krawędzi ekranu
        verticalArrangement = Arrangement.spacedBy(10.dp, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally // Wyśrodkowanie w poziomie
    ) {
        ThemeLabel("Ultimate Time Traveller")
        Label1("Step 1. Choose a date")
        DatePickerDocked(onDateSelected = { selectedDate = it })
        Label1("Step 2. Choose number and type")
        Input1(num = num, onNumChange = { num = it })
        SingleChoiceSegmentedButton(selectedIndex = selectedIndex, onSelectedIndexChange = { selectedIndex = it })
        TimeTravelButton(num, selectedIndex, selectedDate)
        Label3()
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
                if (newNum.isEmpty() || (newNum.all { it.isDigit() })) {
                    onNumChange(newNum)
                }
                if(newNum.length == 1 && newNum[0] == '0'){
                    onNumChange("")
                }
            },
            label = { Text(text = "number") },
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
fun TimeTravelButton(num: String, selectedIndex: Int, selectedDate: Long?) {
    FilledTonalButton(onClick = {
        val convertedNum = num.toLongOrNull()

        val newDateString = if (selectedDate != null && convertedNum != null) {
            var newDateMillis: Long = 0
            if(selectedIndex == 0){
                newDateMillis = selectedDate + (convertedNum * 60 * 60 * 1000L)
            }
            if(selectedIndex == 1){
                newDateMillis = selectedDate + (convertedNum * 24 * 60 * 60 * 1000L)
            }
            if(selectedIndex == 2){
                newDateMillis = selectedDate + (convertedNum * 7 * 24 * 60 * 60 * 1000L)
            }
            convertMillisToDate(newDateMillis)
        } else {
            "Invalid date or number"
        }
        println("Button clicked!    $num ms    $selectedIndex   $selectedDate  ->  $newDateString")
    }) {
        Text("Time Travel!")
    }
}


@Composable
fun Label1(name: String) {
    Column(
        modifier = Modifier
            .height(60.dp),
        horizontalAlignment = Alignment.CenterHorizontally, // Wyśrodkowanie w poziomie
        verticalArrangement = Arrangement.Bottom
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
        verticalArrangement = Arrangement.Bottom
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
fun Label3() {
    Column(
        modifier = Modifier
            .height(100.dp),
        horizontalAlignment = Alignment.CenterHorizontally, // Wyśrodkowanie w poziomie
        verticalArrangement = Arrangement.Bottom
    ) {
        Text(
            text = "[aaaaaaa]",
            fontSize = 35.sp
        )
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerDocked(onDateSelected: (Long?) -> Unit) {
    var showDatePicker by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState(
    )

    val selectedDate = datePickerState.selectedDateMillis?.let {
        convertMillisToDate(it)
    } ?: ""

    Box(
        modifier = Modifier.fillMaxWidth()
    ) {
        OutlinedTextField(
            value = selectedDate,
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
                        onDateSelected(datePickerState.selectedDateMillis)
                    }
                }
            }
        }
    }
}

fun convertMillisToDate(millis: Long): String {
    val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    return formatter.format(Date(millis))
}

