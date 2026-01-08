package biz.ei6.judo.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import biz.ei6.judo.data.JudoEventRepository

class JudoVMFactory(
    private val repo: JudoEventRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(JudoVM::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return JudoVM(repo) as T
        }
        error("Unknown VM")
    }
}
