package biz.ei6.judo.data

import biz.ei6.judo.domain.JudoEvent
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
fun JudoEvent.toEntity(): JudoEventEntity = JudoEventEntity(
    id = id.toString(),
    type = type,
    title = title,
    description = description,
    dateLabel = dateLabel,
    timeRange = timeRange,
    location = location,
    latitute = latitute,
    longitude = longitude,
    isFavorite = isFavorite,
    thumbImageUrl = thumbImageUrl,
    featuredImageUrl = featuredImageUrl
)

@OptIn(ExperimentalUuidApi::class)
fun JudoEventEntity.toDomain(): JudoEvent = JudoEvent(
    id = Uuid.parse(id),
    type = type,
    title = title,
    description = description,
    dateLabel = dateLabel,
    timeRange = timeRange,
    location = location,
    latitute = latitute,
    longitude = longitude,
    isFavorite = isFavorite,
    thumbImageUrl = thumbImageUrl,
    featuredImageUrl = featuredImageUrl
)
