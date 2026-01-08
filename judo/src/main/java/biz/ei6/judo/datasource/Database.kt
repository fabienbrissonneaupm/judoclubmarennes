package biz.ei6.judo.datasource
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import biz.ei6.judo.data.JudoEventEntity

@Database(
    entities = [JudoEventEntity::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(AppConverters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun judoEventDao(): JudoEventDao
}
