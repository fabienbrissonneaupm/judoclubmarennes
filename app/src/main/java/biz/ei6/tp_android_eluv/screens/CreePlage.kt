package biz.ei6.tp_android_eluv.screens


import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.HorizontalDivider
import androidx.compose.ui.tooling.preview.Preview


data class NewBeachFormState(
    val name: String = "",
    val description: String = "",
    val lengthMeters: String = "",
    val widthMeters: String = "",
    val gps: String = ""
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewBeachScreen(
    state: NewBeachFormState = NewBeachFormState(),
    onStateChange: (NewBeachFormState) -> Unit = {},
    onBack: () -> Unit = {},
    onPickPhoto: () -> Unit = {},
    onUseCurrentLocation: () -> Unit = {},
    onSave: () -> Unit = {}
) {
    val primary = Color(0xFF137FEC)
    val bg = Color(0xFFF6F7F8)
    val card = Color.White
    val textSecondary = Color(0xFF64748B)

    Scaffold(
        containerColor = bg,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Nouvelle Plage",
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBackIosNew,
                            contentDescription = "Retour"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = bg.copy(alpha = 0.95f)
                )
            )
        },
        bottomBar = {
            Surface(
                color = card.copy(alpha = 0.80f),
                tonalElevation = 0.dp,
                shadowElevation = 8.dp
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                        .padding(top = 12.dp, bottom = 18.dp)
                ) {
                    Button(
                        onClick = onSave,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp),
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = primary)
                    ) {
                        Text("Enregistrer la plage", fontWeight = FontWeight.Bold)
                        Spacer(Modifier.width(10.dp))
                        Icon(Icons.Filled.Check, contentDescription = null)
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
                .padding(horizontal = 20.dp)
                .padding(top = 16.dp, bottom = 110.dp), // espace pour le footer fixe
            verticalArrangement = Arrangement.spacedBy(18.dp)
        ) {
            PhotoUploadCard(
                primary = primary,
                textSecondary = textSecondary,
                onClick = onPickPhoto
            )

            Spacer(Modifier.height(2.dp))

            SectionTitle("Informations Générales", textSecondary)

            LabeledTextField(
                label = "Nom de la plage",
                value = state.name,
                onValueChange = { onStateChange(state.copy(name = it)) },
                placeholder = "ex: Plage de Palombaggia",
                card = card,
                primary = primary
            )

            LabeledTextArea(
                label = "Description",
                value = state.description,
                onValueChange = { onStateChange(state.copy(description = it)) },
                placeholder = "Type de sable, clarté de l'eau, accès...",
                card = card,
                primary = primary
            )

            HorizontalDivider(color = Color(0xFFE2E8F0))

            SectionTitle("Dimensions", textSecondary)

            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                LabeledNumberFieldWithSuffix(
                    modifier = Modifier.weight(1f),
                    label = "Longueur (m)",
                    value = state.lengthMeters,
                    onValueChange = { onStateChange(state.copy(lengthMeters = it)) },
                    suffix = "m",
                    card = card,
                    primary = primary
                )
                LabeledNumberFieldWithSuffix(
                    modifier = Modifier.weight(1f),
                    label = "Largeur (m)",
                    value = state.widthMeters,
                    onValueChange = { onStateChange(state.copy(widthMeters = it)) },
                    suffix = "m",
                    card = card,
                    primary = primary
                )
            }

            HorizontalDivider(color = Color(0xFFE2E8F0))

            SectionTitle("Localisation", textSecondary)

            GPSField(
                value = state.gps,
                onValueChange = { onStateChange(state.copy(gps = it)) },
                onUseCurrentLocation = onUseCurrentLocation,
                card = card,
                primary = primary,
                helperText = "Appuyez sur la cible pour utiliser votre position actuelle."
            )

            Spacer(Modifier.height(10.dp))
        }
    }
}

@Composable
private fun PhotoUploadCard(
    primary: Color,
    textSecondary: Color,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(14.dp),
        color = Color.White.copy(alpha = 0.55f),
        border = BorderStroke(2.dp, Color(0xFFCBD5E1)), // slate-300
        tonalElevation = 0.dp,
        shadowElevation = 0.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 28.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Surface(
                color = primary.copy(alpha = 0.10f),
                shape = CircleShape,
                modifier = Modifier.size(56.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        Icons.Filled.PhotoCamera,
                        contentDescription = null,
                        tint = primary,
                        modifier = Modifier.size(26.dp)
                    )
                }
            }

            Text("Ajouter une photo", fontWeight = FontWeight.Bold)

            Text(
                "Appuyez pour prendre une photo ou choisir dans la galerie",
                color = textSecondary,
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}

@Composable
private fun SectionTitle(text: String, color: Color) {
    Text(
        text = text.uppercase(),
        color = color,
        style = MaterialTheme.typography.labelLarge,
        fontWeight = FontWeight.SemiBold,
        modifier = Modifier.padding(start = 2.dp)
    )
}

@Composable
private fun LabeledTextField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    card: Color,
    primary: Color
) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(label, fontWeight = FontWeight.SemiBold, modifier = Modifier.padding(start = 2.dp))
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text(placeholder) },
            shape = RoundedCornerShape(14.dp),
            singleLine = true,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = primary,
                cursorColor = primary,
                focusedContainerColor = card,
                unfocusedContainerColor = card
            )
        )
    }
}

@Composable
private fun LabeledTextArea(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    card: Color,
    primary: Color
) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(label, fontWeight = FontWeight.SemiBold, modifier = Modifier.padding(start = 2.dp))
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 130.dp),
            placeholder = { Text(placeholder) },
            shape = RoundedCornerShape(14.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = primary,
                cursorColor = primary,
                focusedContainerColor = card,
                unfocusedContainerColor = card
            )
        )
    }
}

@Composable
private fun LabeledNumberFieldWithSuffix(
    modifier: Modifier = Modifier,
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    suffix: String,
    card: Color,
    primary: Color
) {
    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(label, fontWeight = FontWeight.SemiBold, modifier = Modifier.padding(start = 2.dp))
        OutlinedTextField(
            value = value,
            onValueChange = { onValueChange(it.filter { ch -> ch.isDigit() }) },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("0") },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            trailingIcon = { Text(suffix, color = Color(0xFF94A3B8)) },
            shape = RoundedCornerShape(14.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = primary,
                cursorColor = primary,
                focusedContainerColor = card,
                unfocusedContainerColor = card
            )
        )
    }
}

@Composable
private fun GPSField(
    value: String,
    onValueChange: (String) -> Unit,
    onUseCurrentLocation: () -> Unit,
    card: Color,
    primary: Color,
    helperText: String
) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text("Coordonnées GPS", fontWeight = FontWeight.SemiBold, modifier = Modifier.padding(start = 2.dp))

        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("Latitude, Longitude") },
            singleLine = true,
            leadingIcon = {
                Icon(Icons.Outlined.LocationOn, contentDescription = null, tint = Color(0xFF94A3B8))
            },
            trailingIcon = {
                IconButton(onClick = onUseCurrentLocation) {
                    Icon(Icons.Filled.MyLocation, contentDescription = "Utiliser ma position", tint = primary)
                }
            },
            shape = RoundedCornerShape(14.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = primary,
                cursorColor = primary,
                focusedContainerColor = card,
                unfocusedContainerColor = card
            )
        )

        Text(
            helperText,
            style = MaterialTheme.typography.bodySmall,
            color = Color(0xFF64748B),
            modifier = Modifier.padding(start = 2.dp)
        )
    }
}

@Composable
@Preview
fun NewBeachScreenPreview() {
    NewBeachScreen()
}