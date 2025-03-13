package com.example.zad2

import android.util.Log
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.waitForUpOrCancellation
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TimePickerDefaults
import androidx.compose.material3.TimePickerState
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import com.example.zad2.ui.theme.blackCustom
import com.example.zad2.ui.theme.grayCustom
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale


@Composable
fun DatePickerFieldToModal(selectedDate: Long?,
                           onDateSelected: (Long) -> Unit,
                           modifier: Modifier = Modifier) {

    var showModal by remember { mutableStateOf(false) }

    OutlinedTextField(
        value = selectedDate?.let { convertMillisToDate(it) } ?: "",
        onValueChange = { },
        label = { Text("Date") },
        colors = OutlinedTextFieldDefaults.colors(
            focusedTextColor = Color.White, // Kolor tekstu, gdy aktywne
            unfocusedTextColor = Color.White, // Kolor tekstu normalnie
            focusedBorderColor = Color.White, // Kolor obramowania po kliknięciu
            unfocusedBorderColor = Color.White, // Kolor obramowania normalnie
            focusedLabelColor = Color.White, // Kolor etykiety po kliknięciu
            unfocusedLabelColor = Color.White, // Kolor etykiety normalnie
            cursorColor = Color.White, // Kolor kursora
            focusedContainerColor = Color.DarkGray, // Tło pola po kliknięciu
            unfocusedContainerColor = grayCustom // Tło pola normalnie
        ),
        placeholder = { Text("MM/DD/YYYY") },

        trailingIcon = {
            Icon(Icons.Default.DateRange, contentDescription = "Select date")
        },

        modifier = modifier
            .fillMaxWidth()
            .pointerInput(selectedDate) {
                awaitEachGesture {
                    awaitFirstDown(pass = PointerEventPass.Initial)
                    val upEvent = waitForUpOrCancellation(pass = PointerEventPass.Initial)
                    if (upEvent != null) {
                        showModal = true
                    }
                }
            }
    )


    if (showModal) {
        DatePickerModal(
            onDateSelected = { newDate ->
                newDate?.let {
                    val calendar = Calendar.getInstance().apply {
                        timeInMillis = it
                        set(Calendar.HOUR_OF_DAY, 0)
                        set(Calendar.MINUTE, 0)
                        set(Calendar.SECOND, 0)
                        set(Calendar.MILLISECOND, 0)
                    }
                    onDateSelected(calendar.timeInMillis) // Przekazujemy tylko datę bez czasu
                    Log.v("DatePicker", "Selected date in milliseconds: ${calendar.timeInMillis}")
                }
                showModal = false
            },
            onDismiss = { showModal = false }
        )
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerModal(
    onDateSelected: (Long?) -> Unit,
    onDismiss: () -> Unit
) {
    val datePickerState = rememberDatePickerState()

    DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = {
                onDateSelected(datePickerState.selectedDateMillis)
                onDismiss()
            }) {
                Text("OK")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    ) {
        MaterialTheme(
            colorScheme = MaterialTheme.colorScheme.copy(
                onSurface = blackCustom,
                onSurfaceVariant = blackCustom
            )
        ) {
            DatePicker(state = datePickerState)
        }
    }
}


fun convertMillisToDate(millis: Long): String {
    val formatter = SimpleDateFormat("MM/dd/yyyy", Locale.getDefault())
    val formattedDate = formatter.format(Date(millis))
    return formattedDate
}