package sh.siava.pixelxpert.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

// Using Material 3 defaults and overriding specific colors
// For a theme that more closely follows the XML, dynamic color schemes are a good choice.

private val LightSuccess = Color(0xFF008753)
private val LightOnSuccess = Color.White
private val LightError = Color(0xFFDA000E)
private val LightOnError = Color.White

private val DarkSuccess = Color(0xFF00C479)
private val DarkOnSuccess = Color.Black // Or a very dark grey
private val DarkError = Color(0xFFFF5052)
private val DarkOnError = Color.Black // Or a very dark grey


private val LightCustomColorScheme = lightColorScheme(
    primary = Color(0xFF00698C), // Example: A blue shade from M3 builder
    onPrimary = Color.White,
    primaryContainer = Color(0xFFC2E8FF),
    onPrimaryContainer = Color(0xFF001F2D),
    secondary = Color(0xFF4E616D), // Example: A greyish blue
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFD1E5F4),
    onSecondaryContainer = Color(0xFF0A1E28),
    tertiary = Color(0xFF615A7D), // Example: A purple shade
    onTertiary = Color.White,
    tertiaryContainer = Color(0xFFE7DEFF),
    onTertiaryContainer = Color(0xFF1D1736),
    error = LightError,
    onError = LightOnError,
    background = Color(0xFFFDFBFF), // M3 default light background
    onBackground = Color(0xFF1A1C1E), // M3 default light onBackground
    surface = Color(0xFFFDFBFF), // M3 default light surface
    onSurface = Color(0xFF1A1C1E), // M3 default light onSurface
    surfaceVariant = Color(0xFFDDE3EA), // M3 default light surfaceVariant
    onSurfaceVariant = Color(0xFF41484D), // M3 default light onSurfaceVariant
    outline = Color(0xFF71787E), // M3 default light outline
    // Custom success colors
    // There isn't a direct 'success' slot in ColorScheme, so these are custom.
    // You might use them by creating extension properties on ColorScheme if needed elsewhere.
)

private val DarkCustomColorScheme = darkColorScheme(
    primary = Color(0xFF78D1FF), // Example: A light blue shade from M3 builder (dark theme primary)
    onPrimary = Color(0xFF00364A),
    primaryContainer = Color(0xFF004E6B),
    onPrimaryContainer = Color(0xFFC2E8FF),
    secondary = Color(0xFFB5CAD7), // Example: A light greyish blue
    onSecondary = Color(0xFF20333E),
    secondaryContainer = Color(0xFF374955),
    onSecondaryContainer = Color(0xFFD1E5F4),
    tertiary = Color(0xFFCAC0E8), // Example: A light purple shade
    onTertiary = Color(0xFF332C4C),
    tertiaryContainer = Color(0xFF4A4364),
    onTertiaryContainer = Color(0xFFE7DEFF),
    error = DarkError,
    onError = DarkOnError,
    background = Color(0xFF1A1C1E), // M3 default dark background
    onBackground = Color(0xFFE2E2E6), // M3 default dark onBackground
    surface = Color(0xFF1A1C1E), // M3 default dark surface
    onSurface = Color(0xFFE2E2E6), // M3 default dark onSurface
    surfaceVariant = Color(0xFF41484D), // M3 default dark surfaceVariant
    onSurfaceVariant = Color(0xFFC1C7CE), // M3 default dark onSurfaceVariant
    outline = Color(0xFF8B9298), // M3 default dark outline
    // Custom success colors
)

@Composable
fun PixelXpertTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android S+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkCustomColorScheme
        else -> LightCustomColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = AppTypography, // Using AppTypography from Typography.kt
        content = content
    )
}

// It's good practice to define custom colors that are not part of the standard
// Material ColorScheme as extensions, if they are to be used widely.

// These extension properties provide easy access to custom semantic colors.
// Note: Material 3's ColorScheme doesn't have a 'success' slot by default.
// 'error' is standard, so direct use of colorScheme.error is preferred.

val ColorScheme.success: Color
    @Composable
    get() = if (isSystemInDarkTheme()) DarkSuccess else LightSuccess

val ColorScheme.onSuccess: Color
    @Composable
    get() = if (isSystemInDarkTheme()) DarkOnSuccess else LightOnSuccess
