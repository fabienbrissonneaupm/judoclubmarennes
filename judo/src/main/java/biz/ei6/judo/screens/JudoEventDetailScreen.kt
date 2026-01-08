package biz.ei6.judo.screens


import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import biz.ei6.judo.domain.JudoEvent
import biz.ei6.judo.framework.OsmMapPreview
import biz.ei6.judo.presentation.JudoVM
import biz.ei6.judo.ui.theme.TheTheme
import coil.compose.AsyncImage
import kotlin.uuid.ExperimentalUuidApi


@OptIn(ExperimentalUuidApi::class)
@Composable
fun JudoEventDetailScreen(
    vm: JudoVM = viewModel(),
    selected: JudoEvent,
    onBack: () -> Unit = {},
    onEdit: () -> Unit = {},
    onMore: () -> Unit = {},
) {
    val events by vm.events.collectAsState()
    val current = events.firstOrNull { it.id == selected.id } ?: selected

    val primary = MaterialTheme.colorScheme.primary
    val bg = MaterialTheme.colorScheme.background
    val surface = MaterialTheme.colorScheme.surface
    val muted = Color(0xFF9DA6B9)


    Scaffold(
        containerColor = surface,
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = {  vm.toggleFavorite(selected.id)},
                containerColor = primary,
                contentColor = Color.White,
                shape = RoundedCornerShape(16.dp),
                text = { Text("Favori", fontWeight = FontWeight.Bold) },
                icon = { if(current.isFavorite)
                        Icon(Icons.Filled.Favorite, contentDescription = null)
                    else
                        Icon(Icons.Outlined.FavoriteBorder, contentDescription = null)
                }
            )
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
        ) {

            /* ───────────── Top App Bar ───────────── */

            Column(
                modifier = Modifier
                    .background(bg)
                    .padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Retour",
                            tint = Color.White)
                    }
                    Row {
                        IconButton(onClick = onEdit) {
                            Icon(Icons.Outlined.Edit, contentDescription = "Éditer",
                                tint = Color.White)
                        }
                        IconButton(onClick = onMore) {
                            Icon(Icons.Outlined.MoreVert, contentDescription = "Plus",
                                tint = Color.White)
                        }
                    }
                }

                Text(
                    selected.title,
                    color = Color.White,
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold
                )
            }

            /* ───────────── Hero Image ───────────── */

            Box(
                modifier = Modifier

                    .padding(16.dp)
                    .background(surface)
                    .aspectRatio(16f / 9f)
                    .clip(RoundedCornerShape(14.dp))
            ) {
                AsyncImage(
                    model = selected.featuredImageUrl,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )

                Surface(
                    color = primary.copy(alpha = 0.9f),
                    shape = RoundedCornerShape(6.dp),
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(12.dp)
                ) {
                    Text(
                        text = selected.type.toString(),
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        color = Color.White,
                        style = MaterialTheme.typography.labelSmall
                    )
                }
            }

            /* ───────────── Info Cards ───────────── */

            InfoCard(
                icon = Icons.Outlined.CalendarMonth,
                title = "Date et Heure",
                lines = listOf(selected.dateLabel , selected.timeRange),
                trailing = {

                },
                surface = surface,
                primary = primary,
                muted = muted
            )

            InfoCard(
                icon = Icons.Outlined.LocationOn,
                title = "Lieu",
                lines = listOf(selected.location),
                trailing = {
                    IconButton(onClick = {  }) {
                        Icon(Icons.Outlined.Map, contentDescription = "Carte", tint = primary)
                    }
                },
                surface = surface,
                primary = primary,
                muted = muted
            )

//            /* ───────────── Map Preview ───────────── */
//
//            Box(
//                modifier = Modifier
//                    .padding(horizontal = 16.dp)
//                    .height(130.dp)
//                    .clip(RoundedCornerShape(14.dp))
//            ) {
//                AsyncImage(
//                    model = "https://placehold.co/600x400?text=Map+Viroflay",
//
//                    contentDescription = null,
//                    contentScale = ContentScale.Crop,
//                    modifier = Modifier.fillMaxSize()
//                )
//
//                Box(
//                    modifier = Modifier
//                        .fillMaxSize()
//                        .background(Color.Black.copy(alpha = 0.1f)),
//                    contentAlignment = Alignment.Center
//                ) {
//                    Surface(
//                        shape = CircleShape,
//                        color = Color.White.copy(alpha = 0.85f)
//                    ) {
//                        Icon(
//                            Icons.Outlined.NearMe,
//                            contentDescription = null,
//                            tint = primary,
//                            modifier = Modifier.padding(8.dp)
//                        )
//                    }
//                }
//            }

            /* ───────────── Map Preview ───────────── */

            OsmMapPreview(
                latitude = selected.latitute,
                longitude = selected.longitude,
                primary = primary,
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            HorizontalDivider(Modifier.padding(16.dp), color = Color.DarkGray)

            /* ───────────── Description ───────────── */

            Column(Modifier.padding(horizontal = 16.dp)) {
                Text(
                    "À propos de l'événement",
                    color = Color.White,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text=selected.description,
                    color = muted,
                    modifier = Modifier.padding(top = 8.dp)
                )


            }

            /* ───────────── Galerie ───────────── */

            Column(Modifier.padding(top = 16.dp)) {
                Row(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Galerie", color = Color.White, style = MaterialTheme.typography.titleLarge)

                }

                Row(
                    modifier = Modifier
                        .horizontalScroll(rememberScrollState())
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    for(url in selected.thumbImageUrl) {
                        AsyncImage(
                            model = url,
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .size(120.dp)
                                .clip(RoundedCornerShape(14.dp))
                        )
                    }
                }
            }

            Spacer(Modifier.height(100.dp))
        }
    }
}

/* ───────────── Reusable Components ───────────── */

@Composable
private fun InfoCard(
    icon: ImageVector,
    title: String,
    lines: List<String>,
    trailing: @Composable () -> Unit,
    surface: Color,
    primary: Color,
    muted: Color
) {
    Surface(
        color = surface,
        shape = RoundedCornerShape(14.dp),
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 6.dp)
            .fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                shape = RoundedCornerShape(8.dp),
                color = primary.copy(alpha = 0.1f)
            ) {
                Icon(
                    icon,
                    contentDescription = null,
                    tint = primary,
                    modifier = Modifier.padding(10.dp)
                )
            }

            Spacer(Modifier.width(12.dp))

            Column(Modifier.weight(1f)) {
                Text(title, color = Color.White, fontWeight = FontWeight.SemiBold)
                lines.forEach {
                    Text(it, color = muted, style = MaterialTheme.typography.bodySmall)
                }
            }

            trailing()
        }
    }
}



@Composable
@Preview(showBackground = true)
fun JudoEventDetailScreenPreview() {
    TheTheme(darkTheme = true) {
        JudoEventDetailScreen (selected = JudoEvent.DEFAULT)
    }
}

