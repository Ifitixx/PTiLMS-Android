package com.pizzy.ptilms.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = DarkBlue,
    onPrimary = Cream, // Text/icons on DarkBlue
    secondary = DarkOrange,
    onSecondary = Cream, // Text/icons on DarkOrange
    tertiary = DarkCream,
    onTertiary = DarkGray, // Text/icons on DarkCream
    background = DarkGray,
    onBackground = LightGray, // Text/icons on DarkGray
    surface = DarkGray,
    onSurface = LightGray, // Text/icons on DarkGray
    error = Color.Red,
    onError = Color.White,
)

private val LightColorScheme = lightColorScheme(
    primary = LightBlue,
    onPrimary = DarkGray, // Text/icons on LightBlue
    secondary = Orange,
    onSecondary = DarkGray, // Text/icons on Orange
    tertiary = Cream,
    onTertiary = LightGray, // Text/icons on Cream
    background = LightGray,
    onBackground = DarkGray, // Text/icons on LightGray
    surface = LightGray,
    onSurface = DarkGray, // Text/icons on LightGray
    error = Color.Red,
    onError = Color.White,
)

@Composable
fun PTiLMSTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}