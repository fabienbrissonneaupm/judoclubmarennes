package biz.ei6.judo.datasource
import androidx.room.TypeConverter

class AppConverters {
    private val sep = '\u001F'.toString()

    @TypeConverter
    fun listToString(value: List<String>?): String =
        value?.joinToString(sep).orEmpty()

    @TypeConverter
    fun stringToList(value: String?): List<String> =
        value?.takeIf { it.isNotBlank() }?.split(sep) ?: emptyList()
}
