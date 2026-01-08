package biz.ei6.judo.framework

import android.Manifest
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.*

@Composable
fun AppStartupEffects() {
    val context = androidx.compose.ui.platform.LocalContext.current

    val notifPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        // même si refusé, tu peux quand même scheduler (le worker tournera, mais pas de notif)
        BackgroundSync.schedule(context)
    }

    LaunchedEffect(Unit) {
        Notif.ensureChannel(context)

        if (Build.VERSION.SDK_INT >= 33) {
            notifPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        } else {
            BackgroundSync.schedule(context)
        }
    }
}
