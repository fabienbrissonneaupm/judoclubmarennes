package biz.ei6.judo.presentation

import androidx.lifecycle.ViewModel
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.viewModelScope

import biz.ei6.judo.data.JudoEventRepository
import biz.ei6.judo.domain.JudoEvent
import biz.ei6.judo.screens.Screen
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

class JudoVM(private val repo: JudoEventRepository) : ViewModel() {

    // Back stack observable par Compose
    val backStack: SnapshotStateList<Screen> = mutableStateListOf(Screen.Home)

    fun push(screen: Screen) {
        backStack.add(screen)
    }

    fun pop(): Boolean {
        // retourne true si on a effectivement pop
        return if (backStack.size > 1) {
            backStack.removeAt(backStack.lastIndex)
            true
        } else {
            false
        }
    }

    fun goTop(tab: Screen) {
        backStack.clear()
        backStack.add(tab)
    }

    // Gestion des données
   // val events: StateFlow<List<JudoEvent>> = repo.events
    val events: StateFlow<List<JudoEvent>> =
        repo.events.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList()
        )
    init {
        viewModelScope.launch { repo.init() }
    }

    @OptIn(ExperimentalUuidApi::class)
    fun toggleFavorite(id: Uuid) {
        viewModelScope.launch {repo.toggleFavorite(id)}
    }

    fun addEvent(event: JudoEvent) {
        viewModelScope.launch {
            repo.add(event)
        }
    }

}
