package biz.ei6.tp_android_eluv.screens



import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.OpenInNew
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.Straighten
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material.icons.outlined.MyLocation
import androidx.compose.material.icons.outlined.WidthFull
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage

data class BeachDetailsUi(
    val name: String,
    val rating: Double,
    val location: String,
    val length: String,
    val width: String,
    val gps: String,
    val about: String,
    val heroImages: List<String>,
    val mapImageUrl: String,
    val galleryImages: List<String>
)

@Composable
fun BeachDetailsScreen(
    beach: BeachDetailsUi = demoBeachDetails(),
    onBack: () -> Unit = {},
    onToggleFavorite: () -> Unit = {},
    onOpenMap: () -> Unit = {},
    onSeeAllGallery: () -> Unit = {},
    onMoreInfo: () -> Unit = {}
) {
    val primary = Color(0xFF137FEC)
    val bg = Color(0xFFF6F7F8)
    val textPrimary = Color(0xFF0D141B)
    val textSecondary = Color(0xFF64748B) // slate-ish
    val card = Color.White

    var expanded by remember { mutableStateOf(false) }
    var currentPage by remember { mutableIntStateOf(0) }

    // Bottom bar + contenu scrollable
    Scaffold(
        containerColor = bg,
        bottomBar = {
            Surface(
                color = card.copy(alpha = 0.90f),
                tonalElevation = 0.dp,
                shadowElevation = 8.dp
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .padding(top = 12.dp, bottom = 24.dp)
                ) {
                    Button(
                        onClick = onMoreInfo,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp),
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = primary)
                    ) {
                        Text("Voir plus d'infos", fontWeight = FontWeight.Bold)
                        Spacer(Modifier.width(8.dp))
                        Icon(Icons.Filled.OpenInNew, contentDescription = null, modifier = Modifier.size(18.dp))
                    }
                }
            }
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(bottom = 12.dp)
        ) {
            item {
                HeroCarousel(
                    images = beach.heroImages,
                    currentPage = currentPage,
                    onPageChanged = { currentPage = it },
                    onBack = onBack,
                    onToggleFavorite = onToggleFavorite,
                    primary = primary
                )
            }

            item {
                // Body “carte” qui remonte un peu sur le hero
                Surface(
                    color = bg,
                    shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
                    tonalElevation = 0.dp,
                    shadowElevation = 10.dp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .offset(y = (-18).dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                            .padding(bottom = 92.dp), // laisse de la place au bottom bar
                        verticalArrangement = Arrangement.spacedBy(18.dp)
                    ) {
                        HeaderInfo(
                            name = beach.name,
                            rating = beach.rating,
                            location = beach.location,
                            primary = primary,
                            textPrimary = textPrimary,
                            textSecondary = textSecondary
                        )

                        StatsGrid(
                            length = beach.length,
                            width = beach.width,
                            gps = beach.gps,
                            primary = primary,
                            card = card,
                            textPrimary = textPrimary,
                            textSecondary = textSecondary
                        )

                        AboutSection(
                            about = beach.about,
                            expanded = expanded,
                            onToggle = { expanded = !expanded },
                            primary = primary,
                            textPrimary = textPrimary,
                            textSecondary = textSecondary
                        )

                        MapPreview(
                            mapUrl = beach.mapImageUrl,
                            onOpenMap = onOpenMap,
                            primary = primary
                        )

                        GallerySection(
                            images = beach.galleryImages,
                            onSeeAll = onSeeAllGallery,
                            primary = primary,
                            textPrimary = textPrimary
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun HeroCarousel(
    images: List<String>,
    currentPage: Int,
    onPageChanged: (Int) -> Unit,
    onBack: () -> Unit,
    onToggleFavorite: () -> Unit,
    primary: Color
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(320.dp) // ~40vh
    ) {
        // Carousel horizontal
        LazyRow(
            modifier = Modifier.fillMaxSize(),
        ) {
            itemsIndexed(images) { index, url ->
                Box(
                    modifier = Modifier
                        .fillParentMaxWidth()
                        .fillParentMaxHeight()
                ) {
                    AsyncImage(
                        model = url,
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                    // gradient top (comme ton overlay)
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                Brush.verticalGradient(
                                    colors = listOf(Color.Black.copy(alpha = 0.35f), Color.Transparent)
                                )
                            )
                    )
                }

                // update page "au mieux" (simple, sans pager)
                LaunchedEffect(index) {
                    // rien ici
                }
            }
        }

        // Overlay header (back + fav)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .padding(top = 44.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            GlassIconButton(onClick = onBack) {
                Icon(Icons.Filled.ArrowBack, contentDescription = "Retour", tint = Color.White)
            }
            GlassIconButton(onClick = onToggleFavorite) {
                Icon(Icons.Outlined.FavoriteBorder, contentDescription = "Favori", tint = Color.White)
            }
        }

        // Pagination dots (simple)
        Row(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 14.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            images.forEachIndexed { i, _ ->
                Box(
                    modifier = Modifier
                        .size(6.dp)
                        .clip(CircleShape)
                        .background(if (i == currentPage) Color.White else Color.White.copy(alpha = 0.5f))
                )
            }
        }

        // Astuce: sans Pager, on peut au moins permettre un clic pour changer de "page"
        // (si tu veux un vrai pager: dis-moi, je te le fais avec foundation.pager)
        Row(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 14.dp)
        ) {
            // no-op
        }
    }
}

