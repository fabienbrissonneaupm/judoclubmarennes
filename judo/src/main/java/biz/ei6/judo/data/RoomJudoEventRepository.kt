package biz.ei6.judo.data
import android.util.Log
import biz.ei6.judo.datasource.JudoEventDao
import biz.ei6.judo.domain.JudoEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

import kotlinx.coroutines.withContext
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

class RoomJudoEventRepository(
    private val dao: JudoEventDao,
    private val defaultSeed: List<JudoEvent> = JudoEvent.LIST
) : JudoEventRepository {

    // 1️⃣ Room renvoie des Entity → on mappe vers le Domain
    override val events: Flow<List<JudoEvent>> =
        dao.observeAll().map { list ->
            list.map { it.toDomain() }
        }

    // 2️⃣ init : seed avec conversion Domain → Entity
    override suspend fun init() = withContext(Dispatchers.IO) {
        Log.d("INITIALISATION", "Passage par init")
        Log.d("INITIALISATION", "Nombre d'évenements dans la base de données : ${dao.count()}")
        Log.d("INITIALISATION", "Nombre d'évenements par défaut : ${defaultSeed.size}")
        if (defaultSeed.isNotEmpty() && dao.count() == 0) {
            Log.d("INITIALISATION", "Initialisation de la base de données")
            defaultSeed.forEach { event ->
                dao.upsert(event.toEntity())
                Log.d("INITIALISATION", "Ajout de l'évenement ${event.title}")
            }
        }
    }

    // 3️⃣ toggleFavorite : Uuid → String
    @OptIn(ExperimentalUuidApi::class)
    override suspend fun toggleFavorite(id: Uuid) {
        dao.toggleFavorite(id.toString())
    }

    // 4️⃣ add : Domain → Entity
    override suspend fun add(event: JudoEvent) {
        dao.upsert(event.toEntity())
    }

    // 5️⃣ remove : Uuid → String
    @OptIn(ExperimentalUuidApi::class)
    override suspend fun remove(id: Uuid) {
        dao.deleteById(id.toString())
    }
}

