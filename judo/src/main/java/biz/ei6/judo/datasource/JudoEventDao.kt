package biz.ei6.judo.datasource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import biz.ei6.judo.data.JudoEventEntity

import kotlinx.coroutines.flow.Flow
import kotlin.uuid.ExperimentalUuidApi

@Dao
@OptIn(ExperimentalUuidApi::class)
interface JudoEventDao {

    @Query("SELECT * FROM judo_events")
    fun observeAll(): Flow<List<JudoEventEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(event: JudoEventEntity)

    @OptIn(ExperimentalUuidApi::class)
    @Query("DELETE FROM judo_events WHERE id = :id")
    suspend fun deleteById(id: String)

    @OptIn(ExperimentalUuidApi::class)
    @Query("UPDATE judo_events SET isFavorite = CASE WHEN isFavorite = 1 THEN 0 ELSE 1 END WHERE id = :id")
    suspend fun toggleFavorite(id: String)

    @Query("SELECT COUNT(*) FROM judo_events")
    suspend fun count(): Int
}