@Composable
private fun GlassIconButton(
    onClick: () -> Unit,
    content: @Composable () -> Unit
) {
    Surface(
        onClick = onClick,
        shape = CircleShape,
        color = Color.Black.copy(alpha = 0.20f),
        tonalElevation = 0.dp,
        shadowElevation = 0.dp,
        modifier = Modifier.size(40.dp)
    ) {
        Box(contentAlignment = Alignment.Center) { content() }
    }
}

@Composable
private fun HeaderInfo(
    name: String,
    rating: Double,
    location: String,
    primary: Color,
    textPrimary: Color,
    textSecondary: Color
) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = name,
                color = textPrimary,
                style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                modifier = Modifier.weight(1f),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(Modifier.width(12.dp))

            Surface(
                color = primary.copy(alpha = 0.10f),
                shape = RoundedCornerShape(10.dp)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Outlined.Star, contentDescription = null, tint = primary, modifier = Modifier.size(18.dp))
                    Spacer(Modifier.width(4.dp))
                    Text(
                        text = "%.1f".format(rating),
                        color = primary,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Outlined.LocationOn, contentDescription = null, tint = primary, modifier = Modifier.size(18.dp))
            Spacer(Modifier.width(6.dp))
            Text(
                text = location,
                color = textSecondary,
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium)
            )
        }
    }
}

