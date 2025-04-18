package com.example.zad6

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.window.Dialog
import com.example.compose.Zad6Theme
import com.google.maps.android.compose.GoogleMap
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Zad6Theme {
                MainScreen()
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MainScreen() {
    var xValue by remember { mutableStateOf("") }
    var yValue by remember { mutableStateOf("") }
    var isSearchBarVisible by remember { mutableStateOf(true) }
    val keyboardController = LocalSoftwareKeyboardController.current
    val context = LocalContext.current
    var isMissingCoordinatesDialogVisible by remember { mutableStateOf(false) } // Nowy stan dla Dialogu
    var isPopularSelected by remember { mutableStateOf(false) }
    var isMapTypeMenuVisible by remember { mutableStateOf(false) }
    var mapType by remember { mutableStateOf(MapType.NORMAL) }
    val properties by remember(mapType) {
        mutableStateOf(MapProperties(mapType = mapType))
    }
    val uiSettings by remember { mutableStateOf(MapUiSettings(zoomControlsEnabled = true)) }

    val cities = listOf(
        "Sydney" to LatLng(-33.8688, 151.2093), // Sydney, Australia
        "Warszawa" to LatLng(52.237049, 21.017532), // Warszawa, Polska
        "Moskwa" to LatLng(55.7558, 37.6173), // Moskwa, Rosja
        "Ottawa" to LatLng(45.4215, -75.6972) // Ottawa, Kanada
    )

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(cities[0].second, 10f) // domyślnie Warszawa
    }

    if(isPopularSelected){
        Dialog(onDismissRequest = { isPopularSelected = false }) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                shape = RoundedCornerShape(16.dp),
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("Popularne miejsca", style = MaterialTheme.typography.titleLarge)
                    Spacer(modifier = Modifier.height(16.dp))

                    Column(
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        cities.forEach { (name, location) ->
                            Button(
                                onClick = {
                                    cameraPositionState.position = CameraPosition.fromLatLngZoom(location, 12f)
                                    isPopularSelected = false
                                },
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(name)
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))
                        Button(
                            onClick = { isPopularSelected = false },
                            modifier = Modifier.align(Alignment.CenterHorizontally) // Wyśrodkowanie przycisku
                        ) {
                            Text("Zamknij")
                        }
                    }
                }
            }
        }
    }


    if (isMissingCoordinatesDialogVisible) {
        Dialog(onDismissRequest = { isMissingCoordinatesDialogVisible = false }) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                shape = RoundedCornerShape(16.dp),
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally // Wyśrodkowanie w poziomie
                ) {
                    Text(
                        "Błędne wartości dla współrzędnych geograficznych!",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontSize = 16.sp, // Zmniejszenie rozmiaru czcionki
                            fontWeight = FontWeight.Normal // Zmiana na normalną wagę czcionki
                        ),
                        modifier = Modifier.align(Alignment.CenterHorizontally) // Wyśrodkowanie w poziomie
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = { isMissingCoordinatesDialogVisible = false },
                        modifier = Modifier.align(Alignment.CenterHorizontally) // Wyśrodkowanie przycisku
                    ) {
                        Text("Zamknij")
                    }
                }
            }
        }
    }

    if(isMapTypeMenuVisible){
        Dialog(onDismissRequest = { isMapTypeMenuVisible = false }) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                shape = RoundedCornerShape(16.dp),
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("Typ mapy", style = MaterialTheme.typography.titleLarge)
                    Spacer(modifier = Modifier.height(16.dp))

                    Column(
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        MapTypeButton("Normalna", MapType.NORMAL, mapType) {
                            mapType = MapType.NORMAL
                            isMapTypeMenuVisible = false
                        }
                        MapTypeButton("Satelitarna", MapType.SATELLITE, mapType) {
                            mapType = MapType.SATELLITE
                            isMapTypeMenuVisible = false
                        }
                        MapTypeButton("Hybrydowa", MapType.HYBRID, mapType) {
                            mapType = MapType.HYBRID
                            isMapTypeMenuVisible = false
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = { isMapTypeMenuVisible = false },
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    ) {
                        Text("Zamknij")
                    }
                }
            }
        }
    }

    Zad6Theme {
        Scaffold { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .pointerInput(Unit) {
                        detectTapGestures(
                            onTap = {
                                // Ukrywanie klawiatury po kliknięciu poza nią
                                keyboardController?.hide()
                            }
                        )
                    }
            ) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Znajdowato-Inator",
                    modifier = Modifier
                        .align(alignment = Alignment.CenterHorizontally),
                    style = TextStyle(
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black,
                    )
                )
                Column {

                    AnimatedVisibility(
                        visible = isSearchBarVisible,
                        //enter = slideInVertically(initialOffsetY = { -it }), // Wsuń z góry
                        //exit = slideOutVertically(targetOffsetY = { -it })  // Wysuń do góry
                    ){
                        Column {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp, vertical = 8.dp)
                            ) {
                                TextField(
                                    value = yValue,
                                    onValueChange = { yValue = it },
                                    modifier = Modifier.weight(1f),
                                    label = { Text("Szerokość (-90 do +90)",
                                        style = TextStyle(fontSize = 12.sp)) },
                                    singleLine = true,
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                )

                                TextField(
                                    value = xValue,
                                    onValueChange = { xValue = it },
                                    modifier = Modifier.weight(1f),
                                    label = { Text("Długość (-180 do +180)",
                                        style = TextStyle(fontSize = 12.sp)) },
                                    singleLine = true,
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                )
                            }

                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp, vertical = 8.dp)
                            ) {
                                Button(
                                    contentPadding = PaddingValues(horizontal = 8.dp),
                                    modifier = Modifier.height(IntrinsicSize.Min),
                                    onClick = {isPopularSelected = true }
                                ) {
                                    Text("★ Popularne ★", fontSize = 14.sp)
                                }
                                Button(
                                    onClick = {
                                        val lat = yValue.toDoubleOrNull()
                                        val lng = xValue.toDoubleOrNull()


                                        if (lat != null && lng != null) {
                                            // Sprawdzenie, czy współrzędne są w dopuszczalnym zakresie
                                            if (lat in -90.0..90.0 && lng in -180.0..180.0) {
                                                val target = LatLng(lat, lng)
                                                cameraPositionState.position = CameraPosition.fromLatLngZoom(target, 15f)
                                            } else {
                                                // Jeśli współrzędne są poza zakresem, wyświetl komunikat o błędzie
                                                isMissingCoordinatesDialogVisible = true
                                            }
                                        } else {
                                            // Jeśli wartości są puste, wyświetl dialog o błędzie
                                            isMissingCoordinatesDialogVisible = true
                                        }
                                    },
                                    contentPadding = PaddingValues(horizontal = 16.dp),
                                    modifier = Modifier
                                        .width(300.dp)
                                        .height(IntrinsicSize.Min)
                                ) {
                                    Text("Teleportacja!", fontSize = 14.sp)
                                }
                            }
                        }

                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {


                        IconButton(
                            onClick = { isMapTypeMenuVisible = !isMapTypeMenuVisible }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Settings,
                                contentDescription = "Ustawienia mapy"
                            )
                        }
                        IconButton(
                            onClick = { isSearchBarVisible = !isSearchBarVisible }
                        ) {
                            Icon(
                                imageVector = if (isSearchBarVisible) Icons.Default.Close else Icons.Default.KeyboardArrowDown,
                                contentDescription = if (isSearchBarVisible) "Ukryj pasek wyszukiwania" else "Pokaż pasek wyszukiwania"
                            )
                        }
                    }
                }

                GoogleMap(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .pointerInput(Unit) {
                            detectTapGestures(
                                onTap = {
                                    // Ukrywanie klawiatury po kliknięciu na mapę
                                    keyboardController?.hide()
                                }
                            )
                        },
                    cameraPositionState = cameraPositionState,
                    properties = properties,
                    uiSettings = uiSettings
                )
            }
        }
    }
}

@Composable
fun MapTypeButton(
    text: String,
    type: MapType,
    currentType: MapType,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        // Zmień kolor, aby podświetlić aktywny typ mapy
        colors = ButtonDefaults.buttonColors(
            containerColor = if (type == currentType) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondaryContainer,
            contentColor = if (type == currentType) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSecondaryContainer
        ),
        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp),
        modifier = Modifier
            .height(IntrinsicSize.Min) // Dopasuj wysokość
            .width(200.dp)
    ) {
        Text(text, fontSize = 12.sp) // Mniejsza czcionka dla przycisków
    }
}