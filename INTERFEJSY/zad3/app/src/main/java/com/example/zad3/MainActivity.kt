package com.example.zad3

import android.R
import android.graphics.fonts.FontStyle
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.zad3.ui.theme.Zad3Theme

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Zad3Theme {
                MainScreen()
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MainScreen() {
    var number by remember { mutableStateOf("") }
    var selectedOption by remember { mutableStateOf("Decimal") }

    Surface(
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically), // Wyśrodkowanie w pionie
            horizontalAlignment = Alignment.CenterHorizontally // Wyśrodkowanie w poziomie
        ) {
            Label("NUMBER CONVERTER")
            Input1(num = number, onNumChange = { number = it })
            RadioButtonSingleSelection(selectedOption, onOptionSelected = { selectedOption = it })
            LabeledBox(convertNumber(number, selectedOption)) // Konwersja liczby
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Input1(num: String, onNumChange: (String) -> Unit) {
    val keyboardController = LocalSoftwareKeyboardController.current
    var maxNum = 8
    Box(
        modifier = Modifier
            .border(4.dp, Color(0xFFFFA500), RoundedCornerShape(16.dp))
            .padding(4.dp)
            .fillMaxWidth(0.9f)
    ) {
        TextField(
            modifier = Modifier.fillMaxWidth(),
            value = num,
            onValueChange = { newNum ->
                if (newNum.isEmpty() || (newNum.all { it.isDigit() } && newNum.length <= maxNum)) {
                    onNumChange(newNum)
                }
                if (newNum.length == 1 && newNum[0] == '0') {
                    onNumChange("")
                }
            },
            singleLine = true,
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Number
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    keyboardController?.hide()
                }
            ),
            label = { Text("Enter number") },
            textStyle = TextStyle(fontSize = 20.sp),
            shape = RoundedCornerShape(16.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color.Transparent,
                unfocusedBorderColor = Color.Transparent,
                disabledBorderColor = Color.Transparent,
                errorBorderColor = Color.Red,
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White
            ),
            //modifier = Modifier.fillMaxWidth() // Dopasowanie do szerokości Boxa
        )
    }
}



@Composable
fun RadioButtonSingleSelection(selectedOption: String, onOptionSelected: (String) -> Unit) {
    val radioOptions = listOf("Decimal", "Binary", "Octal", "Hexadecimal")

    Column(Modifier.selectableGroup()) {
        radioOptions.forEach { text ->
            Row(
                Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .selectable(
                        selected = (text == selectedOption),
                        onClick = { onOptionSelected(text) }, // Aktualizacja wybranego systemu liczbowego
                        role = Role.RadioButton
                    )
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    selected = (text == selectedOption),
                    onClick = null, // null recommended for accessibility
                    colors = androidx.compose.material3.RadioButtonDefaults.colors(
                        selectedColor = Color(0xFFFFA500),  // Pomarańczowy dla zaznaczonego
                        unselectedColor = Color.Gray,       // Szary dla niezaznaczonych
                        disabledSelectedColor = Color.LightGray,
                        disabledUnselectedColor = Color.DarkGray
                    )
                )

                Text(
                    text = text,
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(start = 16.dp)
                )
            }
        }
    }
}


@Composable
fun Label(lab: String) {
    Text(
        text = lab,
        fontWeight = FontWeight.ExtraBold,
        color = Color.Black,
        fontSize = 25.sp,
    )
}

@Composable
fun LabeledBox(label: String) {
    Column(
        modifier = Modifier.fillMaxWidth(0.9f) // Ograniczenie szerokości
    ) {
        // Tekst nad Boxem, wyrównany do lewej strony
        Text(
            text = "Output:",
            fontWeight = FontWeight.ExtraBold,
            color = Color.Black,
            fontSize = 25.sp,
            modifier = Modifier.padding(start = 4.dp, bottom = 4.dp) // Przesunięcie w dół i na lewo
        )

        Box(
            modifier = Modifier
                .border(4.dp, Color(0xFFFFA500), RoundedCornerShape(12.dp))
                .background(Color(0xFFFFA500), RoundedCornerShape(12.dp))
                .padding(horizontal = 12.dp, vertical = 4.dp)
                .fillMaxWidth()
                .height(50.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = label,
                color = Color.White,
                fontSize = 16.sp,
                textAlign = TextAlign.Center
            )
        }
    }
}

fun convertNumber(input: String, system: String): String {
    if (input.isEmpty()) return "Enter a number"

    return try {
        val decimal = input.toInt()

        when (system) {
            "Binary" -> "Binary: \n" + decimal.toString(2)
            "Octal" -> "Octal: \n" + decimal.toString(8)
            "Hexadecimal" -> "Hex: \n 0x" + decimal.toString(16).uppercase()
            else -> "Decimal:\n $decimal"
        }
    } catch (e: NumberFormatException) {
        "Invalid number"
    }
}