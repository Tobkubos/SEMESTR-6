package com.example.zad4

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.zad4.ui.theme.Zad4Theme
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight


data class Phone(
    val number: String,
    val start_date: Date,
    val end_date: Date,
)
fun formatDate(date: Date): String {
    val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm")  // Format daty
    return dateFormat.format(date)
}


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Zad4Theme {
                MainScreen()
            }
        }
    }
}
fun formatPhoneNumber(number: String): String {
    // Zasadniczo dzielimy numer na grupy po 3 cyfry
    val regex = "(\\d{3})(\\d{3})(\\d{3})".toRegex()
    return regex.replace(number, "$1 $2 $3")
}
@Preview(showBackground = true)
@Composable
fun MainScreen() {
    Zad4Theme{
        var setIndex by remember { mutableStateOf(0) }  // Zmienna przechowująca wybrany zestaw
        var searchText by remember { mutableStateOf("") }  // Zmienna na tekst wyszukiwania
        var selectedPhoneNumber by remember { mutableStateOf<Phone?>(null) }  // Zmienna przechowująca wybrany numer telefonu
        val keyboardController = LocalSoftwareKeyboardController.current

        // Dane dla obu zestawów
        val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
        val phoneData1 = listOf(
            Phone("123456789", dateFormat.parse("23/21/2023 14:23")!!, dateFormat.parse("23/21/2023 14:29")!!),
            Phone("888888888", dateFormat.parse("24/21/2023 10:00")!!, dateFormat.parse("24/21/2023 10:30")!!),
            Phone("777777777", dateFormat.parse("25/21/2023 12:00")!!, dateFormat.parse("25/21/2023 12:15")!!),
            Phone("666666666", dateFormat.parse("26/21/2023 09:10")!!, dateFormat.parse("26/21/2023 09:40")!!),
            Phone("555555555", dateFormat.parse("27/21/2023 11:00")!!, dateFormat.parse("27/21/2023 11:30")!!),
            Phone("444444444", dateFormat.parse("28/21/2023 08:45")!!, dateFormat.parse("28/21/2023 09:00")!!),
            Phone("814488888", dateFormat.parse("24/21/2023 10:00")!!, dateFormat.parse("24/21/2023 10:30")!!),
            Phone("777757477", dateFormat.parse("25/21/2023 12:00")!!, dateFormat.parse("25/21/2023 12:15")!!),
            Phone("633333336", dateFormat.parse("26/21/2023 09:10")!!, dateFormat.parse("26/21/2023 09:40")!!),
            Phone("757575755", dateFormat.parse("27/21/2023 11:00")!!, dateFormat.parse("27/21/2023 11:30")!!),
            Phone("449999999", dateFormat.parse("28/21/2023 08:45")!!, dateFormat.parse("28/21/2023 09:00")!!),
            Phone("112344433", dateFormat.parse("29/21/2023 13:00")!!, dateFormat.parse("29/21/2023 13:20")!!)
        )

        val phoneData2 = listOf(
            Phone("324433424", dateFormat.parse("23/21/2023 14:23")!!, dateFormat.parse("23/21/2023 14:29")!!),
            Phone("666757657", dateFormat.parse("24/21/2023 10:00")!!, dateFormat.parse("24/21/2023 10:30")!!),
            Phone("098898987", dateFormat.parse("25/21/2023 12:00")!!, dateFormat.parse("25/21/2023 12:15")!!),
            Phone("879087090", dateFormat.parse("26/21/2023 09:10")!!, dateFormat.parse("26/21/2023 09:40")!!),
            Phone("089780707", dateFormat.parse("27/21/2023 11:00")!!, dateFormat.parse("27/21/2023 11:30")!!),
            Phone("678656544", dateFormat.parse("28/21/2023 08:45")!!, dateFormat.parse("28/21/2023 09:00")!!),
            Phone("123870290", dateFormat.parse("26/21/2023 09:10")!!, dateFormat.parse("26/21/2023 09:40")!!),
            Phone("327807207", dateFormat.parse("27/21/2023 11:00")!!, dateFormat.parse("27/21/2023 11:30")!!),
            Phone("222256544", dateFormat.parse("28/21/2023 08:45")!!, dateFormat.parse("28/21/2023 09:00")!!),
            Phone("331311333", dateFormat.parse("29/21/2023 13:00")!!, dateFormat.parse("29/21/2023 13:20")!!)
        )

        // Filtruj dane na podstawie wyszukiwanego tekstu
        val filteredData = when (setIndex) {
            0 -> phoneData1.filter { it.number.startsWith(searchText, ignoreCase = true) }  // Zestaw 1
            1 -> phoneData2.filter { it.number.startsWith(searchText, ignoreCase = true) }  // Zestaw 2
            else -> emptyList()
        }

        Surface(
            color = MaterialTheme.colorScheme.background,
            modifier = Modifier.clickable { keyboardController?.hide() }
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.Top),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Greeting("Przeszukiwacz")
                SingleChoiceSegmentedButton2(selectedIndex = setIndex, onSelectedIndexChange = { setIndex = it })
                SearchBar(searchText, onSearchTextChange = { searchText = it })

                // Box z maskowaniem dla LazyColumn
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(350.dp)  // Wysokość dla przewijania
                        .clip(shape = MaterialTheme.shapes.medium)  // Maska
                        .border(1.dp, MaterialTheme.colorScheme.onBackground)  // Obwódka
                        .padding(4.dp)  // Padding w boxie
                ) {
                    // LazyColumn z przewijaniem i elementami wyświetlanymi w Box
                    LazyColumn {
                        items(filteredData) { data ->
                            Button(
                                onClick = {
                                    // Zaktualizuj numer telefonu po kliknięciu
                                    selectedPhoneNumber = data
                                    keyboardController?.hide()
                                },
                                modifier = Modifier
                                    .fillMaxWidth(),
                                shape = RectangleShape
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.Start  // Wyrównanie do lewej
                                ) {
                                    Text(text = formatPhoneNumber(data.number))  // Tekst wewnątrz przycisku
                                }
                            }
                        }
                    }
                }
                Text(
                    text = "Informacje o numerze",
                    fontSize = 14.sp,
                    modifier = Modifier.padding(top = 10.dp) // Usunięcie zbędnego przecinka i poprawne użycie Modifier
                )
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp)
                        .clip(shape = MaterialTheme.shapes.medium)
                        .border(1.dp, MaterialTheme.colorScheme.onBackground)
                        .padding(8.dp)
                ) {

                    TextField(
                        value = if (selectedPhoneNumber != null) {
                            // Formatowanie tekstu: numer telefonu + daty
                            "Numer: ${selectedPhoneNumber?.number}\n" +
                                    "Data początkowa: ${formatDate(selectedPhoneNumber?.start_date ?: Date())}\n" +
                                    "Data końcowa: ${formatDate(selectedPhoneNumber?.end_date ?: Date())}"
                        } else "",
                        onValueChange = { /* Brak akcji, tekst jest tylko do wyświetlenia */ },
                        enabled = false, // Tylko do odczytu
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(150.dp),
                        textStyle = TextStyle(
                            color = Color(0xFF15444F), // Zmieniamy kolor czcionki na czerwony
                            fontSize = 16.sp, // Możesz także zmienić rozmiar czcionki
                            fontWeight = FontWeight.Bold // Opcjonalnie, zmiana na pogrubioną czcionkę
                        )
                    )
                }
            }
        }
    }
}

