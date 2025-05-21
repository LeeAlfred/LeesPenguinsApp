package com.example.lees_penguins

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image // <-- NEW IMPORT
import androidx.compose.foundation.layout.Arrangement // <-- NEW IMPORT
import androidx.compose.foundation.layout.Column // <-- NEW IMPORT
import androidx.compose.foundation.layout.Row // <-- NEW IMPORT
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth // <-- NEW IMPORT
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button // <-- NEW IMPORT
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment // <-- NEW IMPORT
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource // <-- NEW IMPORT
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp // <-- NEW IMPORT
import com.example.lees_penguins.ui.theme.Lees_PenguinsTheme
import java.time.LocalDate // <-- NEW IMPORT
import java.time.format.DateTimeFormatter // <-- NEW IMPORT
import androidx.compose.ui.layout.ContentScale // <-- NEW IMPORT
import androidx.compose.ui.platform.LocalContext // <-- NEW IMPORT (for asset management)
import android.graphics.BitmapFactory // <-- NEW IMPORT (for asset management)
import android.graphics.Bitmap // <-- NEW IMPORT (for asset management)
import androidx.compose.ui.graphics.asImageBitmap // <-- NEW IMPORT (for converting Bitmap to ImageBitmap)


class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Lees_PenguinsTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    // CALL OUR NEW MAIN APP COMPOSABLE HERE
                    PenguinAppLayout(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

// ------------------- NEW COMPOSABLE FUNCTION BELOW -------------------
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun PenguinAppLayout(modifier: Modifier = Modifier) {
    val currentDate = LocalDate.now()
    // Define the desired date format (ISO 8601)
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    // Format the current date
    val formattedDate = currentDate.format(formatter)
    // NEW: Get the Android context to access assets
    val context = LocalContext.current
    Column(
        modifier = modifier
            .fillMaxSize() // Make the column fill the screen
            .padding(16.dp), // Add some overall padding
        horizontalAlignment = Alignment.CenterHorizontally, // Center contents horizontally
        verticalArrangement = Arrangement.SpaceBetween // Distribute space between elements
    ) {
        // 1. Date TextView (equivalent)
        Text(
            text = "Today's Date: $formattedDate", // Placeholder date for now
            modifier = Modifier.padding(bottom = 16.dp)
        )

        val todayImageFilename = "2025-07-05 0.001 Rumii Only You Can Walk Your Path Quote.png" // <--- REPLACE WITH AN ACTUAL FILENAME FROM YOUR ASSETS

        var bitmap: Bitmap? = null
        try {
            // Open the image file from assets
            val inputStream = context.assets.open(todayImageFilename)
            // Decode the input stream into a Bitmap
            bitmap = BitmapFactory.decodeStream(inputStream)
        } catch (e: Exception) {
            e.printStackTrace()
            // Handle error, e.g., show a placeholder or an error message
        }

        if (bitmap != null) {
            Image(
                bitmap = bitmap.asImageBitmap(), // Convert Android Bitmap to Compose ImageBitmap
                contentDescription = "Daily Penguin Image",
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(), // Make it fill width for better display
                contentScale = ContentScale.Fit // Adjust how the image scales
            )
        } else {
            // Show a fallback text if image loading fails
            Text(text = "Error loading penguin image!")
        }



        // 3. Navigation Buttons (equivalent)
        Row(
            modifier = Modifier
                .fillMaxWidth() // Make the row fill the width
                .padding(top = 16.dp), // Add some padding above buttons
            horizontalArrangement = Arrangement.SpaceAround, // Distribute buttons evenly
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(onClick = { /* TODO: Implement previous day logic */ }) {
                Text(text = "Previous")
            }
            Button(onClick = { /* TODO: Implement today logic */ }) {
                Text(text = "Today")
            }
            Button(onClick = { /* TODO: Implement next day logic */ }) {
                Text(text = "Next")
            }
        }
    }
}
// ------------------- END NEW COMPOSABLE FUNCTION -------------------


@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true)
@Composable
fun DefaultPreview() { // Renamed from GreetingPreview to be more generic for our new layout
    Lees_PenguinsTheme {
        PenguinAppLayout() // Preview our new layout
    }
}

// You can remove or keep the old Greeting composable if you want to reuse it,
// but it's not being used in the main app now.
// @Composable
// fun Greeting(name: String, modifier: Modifier = Modifier) {
//     Text(
//         text = "Hi, my name is $name!",
//         modifier = modifier
//     )
// }