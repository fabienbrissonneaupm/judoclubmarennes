package biz.ei6.judo.screens


import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material.icons.outlined.CalendarToday
import androidx.compose.material.icons.outlined.Link
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.PhotoCamera
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import biz.ei6.judo.presentation.JudoVM

data class NewEventState(
    val title: String = "",
    val description: String = "",
    val date: String = "",   // ex: "12/10/2024"
    val time: String = "",   // ex: "09:00"
    val location: String = "",
    val externalLink: String = "",
    val coverImageUrl: String? = null // si tu veux afficher une image sélectionnée
)

@Composable
fun JudoEventNewScreen(
    vm: JudoVM = viewModel(),
    state: NewEventState = NewEventState(),
    onStateChange: (NewEventState) -> Unit = {},
    onBack: () -> Unit = {},
    onSaveDraft: () -> Unit = {},
    onPickCover: () -> Unit = {},
    onPickDate: () -> Unit = {},   // ouvre un DatePickerDialog
    onPickTime: () -> Unit = {},   // ouvre un TimePickerDialog
    onUseMyLocation: () -> Unit = {},
    onCreateEvent: () -> Unit = {}
) {
    val primary = Color(0xFF135BEC)
    val bg = Color(0xFF101622)
    val surface = Color(0xFF1C212C)
    val border = Color(0xFF2D3442)
    val textSecondary = Color(0xFF9DA6B9)

    Scaffold(
        containerColor = bg,
        topBar = {
            Surface(
                color = bg.copy(alpha = 0.90f),
                shadowElevation = 0.dp
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .padding(top = 12.dp, bottom = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Filled.ArrowBackIosNew, contentDescription = "Retour", tint = Color.White)
                    }

                    Text(
                        "Nouvel événement",
                        modifier = Modifier.weight(1f),
                        color = Color.White,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )

                    TextButton(onClick = onSaveDraft) {
                        Text("Enregistrer", color = primary, fontWeight = FontWeight.Bold)
                    }
                }
            }
        },
        bottomBar = {
            Surface(
                color = bg.copy(alpha = 0.95f),
                shadowElevation = 10.dp
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .padding(top = 12.dp, bottom = 24.dp)
                ) {
                    Button(
                        onClick = onCreateEvent,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = primary)
                    ) {
                        Icon(Icons.Filled.AddCircle, contentDescription = null)
                        Spacer(Modifier.width(10.dp))
                        Text("Créer l'événement", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
                    }
                }
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp)
                .padding(top = 14.dp, bottom = 110.dp),
            verticalArrangement = Arrangement.spacedBy(18.dp)
        ) {

            // ───── Visuel ─────
            SectionLabel("Visuel", textSecondary)
            CoverUploader(
                coverImageUrl = state.coverImageUrl,
                onClick = onPickCover,
                surface = surface,
                border = border,
                primary = primary
            )

            // ───── Détails ─────
            SectionLabel("Détails de l'événement", textSecondary)

            DarkOutlinedField(
                value = state.title,
                onValueChange = { onStateChange(state.copy(title = it)) },
                placeholder = "Nom du tournoi ou de la séance",
                surface = surface,
                border = border,
                primary = primary
            )

            DarkOutlinedArea(
                value = state.description,
                onValueChange = { onStateChange(state.copy(description = it)) },
                placeholder = "Description (catégories de poids, règles, etc.)",
                surface = surface,
                border = border,
                primary = primary
            )

            // ───── Date & Heure ─────
            SectionLabel("Date et Heure", textSecondary)

            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                ClickableIconField(
                    modifier = Modifier.weight(1f),
                    value = state.date,
                    placeholder = "Date",
                    icon = Icons.Outlined.CalendarToday,
                    onClick = onPickDate,
                    surface = surface,
                    border = border,
                    primary = primary,
                    textSecondary = textSecondary
                )
                ClickableIconField(
                    modifier = Modifier.weight(1f),
                    value = state.time,
                    placeholder = "Heure",
                    icon = Icons.Outlined.Schedule,
                    onClick = onPickTime,
                    surface = surface,
                    border = border,
                    primary = primary,
                    textSecondary = textSecondary
                )
            }

            // ───── Lieu ─────
            SectionLabel("Lieu", textSecondary)

            LocationField(
                value = state.location,
                onValueChange = { onStateChange(state.copy(location = it)) },
                onUseMyLocation = onUseMyLocation,
                surface = surface,
                border = border,
                primary = primary,
                textSecondary = textSecondary
            )

            MapPreview(
                imageUrl = "https://lh3.googleusercontent.com/aida-public/AB6AXuDxq6EapL7sIC6mvA9quRvcnZmmMbBcsqB0EXeTlJ3KMDupntScIVn9LdJX_vYUA_u2NgS6TCAg_yBsVpRcklYtff0EW0YuyIEsogLmkkuxyFq2mXjGVpZE0DOtTAaWRJDZYeA_-H_pZU4U3CSeq-nWTVgrFYCMfQHWFblsrpxkMiD13nnBOR4zK04DbvsW522i6kSF_9Isrlb5MXZ57mtZ_GWfEgfLFfMN6ihhnPMAOhLGTHbj3QtIFMb1LBUvkf6-J5RzCOUqYAMs",
                surface = surface,
                border = border,
                primary = primary
            )

            // ───── Lien ─────
            SectionLabel("Information Complémentaire", textSecondary)

            IconTextField(
                value = state.externalLink,
                onValueChange = { onStateChange(state.copy(externalLink = it)) },
                placeholder = "https://www.ffjudo.com/...",
                icon = Icons.Outlined.Link,
                keyboardType = KeyboardType.Uri,
                surface = surface,
                border = border,
                primary = primary,
                textSecondary = textSecondary
            )
        }
    }
}

