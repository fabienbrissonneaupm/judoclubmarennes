package biz.ei6.judo.ui.theme

import android.app.Activity
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
    primary = Color(0xFFE53935),        // Rouge principal (CTA)
    onPrimary = Color(0xFFFFFFFF),

    secondary = Color(0xFFB71C1C),      // Rouge plus sombre
    onSecondary = Color(0xFFFFFFFF),

    tertiary = Color(0xFFFFCDD2),       // Rouge clair / highlight doux
    onTertiary = Color(0xFF1A1A1A),

    background = Color(0xFF0E0E0E),     // Noir profond
    onBackground = Color(0xFFF5F5F5),

    surface = Color(0x585859FF),        // Cartes / surfaces
    onSurface = Color(0xFFF5F5F5),

    surfaceVariant = Color(0x6A6A6BFF),
    onSurfaceVariant = Color(0xFFF5F5F5),

    outline = Color(0xFF2E2E2E)
)


private val LightColorScheme = lightColorScheme(
    primary = Color(0xFFD32F2F),        // Rouge légèrement plus doux
    onPrimary = Color(0xFFFFFFFF),

    secondary = Color(0xFFB71C1C),
    onSecondary = Color(0xFFFFFFFF),

    tertiary = Color(0xFFFFEBEE),
    onTertiary = Color(0xFF1A1A1A),

    background = Color(0xFFFFFFFF),    // Blanc pur
    onBackground = Color(0xFF1A1A1A),

    surface = Color(0xFFF7F7F7),
    surfaceVariant = Color(0xFFEDEDED),

    onSurface = Color(0xFF1A1A1A),
    onSurfaceVariant = Color(0xFF5A5A5A),

    outline = Color(0xFFE0E0E0)
)


@Composable
fun TheTheme(
    darkTheme: Boolean = true,//isSystemInDarkTheme(),
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
        content = content
    )
}