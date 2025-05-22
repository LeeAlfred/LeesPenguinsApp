package com.example.lees_penguins

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember // <-- NEW IMPORT
import androidx.compose.runtime.mutableStateOf // <-- NEW IMPORT
import androidx.compose.runtime.getValue // <-- NEW IMPORT
import androidx.compose.runtime.setValue // <-- NEW IMPORT
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
// import androidx.compose.ui.res.painterResource // No longer needed if using assets.open directly
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.lees_penguins.ui.theme.Lees_PenguinsTheme


import androidx.compose.ui.graphics.Color // <-- Make sure this is here
import androidx.compose.ui.text.font.FontWeight // <-- Make sure this is here
import androidx.compose.ui.unit.sp // <-- Make sure this is here (for text size)


// Imports for image loading from assets
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import android.graphics.BitmapFactory
import android.graphics.Bitmap
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.ui.graphics.asImageBitmap

// Imports for date handling
import java.time.LocalDate
import java.time.format.DateTimeFormatter


class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Lees_PenguinsTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    PenguinAppLayout(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

// ------------------- UPDATED COMPOSABLE FUNCTION BELOW -------------------
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun PenguinAppLayout(modifier: Modifier = Modifier) {

    // --- STATE FOR THE CURRENTLY DISPLAYED DATE ---

    var displayedDate by remember { mutableStateOf(LocalDate.now()) }

    // --- Formatter for Image Lookup (yyyy-MM-dd) ---

    val filenameFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    val filenameDate = displayedDate.format(filenameFormatter) // This is what the image lookup uses


    // --- Formatter for User Display (EEEE dd MMMM) ---
    val displayFormatter = DateTimeFormatter.ofPattern("EEEE dd MMMM")
    val displayDateText = displayedDate.format(displayFormatter) // This is what the Text composable uses

    val context = LocalContext.current

    // --- DYNAMIC IMAGE FILENAME DETERMINATION (uses filenameDate)---
    var imageFileName: String? = null
    try {
        // List all files in the assets folder
        val assetFiles = context.assets.list("") // List all top-level asset files
        if (assetFiles != null) {
            // Find the first file that starts with our formatted date and ends with .png
            imageFileName = assetFiles.firstOrNull { it.startsWith(filenameDate) && it.endsWith(".png") }
        }
    } catch (e: Exception) {
        e.printStackTrace()
        // Log this error or show a toast message
        // For debugging: Log.e("PenguinApp", "Error listing assets: ${e.message}")
    }

    var bitmap: Bitmap? = null
    if (imageFileName != null) { // Only attempt to load if a filename was found
        try {
            val inputStream = context.assets.open(imageFileName) // Use the dynamically found filename
            bitmap = BitmapFactory.decodeStream(inputStream)
        } catch (e: Exception) {
            e.printStackTrace()
            // Log this error or show a toast message
            // For debugging: Log.e("PenguinApp", "Error decoding image $imageFileName: ${e.message}")
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        // 1. Date Text
        Text(
            text = displayDateText,
            modifier = Modifier.padding(bottom = 16.dp),
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFFC94235)
        )

        // 2. Penguin Image
        if (bitmap != null) {
            val formattedDate = ""
            Image(
                bitmap = bitmap.asImageBitmap(), // Convert Android Bitmap to Compose ImageBitmap
                contentDescription = "Daily Penguin Image for $formattedDate", // Update content description
                modifier = Modifier
                    .weight(1f) // Makes the image take up available vertical space
                    .fillMaxWidth(), // Make it fill width for better display
                contentScale = ContentScale.Fit // Adjust how the image scales
            )
        } else {
            // Fallback if no image found for today's date or loading failed
            val formattedDate = ""
            Text(text = "No penguin image found for $formattedDate :(",
                modifier = Modifier.weight(1f).fillMaxWidth().padding(16.dp)) // Give fallback text some space
        }

        // 3. Navigation Buttons
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(onClick = {displayedDate = displayedDate.minusDays(1) }) {
                Text(text = "Previous")
            }
            Button(onClick = {displayedDate = LocalDate.now() }) {
                Text(text = "Today")
            }
            Button(onClick = { displayedDate = displayedDate.plusDays(1) }) {
                Text(text = "Next")
            }
        }
    }
}
// ------------------- END UPDATED COMPOSABLE FUNCTION -------------------


@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    Lees_PenguinsTheme {
        PenguinAppLayout()
    }
}