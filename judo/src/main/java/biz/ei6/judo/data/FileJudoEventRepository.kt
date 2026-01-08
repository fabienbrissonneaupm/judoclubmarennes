
package biz.ei6.judo.data

import android.content.Context
import biz.ei6.judo.domain.JudoEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.withContext
import java.io.File
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

class FileJudoEventRepository(
    context: Context,
    fileName: String = "judo_events.csv"
) : JudoEventRepository {

    private val file = File(context.applicationContext.filesDir, fileName)

    private val _events = MutableStateFlow<List<JudoEvent>>(emptyList())
    override val events: StateFlow<List<JudoEvent>> = _events

    /**
     * À appeler une fois au démarrage (ex: ViewModel init).
     */
    override suspend fun init() {
        _events.value = loadOrDefault()
    }

    @OptIn(ExperimentalUuidApi::class)
    override suspend fun toggleFavorite(id: Uuid) {
        val updated = _events.value.map { e ->
            if (e.id == id) e.copy(isFavorite = !e.isFavorite) else e
        }
        _events.value = updated
        persist(updated)
    }

    @OptIn(ExperimentalUuidApi::class)
    override suspend fun add(event: JudoEvent) {
        val updated = _events.value
            .filterNot { it.id == event.id } + event

        _events.value = updated
        persist(updated)
    }

    @OptIn(ExperimentalUuidApi::class)
    override suspend fun remove(id: Uuid) {
        val updated = _events.value.filterNot { it.id == id }
        _events.value = updated
        persist(updated)
    }

    // --------------------
    // IO
    // --------------------

    private suspend fun loadOrDefault(): List<JudoEvent> = withContext(Dispatchers.IO) {
        if (!file.exists()) {
            val initial = JudoEvent.LIST
            writeFile(initial)
            return@withContext initial
        }

        val lines = runCatching { file.readLines() }.getOrElse { emptyList() }
        val loaded = lines.mapNotNull { judoEventFromCsv(it) }

        if (loaded.isEmpty()) {
            val fallback = JudoEvent.LIST
            writeFile(fallback)
            fallback
        } else loaded
    }

    private suspend fun persist(list: List<JudoEvent>) = withContext(Dispatchers.IO) {
        writeFile(list)
    }

    private fun writeFile(list: List<JudoEvent>) {
        // Écriture simple (tu peux garder le .tmp si tu veux)
        val content = list.joinToString("\n") { it.toCsvLine() }
        file.writeText(content)
    }
}