/* ───────────────────────── Components ───────────────────────── */

@Composable
private fun SectionLabel(text: String, color: Color) {
    Text(
        text = text,
        color = color,
        style = MaterialTheme.typography.labelLarge,
        fontWeight = FontWeight.Medium,
        modifier = Modifier.padding(start = 2.dp)
    )
}

@Composable
private fun CoverUploader(
    coverImageUrl: String?,
    onClick: () -> Unit,
    surface: Color,
    border: Color,
    primary: Color
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(16f / 9f)
            .clickable(onClick = onClick),
        color = surface,
        shape = RoundedCornerShape(14.dp),
        border = BorderStroke(2.dp, border),
        tonalElevation = 0.dp,
        shadowElevation = 0.dp
    ) {
        if (coverImageUrl != null) {
            AsyncImage(
                model = coverImageUrl,
                contentDescription = "Visuel",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        } else {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Surface(
                    color = primary.copy(alpha = 0.15f),
                    shape = CircleShape,
                    modifier = Modifier.size(48.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(Icons.Outlined.PhotoCamera, contentDescription = null, tint = primary)
                    }
                }
                Spacer(Modifier.height(10.dp))
                Text("Ajouter une photo de couverture", color = Color(0xFF94A3B8))
            }
        }
    }
}

@Composable
private fun DarkOutlinedField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    surface: Color,
    border: Color,
    primary: Color
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = Modifier.fillMaxWidth(),
        placeholder = { Text(placeholder, color = Color(0xFF9DA6B9)) },
        singleLine = true,
        shape = RoundedCornerShape(14.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = surface,
            unfocusedContainerColor = surface,
            focusedBorderColor = primary,
            unfocusedBorderColor = border,
            cursorColor = primary,
            focusedTextColor = Color.White,
            unfocusedTextColor = Color.White
        )
    )
}

@Composable
private fun DarkOutlinedArea(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    surface: Color,
    border: Color,
    primary: Color
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 120.dp),
        placeholder = { Text(placeholder, color = Color(0xFF9DA6B9)) },
        shape = RoundedCornerShape(14.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = surface,
            unfocusedContainerColor = surface,
            focusedBorderColor = primary,
            unfocusedBorderColor = border,
            cursorColor = primary,
            focusedTextColor = Color.White,
            unfocusedTextColor = Color.White
        )
    )
}

@Composable
private fun ClickableIconField(
    modifier: Modifier = Modifier,
    value: String,
    placeholder: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    onClick: () -> Unit,
    surface: Color,
    border: Color,
    primary: Color,
    textSecondary: Color
) {
    // Champ "read-only" cliquable (comme input date/time)
    Surface(
        modifier = modifier
            .height(56.dp)
            .clip(RoundedCornerShape(14.dp))
            .clickable(onClick = onClick),
        color = surface,
        border = BorderStroke(1.dp, border),
        shape = RoundedCornerShape(14.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(icon, contentDescription = null, tint = textSecondary)
            Spacer(Modifier.width(10.dp))
            Text(
                text = if (value.isBlank()) placeholder else value,
                color = if (value.isBlank()) textSecondary else Color.White,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
private fun LocationField(
    value: String,
    onValueChange: (String) -> Unit,
    onUseMyLocation: () -> Unit,
    surface: Color,
    border: Color,
    primary: Color,
    textSecondary: Color
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = Modifier.fillMaxWidth(),
        placeholder = { Text("Adresse ou coordonnées GPS", color = textSecondary) },
        leadingIcon = { Icon(Icons.Outlined.LocationOn, contentDescription = null, tint = textSecondary) },
        trailingIcon = {
            IconButton(onClick = onUseMyLocation) {
                Icon(Icons.Filled.MyLocation, contentDescription = "Utiliser ma position", tint = primary)
            }
        },
        singleLine = true,
        shape = RoundedCornerShape(14.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = surface,
            unfocusedContainerColor = surface,
            focusedBorderColor = primary,
            unfocusedBorderColor = border,
            cursorColor = primary,
            focusedTextColor = Color.White,
            unfocusedTextColor = Color.White
        )
    )
}

@Composable
private fun MapPreview(
    imageUrl: String,
    surface: Color,
    border: Color,
    primary: Color
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(130.dp)
            .clip(RoundedCornerShape(14.dp))
            .background(surface)
            .border(BorderStroke(1.dp, border), RoundedCornerShape(14.dp))
    ) {
        AsyncImage(
            model = imageUrl,
            contentDescription = "Aperçu carte",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.25f)),
            contentAlignment = Alignment.Center
        ) {
            Surface(color = surface, shape = CircleShape, shadowElevation = 6.dp) {
                Icon(
                    Icons.Outlined.LocationOn,
                    contentDescription = null,
                    tint = primary,
                    modifier = Modifier.padding(10.dp)
                )
            }
        }
    }
}

@Composable
private fun IconTextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    keyboardType: KeyboardType,
    surface: Color,
    border: Color,
    primary: Color,
    textSecondary: Color
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = Modifier.fillMaxWidth(),
        placeholder = { Text(placeholder, color = textSecondary) },
        leadingIcon = { Icon(icon, contentDescription = null, tint = textSecondary) },
        singleLine = true,
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        shape = RoundedCornerShape(14.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = surface,
            unfocusedContainerColor = surface,
            focusedBorderColor = primary,
            unfocusedBorderColor = border,
            cursorColor = primary,
            focusedTextColor = Color.White,
            unfocusedTextColor = Color.White
        )
    )
}


@Composable
@Preview(showBackground = true)
fun NewJudoEventScreenPreview() {
    JudoEventNewScreen()
}