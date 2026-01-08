package biz.ei6.judo.screens

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
sealed interface Screen : NavKey {

    @Serializable data object Home : Screen
    @Serializable data object Map : Screen
    @Serializable data object Favorites : Screen
    @Serializable data object Profile : Screen

    @Serializable data class EventDetail(val eventId: String) : Screen

    @Serializable data object EventNew : Screen
}
