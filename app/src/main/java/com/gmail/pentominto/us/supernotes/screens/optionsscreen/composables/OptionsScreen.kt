package com.gmail.pentominto.us.supernotes.screens.optionsscreen.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.gmail.pentominto.us.supernotes.screens.optionsscreen.OptionsScreenViewModel

@Composable
fun OptionsScreen(
    viewModel: OptionsScreenViewModel = hiltViewModel()
) {
    val context = LocalContext.current

    val optionsState by remember { viewModel.optionsScreenState }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .background(MaterialTheme.colors.background)

    ) {
        OptionsTitle()

        OptionsList(
            optionsState,
            viewModel,
            context
        )
    }
}

@Composable
private fun OptionsTitle() {
    Text(
        text = "Options",
        fontSize = 36.sp,
        modifier = Modifier
            .padding(16.dp),
        color = MaterialTheme.colors.onBackground
    )
}



