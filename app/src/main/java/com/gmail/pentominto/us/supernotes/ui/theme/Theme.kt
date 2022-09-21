package com.gmail.pentominto.us.supernotes.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorPalette = darkColors(
    primary = Metal,
    primaryVariant = Charcoal,
    secondary = Fossil,
    secondaryVariant = Color.White,
    background = Spider,
    onBackground = Color.White
)


private val LightColorPalette = lightColors(
    primary = LightLimeGreen,
    primaryVariant = Pine,
    secondary = LightWalnutBrown,
    secondaryVariant = BrownBark,
    background = Color.White,
    onBackground = Color.Black

    /* Other default colors to override
    background = Color.White,
    surface = Color.White,
    onPrimary = Color.White,
    onSecondary = Color.Black,
    onBackground = Color.Black,
    onSurface = Color.Black,
    */
)

@Composable
fun SuperNotesTheme(darkTheme : Boolean = isSystemInDarkTheme(), content : @Composable () -> Unit) {

    val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }

    MaterialTheme(
        colors = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}