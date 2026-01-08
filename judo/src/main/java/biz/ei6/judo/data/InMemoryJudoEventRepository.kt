package biz.ei6.judo.data

import biz.ei6.judo.domain.JudoEvent
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid
/*class InMemoryJudoEventRepository : JudoEventRepository {

    private val _events = MutableStateFlow(JudoEvent.LIST)
    override val events: StateFlow<List<JudoEvent>> = _events

    @OptIn(ExperimentalUuidApi::class)
    override fun toggleFavorite(id: Uuid) {
        _events.value = _events.value.map { e ->
            if (e.id == id) e.copy(isFavorite = !e.isFavorite) else e
        }
    }

    override suspend fun add(event: JudoEvent) {
        TODO("Not yet implemented")
    }

    @OptIn(ExperimentalUuidApi::class)
    override suspend fun remove(id: Uuid) {
        TODO("Not yet implemented")
    }
}*/