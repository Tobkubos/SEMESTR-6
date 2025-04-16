package com.example.zad5

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.* // Importuj wszystko z layout
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.* // Importuj wszystko z material3
import androidx.compose.runtime.* // Importuj wszystko z runtime
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.Dialog
import com.example.zad5.ui.theme.Zad5Theme
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Zad5Theme {
                MainScreen()
            }
        }
    }
}

data class PopularSite(val name: String, val url: String)

val popularSitesList = listOf(
    PopularSite("Facebook", "https://facebook.com"),
    PopularSite("WFiIS UŁ", "https://www.wfis.uni.lodz.pl/"),
    PopularSite("nie klikać", "https://www.youtube.com/watch?v=dQw4w9WgXcQ")
)

@SuppressLint("SetJavaScriptEnabled")
@Preview(showBackground = true)
@Composable
fun MainScreen() {
    var searchText by remember { mutableStateOf("google.com") }
    var urlToLoad by remember { mutableStateOf("https://google.com") }
    var webViewInstance by remember { mutableStateOf<WebView?>(null) }
    var canGoBack by remember { mutableStateOf(false) }
    var canGoForward by remember { mutableStateOf(false) }
    var isSearchBarVisible by remember { mutableStateOf(true) }
    var showPopularSitesDialog by remember { mutableStateOf(false) }
    var showEmptyUrlDialog by remember { mutableStateOf(false) }
    val keyboardController = LocalSoftwareKeyboardController.current
    val context = LocalContext.current

    fun updateNavigationState(webView: WebView?) {
        canGoBack = webView?.canGoBack() ?: false
        canGoForward = webView?.canGoForward() ?: false
    }


    fun prepareAndLoadUrl(input: String) {
        keyboardController?.hide()
        val trimmedInput = input.trim()
        if (trimmedInput.isBlank()) {
            showEmptyUrlDialog = true
            return
        }

        val finalUrl = when {
            trimmedInput.startsWith("http://") || trimmedInput.startsWith("https://") -> trimmedInput
            trimmedInput.contains(".") && !trimmedInput.contains(" ") -> "https://$trimmedInput"
            else -> "https://www.google.com/search?q=${URLEncoder.encode(trimmedInput, StandardCharsets.UTF_8.toString())}"
        }

        urlToLoad = finalUrl
        searchText = finalUrl
    }

    if (showPopularSitesDialog) {
        Dialog(onDismissRequest = { showPopularSitesDialog = false }) { // Zamyka się po kliknięciu poza
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
                    Text("Popularne Strony", style = MaterialTheme.typography.titleLarge)
                    Spacer(modifier = Modifier.height(16.dp))

                    // Lista przycisków dla każdej strony
                    popularSitesList.forEach { site ->
                        TextButton(
                            onClick = {
                                prepareAndLoadUrl(site.url)
                                showPopularSitesDialog = false
                            },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(site.name)
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                    // Przycisk do zamknięcia dialogu
                    Button(onClick = { showPopularSitesDialog = false }) {
                        Text("Zamknij")
                    }
                }
            }
        }
    }

    Zad5Theme {
        Scaffold { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Wyszukiwaczo-Inator",
                    modifier = Modifier
                        .align(alignment = Alignment.CenterHorizontally),
                            style = TextStyle(
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold,  // Wybierz odpowiednią wagę czcionki
                                color = Color.Black,)  // Centrujemy tekst w obrębie dostępnej przestrzeni
                )
                AnimatedVisibility(
                    visible = isSearchBarVisible,
                    //enter = slideInVertically(initialOffsetY = { -it }), // Wsuń z góry
                    //exit = slideOutVertically(targetOffsetY = { -it })  // Wysuń do góry
                ) {
                    Column {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 8.dp)
                        ) {
                            BasicTextField(
                                value = searchText,
                                onValueChange = { searchText = it },
                                modifier = Modifier
                                    .weight(1f)
                                    .border(
                                        1.dp,
                                        MaterialTheme.colorScheme.outline,
                                        RoundedCornerShape(8.dp)
                                    )
                                    .padding(horizontal = 12.dp, vertical = 10.dp),
                                textStyle = TextStyle(
                                    color = MaterialTheme.colorScheme.onSurface,
                                    fontSize = 16.sp,
                                ),
                                decorationBox = { innerTextField ->
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Box(
                                            modifier = Modifier.weight(1f),
                                            contentAlignment = Alignment.CenterStart
                                        ) {
                                            if (searchText.isEmpty()) {
                                                Text(
                                                    "Wpisz URL lub wyszukaj...",
                                                    color = Color.Gray,
                                                    fontSize = 16.sp
                                                )
                                            }
                                            innerTextField()
                                        }

                                        if (searchText.isNotEmpty()) {
                                            IconButton(onClick = { searchText = "" }) {
                                                Icon(
                                                    imageVector = Icons.Default.Close,
                                                    contentDescription = "Wyczyść",
                                                    tint = Color.Gray
                                                )
                                            }
                                        }
                                    }
                                },
                                singleLine = true,
                                keyboardActions = KeyboardActions(
                                    onSearch = {
                                        prepareAndLoadUrl(searchText)
                                    }
                                ),
                                keyboardOptions = KeyboardOptions.Default.copy(
                                    imeAction = ImeAction.Search
                                )
                            )

                            Button(
                                contentPadding = PaddingValues(horizontal = 16.dp),
                                modifier = Modifier.height(IntrinsicSize.Min),
                                onClick = { prepareAndLoadUrl(searchText) }
                            ) {
                                Text("Załaduj", fontSize = 14.sp)
                            }
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
                                onClick = { showPopularSitesDialog = true }
                            ) {
                                Text("★ Popularne ★", fontSize = 14.sp)
                            }
                        }
                    }
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .height(48.dp)
                ) {
                    // Przycisk Back
                    Button(
                        onClick = { webViewInstance?.goBack()
                            searchText = ""},
                        enabled = canGoBack,
                        modifier = Modifier.weight(1f),
                        contentPadding = PaddingValues(horizontal = 8.dp)
                    ) {
                        Text("Poprzednia", fontSize = 14.sp, maxLines = 1)
                    }

                    // Przycisk Forward
                    Button(
                        onClick = { webViewInstance?.goForward()
                            searchText = "" },
                        enabled = canGoForward,
                        modifier = Modifier.weight(1f),
                        contentPadding = PaddingValues(horizontal = 8.dp)
                    ) {
                        Text("Następna", fontSize = 14.sp, maxLines = 1)
                    }

                    // Przycisk Hide/Show Search Bar
                    IconButton(
                        onClick = { isSearchBarVisible = !isSearchBarVisible }
                    ) {
                        Icon(
                            imageVector = if (isSearchBarVisible) Icons.Default.Close else Icons.Default.KeyboardArrowDown,
                            contentDescription = if (isSearchBarVisible) "Ukryj pasek wyszukiwania" else "Pokaż pasek wyszukiwania"
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                AndroidView(
                    factory = {
                        WebView(context).apply {
                            layoutParams = ViewGroup.LayoutParams(
                                ViewGroup.LayoutParams.MATCH_PARENT,
                                ViewGroup.LayoutParams.MATCH_PARENT
                            )
                            settings.javaScriptEnabled = true // Włącz JS
                            settings.domStorageEnabled = true // Często potrzebne
                            settings.databaseEnabled = true   // Czasem potrzebne
                            webViewClient = object : WebViewClient() {
                                override fun onPageFinished(view: WebView?, url: String?) {
                                    super.onPageFinished(view, url)
                                    updateNavigationState(view)
                                }
                                override fun onPageStarted(view: WebView?, url: String?, favicon: android.graphics.Bitmap?) {
                                    super.onPageStarted(view, url, favicon)
                                    updateNavigationState(view)
                                }
                            }
                            webChromeClient = WebChromeClient()
                            loadUrl(urlToLoad)
                            // Zapisz instancję
                            webViewInstance = this
                        }
                    },
                    update = { webView ->

                        if (webViewInstance != webView) {
                            webViewInstance = webView
                        }

                        if (webView.url != urlToLoad) {
                            webView.loadUrl(urlToLoad)
                        }

                        updateNavigationState(webView)
                    },
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                )
            }
        }
    }
    if (showEmptyUrlDialog) {
        AlertDialog(
            onDismissRequest = { showEmptyUrlDialog = false },
            title = { Text("Błąd") },
            text = { Text("Brak URL do załadowania.") },
            confirmButton = {
                TextButton(onClick = { showEmptyUrlDialog = false }) {
                    Text("OK")
                }
            }
        )
    }
    BackHandler(enabled = canGoBack) {
        webViewInstance?.goBack()
    }
}