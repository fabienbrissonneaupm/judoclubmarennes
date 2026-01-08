package biz.ei6.judo

import AppNav3
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.viewmodel.compose.viewModel
import biz.ei6.judo.data.FileJudoEventRepository

import biz.ei6.judo.data.JudoEventRepository
import biz.ei6.judo.data.RoomJudoEventRepository
import biz.ei6.judo.datasource.DatabaseProvider
import biz.ei6.judo.framework.AppStartupEffects
import biz.ei6.judo.presentation.JudoVM
import biz.ei6.judo.presentation.JudoVMFactory


import biz.ei6.judo.ui.theme.TheTheme


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val db = DatabaseProvider.get(applicationContext)
        val repo = RoomJudoEventRepository(db.judoEventDao())

        setContent {
            AppStartupEffects()
            val vm: JudoVM = viewModel(factory = JudoVMFactory((repo)))
            TheTheme {
                  AppNav3 (vm)
            }
        }
    }
}
