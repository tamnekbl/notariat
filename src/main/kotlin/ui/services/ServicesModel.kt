package ui.services

import androidx.compose.runtime.mutableStateListOf
import cafe.adriel.voyager.core.model.StateScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import db.dao.Service
import db.reps.ServiceRepository
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import utils.Loading
import utils.logging.Log

class ServicesModel(
    val repository: ServiceRepository
) : StateScreenModel<ServicesState>(ServicesState()) {

    val services = mutableStateListOf<Service>()

    init {
        Log.i("Model Created")
    }

    fun getServices(){
        services.clear()
        flow { emit(repository)}
            .map {
                mutableState.value = state.value.copy(loading = Loading.Progress())
                it.getAll()
            }
            .onEach {
                services.addAll(it)
                mutableState.value = state.value.copy(loading = Loading.Success())
            }
            .catch { Log.e(it) }
            .launchIn(screenModelScope)

    }
}
