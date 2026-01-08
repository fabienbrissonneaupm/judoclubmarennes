import android.util.Log
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.activity.compose.BackHandler
import androidx.navigation3.ui.NavDisplay
import androidx.navigation3.runtime.entryProvider
import biz.ei6.judo.domain.JudoEvent
import biz.ei6.judo.presentation.JudoVM
import biz.ei6.judo.screens.JudoEventDetailScreen
import biz.ei6.judo.screens.JudoEventNewScreen
import biz.ei6.judo.screens.JudoEventsScreen
import biz.ei6.judo.screens.Screen
import kotlin.uuid.ExperimentalUuidApi

@OptIn(ExperimentalUuidApi::class)
@Composable
fun AppNav3(navVM: JudoVM = viewModel()) {

    // Gère le bouton système “Back”
    BackHandler(enabled = navVM.backStack.size > 1) {
        navVM.pop()
    }

    NavDisplay(
        backStack = navVM.backStack,
        entryProvider = entryProvider {

            entry<Screen.Home> {
                JudoEventsScreen(
                    vm = navVM,
                    onEventClick = { e -> navVM.push(Screen.EventDetail(e.id.toString())) },
                    onFabClick = { navVM.push(Screen.EventNew) },
                    onBottomNav = { label ->
                        when (label) {
                            "Accueil" -> navVM.goTop(Screen.Home)
                            "Carte" -> navVM.goTop(Screen.Map)
                            "Favoris" -> navVM.goTop(Screen.Favorites)
                            "Profil" -> navVM.goTop(Screen.Profile)
                        }
                    }
                )
            }

            entry<Screen.EventDetail> { key ->
                val event = JudoEvent.LIST.firstOrNull { it.id.toString() == key.eventId }
                    ?: JudoEvent.DEFAULT

                JudoEventDetailScreen(
                    vm = navVM,
                    selected = event,
                    onBack = { navVM.pop() }
                )
            }
            entry<Screen.EventNew> { key ->

                JudoEventNewScreen(
                    vm = navVM,
                    onBack = { navVM.pop() }
                )
            }
            entry<Screen.Map> {
                JudoMapScreen(navVM.events,
                    onBack = { navVM.goTop(Screen.Home) })
            }
            entry<Screen.Favorites> { /* … */ }
            entry<Screen.Profile> { /* … */ }
        }
    )
}