@Composable
private fun StatsGrid(
    length: String,
    width: String,
    gps: String,
    primary: Color,
    card: Color,
    textPrimary: Color,
    textSecondary: Color
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        StatCard(
            title = "Longueur",
            value = length,
            icon = { Icon(Icons.Outlined.Straighten, contentDescription = null, tint = primary) },
            primary = primary,
            card = card,
            textPrimary = textPrimary,
            textSecondary = textSecondary,
            modifier = Modifier.weight(1f)
        )
        StatCard(
            title = "Largeur",
            value = width,
            icon = { Icon(Icons.Outlined.WidthFull, contentDescription = null, tint = primary) },
            primary = primary,
            card = card,
            textPrimary = textPrimary,
            textSecondary = textSecondary,
            modifier = Modifier.weight(1f)
        )
        StatCard(
            title = "GPS",
            value = gps,
            icon = { Icon(Icons.Outlined.MyLocation, contentDescription = null, tint = primary) },
            primary = primary,
            card = card,
            textPrimary = textPrimary,
            textSecondary = textSecondary,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun StatCard(
    title: String,
    value: String,
    icon: @Composable () -> Unit,
    primary: Color,
    card: Color,
    textPrimary: Color,
    textSecondary: Color,
    modifier: Modifier = Modifier
) {
    Surface(
        color = card,
        shape = RoundedCornerShape(14.dp),
        tonalElevation = 0.dp,
        shadowElevation = 1.dp,
        modifier = modifier
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Surface(
                color = primary.copy(alpha = 0.10f),
                shape = CircleShape,
                modifier = Modifier.size(40.dp)
            ) {
                Box(contentAlignment = Alignment.Center) { icon() }
            }

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = title.uppercase(),
                    color = textSecondary,
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = value,
                    color = textPrimary,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
private fun AboutSection(
    about: String,
    expanded: Boolean,
    onToggle: () -> Unit,
    primary: Color,
    textPrimary: Color,
    textSecondary: Color
) {
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Text(
            text = "À propos",
            color = textPrimary,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )

        Text(
            text = about,
            color = textSecondary,
            style = MaterialTheme.typography.bodyLarge,
            maxLines = if (expanded) Int.MAX_VALUE else 4,
            overflow = TextOverflow.Ellipsis
        )

        Text(
            text = if (expanded) "Réduire" else "Lire la suite",
            color = primary,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.clickable(onClick = onToggle)
        )
    }
}

@Composable
private fun MapPreview(
    mapUrl: String,
    onOpenMap: () -> Unit,
    primary: Color
) {
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Text(
            text = "Localisation",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(160.dp)
                .clip(RoundedCornerShape(14.dp))
        ) {
            AsyncImage(
                model = mapUrl,
                contentDescription = "Carte",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
            Box(Modifier.fillMaxSize().background(Color.Black.copy(alpha = 0.10f)))

            // Pin (cercle + point)
            Box(
                modifier = Modifier.align(Alignment.Center),
                contentAlignment = Alignment.Center
            ) {
                Surface(
                    color = primary.copy(alpha = 0.20f),
                    shape = CircleShape,
                    modifier = Modifier.size(48.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Box(
                            modifier = Modifier
                                .size(10.dp)
                                .clip(CircleShape)
                                .background(primary)
                        )
                    }
                }
            }

            Surface(
                color = Color.White.copy(alpha = 0.90f),
                shape = RoundedCornerShape(10.dp),
                shadowElevation = 2.dp,
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(12.dp)
                    .clickable(onClick = onOpenMap)
            ) {
                Text(
                    "Ouvrir la carte",
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
private fun GallerySection(
    images: List<String>,
    onSeeAll: () -> Unit,
    primary: Color,
    textPrimary: Color
) {
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Galerie",
                color = textPrimary,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Voir tout",
                color = primary,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.clickable(onClick = onSeeAll)
            )
        }

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(end = 2.dp)
        ) {
            itemsIndexed(images) { _, url ->
                AsyncImage(
                    model = url,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(width = 128.dp, height = 96.dp)
                        .clip(RoundedCornerShape(12.dp))
                )
            }
        }
    }
}

private fun demoBeachDetails() = BeachDetailsUi(
    name = "Plage de Santa Giulia",
    rating = 4.8,
    location = "Porto-Vecchio, Corse, France",
    length = "2 km",
    width = "50m",
    gps = "41.5°N",
    about = "La plage de Santa Giulia est l'un des joyaux de la Corse. Connue pour ses eaux cristallines peu profondes et son sable fin et blanc, elle ressemble à un lagon polynésien. C'est l'endroit idéal pour les familles et les amateurs de sports nautiques. Le cadre est tout simplement idyllique pour se détendre et profiter du soleil méditerranéen.",
    heroImages = listOf(
        "https://lh3.googleusercontent.com/aida-public/AB6AXuDR2gVC9S3aDYOvwM48y57OW309_1G_cDJOcaccDo1T-9VN-Atsii7LBll6yoi_sThrrXPRUOfFM5AeTn-zqPlKhVWI4Z78IjOlB0l5poRSx07nB48ecoUq9liGLW8d2iUUvO1pLcwWROWd7xzhbhWK-GUkryb9eo1CmeEKD8srtI-b6FgVI6b9zRgX_7ThRf3yKocu9q4OOF9jlBJ8Zj8ogSop-q_QjHFmLffOsMGDOGFYapxbbbdBVud9zrqgfbK6ROyms9FouYs",
        "https://lh3.googleusercontent.com/aida-public/AB6AXuCquJGDz3HRP3X-Mhp2PHy3-eTe57IME3bJg89xBslDJNHneKA-qSAilsFJIBreSFL9c_KkeCtE2FLBi3TBPz18stG4pT825pqJvwLFZCRbZ_cb4l6lC45Z9LfUsZb-jVpIbODAK6aYHY9kNH8uiW9Oi-nWM2R43insXsKFUk1Rx3CTp_CAvM6HBF2vIzlW4MfJZaG9h5BGI2Kg4_at1oFBWTFedIcKpMddW31T0kZiE3V7VYMB_iyNYD0PRDuJEBY7jWuVkn5HvPg",
        "https://lh3.googleusercontent.com/aida-public/AB6AXuBC7IykmumI_g07w8rLwS4m1p0zAt3tZFHV0rZaEW9Vjp578vDqm74SU-3QhxH4ryVpJgDdTHUwVQ14QqNAdyEju3P1rRhU0aASaQYCDvT5NljeJD4vuI8OyVcTDbjXczYafeMuchkfdDpnY0CSna1Y36jx0M1ZBIqFRLobrOvqctdQy8s9tplGNx0YQ19SNHC3_s9l9u1rCxF3KodgkCjYRiurMdX9BFCXxNRkjdTGpOjPWVRqw7x19vvtNrj24ejmamWFCbUf2pg"
    ),
    mapImageUrl = "https://lh3.googleusercontent.com/aida-public/AB6AXuAo9D0MNZmcS29i0oiURccJTBtHFNUU6jxxBqHrmotKzFoGGpvFFYVWlZGslAgfFhQ63rExV3SvPoXT1iEuFTh4Kdptj2TZvuApl3Q7Z4-XEXbWl50tb5hmZ3Abvr25dhGO6g9rb0jftd-ERplPwu3NDZX_1DFTtcmrSHh9wOmMQ2YGvyoKkFPcyHUimwM2JHQTH31rZ_ET67J-nl3e2yDAUtbcGRhxNCn8RiT9EXY-3sK5AbCbM4VIqeyfK-BqfsKgIvO2iMHdYXc",
    galleryImages = listOf(
        "https://lh3.googleusercontent.com/aida-public/AB6AXuC-Ny2Nc-9k3kiwSv_-kZEzsV8NBzBgml7zcn1KNRBGnjWwr0djQCDIZC6THI6NNjBdgYrgu_aaiy6jjKHBdd7CmJRovIiGhV6GziJmb3Ph0zvCXW-hGMTxZoiGgJR9wBOwM8dW2Z2it0pyJ_s0sCkXEI4dXpvF_iLftIP5vUA1EU2eX24_S03gsSRHCx2EFyLCnPfZJ_tf98Xa7Y2bVqz8xT-2WVSNhYz-I1xBbXL9kplYk5Ja34iVaavrgJmSq_mRG8OneSXQbQE",
        "https://lh3.googleusercontent.com/aida-public/AB6AXuAsmdQtGW5jD4rfqRh2xzcaxGZq8FHOc8M0K6NUweGLVvRZSlIymhAJFROran6Th7A9cBdSSwNoCWnl68LCOyTGPZ-TlYB7yqavzTum-mVGfkviJChnlO43quCs26vDv5XaBh1F32m5MlIG8gIY0mekzvbTcQ0Eoisr9fx4syN2pjjbNWBdhZKZ13Rt9DLoL3Vw55DfHtLWCLuHbIybJQFQUtgiAoe5i-lpJZ-HuSOMd3ZbEHfmYjqPnRgKWaePdmonJPD_W5ufOcc",
        "https://lh3.googleusercontent.com/aida-public/AB6AXuBfyfWg2G5B9naGzujf99hG-YSZcbw1RCYBkBF4sW0fak5tvqRK3atnkltDlYGh0tq_y52Sptvn-3WYnSoig6Mg2cZrz8RgLC3hBIvTmpkbIFAYSssX19hw_yZhW6XKrlQcqLAehPO4jFwmgHmD-KOyY_lrWFzh4NlNg-hPOIJz703H0ma3dirb2SZGe_JX4baktc9jYpvoLPt9juYSzns_k3hqJZHwa2m4X782DLkXb8DOYqiYpuy2NG3HhyRRPF0COTXWI9dU_gQ",
        "https://lh3.googleusercontent.com/aida-public/AB6AXuAsKgqk3ZBkClcrFWRSz6cbg_E8zMrBjh7pjKXXkV1VLWhodTTNduXPUJ7ijPv76HEA-YSgn7w9hpBafpqcnxkAylcOfOx1XRqQxII7sRQ2w2B7wWGTZmor6BEtFCR8wYfRN-M3Xnv6VpLl-bWzsmNP-vBYvb3Trj2bCKUf3K8pFiul2aQIvb48Aqvv2151k9Ps1YFzF9kw0h-kE_bHwVkQhzK5XAQjRBxoR-Wtvyy0wpkWfksuULnS4qzu9wqJvqktPoMcOMgAsVE"
    )
)

@Composable
@Preview
fun BeachDetailsPreview() {
    BeachDetailsScreen {  }
}