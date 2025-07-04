package com.example.thecodecup.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.graphics.Color

private val LightColorScheme = lightColorScheme(
    primary = CoffeeBrown,
    onPrimary = TextOnPrimary,
    primaryContainer = CoffeeLight,
    onPrimaryContainer = TextPrimary,

    secondary = CoffeeAccent,
    onSecondary = TextPrimary,
    secondaryContainer = CoffeeLight,
    onSecondaryContainer = TextPrimary,

    tertiary = CoffeeDark,
    onTertiary = TextOnPrimary,

    background = BackgroundColor,
    onBackground = TextPrimary,

    surface = SurfaceColor,
    onSurface = TextPrimary,
    surfaceVariant = LightGray,
    onSurfaceVariant = TextSecondary,

    surfaceTint = CoffeeBrown,
    inverseSurface = TextPrimary,
    inverseOnSurface = TextOnPrimary,

    error = ErrorColor,
    onError = TextOnPrimary,
    errorContainer = ErrorColor.copy(alpha = 0.1f),
    onErrorContainer = ErrorColor,

    outline = CoffeeGray,
    outlineVariant = LightGray,

    scrim = TextPrimary.copy(alpha = 0.32f)
)

private val DarkColorScheme = darkColorScheme(
    primary = CoffeeLight,
    onPrimary = DarkBackground,
    primaryContainer = CoffeeDark,
    onPrimaryContainer = CoffeeLight,

    secondary = CoffeeAccent,
    onSecondary = DarkBackground,
    secondaryContainer = CoffeeDark,
    onSecondaryContainer = CoffeeLight,

    tertiary = CoffeeLight,
    onTertiary = DarkBackground,

    background = DarkBackground,
    onBackground = DarkTextPrimary,

    surface = DarkSurface,
    onSurface = DarkTextPrimary,
    surfaceVariant = DarkCardBackground,
    onSurfaceVariant = DarkTextSecondary,

    surfaceTint = CoffeeLight,
    inverseSurface = DarkTextPrimary,
    inverseOnSurface = DarkBackground,

    error = Color(0xFFFFB4AB),
    onError = Color(0xFF690005),
    errorContainer = Color(0xFF93000A),
    onErrorContainer = Color(0xFFFFDAD6),

    outline = Color(0xFF938F99),
    outlineVariant = DarkCardBackground,

    scrim = Color(0xFF000000)
)

@Composable
fun TheCodeCupTheme(
    themeManager: ThemeManager? = null,
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        themeManager?.isDarkMode == true -> DarkColorScheme
        darkTheme && themeManager == null -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = {
            if (themeManager != null) {
                CompositionLocalProvider(LocalThemeManager provides themeManager) {
                    content()
                }
            } else {
                content()
            }
        }
    )
}

object CoffeeTheme {
    val colors = LightColorScheme
    val typography = Typography
}