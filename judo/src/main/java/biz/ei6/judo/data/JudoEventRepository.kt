package biz.ei6.judo.data

import biz.ei6.judo.domain.JudoEvent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid
/*
interface JudoEventRepository {
    val events: StateFlow<List<JudoEvent>>
    @OptIn(ExperimentalUuidApi::class)
    suspend fun toggleFavorite(id: Uuid)
    suspend fun add(event: JudoEvent)
    @OptIn(ExperimentalUuidApi::class)
    suspend fun remove(id: Uuid)
    suspend fun init()
}*/


interface JudoEventRepository {
    val events: Flow<List<JudoEvent>>

    @OptIn(ExperimentalUuidApi::class)
    suspend fun toggleFavorite(id: Uuid)

    suspend fun add(event: JudoEvent)

    @OptIn(ExperimentalUuidApi::class)
    suspend fun remove(id: Uuid)

    suspend fun init()
}
