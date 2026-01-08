package biz.ei6.tp_android_eluv.screens

import androidx.compose.foundation.ExperimentalFoundationApi





import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.ChevronRight
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import androidx.compose.material.icons.outlined.Star
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview

data class BeachUi(
    val id: Long,
    val name: String,
    val location: String,
    val rating: Double,
    val imageUrl: String
)

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun BeachesScreen(
    beaches: List<BeachUi> = demoBeaches(),
    onBeachClick: (BeachUi) -> Unit = {},
    onProfileClick: () -> Unit = {},
    onAddClick: () -> Unit = {}
) {
    // Couleurs proches du HTML
    val primary = Color(0xFF137FEC)
    val bgLight = Color(0xFFF6F7F8)
    val cardLight = Color(0xFFFFFFFF)
    val textPrimary = Color(0xFF0D141B)
    val textSecondary = Color(0xFF4C739A)

    var query by remember { mutableStateOf("") }
    var selectedTag by remember { mutableStateOf("Tout") }
    val tags = listOf("Tout", "Sable fin", "Criques", "Familial")

    val filtered = remember(beaches, query, selectedTag) {
        beaches
            .asSequence()
            .filter {
                query.isBlank() ||
                        it.name.contains(query, ignoreCase = true) ||
                        it.location.contains(query, ignoreCase = true)
            }
            .filter {
                selectedTag == "Tout" || when (selectedTag) {
                    "Sable fin" -> it.name.contains("Plage", ignoreCase = true)
                    "Criques" -> it.name.contains("Calanque", ignoreCase = true)
                    "Familial" -> it.rating >= 4.5
                    else -> true
                }
            }
            .toList()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(bgLight)
    ) {
        Scaffold(
            containerColor = bgLight,
            floatingActionButton = {
                FloatingActionButton(
                    onClick = onAddClick,
                    containerColor = primary,
                    contentColor = Color.White
                ) {
                    Icon(Icons.Rounded.Add, contentDescription = "Ajouter", modifier = Modifier.size(28.dp))
                }
            }
        ) { padding ->
            // Sticky header via LazyColumn + stickyHeader
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentPadding = PaddingValues(bottom = 92.dp, top = 0.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                stickyHeader {
                    HeaderBlock(
                        title = "Liste des Plages",
                        query = query,
                        onQueryChange = { query = it },
                        tags = tags,
                        selectedTag = selectedTag,
                        onTagSelected = { selectedTag = it },
                        onProfileClick = onProfileClick,
                        primary = primary,
                        bg = bgLight,
                        card = cardLight,
                        textPrimary = textPrimary,
                        textSecondary = textSecondary
                    )
                }

                item { Spacer(Modifier.height(6.dp)) }

                items(filtered, key = { it.id }) { beach ->
                    BeachRow(
                        beach = beach,
                        onClick = { onBeachClick(beach) },
                        card = cardLight,
                        textPrimary = textPrimary,
                        textSecondary = textSecondary,
                        primary = primary
                    )
                }

                item { Spacer(Modifier.height(6.dp)) }
            }
        }
    }
}

