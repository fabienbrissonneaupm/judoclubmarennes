package biz.ei6.judo.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import biz.ei6.judo.domain.EventType

@Entity(tableName = "judo_events")
data class JudoEventEntity(
    @PrimaryKey val id: String,
    val type: EventType,
    val title: String,
    val description: String,
    val dateLabel: String,
    val timeRange: String,
    val location: String,
    val latitute: Double,
    val longitude: Double,
    val isFavorite: Boolean,
    val thumbImageUrl: List<String>,
    val featuredImageUrl: String?
)