@Composable
fun SingleChoiceSegmentedButton2(selectedIndex: Int, onSelectedIndexChange: (Int) -> Unit) {
    val options = listOf("Zestaw 1", "Zestaw 2")

    SingleChoiceSegmentedButtonRow(
        modifier = Modifier.fillMaxWidth()
    ) {
        options.forEachIndexed { index, label ->
            SegmentedButton(
                shape = SegmentedButtonDefaults.itemShape(
                    index = index,
                    count = options.size
                ),
                onClick = { onSelectedIndexChange(index) },
                selected = index == selectedIndex,
                label = { Text(label) },
                colors = SegmentedButtonDefaults.colors(
                    // Kolor tła przycisku w przypadku zaznaczenia
                    activeContainerColor = Color(0xFF339F93),
                )
            )
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = name,
        modifier = modifier
            .padding(10.dp),
        fontSize = 37.sp
    )
}

@Composable
fun SearchBar(searchText: String, onSearchTextChange: (String) -> Unit) {
    val keyboardController = LocalSoftwareKeyboardController.current
    TextField(
        value = searchText,
        onValueChange = onSearchTextChange,
        modifier = Modifier
            .fillMaxWidth()
            .clickable { keyboardController?.show() },
        placeholder = { Text("Wyszukaj...") },
        singleLine = true,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        trailingIcon = {
            Icon(imageVector = Icons.Default.Search, contentDescription = "Search",
            modifier = Modifier.clickable {
                keyboardController?.hide() // Ukrycie klawiatury po kliknięciu w ikonę
            })
        }
    )
}