@Composable
private fun HeaderBlock(
    title: String,
    query: String,
    onQueryChange: (String) -> Unit,
    tags: List<String>,
    selectedTag: String,
    onTagSelected: (String) -> Unit,
    onProfileClick: () -> Unit,
    primary: Color,
    bg: Color,
    card: Color,
    textPrimary: Color,
    textSecondary: Color
) {
    Surface(
        color = bg.copy(alpha = 0.95f),
        tonalElevation = 0.dp,
        shadowElevation = 0.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .padding(top = 44.dp, bottom = 10.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = title,
                    color = textPrimary,
                    style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold)
                )
                IconButton(onClick = onProfileClick) {
                    Icon(
                        imageVector = Icons.Outlined.AccountCircle,
                        contentDescription = "Profil",
                        tint = textPrimary,
                        modifier = Modifier.size(30.dp)
                    )
                }
            }

            Spacer(Modifier.height(10.dp))

            // Search bar style "pill"
            OutlinedTextField(
                value = query,
                onValueChange = onQueryChange,
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Rechercher une plage...", color = textSecondary) },
                leadingIcon = { Icon(Icons.Outlined.Search, contentDescription = null, tint = textSecondary) },
                singleLine = true,
                shape = RoundedCornerShape(20.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedContainerColor = card,
                    focusedContainerColor = card,
                    unfocusedBorderColor = Color.Transparent,
                    focusedBorderColor = primary,
                    cursorColor = primary
                )
            )

            Spacer(Modifier.height(12.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                tags.forEach { tag ->
                    val selected = tag == selectedTag
                    FilterChip(
                        selected = selected,
                        onClick = { onTagSelected(tag) },
                        label = {
                            Text(
                                tag,
                                fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Medium
                            )
                        },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = primary,
                            selectedLabelColor = Color.White,
                            containerColor = card,
                            labelColor = textSecondary
                        ),
                        border = FilterChipDefaults.filterChipBorder(
                            borderColor = Color.Transparent,
                            selectedBorderColor = Color.Transparent,
                            borderWidth = 0.dp,
                            selectedBorderWidth = 0.dp,
                            enabled = true,
                            selected = true
                        )
                    )
                }
            }
        }
    }
}

@Composable
private fun BeachRow(
    beach: BeachUi,
    onClick: () -> Unit,
    card: Color,
    textPrimary: Color,
    textSecondary: Color,
    primary: Color
) {
    Surface(
        color = card,
        shape = RoundedCornerShape(20.dp),
        tonalElevation = 0.dp,
        shadowElevation = 1.dp,
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .fillMaxWidth()
            .clickable(onClick = onClick)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Image
            AsyncImage(
                model = beach.imageUrl,
                contentDescription = beach.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(64.dp)
                    .clip(RoundedCornerShape(14.dp))
            )

            Spacer(Modifier.width(14.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    beach.name,
                    color = textPrimary,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    beach.location,
                    color = textSecondary,
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(Modifier.height(4.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Outlined.Star,
                        contentDescription = null,
                        tint = Color(0xFFF4B400), // proche du jaune "star"
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(Modifier.width(4.dp))
                    Text(
                        text = "%.1f".format(beach.rating),
                        color = textPrimary,
                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.SemiBold)
                    )
                }
            }

            Icon(
                imageVector = Icons.Outlined.ChevronRight,
                contentDescription = null,
                tint = textSecondary,
                modifier = Modifier.size(26.dp)
            )
        }
    }
}

