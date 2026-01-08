package biz.ei6.judo.screens


import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.ChevronRight
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import biz.ei6.judo.domain.EventType
import biz.ei6.judo.domain.JudoEvent
import biz.ei6.judo.presentation.JudoVM
import biz.ei6.judo.ui.theme.TheTheme
import coil.compose.AsyncImage
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid


@OptIn(ExperimentalUuidApi::class)
@Composable
fun JudoEventsScreen(
    vm: JudoVM = viewModel(),
    onEventClick: (JudoEvent) -> Unit = {},
    onFabClick: () -> Unit = {},
    onBottomNav: (String) -> Unit = {}
) {

    val events by vm.events.collectAsState()

    var bottomSelected by remember { mutableStateOf("Accueil") }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        floatingActionButton = {
            FloatingActionButton(
                onClick = onFabClick,
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = Color.White,
                shape = RoundedCornerShape(16.dp)
            ) {
                Icon(Icons.Filled.Add, contentDescription = "Ajouter")
            }
        },
        bottomBar = {
            BottomBar(
                selected = bottomSelected,
                onSelect = {
                    bottomSelected = it
                    onBottomNav(it)
                }
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),

            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(
                items = events,
                key = { it.id }
            ) { e ->
                StandardEventCard(
                    event = e,
                    onClick = { onEventClick(e) }
                )
            }
        }
    }
}

@Composable
private fun TopBarDark(
    title: String,
    profileImageUrl: String,
    onBack: () -> Unit,
    onProfile: () -> Unit,
    bg: Color
) {
    Surface(
        color = bg.copy(alpha = 0.95f),
        tonalElevation = 0.dp,
        shadowElevation = 0.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .padding(top = 10.dp, bottom = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBack) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Retour", tint = Color.White)
            }
            Text(
                title,
                modifier = Modifier.weight(1f),
                color = MaterialTheme.colorScheme.onSurface,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            IconButton(onClick = onProfile) {
                AsyncImage(
                    model = profileImageUrl,
                    contentDescription = "Profil",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape)
                )
            }
        }
    }
}

@Composable
private fun StandardEventCard(
    event: JudoEvent,
    onClick: () -> Unit
) {

    Surface(
        onClick = onClick,
        color = MaterialTheme.colorScheme.surfaceVariant,
        shape = RoundedCornerShape(14.dp),
        shadowElevation = 1.dp,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .padding(12.dp)
                ,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // left thumb (image or date box)
            if (event.thumbImageUrl.isNotEmpty()) {
                Box(
                    modifier = Modifier
                        .size(96.dp)
                        .clip(RoundedCornerShape(12.dp))
                ) {
                    AsyncImage(
                        model = event.thumbImageUrl[0],
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )

                }
            } else {
                Surface(
                    color = MaterialTheme.colorScheme.surface,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.size(96.dp)
                ) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text((event.dateLabel ).uppercase(),
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontWeight = FontWeight.Bold)
                        Text(event.timeRange,
                            color = MaterialTheme.colorScheme.primary,
                            style = MaterialTheme.typography.headlineSmall,
                                    fontWeight = FontWeight.Bold)
                    }
                }
            }

            Spacer(Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = when (event.type) {
                            EventType.Stage -> "Stage Technique"
                            EventType.Competition -> "Compétition"
                            EventType.Grade -> "Passage de Grade"
                            else -> "Événement"
                        },
                        color = when (event.type) {
                            EventType.Grade -> Color(0xFFF59E0B) // amber
                            else -> MaterialTheme.colorScheme.primary
                        },
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold
                    )
                    if (event.dateLabel.isNotBlank()) {
                        Text(event.dateLabel, color = MaterialTheme.colorScheme.onSurface,
                            style = MaterialTheme.typography.labelMedium)
                    }
                }

                Text(
                    event.title,
                    color = MaterialTheme.colorScheme.onSurfaceVariant ,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Outlined.LocationOn,
                        contentDescription = null, tint = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.size(18.dp))
                    Spacer(Modifier.width(6.dp))
                    Text(
                        event.location,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }


            }

            IconButton(onClick = onClick) {
                Icon(Icons.Outlined.ChevronRight, contentDescription = null, tint = MaterialTheme.colorScheme.onSurface)
            }
        }
    }
}

@Composable
private fun BottomBar(
    selected: String,
    onSelect: (String) -> Unit
) {
    Surface(color = MaterialTheme.colorScheme.surface, tonalElevation = 0.dp, shadowElevation = 8.dp) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(80.dp)
                .padding(horizontal = 8.dp)
                .padding(bottom = 14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            BottomItem(
                label = "Accueil",
                selected = selected == "Accueil",
                icon = Icons.Filled.Home,
                onClick = { onSelect("Accueil") }
            )
            BottomItem(
                label = "Carte",
                selected = selected == "Carte",
                icon = Icons.Filled.Map,
                onClick = { onSelect("Carte") }
            )
            BottomItem(
                label = "Favoris",
                selected = selected == "Favoris",
                icon = Icons.Filled.Favorite,
                onClick = { onSelect("Favoris") }
            )
            BottomItem(
                label = "Profil",
                selected = selected == "Profil",
                icon = Icons.Filled.Person,
                onClick = { onSelect("Profil") }
            )
        }
    }
}

@Composable
private fun BottomItem(
    label: String,
    selected: Boolean,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier

            .clickable(onClick = onClick)
            .padding(vertical = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        if (selected) {
            Surface(color = MaterialTheme.colorScheme.primary.copy(alpha = 0.10f),
                shape = RoundedCornerShape(999.dp)) {
                Row(Modifier.padding(horizontal = 16.dp, vertical = 4.dp)) {
                    Icon(icon, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                }
            }
            Text(label, color = MaterialTheme.colorScheme.onPrimary,
                style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Medium)
        } else {
            Icon(icon, contentDescription = null, tint = MaterialTheme.colorScheme.onSurface)
            Text(label, color = MaterialTheme.colorScheme.onSurface,
                style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Medium)
        }
    }
}

@Composable
@Preview(showBackground = true)
fun StandardEventCardPreview() {
    TheTheme () {
        StandardEventCard (JudoEvent.DEFAULT,  {})
    }
}
@Composable
@Preview(showBackground = true)
fun JudoEventsScreenPreview() {
    TheTheme () {
        JudoEventsScreen ()
    }
}