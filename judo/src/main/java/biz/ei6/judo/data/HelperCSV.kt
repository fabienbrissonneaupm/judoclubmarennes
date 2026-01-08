package biz.ei6.judo.data

import biz.ei6.judo.domain.EventType
import biz.ei6.judo.domain.JudoEvent
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

private const val SEP = "#"
private const val LIST_SEP = "|"

// Escape minimal mais fiable pour les besoins
private fun enc(s: String): String =
    s.replace("\\", "\\\\")
        .replace("\n", "\\n")
        .replace("\r", "\\r")
        .replace(SEP, "\\h")       // h = hash
        .replace(LIST_SEP, "\\p")  // p = pipe

private fun dec(s: String): String =
    s.replace("\\p", LIST_SEP)
        .replace("\\h", SEP)
        .replace("\\r", "\r")
        .replace("\\n", "\n")
        .replace("\\\\", "\\")

// Helpers double tolérants (virgule / point)
private fun parseDoubleSafe(s: String): Double =
    s.replace(',', '.').toDoubleOrNull() ?: 0.0

@OptIn(ExperimentalUuidApi::class)
fun JudoEvent.toCsvLine(): String {
    val thumbs = thumbImageUrl.joinToString(LIST_SEP) { enc(it) }
    val featured = featuredImageUrl?.let { enc(it) } ?: ""

    return listOf(
        id.toString(),
        type.name,
        enc(title),
        enc(description),
        enc(dateLabel),
        enc(timeRange),
        enc(location),
        latitute.toString(),
        longitude.toString(),
        isFavorite.toString(),
        thumbs,
        featured
    ).joinToString(SEP)
}

@OptIn(ExperimentalUuidApi::class)
fun judoEventFromCsv(line: String): JudoEvent? {
    // IMPORTANT: comme on a remplacé les # par \h, on peut split sur # sans ambiguïté
    val p = line.split(SEP)
    if (p.size < 12) return null

    return runCatching {
        val id = Uuid.parse(p[0])
        val type = EventType.valueOf(p[1])

        val title = dec(p[2])
        val description = dec(p[3])
        val dateLabel = dec(p[4])
        val timeRange = dec(p[5])
        val location = dec(p[6])

        val lat = parseDoubleSafe(p[7])
        val lon = parseDoubleSafe(p[8])
        val fav = p[9].toBoolean()

        val thumbsField = p[10]
        val thumbs = if (thumbsField.isBlank()) emptyList()
        else thumbsField.split(LIST_SEP).map { dec(it) }

        val featuredField = p[11]
        val featured = featuredField.takeIf { it.isNotBlank() }?.let { dec(it) }

        JudoEvent(
            id = id,
            type = type,
            title = title,
            description = description,
            dateLabel = dateLabel,
            timeRange = timeRange,
            location = location,
            latitute = lat,
            longitude = lon,
            isFavorite = fav,
            thumbImageUrl = thumbs,
            featuredImageUrl = featured
        )
    }.getOrNull()
}