private fun demoBeaches() = listOf(
    BeachUi(
        id = 1,
        name = "Plage de Pampelonne",
        location = "Ramatuelle, France",
        rating = 4.8,
        imageUrl = "https://lh3.googleusercontent.com/aida-public/AB6AXuACm5xh_hq_MBgDTw0IhpAbUM_ox16eciOBb24hGHlYya5qMpQQPpNFW_4RSWBVwvIQMTfMfjxtwjVoprW4C2qimROW-MLP6KYfTPH9ymtnDbLoZ7W5INmFUXLTob6ozm_mYMYFK3eXzbhbt944MiDy99z_TUTq5ZPZYAVpsRaxxPqTsjCAKCAwOE4MM2-J907Ffg3BurcrfwIBZkISasEk3WMczMbABuKifOylu9drYG9dlmoTvgEs2RUfILxoHon1SZpTRuePy48"
    ),
    BeachUi(
        id = 2,
        name = "Plage de Palombaggia",
        location = "Porto-Vecchio, Corse",
        rating = 4.9,
        imageUrl = "https://lh3.googleusercontent.com/aida-public/AB6AXuAxNBrwbqbVfZkI-_BNZiW6lPwIaru2Hc7sNoktQOf4nlqcqQ4wv5ZIT_p3M7oxOhe83f7NKFnwd3MdwvoMTSDJu_WcGlqmfQ50DlJWoxssT-ZmvulE5KXQ_2SW-kroIK_ewyljL_yL2wzKGMDeoxty9Q4czUMGAp_IxI6xRSypD1fUJ8HsBHTVRN6yzilIiku5J4U7RXz_PBnVuS1bTtq6RBOGTlCgRG992SaDXMOOArYhjja2WnrPxoePxXzK9OnMTP_bUtiqyRs"
    ),
    BeachUi(
        id = 3,
        name = "Calanque d'En-Vau",
        location = "Cassis, France",
        rating = 4.7,
        imageUrl = "https://lh3.googleusercontent.com/aida-public/AB6AXuAFwVo0XFrfNg-9NlIianyXum-n5hbeLI3JdjU0nYrFWgK6rNdikSdEIShP1TBAMrd1bWIvVl7j8jwYnogV6o2JPGjQQUwigdA6-SCTL4K0TrvgyGWDDIZMrKOtGBg9ajc_scXJx3qDjO0Qcxt4P2YOyf7nHMe7-MGwd8snn-m9BdEYUZhazp7jXkM3CX-SXr3aYu32xXx5o4PfbxAtk0gD26T-8Sn8OKHKtFA9iDNA4Je-3OnUL88hkszXHbxgN_TScdWSdIOaqAU"
    ),
    BeachUi(
        id = 4,
        name = "Plage du Prado",
        location = "Marseille, France",
        rating = 4.2,
        imageUrl = "https://lh3.googleusercontent.com/aida-public/AB6AXuDxS7_soWatEhQaHIw3ABKpm0tYclDe-1ny5ho9oJTqIeei-789yh_lwm2VbpMl3b-JIhWA-AwuyBZts9VkIC6wSP-_pgRljdNX6tNW4cOm1uCQZbXp_OgN71YY2xqhohlBAeRj6Li_G_zFEjzZwBIGBSVsZyEZQ9aDSuTeryz_2fj8Ig9otiCiMKYc1U6kgUhj0xjpCelyg5hsvyeXRBeEkykf7UdOjGGNRVLMDufqrrMH7Ltxwi30QxLWuWeSbHQgqYuxNimVeiM"
    ),
    BeachUi(
        id = 5,
        name = "Grande Plage",
        location = "Biarritz, France",
        rating = 4.6,
        imageUrl = "https://lh3.googleusercontent.com/aida-public/AB6AXuD3sHR5Ydkhtf4FxsgDDnRZfN-t5TRvial7Y6jbeDyNERWo9NErJA4hatABUFlqujns4DcEAcgHAvWmRSPP2VpIQIy4l7PbUPKd3XzznnJSzH1mxn4gVB-uEdoeBwojW5JIghB5kvM42wemWkvpgEQGdQrOqtREeuYy0jCwVLMQ69Kaqdy6bHbws5U45-3DJT0nAopgI6oWjDOxspxGcMlRp-2hKyRkzIg62C3XWDmHw4JdKzk1tX8nR8biwtW8HJqR74aIBIaJ1U4"
    ),
    BeachUi(
        id = 6,
        name = "Plage de Santa Giulia",
        location = "Porto-Vecchio, Corse",
        rating = 4.9,
        imageUrl = "https://lh3.googleusercontent.com/aida-public/AB6AXuDyB5hmnurCk-xSG4TCVsiQ60rY1IRmxjjh_BCRz6iMJezxi9jfJ1dGdTxf0gBc--9_CM2Y1f7QO8URK6ZbKHtfYP76ZY-k0TOwDv1WnKX-rEHre7uJ2vmtC8JokTxlch0QzV_4xo3b7UVb2YbA8DvV7h2jQyUHyJx59NxtMebthzucI_wicepwcBllAv6gwvU9sfPcFofhE1GJBuqOl0bN8wbgYReZuI3PzXk1z3i8krM0le9cDbBiSq8f_N9LKfSzj2jeh0_42ZI"
    )
)

@Composable
@Preview
fun BeachesScreensPreview() {
    BeachesScreen {  }
}