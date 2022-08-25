package com.gmail.pentominto.us.supernotes

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.gmail.pentominto.us.supernotes.ui.theme.SuperNotesTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState : Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SuperNotesTheme {
                // A surface container using the 'background' color from the theme
                AllNotesScreen()
            }
        }
    }
}

