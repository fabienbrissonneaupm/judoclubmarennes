package biz.ei6.judo.framework

import android.view.ViewGroup
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.NearMe
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.CustomZoomButtonsController
import org.osmdroid.views.overlay.Marker

@Composable
fun OsmMapPreview(
    latitude: Double,
    longitude: Double,
    modifier: Modifier = Modifier,
    primary: androidx.compose.ui.graphics.Color
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    // Important: configure osmdroid (userAgent obligatoire sur certaines configs)
    LaunchedEffect(Unit) {
        Configuration.getInstance().userAgentValue = context.packageName
    }

    val geoPoint = remember(latitude, longitude) { GeoPoint(latitude, longitude) }

    // On garde la MapView en mémoire pour gérer onResume/onPause
    val mapView = remember {
        MapView(context).apply {
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
            setTileSource(TileSourceFactory.MAPNIK)
            setMultiTouchControls(false) // preview: pas besoin
            zoomController.setVisibility(CustomZoomButtonsController.Visibility.NEVER)
            controller.setZoom(15.5)
            controller.setCenter(geoPoint)
            // petit tilt/rotation off par défaut
            isTilesScaledToDpi = true
        }
    }

    // Marker unique (évite les doublons)
    val marker = remember {
        Marker(mapView).apply {
            position = geoPoint
            setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
            title = "Lieu"
        }
    }

    // Met à jour la position si latitude/longitude changent
    LaunchedEffect(geoPoint) {
        mapView.controller.setCenter(geoPoint)
    }

    // Lifecycle -> forward onResume/onPause à osmdroid
    DisposableEffect(lifecycleOwner) {
        val obs = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_RESUME -> mapView.onResume()
                Lifecycle.Event.ON_PAUSE -> mapView.onPause()
                else -> Unit
            }
        }
        lifecycleOwner.lifecycle.addObserver(obs)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(obs)
            // Optionnel: mapView.onDetach() si tu veux libérer plus agressivement
        }
    }

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(14.dp))
            .height(130.dp)
    ) {
        AndroidView(
            factory = { mapView },
            modifier = Modifier.fillMaxSize(),
            update = {
                // au cas où
                it.controller.setCenter(geoPoint)
            }
        )

        // Overlay léger comme ton code
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.10f)),
            contentAlignment = Alignment.Center
        ) {
            Surface(
                shape = CircleShape,
                color = Color.White.copy(alpha = 0.85f)
            ) {
                Icon(
                    Icons.Outlined.NearMe,
                    contentDescription = null,
                    tint = primary,
                    modifier = Modifier.padding(8.dp)
                )
            }
        }
    }
}
