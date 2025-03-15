package com.example.zad2

import android.R
import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface

import androidx.compose.material3.Text
import androidx.compose.material3.lightColorScheme

import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.zad2.ui.theme.Zad2Theme

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.PreviewDynamicColors
import androidx.compose.ui.tooling.preview.PreviewLightDark

import androidx.compose.ui.unit.sp
import com.example.zad2.ui.theme.blackCustom
import java.util.Locale


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContent {
            Zad2Theme {
                MainScreen()
            }
        }
    }

    override fun attachBaseContext(newBase: Context) {
        val locale = Locale("en") // Wymuszenie języka angielskiego
        Locale.setDefault(locale)

        val config = Configuration(newBase.resources.configuration)
        config.setLocale(locale)

        val context = newBase.createConfigurationContext(config)
        super.attachBaseContext(context)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
fun MainScreen() {
    var selectedTime1 by remember { mutableStateOf<Long?>(null) }
    var selectedTime2 by remember { mutableStateOf<Long?>(null) }
    var selectedDate1 by remember { mutableStateOf<Long?>(null) }
    var selectedDate2 by remember { mutableStateOf<Long?>(null) }
    var Weeks by remember { mutableStateOf("0") }
    var Days by remember { mutableStateOf("0") }
    var Hours by remember { mutableStateOf("0") }
    var Minutes by remember { mutableStateOf("0") }
    Surface {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Label("Time Calculator", 25, blackCustom)

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .background(
                    color = MaterialTheme.colorScheme.primaryContainer,
                    shape = RoundedCornerShape(16.dp) // Zaokrąglone rogi
                )
                .padding(16.dp),

            contentAlignment = Alignment.Center,
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Label("Date 1", 20, Color.White)
                DatePickerFieldToModal(selectedDate = selectedDate1, onDateSelected = { newDate -> selectedDate1 = newDate })
                TimePickerFieldToModal(selectedTime = selectedTime1, onTimeSelected = { newTime -> selectedTime1 = newTime })
            }
        }

        HorizontalDivider(thickness = 5.dp, color = blackCustom)

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .background(
                    color = MaterialTheme.colorScheme.primaryContainer,
                    shape = RoundedCornerShape(16.dp), // Zaokrąglone rogi
                )
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally)
            {
                Label("Date 2", 20, Color.White)
                DatePickerFieldToModal(selectedDate = selectedDate2, onDateSelected = { newDate -> selectedDate2 = newDate })
                TimePickerFieldToModal(selectedTime = selectedTime2, onTimeSelected = { newTime -> selectedTime2 = newTime })
            }
        }
        Log.d("ThemeTest", MaterialTheme.colorScheme.primary.toString())
        HorizontalDivider(thickness = 5.dp, color = blackCustom)

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Label("Weeks", 16, blackCustom)
                Label(Weeks, 16, blackCustom)
            }
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Label("Days", 16, blackCustom)
                Label(Days, 16, blackCustom)
            }
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Label("Hours", 16, blackCustom)
                Label(Hours, 16, blackCustom)
            }
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Label("Minutes", 16, blackCustom)
                Label(Minutes, 16, blackCustom)
            }
        }

        CalculateButton(selectedDate1, selectedDate2, selectedTime1, selectedTime2,
            onResult = { w, d, h, m ->
            Weeks = w
            Days = d
            Hours = h
            Minutes = m
        })
    }
}
}

@Composable
fun Label(name: String, fontsize : Int, color : Color ,modifier: Modifier = Modifier) {
    Text(
        text = name,
        modifier = modifier,
        fontSize = fontsize.sp,
        color = color
    )
}

@Composable
fun CalculateButton(
    selectedDate1: Long?,
    selectedDate2: Long?,
    selectedTime1: Long?,
    selectedTime2: Long?,
    onResult: (weeks: String, days: String, hours: String, minutes: String) -> Unit


)
    {
    Button(onClick = {
        if(selectedDate1 != null && selectedDate2!= null && selectedTime1 != null && selectedTime2 != null){
            val date1  = selectedDate1 + selectedTime1
            val date2  = selectedDate2 + selectedTime2

            val diffMillis = kotlin.math.abs(date1 - date2)
            val diffMinutes = diffMillis / (1000 * 60)
            val diffHours = diffMinutes / 60
            val diffDays = diffHours / 24
            val diffWeeks = diffDays / 7

            // Przekazujemy wynik do funkcji onResult
            onResult(diffWeeks.toString(), (diffDays % 7).toString(), (diffHours % 24).toString(), (diffMinutes % 60).toString())
        }
    },
        modifier = Modifier
            .width(200.dp)  // Ustawienie szerokości na 200dp
            .height(50.dp)  // Ustawienie wysokości na 50dp
    ) {
        Text("Calculate")
    }
}